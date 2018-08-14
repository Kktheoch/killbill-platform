// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: payment/payment_transaction_info_plugin.proto

package org.killbill.billing.osgi.bundles.rpc.gen;

public interface PaymentTransactionInfoPluginOrBuilder extends
    // @@protoc_insertion_point(interface_extends:payment.PaymentTransactionInfoPlugin)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>required string kb_payment_id = 1;</code>
   */
  boolean hasKbPaymentId();
  /**
   * <code>required string kb_payment_id = 1;</code>
   */
  java.lang.String getKbPaymentId();
  /**
   * <code>required string kb_payment_id = 1;</code>
   */
  com.google.protobuf.ByteString
      getKbPaymentIdBytes();

  /**
   * <code>required string kb_transaction_payment_id = 2;</code>
   */
  boolean hasKbTransactionPaymentId();
  /**
   * <code>required string kb_transaction_payment_id = 2;</code>
   */
  java.lang.String getKbTransactionPaymentId();
  /**
   * <code>required string kb_transaction_payment_id = 2;</code>
   */
  com.google.protobuf.ByteString
      getKbTransactionPaymentIdBytes();

  /**
   * <code>required .payment.PaymentTransactionInfoPlugin.TransactionType transaction_type = 3;</code>
   */
  boolean hasTransactionType();
  /**
   * <code>required .payment.PaymentTransactionInfoPlugin.TransactionType transaction_type = 3;</code>
   */
  org.killbill.billing.osgi.bundles.rpc.gen.PaymentTransactionInfoPlugin.TransactionType getTransactionType();

  /**
   * <code>required string amount = 4;</code>
   */
  boolean hasAmount();
  /**
   * <code>required string amount = 4;</code>
   */
  java.lang.String getAmount();
  /**
   * <code>required string amount = 4;</code>
   */
  com.google.protobuf.ByteString
      getAmountBytes();

  /**
   * <code>required string currency = 5;</code>
   */
  boolean hasCurrency();
  /**
   * <code>required string currency = 5;</code>
   */
  java.lang.String getCurrency();
  /**
   * <code>required string currency = 5;</code>
   */
  com.google.protobuf.ByteString
      getCurrencyBytes();

  /**
   * <code>required string created_date = 6;</code>
   */
  boolean hasCreatedDate();
  /**
   * <code>required string created_date = 6;</code>
   */
  java.lang.String getCreatedDate();
  /**
   * <code>required string created_date = 6;</code>
   */
  com.google.protobuf.ByteString
      getCreatedDateBytes();

  /**
   * <code>required string effective_date = 7;</code>
   */
  boolean hasEffectiveDate();
  /**
   * <code>required string effective_date = 7;</code>
   */
  java.lang.String getEffectiveDate();
  /**
   * <code>required string effective_date = 7;</code>
   */
  com.google.protobuf.ByteString
      getEffectiveDateBytes();

  /**
   * <code>required .payment.PaymentTransactionInfoPlugin.PaymentPluginStatus getStatus = 8;</code>
   */
  boolean hasGetStatus();
  /**
   * <code>required .payment.PaymentTransactionInfoPlugin.PaymentPluginStatus getStatus = 8;</code>
   */
  org.killbill.billing.osgi.bundles.rpc.gen.PaymentTransactionInfoPlugin.PaymentPluginStatus getGetStatus();

  /**
   * <code>required string gateway_error = 9;</code>
   */
  boolean hasGatewayError();
  /**
   * <code>required string gateway_error = 9;</code>
   */
  java.lang.String getGatewayError();
  /**
   * <code>required string gateway_error = 9;</code>
   */
  com.google.protobuf.ByteString
      getGatewayErrorBytes();

  /**
   * <code>required string gateway_error_code = 10;</code>
   */
  boolean hasGatewayErrorCode();
  /**
   * <code>required string gateway_error_code = 10;</code>
   */
  java.lang.String getGatewayErrorCode();
  /**
   * <code>required string gateway_error_code = 10;</code>
   */
  com.google.protobuf.ByteString
      getGatewayErrorCodeBytes();

  /**
   * <code>required string first_payment_reference_id = 11;</code>
   */
  boolean hasFirstPaymentReferenceId();
  /**
   * <code>required string first_payment_reference_id = 11;</code>
   */
  java.lang.String getFirstPaymentReferenceId();
  /**
   * <code>required string first_payment_reference_id = 11;</code>
   */
  com.google.protobuf.ByteString
      getFirstPaymentReferenceIdBytes();

  /**
   * <code>required string second_payment_reference_id = 12;</code>
   */
  boolean hasSecondPaymentReferenceId();
  /**
   * <code>required string second_payment_reference_id = 12;</code>
   */
  java.lang.String getSecondPaymentReferenceId();
  /**
   * <code>required string second_payment_reference_id = 12;</code>
   */
  com.google.protobuf.ByteString
      getSecondPaymentReferenceIdBytes();

  /**
   * <code>repeated .payment.PluginProperty properties = 13;</code>
   */
  java.util.List<org.killbill.billing.osgi.bundles.rpc.gen.PluginProperty> 
      getPropertiesList();
  /**
   * <code>repeated .payment.PluginProperty properties = 13;</code>
   */
  org.killbill.billing.osgi.bundles.rpc.gen.PluginProperty getProperties(int index);
  /**
   * <code>repeated .payment.PluginProperty properties = 13;</code>
   */
  int getPropertiesCount();
  /**
   * <code>repeated .payment.PluginProperty properties = 13;</code>
   */
  java.util.List<? extends org.killbill.billing.osgi.bundles.rpc.gen.PluginPropertyOrBuilder> 
      getPropertiesOrBuilderList();
  /**
   * <code>repeated .payment.PluginProperty properties = 13;</code>
   */
  org.killbill.billing.osgi.bundles.rpc.gen.PluginPropertyOrBuilder getPropertiesOrBuilder(
      int index);
}
