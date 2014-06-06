/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.server.listeners;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheck;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.codahale.metrics.servlets.HealthCheckServlet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableList;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.ServletModule;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.management.ManagementService;
import org.killbill.billing.lifecycle.api.BusService;
import org.killbill.billing.lifecycle.api.Lifecycle;
import org.killbill.billing.platform.api.KillbillConfigSource;
import org.killbill.billing.platform.config.DefaultKillbillConfigSource;
import org.killbill.billing.server.config.KillbillServerConfig;
import org.killbill.billing.server.healthchecks.KillbillHealthcheck;
import org.killbill.billing.server.modules.KillbillServerModule;
import org.killbill.bus.api.PersistentBus;
import org.killbill.commons.embeddeddb.EmbeddedDB;
import org.killbill.commons.skeleton.listeners.GuiceServletContextListener;
import org.killbill.commons.skeleton.modules.BaseServerModuleBuilder;
import org.killbill.commons.skeleton.modules.JMXModule;
import org.killbill.commons.skeleton.modules.JaxrsJacksonModule;
import org.killbill.commons.skeleton.modules.StatsModule;
import org.killbill.notificationq.api.NotificationQueueService;
import org.skife.config.ConfigSource;
import org.skife.config.ConfigurationObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.IOException;
import java.lang.management.ManagementFactory;

public class KillbillPlatformGuiceListener extends GuiceServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(KillbillPlatformGuiceListener.class);

    public static final ImmutableList<String> METRICS_SERVLETS_PATHS = ImmutableList.<String>of("/1.0/healthcheck", "/1.0/metrics", "/1.0/ping", "/1.0/threads");

    protected KillbillServerConfig config;
    protected DefaultKillbillConfigSource configSource;
    protected Injector injector;
    protected Lifecycle killbillLifecycle;
    protected BusService killbillBusService;
    protected EmbeddedDB embeddedDB;

    protected ServletModule getServletModule() {
        final BaseServerModuleBuilder builder = new BaseServerModuleBuilder();
        return builder.build();
    }

    protected Module getModule(ServletContext servletContext) {
        return new KillbillServerModule(servletContext, config, configSource);
    }

    @Override
    public void contextInitialized(final ServletContextEvent event) {
        initializeConfig();

        // Will call super.contextInitialized(event)
        initializeGuice(event);

        initializeMetrics(event);

        registerEhcacheMBeans();

        startLifecycle();
    }

    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
        super.contextDestroyed(sce);

        // Guice error, no need to fill the screen with useless stack traces
        if (killbillLifecycle == null) {
            return;
        }

        stopLifecycle();

        stopEmbeddedDB();
    }

    protected void initializeConfig() {
        configSource = new DefaultKillbillConfigSource();
        config = new ConfigurationObjectFactory(new KillbillPlatformConfigSource(configSource)).build(KillbillServerConfig.class);
    }

    protected void initializeGuice(final ServletContextEvent event) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JodaModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        guiceModules = ImmutableList.<Module>of(getServletModule(),
                                                new JaxrsJacksonModule(new ObjectMapper()),
                                                new JMXModule(KillbillHealthcheck.class, NotificationQueueService.class, PersistentBus.class),
                                                new StatsModule(METRICS_SERVLETS_PATHS.get(0),
                                                                METRICS_SERVLETS_PATHS.get(1),
                                                                METRICS_SERVLETS_PATHS.get(2),
                                                                METRICS_SERVLETS_PATHS.get(3),
                                                                ImmutableList.<Class<? extends HealthCheck>>of(KillbillHealthcheck.class)),
                                                getModule(event.getServletContext()));

        // Start the Guice machinery
        super.contextInitialized(event);

        injector = injector(event);
        event.getServletContext().setAttribute(Injector.class.getName(), injector);

        // Already started at this point - we just need the instance for shutdown
        embeddedDB = injector.getInstance(EmbeddedDB.class);

        killbillLifecycle = injector.getInstance(Lifecycle.class);
        killbillBusService = injector.getInstance(BusService.class);
    }

    protected void initializeMetrics(final ServletContextEvent event) {
        event.getServletContext().setAttribute(HealthCheckServlet.HEALTH_CHECK_REGISTRY, injector.getInstance(HealthCheckRegistry.class));
        event.getServletContext().setAttribute(MetricsServlet.METRICS_REGISTRY, injector.getInstance(MetricRegistry.class));
    }

    protected void registerEhcacheMBeans() {
        final CacheManager cacheManager = injector.getInstance(CacheManager.class);
        if (cacheManager != null) {
            final MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            ManagementService.registerMBeans(cacheManager, mBeanServer, false, true, true, true);
        }
    }

    protected void startLifecycle() {
        // Fire all Startup levels up to service start
        killbillLifecycle.fireStartupSequencePriorEventRegistration();

        // Let's start!
        killbillLifecycle.fireStartupSequencePostEventRegistration();
    }

    protected void stopLifecycle() {
        killbillLifecycle.fireShutdownSequencePriorEventUnRegistration();

        // Complete shutdown sequence
        killbillLifecycle.fireShutdownSequencePostEventUnRegistration();
    }

    protected void stopEmbeddedDB() {
        if (embeddedDB != null) {
            try {
                embeddedDB.stop();
            } catch (final IOException ignored) {
            }
        }
    }

    @VisibleForTesting
    public Injector getInstantiatedInjector() {
        return injector;
    }

    private static final class KillbillPlatformConfigSource implements ConfigSource {
        private final KillbillConfigSource configSource;

        private KillbillPlatformConfigSource(KillbillConfigSource configSource) {
            this.configSource = configSource;
        }

        @Override
        public String getString(String propertyName) {
            return configSource.getString(propertyName);
        }
    }
}
