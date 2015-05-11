package triPOS.Java;

import javax.xml.bind.annotation.XmlElement;

import com.google.gson.annotations.SerializedName;

public class Configuration {

	@XmlElement(name = "allowPartialApprovals")
	@SerializedName("allowPartialApprovals")
	Boolean allowPartialApprovals;

	@XmlElement(name = "checkForDuplicateTransactions")
	@SerializedName("checkForDuplicateTransactions")
	Boolean checkForDuplicateTransactions;

	@XmlElement(name = "currencyCode")
	@SerializedName("currencyCode")
	String currencyCode;

	@XmlElement(name = "marketCode")
	@SerializedName("marketCode")
	String marketCode;

	public Boolean allowPartialApprovals() {
		 return allowPartialApprovals;
		 }
	
	public Boolean checkForDuplicateTransactions() {
		 return checkForDuplicateTransactions;
		 }
	
	public String currencyCode() {
		 return currencyCode;
		 }
	
	public String marketCode() {
		 return marketCode;
		 }
}
