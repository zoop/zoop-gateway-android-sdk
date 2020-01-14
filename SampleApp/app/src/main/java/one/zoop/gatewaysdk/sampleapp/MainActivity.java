package one.zoop.gatewaysdk.sampleapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import one.zoop.sdkesign.esignlib.qtActivity.QTApiActivity;
import one.zoop.sdkesign.esignlib.qtUtils.QtRequestType;
import one.zoop.sdkesign.esignlib.qtUtils.QtStringUtils;

import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.BSA_ERROR;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.BSA_SUCCESS;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.ESIGN_ERROR;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.ESIGN_SUCCESS;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_EMAIL;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_ENV;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_REQUEST_TYPE;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_RESULT;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.QT_TRANSACTION_ID;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.REQUEST_API;
import static one.zoop.sdkesign.esignlib.qtUtils.QtConstantUtils.SDK_ERROR;
import static one.zoop.sdkesign.esignlib.qtUtils.QtRequestType.BSA;
import static one.zoop.sdkesign.esignlib.qtUtils.QtRequestType.ESIGN;

public class MainActivity extends AppCompatActivity {

    private Button btESign, btBsa;
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
        tvResult = findViewById(R.id.tvResponse);
        environment = "QT_PP";
        btESign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QtStringUtils.isNotNullOrEmpty(etGatewayId.getText().toString())) {
                    gatewayId = etGatewayId.getText().toString();
                } else {
                    gatewayId = "1979c1d7-5601-40d1-9186-f9d85ae6b99b";
                }

                Intent gatewayIntent = new Intent(MainActivity.this, QTApiActivity.class);
                gatewayIntent.putExtra(QT_TRANSACTION_ID, gatewayId);
                gatewayIntent.putExtra(QT_ENV, environment);
                gatewayIntent.putExtra(QT_EMAIL, email);   //not mandatory
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
            }

        }
    }
}
