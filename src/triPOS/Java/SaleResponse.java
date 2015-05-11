package triPOS.Java;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SaleResponse {
	@SerializedName("cashbackAmount")
	double cashbackAmount;

	@SerializedName("debitSurchargeAmount")
	double debitSurchargeAmount;

	@SerializedName("approvedAmount")
	double approvedAmount;

	@SerializedName("convenienceFeeAmount")
	double convenienceFeeAmount;

	@SerializedName("subTotalAmount")
	double SubTotalAmount;

	@SerializedName("tipAmount")
	double tipAmount;

	@SerializedName("accountNumber")
	String accountNumber;

	@SerializedName("binValue")
	String binValue;
    
	@SerializedName("cardHolderName")
	String cardHolderName;

	@SerializedName("cardLogo")
	String cardLogo;

	@SerializedName("currencyCode")
	String currencyCode;

	@SerializedName("entryMode")
	String entryMode;

	@SerializedName("paymentType")
	String paymentType;

	@SerializedName("signature")
	Signature signature;

	@SerializedName("terminalId")
	String terminalId;

	@SerializedName("totalAmount")
	double totalAmount;

	@SerializedName("approvalNumber")
	String approvalNumber;

	@SerializedName("isApproved")
	Boolean isApproved;

	@SerializedName("_processor")
	Processor _processor;

	@SerializedName("statusCode")
	String statusCode;

	@SerializedName("transactionDateTime")
	String transactionDateTime;

	@SerializedName("transactionId")
	String transactionId;

	@SerializedName("_errors")
	List<ApiError> _errors;

	@SerializedName("_links")
	List<ApiLink> _links;

	@SerializedName("_logs")
	List<String> _logs;

	@SerializedName("_type")
	String _type;

	@SerializedName("_warnings")
	List<ApiWarning> _warnings;
    
}
