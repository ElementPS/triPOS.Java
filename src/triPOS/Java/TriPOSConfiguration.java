package triPOS.Java;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import java.io.File;

public class TriPOSConfiguration {
	private String developerKey;
	private String version;
	private String developerSecret;
	
	public TriPOSConfiguration(String pathToTriPOSConfig)
	{
		//do work to get developerKey, for now hard code
		this.version = "1.0";
		
		try {					
			File fXmlFile = new File(pathToTriPOSConfig);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			XPath xpath = XPathFactory.newInstance().newXPath();
			doc.getDocumentElement().normalize();
			NodeList nodes = (NodeList) xpath.evaluate("//tripos/developers/developer",
					doc, XPathConstants.NODESET);
			for (int i = 0; i < nodes.getLength(); i++) {
				Node developerNode = nodes.item(i);
				NodeList childs = developerNode.getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
		            Node n = childs.item(j);

		            if (n.getNodeName().equals("developerKey")) {
		            	this.developerKey = n.getTextContent();   
		            } else if (n.getNodeName().equals("developerSecret")) {
		            	this.developerSecret = n.getTextContent();
		            }
		        }
				break;
		    }
			
		}
		catch (Exception ex) {
			
		}
		
	}
	
	public String GetDeveloperKey()
	{
		return this.developerKey;
	}
			
	public String GetDeveloperSecret()
	{
		return this.developerSecret;
	}
	
	public String GetVersion()
	{
		return this.version;
	}	
}
