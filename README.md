# zoop-gateway-android-sdk
AadhaarAPI | Zoop Android SDK for E-sign and Bank Statement Analysis Gateway
## AadhaarAPI E-Sign Gateway 
### 1. INTRODUCTION
As a registered ASP under ESP AadhaarAPI provide WEB and Mobile gateway for E-signing of
documents. Using these gateways any organisation on-boarded with us can get their documents
signed digitally by their customer using Aadhaar number based EKYC (performed on ESP portal).
The mode of transaction can be following:
1. Aadhaar Number + OTP (works only on web and android)
2. Aadhaar Number + Fingerprint (works only on windows machine and android)
3. Aadhaar Number + IRIS (works only on Samsung IRIS tablet) 
This solves the problem of wet signing of documents, which is a costly and time-consuming
process. Aadhaar based E-sign is a valid and legal signature as per government regulations and is
accepted widely across India by various organisations.
Once you have received the API key generated from the dashboard also download the Android
SDK files from the Downloads section. Make sure following files are received inside a zip file:
• .aar file
• Sample App

### 2. PROCESS FLOW
1. Generate the document based on the user info at your backend.
2. At your backend server, Initiate the e-sign transaction using a simple Rest API [POST] call by
converting your document into base64 string or via our Multi-Part Upload API. Details of these
are available in the documents later. You will require API key and Agency Id for accessing this
API which can be generated from the Dashboard.
3. This gateway transaction id then needs to be communicated back to the frontend(in Android
Project) where SDK is to be called.
4. After adding the SDK (.aar file) in your android project, client has to pass the above generated
transaction id to an SDK Intent Function, which will open the SDK.
5. SDK will open with a user login after which and the rest of the process till response will be
handled by the gateway itself.
6. Once the transaction is successful or failed, appropriate handler function will be called with
response JSON, that can be used by the client to process the flow further.
7. Result PDF url will be sent to the responseUrl requested via INIT call. 
8. Client will also have a REST API available to pull the status of a gateway transaction from
backend and reason of failure. 

### 3. INITIATING A GATEWAY TRANSACTION FOR E-SIGN[IP WHITELISTED IN PRODUCTION] 
To initiate a gateway transaction a REST API call has to be made to backend. This call will
generate a **Gateway Transaction Id** which needs to be passed to the frontend web-sdk to launch
the gateway.

#### 3.1 INIT URL: 
URL: POST {{base_url}}/gateway/esign/init/
#### BASE URL:
 **For Pre-Production Environment:** https://preprod.aadhaarapi.com
 
 **For Production Environment:** https://prod.aadhaarapi.com
 
 **Example Url:** https://preprod.aadhaarapi.com/gateway/esign/init/
 
#### 3.2 REQUEST HEADERS: [All Mandatory]
  QT_API_KEY: <<your api key value – available via Dashboard>>
  
  QT_AGENCY_ID: <<your agency id value – available via Dashboard>>
  
  Content-Type: application/json
  
#### 3.3 REQUEST BODY PARAMS: [All Mandatory]
    {
    "document": {
      "data”: "<<document data in based64 format>>",
      "type": "<<pdf is only supported for now>>",
      "info”: "<<information about the document – minimum length 15>>"
    },
    "signerName": "<<name of the signer, must be same as on Aadhaar Card>>",
    "signerCity": "<<city of the signer, preferably as mentioned in Aadhaar>>",
    "purpose": "Purpose of transaction, Mandatory",
    "responseURL":"<<POST[REST] URL to which response is to be sent after the transaction is complete>>",
    "version": "2.0 <<current E-sign version>>"
    } 
  
| Parameters | Description/Values | Checks |
| --- | --- | --- |
| document { | Is an object that provides details of the document. It has below mentioned values in the object |      |
| data | Show file differences that **haven't been** staged | Valid base64 formatted pdf |
| type | Format of the document. For now, only value pdf is supported |      |
| info | Information about the document to be sent to  ESP | Minimum length 15 |
| } |   |   |
| signerName | Name of the signer | Same as in Aadhaar Card |
| signerCity | Place of signing (Current City/As mentioned in Aadhaar) |   |
| purpose | Purpose of document signature | Mandate as per norms. Will be used to generate consent text and logged in DB. |
|responseURL | POST API URL where the Agency receives the response after the e-signing is completed. | A valid POST API URL,  else response back to your server will fail.| 
| version | Current E-sign version (2.0) | Must be 2.0 |

#### 3.4 RESPONSE PARAMS:
    {
    "id": "<<transactionId>>",
    "docs": [
    "<<document ID>>"
    ],
    "request version": "2.0",
    "createdAt": "<<timestamp>>"
    } 

