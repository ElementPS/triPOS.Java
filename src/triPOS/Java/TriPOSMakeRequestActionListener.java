package triPOS.Java;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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
import com.sun.org.apache.xml.internal.security.utils.Base64;

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
		HttpURLConnection conn = null; 
		try { 	
			response = "";
			this.sb = new StringBuilder();

			// added for testing with a Query String
			String queryString = "? 7 *";
			URL url = new URL(this.url);
			conn = (HttpURLConnection) url.openConnection();
			
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
			conn.setRequestProperty("tp-return-logs", "false"); 

			// replace all newlines in the payload with carriage returns
			// expected payload contains carriage returns
			String payload = this.txtRequest.getText();
			if (payload.contains("\n") && !(payload.contains("\r\n"))) {
				payload = payload.replace("\n", "\r\n");
			}

			// Set algorithm to be used in HMAC hashing
			String algorithm = "TP-HMAC-SHA256";
			
			// Get nonce
			UUID nonce = UUID.randomUUID();
			
			// Format date per iso 8601
			TimeZone tz = TimeZone.getTimeZone("UTC");
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'");
			format.setTimeZone(tz);
			String requestDate = format.format(new Date());
			
			String tpauthorization = makeAuthorizationHeader(url, payload, conn.getRequestProperties(), conn.getRequestMethod(), requestDate, nonce.toString(), algorithm, config.GetDeveloperKey(), config.GetDeveloperSecret());
		
			
			conn.setRequestProperty("tp-authorization", tpauthorization);
			
			conn.setConnectTimeout(10000); 
			conn.setReadTimeout(30000); 
			conn.setDoInput(true); 
			conn.setDoOutput(true); 
			
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
			writer.write(payload); 
			writer.close();
			
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
				InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
				BufferedReader rd = new BufferedReader(reader); 
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
		} 
		catch (Exception ex) {
			response = "Exception: " + ex.getMessage() + " " + ex.getCause();
			ex.printStackTrace();
		}
		finally {
			conn.disconnect(); 
		}

		this.txtResponse.setText(response);
    }

	public String getCanonicalSignedHeaders (Map<String,List<String>> httpHeaders) {
    	List<String> unsortedHeaderKeys = new ArrayList<String>();
    	
    	// one of the headers has a null key so we create a list of the keys by iterating
		for (Map.Entry<String, List<String>> header: httpHeaders.entrySet()){
			// exclude the null and "tp-" prefixed headers
			if (header.getKey() != null && !(header.getKey().startsWith("tp-"))) {
				unsortedHeaderKeys.add(header.getKey());
			}
		}

		// sort headers
		Collections.sort(unsortedHeaderKeys);
//		System.out.println(unsortedHeaderKeys);	// uncomment to see sorted headers
		
		StringBuilder canonicalSignedHeaders = new StringBuilder();
		for (String headerKey : unsortedHeaderKeys) {
			canonicalSignedHeaders.append(headerKey + ";");
		}
		
		// remove trailing semicolon
		if (canonicalSignedHeaders.charAt(canonicalSignedHeaders.length() - 1) == ';') {
			canonicalSignedHeaders.deleteCharAt(canonicalSignedHeaders.length() - 1);
		}
		
//		System.out.println("***Canonical signed headers ->\n" + canonicalSignedHeaders);	// uncomment to see canonicalSignedHeaders
		
		// return lower cased headers
    	return canonicalSignedHeaders.toString().toLowerCase(); 
    }
    
    public String getCanonicalHeaders (Map<String,List<String>> httpHeaders, String canonicalSignedHeaders) {
    	StringBuilder canonicalHeaders = new StringBuilder();
    	String [] sortedHeaderKeys = canonicalSignedHeaders.split(";");
    	
    	// iterate through HTTP headers in alphabetical order using canonicalSignedHeader order
    	for (String headerKey : sortedHeaderKeys) {
    		// add the header key and colon
    		canonicalHeaders.append(headerKey + ":");
    		for (String headerValue : httpHeaders.get(headerKey)) {
    			// iterate through this header's values, append each lower cased value, and delimit with a comma 
    			canonicalHeaders.append(headerValue.toLowerCase() + ",");
    		}
    		
    		// remove trailing comma on header values list 
    		if (canonicalHeaders.charAt(canonicalHeaders.length() - 1) == ',') {
    			canonicalHeaders.deleteCharAt(canonicalHeaders.length() - 1);
    		}
    		
    		// newline delimit each header entry
    		canonicalHeaders.append("\n");
    	}
    	
//    	System.out.println("***Canonical Headers ->\n" + canonicalHeaders.toString().trim());	// uncomment to see canonicalHeaders
    	
    	// return canonicalHeaders with whitespace trimmed
    	return canonicalHeaders.toString().trim();
    }

    public String getCanonicalUri (URL url) {
//    	System.out.println("***Canonical URI ->\n" + url.getPath());	// uncomment to print the canonical uri
    	return url.getPath();
    }
    
    public String getCanonicalQuery (URL url) throws UnsupportedEncodingException {
    	String query = null;
    	if (url.getQuery() != null) {
    		query = Base64.encode(url.getQuery().getBytes("UTF-8"));
    	}
    	
//    	System.out.println("***Canonical query ->\n" + query);	// uncomment to print the canonical query
    	return query;
    }
    
    public String createUTF8 (String s) throws UnsupportedEncodingException {
    	byte [] utf8Bytes = s.getBytes("UTF-8");
    	
    	String utf8String = new String(utf8Bytes);
    	
    	return utf8String;
    }
    
    public String hexEncodeHash (String data, String algorithm) throws DigestException, UnsupportedEncodingException, NoSuchAlgorithmException {
    	MessageDigest hashAlgorithm = null;
    	
    	switch (algorithm.toLowerCase()) {
		case "tp-hmac-md5":
			hashAlgorithm = MessageDigest.getInstance("MD5");
			break;

		case "tp-hmac-sha1":
			hashAlgorithm = MessageDigest.getInstance("SHA-1");
			break;
			
		case "tp-hmac-sha256":
			hashAlgorithm = MessageDigest.getInstance("SHA-256");
			break;
			
		case "tp-hmac-sha384":
			hashAlgorithm = MessageDigest.getInstance("SHA-384");
			break;
			
		case "tp-hmac-sha512":
			hashAlgorithm = MessageDigest.getInstance("SHA-512");
			break;
			
		default:
			throw new NoSuchAlgorithmException("Invalid signature algorithm: " + algorithm);
		}
    	
    	
    	byte[] utf8EncodedHash = hashAlgorithm.digest(data.getBytes("UTF-8"));
    	
//    	System.out.println("***UTF-8 encoded hash->\n" + new String(utf8EncodedHash));	// uncomment to print utf-8 encoded hashed data
    	
    	String hexEncodedHashedData = toHex(utf8EncodedHash, true);
		return hexEncodedHashedData;
    }
    
    public String toHex (byte[] data, Boolean lowercase) throws UnsupportedEncodingException {
    	StringBuilder hexString = new StringBuilder();
    	for (byte b : data){
    		if (lowercase){
    			hexString.append(String.format("%02x", b));
    		} else {
    			hexString.append(String.format("%02X", b));
    		}
    	}

//    	System.out.println("***Hex encoded data ->\n" + hexString.toString());	// uncomment to print hex encoded data
    	return hexString.toString();
    }

    public String getCanonicalRequest (String httpMethod, String canonicalUri, String canonicalQuery, String canonicalHeaders, String canonicalSignedHeaders, String payloadHash) {
    	StringBuilder canonicalRequest = new StringBuilder();
    	
    	// for each part of the canonical request we only include if it is a non-null, nonempty string
    	if(httpMethod != null && !httpMethod.isEmpty()) {
    		canonicalRequest.append(httpMethod);
    	} 
    	
		canonicalRequest.append("\n");
		
    	if (canonicalUri != null && !canonicalUri.isEmpty()) {
    		canonicalRequest.append(canonicalUri);
    	} 
    	
		canonicalRequest.append("\n");
		
    	if (canonicalQuery != null && !canonicalQuery.isEmpty()) {
    		canonicalRequest.append(canonicalQuery);
    	}
    	
		canonicalRequest.append("\n");
		
    	if (canonicalHeaders != null && !canonicalHeaders.isEmpty()) {
    		canonicalRequest.append(canonicalHeaders);
    	} 
    	
		canonicalRequest.append("\n");
		
    	if (canonicalSignedHeaders != null && !canonicalSignedHeaders.isEmpty()) {
    		canonicalRequest.append(canonicalSignedHeaders);
    	}
    	
		canonicalRequest.append("\n");
		
    	if (payloadHash != null && !payloadHash.isEmpty()) {
    		canonicalRequest.append(payloadHash);
    	}
    	    	
//    	System.out.println("***Canonical request ->\n" + canonicalRequest.toString());	// uncomment to print canonicalRequest
    	
    	return canonicalRequest.toString();
    }

    public String createHmacSignature (String data, String key, String algorithm) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
    	Mac hashAlgorithm = null;
    	SecretKeySpec secretKey = null;
    	
    	switch (algorithm.toLowerCase()) {
			case "tp-hmac-md5":
				hashAlgorithm = Mac.getInstance("HmacMD5");
		    	secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacMD5");
				break;
	
			case "tp-hmac-sha1":
				hashAlgorithm = Mac.getInstance("HmacSHA1");
				secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
				break;
				
			case "tp-hmac-sha256":
				hashAlgorithm = Mac.getInstance("HmacSHA256");
				secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
				
				break;
			case "tp-hmac-sha384":
				hashAlgorithm = Mac.getInstance("HmacSHA384");
				secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA384");
				break;
				
			case "tp-hmac-sha512":
				hashAlgorithm = Mac.getInstance("HmacSHA512");
				secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512");
				break;
				
			default:
				throw new NoSuchAlgorithmException("Invalid signature algorithm: " + algorithm);
		}
    	hashAlgorithm.init(secretKey);
    	byte[] utf8EncodedHash = hashAlgorithm.doFinal(data.getBytes("UTF-8"));
