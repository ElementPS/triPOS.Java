# triPOS.Java

* More documentation?  http://developer.mercurypay.com
* Questions?  certification@elementps.com
* **Feature request?** Open an issue.
* Feel like **contributing**?  Submit a pull request.

##Overview

This repository demonstrates an integration to the triPOS product.

##Prerequisites

Please contact your Integration Team member for any questions about the below prerequisites.

* After you clone this repository download gson-2.3.1.jar from: http://code.google.com/p/google-gson/ and place it in the libs folder within the triPOS.Java folder.  This library is used to convert JSON to/from Java objects.
* Register and download the triPOS application: https://mft.elementps.com/backend/plugin/Registration/ (once activated, login at https://mft.elementps.com)
* Create Express test account: http://www.elementps.com/Resources/Create-a-Test-Account
* Install and configure triPOS
* Optionally install a hardware peripheral and obtain test cards (but you can be up and running without hardware for testing purposes)
* Currently triPOS is supported on Windows 7

##Documentation/Troubleshooting

* triPOS provides an embedded API documentation feature.  Simply point your favorite browser to:  http://localhost:8080/help/ (for a default install) and the triPOS API documentation anticipates your questions.
* In addition to the help documentation above triPOS writes information to a series of log files located at:  C:\Program Files (x86)\Vantiv\triPOS Service\Logs (for a default install).

##Step 1: Generate a request package

You can either generate an XML request or a JSON request.  This example shows the JSON request.  Also notice that the value in laneId is 9999.  This is the 'null' laneId meaning a transaction will flow through the system without requiring hardware.  All lanes are configured in the triPOS.config file located at:  C:\Program Files (x86)\Vantiv\triPOS Service (if you kept the default installation directory).  If you modify this file make sure to restart the triPOS.NET service in the Services app to read in your latest triPOS.config changes.

```

```

##Step 2:Create message headers

```
```

##Step 3: Send request to triPOS

Use any http library to send a request to triPOS which is listening on a local address:  http://localhost:8080/api/v1/sale (if you kept the install default).

```
```

##Step 4: Receive response from triPOS



```

```

##Step 5: Parse response data


```

```

###©2014-2015 Element Payment Services, Inc., a Vantiv company. All Rights Reserved.

Disclaimer:
This software and all specifications and documentation contained herein or provided to you hereunder (the "Software") are provided free of charge strictly on an "AS IS" basis. No representations or warranties are expressed or implied, including, but not limited to, warranties of suitability, quality, merchantability, or fitness for a particular purpose (irrespective of any course of dealing, custom or usage of trade), and all such warranties are expressly and specifically disclaimed. Element Payment Services, Inc., a Vantiv company, shall have no liability or responsibility to you nor any other person or entity with respect to any liability, loss, or damage, including lost profits whether foreseeable or not, or other obligation for any cause whatsoever, caused or alleged to be caused directly or indirectly by the Software. Use of the Software signifies agreement with this disclaimer notice.

![Analytics](https://ga-beacon.appspot.com/UA-60858025-35/triPOS.Java/readme?pixel)


=======
Integratin to triPOS using Java
>>>>>>> b5f174d77dc46b4d2dcd6e96aa515cbe0b532e9b
