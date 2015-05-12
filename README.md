# triPOS.Java

* Questions?  certification@elementps.com
* **Feature request?** Open an issue.
* Feel like **contributing**?  Submit a pull request.

##Overview

This repository demonstrates an integration to the triPOS product using the Java programming language.  The project was created using Eclipse Luna Release.  The application itself is very simple and allows a user to populate a hard coded credit sale request in either XML or JSON format.  The user may then send that request to triPOS for further processing.

![triPOS.Java](https://github.com/elementps/triPOS.Java/screenshot.png

##Prerequisites

Please contact your Integration Team member for any questions about the below prerequisites.

* After you clone this repository download gson-2.3.1.jar from: http://code.google.com/p/google-gson/ and place it in the libs folder.  This library is used to convert JSON to/from Java objects.
* Register and download the triPOS application: https://mft.elementps.com/backend/plugin/Registration/ (once activated, login at https://mft.elementps.com)
* Create Express test account: http://www.elementps.com/Resources/Create-a-Test-Account
* Install and configure triPOS
* Optionally install a hardware peripheral and obtain test cards (but you can be up and running without hardware for testing purposes)
* Currently triPOS is supported on Windows 7

##Documentation/Troubleshooting

* To view the triPOS embedded API documentation point your favorite browser to:  http://localhost:8080/help/ (for a default install).
* In addition to the help documentation above triPOS writes information to a series of log files located at:  C:\Program Files (x86)\Vantiv\triPOS Service\Logs (for a default install).

##Step 1: Generate a request package

You can either generate an XML request or a JSON request.  This example shows the XML request.  Also notice that the value in laneId is 9999.  This is the 'null' laneId meaning a transaction will flow through the system without requiring hardware.  All lanes are configured in the triPOS.config file located at:  C:\Program Files (x86)\Vantiv\triPOS Service (if you kept the default installation directory).  If you modify this file make sure to restart the triPOS.NET service in the Services app to read in your latest triPOS.config changes.

```
<ns2:saleRequest xmlns:ns2="http://tripos.vantiv.com/2014/09/TriPos.Api">
    <address>
        <billingAddress1>123 Sample Street</billingAddress1>
        <billingAddress2>Suite 101</billingAddress2>
        <billingCity>Chandler</billingCity>
        <billingPostalCode>85224</billingPostalCode>
        <billingState>AZ</billingState>
    </address>
    <cashbackAmount>0.0</cashbackAmount>
    <convenienceFeeAmount>0.0</convenienceFeeAmount>
    <emvFallbackReason>None</emvFallbackReason>
    <tipAmount>0.0</tipAmount>
    <transactionAmount>3.25</transactionAmount>
    <clerkNumber>Clerk101</clerkNumber>
    <configuration>
        <allowPartialApprovals>false</allowPartialApprovals>
        <checkForDuplicateTransactions>true</checkForDuplicateTransactions>
        <currencyCode>Usd</currencyCode>
        <marketCode>Retail</marketCode>
    </configuration>
    <laneId>9999</laneId>
    <referenceNumber>Ref000001</referenceNumber>
    <shiftId>ShiftA</shiftId>
    <ticketNumber>T0000001</ticketNumber>
</ns2:saleRequest>

```

##Step 2:Create message headers

The tp-authorization header below is only useful while testing as the full set of header information is not provided.  This example will be updated with that information in a future release.  For now refer to the integration guide for more information on constructing the headers needed for a production environment.

```
conn.setRequestProperty("content-type", "application/xml");
conn.setRequestProperty("accept", "application/xml");				
conn.setRequestProperty("tp-application-id", "1234"); 
conn.setRequestProperty("tp-application-name", "triPOS.Java"); 
conn.setRequestProperty("tp-application-version", "1.0.0"); 
String tpauthorization = String.format("Version=%s, Credential=%s", config.GetVersion(), config.GetDeveloperKey());
conn.setRequestProperty("tp-authorization", tpauthorization); 
conn.setRequestProperty("tp-return-logs", "false"); 
```

##Step 3: Send request to triPOS

Use any http library to send a request to triPOS which is listening on a local address:  http://localhost:8080/api/v1/sale (if you kept the install default).

```
URL url = new URL(this.url);
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream()); 
writer.write(request); 
writer.close(); 
```

##Step 4: Receive response from triPOS

```
BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); 
while ((line = rd.readLine()) != null) { 
  sb.append(line); 
} 

response = sb.toString();
```

##Step 5: Parse response data

Some information from the response below has been removed for brevity.  Currently the example code is not parsing the response but in a future version the XML response will be converted into a SaleResponse object.

```
<?xml version="1.0" encoding="UTF-8"?>
<saleResponse xmlns="http://tripos.vantiv.com/2014/09/TriPos.Api" xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
  <_errors/>
  <_hasErrors>false</_hasErrors>
  <_type>saleResponse</_type>
  <_processor>
    <processorLogs>
    </processorLogs>
    <processorRawResponse></processorRawResponse>
    <processorReferenceNumber>Ref000001</processorReferenceNumber>
    <processorRequestFailed>false</processorRequestFailed>
    <processorRequestWasApproved>true</processorRequestWasApproved>
    <processorResponseCode>Approved</processorResponseCode>
    <processorResponseMessage>Approved</processorResponseMessage>
  </_processor>
  <approvalNumber>000014</approvalNumber>
  <isApproved>true</isApproved>
  <statusCode>Approved</statusCode>
  <transactionDateTime>2015-05-11T16:14:50.0000000-07:00</transactionDateTime>
  <transactionId>2005001513</transactionId>
  <accountNumber>************6781</accountNumber>
  <binValue>4003000000000000</binValue>
  <cardHolderName>GLOBAL PAYMENTS TEST CARD/</cardHolderName>
  <cardLogo>Visa</cardLogo>
  <currencyCode>Usd</currencyCode>
  <entryMode>Swiped</entryMode>
  <paymentType>Credit</paymentType>
  <signature>
    <data></data>
    <format>PointsBigEndian</format>
    <statusCode>SignaturePresent</statusCode>
  </signature>
  <terminalId>0000009999</terminalId>
  <totalAmount>3.25</totalAmount>
  <approvedAmount>3.25</approvedAmount>
  <convenienceFeeAmount>0</convenienceFeeAmount>
  <subTotalAmount>3.25</subTotalAmount>
  <tipAmount>0</tipAmount>
  <cashbackAmount>0</cashbackAmount>
  <debitSurchargeAmount>0</debitSurchargeAmount>
</saleResponse>
```

###©2014-2015 Element Payment Services, Inc., a Vantiv company. All Rights Reserved.

Disclaimer:
This software and all specifications and documentation contained herein or provided to you hereunder (the "Software") are provided free of charge strictly on an "AS IS" basis. No representations or warranties are expressed or implied, including, but not limited to, warranties of suitability, quality, merchantability, or fitness for a particular purpose (irrespective of any course of dealing, custom or usage of trade), and all such warranties are expressly and specifically disclaimed. Element Payment Services, Inc., a Vantiv company, shall have no liability or responsibility to you nor any other person or entity with respect to any liability, loss, or damage, including lost profits whether foreseeable or not, or other obligation for any cause whatsoever, caused or alleged to be caused directly or indirectly by the Software. Use of the Software signifies agreement with this disclaimer notice.

![Analytics](https://ga-beacon.appspot.com/UA-60858025-35/triPOS.Java/readme?pixel)
