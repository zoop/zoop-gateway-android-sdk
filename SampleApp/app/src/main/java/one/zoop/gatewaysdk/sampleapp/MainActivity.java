package one.zoop.gatewaysdk.sampleapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import one.zoop.gatewaySDK.qtActivity.QTApiActivity;
import one.zoop.gatewaySDK.qtUtils.QtRequestType;
import one.zoop.gatewaySDK.qtUtils.QtStringUtils;

import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.BSA_ERROR;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.BSA_SUCCESS;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.ESIGN_ERROR;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.ESIGN_SUCCESS;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.ITR_ERROR;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.ITR_SUCCESS;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.QT_EMAIL;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.QT_ENV;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.QT_REQUEST_TYPE;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.QT_RESULT;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.QT_TRANSACTION_ID;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.REQUEST_API;
import static one.zoop.gatewaySDK.qtUtils.QtConstantUtils.SDK_ERROR;
import static one.zoop.gatewaySDK.qtUtils.QtRequestType.BSA;
import static one.zoop.gatewaySDK.qtUtils.QtRequestType.ESIGN;
import static one.zoop.gatewaySDK.qtUtils.QtRequestType.ITR;


public class MainActivity extends AppCompatActivity {

    private Button btESign, btBsa, btItr;
    private EditText etGatewayId;
    private String gatewayId, email, environment;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiInit();
    }

    private void uiInit() {
        btESign = findViewById(R.id.btESign);
        btBsa = findViewById(R.id.btBsa);
        etGatewayId = findViewById(R.id.etGatewayId);
        btItr = findViewById(R.id.btItr);
        tvResult = findViewById(R.id.tvResponse);
        environment = "QT_P";
        btESign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QtStringUtils.isNotNullOrEmpty(etGatewayId.getText().toString())) {
                    gatewayId = etGatewayId.getText().toString();
                } else {
                    gatewayId = "ac5d3c26-60c1-4978-a429-30c55e971f81";
                }

                Intent gatewayIntent = new Intent(MainActivity.this, QTApiActivity.class);
                gatewayIntent.putExtra(QT_TRANSACTION_ID, gatewayId);
                gatewayIntent.putExtra(QT_ENV, environment);
                gatewayIntent.putExtra(QT_REQUEST_TYPE, ESIGN.getRequest());
                startActivityForResult(gatewayIntent, REQUEST_API);
            }
        });

        btBsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QtStringUtils.isNotNullOrEmpty(etGatewayId.getText().toString())) {
                    gatewayId = etGatewayId.getText().toString();
                } else {
                    gatewayId = "81a506dd-ef5a-4b70-8197-4fcd5620def5";
                }

                Intent gatewayIntent = new Intent(MainActivity.this, QTApiActivity.class);
                gatewayIntent.putExtra(QT_TRANSACTION_ID, gatewayId);
                gatewayIntent.putExtra(QT_ENV, environment);
                gatewayIntent.putExtra(QT_REQUEST_TYPE, BSA.getRequest());
                startActivityForResult(gatewayIntent, REQUEST_API);
            }
        });

        btItr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QtStringUtils.isNotNullOrEmpty(etGatewayId.getText().toString())) {
                    gatewayId = etGatewayId.getText().toString();
                } else {
                    gatewayId = "10543aba-e09f-4ee0-9452-ee62584fdb2e";
                }

                Intent gatewayIntent = new Intent(MainActivity.this, QTApiActivity.class);
                gatewayIntent.putExtra(QT_TRANSACTION_ID, gatewayId);
                gatewayIntent.putExtra(QT_ENV, environment);
                gatewayIntent.putExtra(QT_REQUEST_TYPE, ITR.getRequest());
                startActivityForResult(gatewayIntent, REQUEST_API);
            }
        });

    }

    public String toPrettyFormat(String jsonString) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();
        String statusCode = json.get("statusCode").getAsString();
        if (statusCode.equals("427")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.webview&hl=en_IN")));
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String responseString0 = "No Data Received";
        if (null != data) {
            responseString0 = data.getStringExtra(QT_RESULT);
            Log.d("SDK test result ", " res 0" + responseString0);
            tvResult.setVisibility(View.VISIBLE);
            tvResult.setText(responseString0 + "  0");
        }
        if (requestCode == REQUEST_API && null != data) {
            String requestType = data.getStringExtra(QT_REQUEST_TYPE);
            if (requestType.equalsIgnoreCase(QtRequestType.SDK_ERROR.getRequest())) {
                if (resultCode == SDK_ERROR) {
                    String responseString = data.getStringExtra(QT_RESULT);
                    Log.d("SDK test result ", requestType + " res " + responseString);
                }
            }
            if (requestType.equalsIgnoreCase(ESIGN.getRequest())) {
                if (resultCode == ESIGN_SUCCESS) {
                    String responseString = data.getStringExtra(QT_RESULT);
                    //handle success for esign
                    tvResult.setText(responseString);
                    Log.d("SDK test result ", requestType + " res " + responseString);
                }
                if (resultCode == ESIGN_ERROR) {
                    String errorString = data.getStringExtra(QT_RESULT);
                    //handle error for esign
                    tvResult.setText(errorString);
                    toPrettyFormat(errorString);
                    Log.d("SDK test error ", requestType + " err " + errorString);
                }


            } else if (requestType.equalsIgnoreCase(BSA.getRequest())) {
                if (resultCode == BSA_SUCCESS) {
                    String responseString = data.getStringExtra(QT_RESULT);
                    //handle success for bsa
                    tvResult.setText(responseString);
                    Log.d("SDK test result bsa", requestType + " res " + responseString);
                }
                if (resultCode == BSA_ERROR) {
                    String errorString = data.getStringExtra(QT_RESULT);
                    //handle error for bsa
                    tvResult.setText(errorString);
                    Log.d("SDK test error bsa", requestType + " err " + errorString);
                }
            } else if (requestType.equalsIgnoreCase(ITR.getRequest())) {
                if (resultCode == ITR_SUCCESS) {
                    String responseString = data.getStringExtra(QT_RESULT);
                    //handle success for itr
                    tvResult.setText(responseString);
                    Log.d("SDK test result bsa", requestType + " res " + responseString);
                }
                if (resultCode == ITR_ERROR) {
                    String errorString = data.getStringExtra(QT_RESULT);
                    //handle error for itr
                    tvResult.setText(errorString);
                    Log.d("SDK test error bsa", requestType + " err " + errorString);
                }
            }

        }
    }
}
//{"id":"10543aba-e09f-4ee0-9452-ee62584fdb2e","response_message":"Session expired or invalid session","response_code":"606"}