//    	System.out.println("***UTF-8 encoded hash->\n " + new String(utf8EncodedHash));	// uncomment to print utf-8 encoded hashed value
    	String hexEncodedKeyedHashedData = toHex(utf8EncodedHash, true);
    	return hexEncodedKeyedHashedData;
    }
    

    public String getUnhashedSignature (String algorithm, String requestDate, String developerKey, String canonicalRequestHash) {
    	StringBuilder unhashedSignature = new StringBuilder();
    	
    	if (algorithm != null && !algorithm.isEmpty()) {
    		unhashedSignature.append(algorithm + "\n");
    	}
    	if (requestDate != null && !requestDate.isEmpty()) {
    		unhashedSignature.append(requestDate + "\n");
    	}
    	if (developerKey != null && !developerKey.isEmpty()) {
    		unhashedSignature.append(developerKey + "\n");
    	}
    	if (canonicalRequestHash != null && !canonicalRequestHash.isEmpty()) {
    		unhashedSignature.append(canonicalRequestHash);
    	}
    	
//    	System.out.println("***Unhashed Signature ->\n" + unhashedSignature.toString());	// uncomment to print un-hashed signature
    	
    	return unhashedSignature.toString();
    }

    public String makeAuthorizationHeader (URL url, String payload, Map<String, List<String>> headers, String method, String requestDate, String nonce, String algorithm, String developerKey, String developerSecret) throws DigestException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException{
		String payloadHash = hexEncodeHash(payload, algorithm);

		// Get canonical signed headers from the request
		String canonicalSignedHeaders = getCanonicalSignedHeaders(headers);
		
		// Get canonical headers from the request 
		String canonicalHeaders = getCanonicalHeaders(headers, canonicalSignedHeaders);
		
		// Get canonical uri from the request
		String canonicalUri = getCanonicalUri(url);
		
		// Get canonical query string
		String canonicalQuery = getCanonicalQuery(url);
		
		// Get canonical request
		String canonicalRequest = getCanonicalRequest(method, canonicalUri, canonicalQuery, canonicalHeaders, canonicalSignedHeaders, payloadHash);
//		String canonicalRequest = getCanonicalRequest("", null, null, null, null, null);		// used for testing with null or empty string params	
		
		// Get canonical request hash
		String canonicalRequestHash = hexEncodeHash(canonicalRequest, algorithm);

		// Get key signature hash
		String keySignatureHash = createHmacSignature(nonce.toString() + config.GetDeveloperSecret(), requestDate , algorithm);
		
		// Get un-hashed signature 
		String unHashedSignature = getUnhashedSignature(algorithm, requestDate, config.GetDeveloperKey(), canonicalRequestHash);
		
		// Get signature
		String signature = createHmacSignature(unHashedSignature, keySignatureHash, algorithm);
		
		String tpauthorization = String.format("Version=%s, Algorithm=%s, Credential=%s, SignedHeaders=%s, Nonce=%s, RequestDate=%s, Signature=%s", 
				config.GetVersion(), algorithm, config.GetDeveloperKey(),  canonicalSignedHeaders, nonce, requestDate, signature );
		
//		System.out.println("TP-Authorization->\n" + tpauthorization);	// uncomment to see the tp-authorization header
		
    	return tpauthorization;
    }
}