The above generated gateway transactionId has to be made available in your android project to
open the E-Sign SDK.

 **Note:**  A transaction is valid only for 30 mins after generation. 
 
### 4. ADDING SDK (.AAR FILE) TO YOUR PROJECT
To add SDK file as library in your Project, Perform the following Steps:

1. Right click on your project and choose “Open Module Settings”.
2. Click the “+” button in the top left to add a new module.
3. Choose “Import .JAR or .AAR Package” and click the “Next” button.
4. Find the AAR file using the ellipsis button (“…”) beside the “File name” field.
5. Keep the app’s module selected and click on the Dependencies pane to add the new
module as a dependency.
6. Use the “+” button of the dependencies screen and choose “Module dependency”.
7. Choose the module and click “OK”. 

### 5. CONFIGURING AND LAUNCHING THE E-SIGN SDK 
#### 5.1  IMPORT FILES 

Import following files in your Activity:

    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.ESIGN_ERROR;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.ESIGN_SUCCESS;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_EMAIL;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_ENV;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_REQUEST_TYPE;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_RESULT;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_TRANSACTION_ID;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.REQUEST_API;
    import static one.zoop.sdkesign.esignlib.qtUtils.QtRequestType.ESIGN;
    
#### 5.2 ADD STRINGS(IN STRINGS.XML FILE) 

Add following strings in Strings.xml according to the application’s requirement. 

    // must be ‘y' for allowing OTP based e-sign authentication to user; else ‘n’
    <string name=“qt_otp_access">y</string>
    // must be ‘y' for allowing Fingerprint based e-sign authentication to user; else ‘n’
    <string name=“qt_fp_access">n</string>
    // must be ‘y' for allowing Iris based e-sign authentication to user; else ‘n’
    <string name=“ qt_iris_access">n</string>
    // must be ‘y' for allowing Phone based login to user; else ‘n’ 
    <string name=“qt_phone_auth_access">n</string>
    // must be ‘y' for allowing signature’s position to be decided by the user; else ‘n’
    <string name=“qt_draggable">y</string>                               
   
#### 5.3 CALL E-SIGN SDK FROM THE ACTIVITY  
**Use the Intent Function to call the E-Sign SDK from your Activity as shown below:**

    String GatewayId, Email, environment;
    Intent gatewayIntent = new Intent(YourActivity.this, QTApiActivity.class);
    gatewayIntent.putExtra(QT_TRANSACTION_ID, GatewayId);
    gatewayIntent.putExtra(QT_EMAIL, Email); //Not Mandatory, can be added to pre-fill the Login Box
    gatewayIntent.putExtra(QT_REQUEST_TYPE, ESIGN.getRequest());
    gatewayIntent.putExtra(QT_ENV, environment);
    startActivityForResult(gatewayIntent, REQUEST_API);
Params:
GatewayId: “Transaction Id generated from your backend must be passed here”

Email: “Add your end user’s email here to pre-fill the login box”

environment: for pre-prod use "QT_PP" and for prod use "QT_P"

Set the QT_REQUEST_TYPE as ESIGN.getRequest() for e-sign based transaction.

Example:

GatewayId = "a051231e-ddc7-449d-8635-bb823485a20d";

Email = “youremail@gmail.com";

environment = "QT_PP";

#### 5.4 HANDLE SDK RESPONSE 

After performing a Successful Transaction: Android download manager will start downloading the Signed PDF file. 

Also the responses incase of successful transaction as well as response in case of error will be sent to your activity & can be handled via onActivityResult( ) method as shown below.

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      if (requestCode == REQUEST_API && null != data) {
        String requestType = data.getStringExtra(QT_REQUEST_TYPE);
        String serviceType = data.getStringExtra(QT_SERVICE_TYPE);
      if(requestType.equalsIgnoreCase(ESIGN.getRequest())){
        //To handle the Success JSON response from SDK
      if (resultCode == ESIGN_SUCCESS) {
        String responseString = data.getStringExtra(QT_RESULT); //handle
        successful response from AadhaarAPI’s e-sign SDK here
        Log.i("SDK test response ", serviceType + " response: " +responseString); // can be removed
        }
        // To handle the Error JSON response from SDK 
      if (resultCode == ESIGN_ERROR) {
       String errorString = data.getStringExtra(QT_RESULT); //handle
        error response from AadhaarAPI’s e-sign SDK here
        Log.i("SDK test error ", serviceType + " error: "+ errorString); // can be removed
        }
      }
    }
 
