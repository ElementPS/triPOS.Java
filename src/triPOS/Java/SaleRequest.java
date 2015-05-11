package triPOS.Java;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

@XmlRootElement(name = "saleRequest", namespace = "http://tripos.vantiv.com/2014/09/TriPos.Api")
public class SaleRequest {
	@XmlElement(name = "address")
	@SerializedName("address")
	Address address;
	
	@XmlElement(name = "cashbackAmount")
	@SerializedName("cashbackAmount")
	double cashbackAmount;
	
	@XmlElement(name = "convenienceFeeAmount")
	@SerializedName("convenienceFeeAmount")
	double convenienceFeeAmount;	

	@XmlElement(name = "emvFallbackReason")
	@SerializedName("emvFallbackReason")
	String emvFallbackReason;

	@XmlElement(name = "tipAmount")
	@SerializedName("tipAmount")
	double tipAmount;

	@XmlElement(name = "transactionAmount")
	@SerializedName("transactionAmount")
	double transactionAmount;

	@XmlElement(name = "clerkNumber")
	@SerializedName("clerkNumber")
	String clerkNumber;

	@XmlElement(name = "configuration")
	@SerializedName("configuration")
	Configuration configuration;

	@XmlElement(name = "laneId")
	@SerializedName("laneId")
	int laneId;
	
	@XmlElement(name = "referenceNumber")
	@SerializedName("referenceNumber")
	String referenceNumber;

	@XmlElement(name = "shiftId")
	@SerializedName("shiftId")
	String shiftId;

	@XmlElement(name = "ticketNumber")
	@SerializedName("ticketNumber")
	String ticketNumber;
		
	public double cashbackAmount() {
		 return cashbackAmount;
	}

	public double convenienceFeeAmount() {
		 return convenienceFeeAmount;
	}

	public String emvFallbackReason() {
		 return emvFallbackReason;
	}

	public double tipAmount() {
		 return tipAmount;
	}

	public double transactionAmount() {
		 return transactionAmount;
	}
	
	public String clerkNumber() {
		 return clerkNumber;
	}
	
	public int laneId() {
		 return laneId;
	}
	
	public String referenceNumber() {
		 return referenceNumber;
	}
	
	public String shiftId() {
		 return shiftId;
	}
	
	public String ticketNumber() {
		 return ticketNumber;
	}
	
	public Address address() {
		 return address;
	}
	
	public Configuration configuration() {
		 return configuration;
	}
	
	public SaleRequest GetSaleRequest() {
		SaleRequest saleRequest = new SaleRequest();
		saleRequest.address = new Address();
		saleRequest.address.BillingAddress1 = "123 Sample Street";
		saleRequest.address.BillingAddress2 = "Suite 101";
		saleRequest.address.BillingCity = "Chandler";
		saleRequest.address.BillingPostalCode = "85224";
		saleRequest.address.BillingState = "AZ";
		saleRequest.cashbackAmount = 0;
		saleRequest.convenienceFeeAmount = 0;
		saleRequest.emvFallbackReason = "None";
		saleRequest.tipAmount = 0;
		saleRequest.transactionAmount = 3.25;
		saleRequest.clerkNumber = "Clerk101";
		saleRequest.configuration = new Configuration();
		saleRequest.configuration.allowPartialApprovals = false;
		saleRequest.configuration.checkForDuplicateTransactions = true;
		saleRequest.configuration.currencyCode = "Usd";
		saleRequest.configuration.marketCode = "Retail";
		saleRequest.laneId = 9999;
		saleRequest.referenceNumber = "Ref000001";
		saleRequest.shiftId = "ShiftA";
		saleRequest.ticketNumber = "T0000001";
		
		return saleRequest;
	}
	
	public String SaleRequestToJSON(SaleRequest saleRequest) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(saleRequest);
	}
	
	public String SaleRequestToXML(SaleRequest saleRequest) {
		String xmlString = "";
		
		try {					
			JAXBContext jaxbContext = JAXBContext.newInstance(SaleRequest.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
	
			// output pretty printed
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			//jaxbMarshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://tripos.vantiv.com/2014/09/TriPos.Api");
	
			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(saleRequest, sw);
			xmlString = sw.toString();
		}
		catch (Exception ex)
		{
		     final StringWriter sw = new StringWriter();
		     final PrintWriter pw = new PrintWriter(sw, true);
		     ex.printStackTrace(pw);
		     xmlString = sw.getBuffer().toString();		   
		}
		
        return xmlString;
	}
	
}
