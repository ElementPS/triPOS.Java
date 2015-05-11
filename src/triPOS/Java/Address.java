package triPOS.Java;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.SerializedName;

public class Address {
	@XmlElement(name = "billingAddress1")
	@SerializedName("BillingAddress1")
	String BillingAddress1;

	@XmlElement(name = "billingAddress2")
	@SerializedName("BillingAddress2")
	String BillingAddress2;

	@XmlElement(name = "billingCity")
	@SerializedName("BillingCity")
	String BillingCity;

	@XmlElement(name = "billingPostalCode")
	@SerializedName("BillingPostalCode")
	String BillingPostalCode;

	@XmlElement(name = "billingState")
	@SerializedName("BillingState")
	String BillingState;

	public String BillingAddress1() {
		 return BillingAddress1;
	}
	
	public String BillingAddress2() {
		 return BillingAddress2;
	}
	
	public String BillingCity() {
		 return BillingCity;
	}
	
	public String BillingPostalCode() {
		 return BillingPostalCode;
	}
	
	public String BillingState() {
		 return BillingState;
	}	
}
