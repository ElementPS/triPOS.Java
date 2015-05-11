package triPOS.Java;

import com.google.gson.annotations.SerializedName;

public class ApiError {
	@SerializedName("userMessage")
	String userMessage;

	@SerializedName("developerMessage")
	String developerMessage;
	
	@SerializedName("errorType")
	String errorType;

	@SerializedName("exceptionMessage")
	String exceptionMessage;

	@SerializedName("exceptionTypeFullName")
	String exceptionTypeFullName;

	@SerializedName("exceptionTypeShortName")
	String exceptionTypeShortName;	

}
