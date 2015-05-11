package triPOS.Java;

import com.google.gson.annotations.SerializedName;

public class Signature {
	@SerializedName("data")
	byte[] data;

	@SerializedName("format")
	String format;

	@SerializedName("statusCode")
	String statusCode;
}
