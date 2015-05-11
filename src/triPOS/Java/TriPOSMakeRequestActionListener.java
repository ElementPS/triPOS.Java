package triPOS.Java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.swing.JTextArea;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TriPOSMakeRequestActionListener implements ActionListener{

    private String url;
    private StringBuilder sb;
    private JTextArea txtResponse;
    private JTextArea txtRequest;
    private TriPOSConfiguration config;
    private BooleanRef isJSON;
    private String response;

    public TriPOSMakeRequestActionListener(String url, JTextArea txtRequest, JTextArea txtResponse,
    									TriPOSConfiguration config, BooleanRef isJSON) {
        super();
        this.url = url;
        this.txtResponse = txtResponse;
        this.txtRequest = txtRequest;
        this.config = config;
        this.sb = new StringBuilder();
        this.isJSON = isJSON;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
		String line; 
		
		try { 	
			response = "";
			this.sb = new StringBuilder();
			
			URL url = new URL(this.url);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST"); 
			if (isJSON.data) {
				conn.setRequestProperty("content-type", "application/json");
				conn.setRequestProperty("accept", "application/json");
			} else {
				conn.setRequestProperty("content-type", "application/xml");
				conn.setRequestProperty("accept", "application/xml");				
			}
			
			conn.setRequestProperty("tp-application-id", "1234"); 
			conn.setRequestProperty("tp-application-name", "triPOS.Java"); 
			conn.setRequestProperty("tp-application-version", "1.0.0"); 
			String tpauthorization = String.format("Version=%s, Credential=%s", config.GetVersion(), config.GetDeveloperKey());
			
			conn.setRequestProperty("tp-authorization", tpauthorization); 
			conn.setRequestProperty("tp-return-logs", "false"); 
			
			conn.setRequestProperty("Content-Length", Integer.toString(this.txtRequest.getText().getBytes().length)); 
			conn.setConnectTimeout(10000); 
			conn.setReadTimeout(30000); 
			conn.setDoInput(true); 
			conn.setDoOutput(true); 
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream()); 
			writer.write(this.txtRequest.getText()); 
			writer.close(); 
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) { 
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); 
				while ((line = rd.readLine()) != null) { 
					sb.append(line); 
				} 
				rd.close();	
				
				if (this.isJSON.data) {
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JsonParser jp = new JsonParser();
					JsonElement je = jp.parse(sb.toString());
					response = gson.toJson(je);
				} else {					
					sb.deleteCharAt(0);
			        Source xmlInput = new StreamSource(new StringReader(sb.toString()));
			        StringWriter stringWriter = new StringWriter();
			        StreamResult xmlOutput = new StreamResult(stringWriter);
			        TransformerFactory transformerFactory = TransformerFactory.newInstance();
			        transformerFactory.setAttribute("indent-number", 2);
			        Transformer transformer = transformerFactory.newTransformer(); 
			        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			        transformer.transform(xmlInput, xmlOutput);
			        response = xmlOutput.getWriter().toString();
				}
				
			} 
			else { 
				response = "Error: " + conn.getResponseCode() + " " + conn.getResponseMessage(); 
			} 
			conn.disconnect(); 
		} 
		catch (Exception ex) { 
			response = "Exception: " + ex.getMessage(); 
		}
		
		this.txtResponse.setText(response);
    }

}
