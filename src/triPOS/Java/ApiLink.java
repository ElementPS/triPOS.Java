package triPOS.Java;

import com.google.gson.annotations.SerializedName;

public class ApiLink {
	@SerializedName("href")
	String href;

	@SerializedName("method")
	String method;

	@SerializedName("rel")
	String rel;

}
