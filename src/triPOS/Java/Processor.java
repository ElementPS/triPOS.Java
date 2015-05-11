package triPOS.Java;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Processor {
	
	@SerializedName("processorLogs")
	List<String> processorLogs;

	@SerializedName("processorRawResponse")
	String processorRawResponse;

	@SerializedName("processorReferenceNumber")
	String processorReferenceNumber;

	@SerializedName("processorRequestFailed")
	String processorRequestFailed;

	@SerializedName("processorRequestWasApproved")
	String processorRequestWasApproved;

	@SerializedName("processorResponseCode")
	ProcessorResponseCode processorResponseCode;

	@SerializedName("processorResponseMessage")
	String processorResponseMessage;

}
