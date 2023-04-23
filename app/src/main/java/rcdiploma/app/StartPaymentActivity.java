package rcdiploma.app;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payumoney.core.PayUmoneyConfig;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.payumoney.sdkui.ui.utils.ResultModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class StartPaymentActivity extends AppCompatActivity {

    EditText amount;
    Button payNow;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_payment);
        amount = findViewById(R.id.payment_amount);
        payNow = findViewById(R.id.payment_now);
        
        payNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amount.getText().toString().trim().equals("")){
                    amount.setError("Amount Required");
                }
                else{
                    launchPayment();
                }
            }
        });
        
    }

    private void launchPayment() {
        PayUmoneyConfig payUmoneyConfig = PayUmoneyConfig.getInstance();
        payUmoneyConfig.setPayUmoneyActivityTitle("Purchase Order");
        payUmoneyConfig.setDoneButtonText("Pay " + getResources().getString(R.string.price_symbol) + amount.getText().toString());
        //setTxnId(System.currentTimeMillis() + "")
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(String.valueOf(convertStringToDouble(amount.getText().toString())))
                .setTxnId("1234567890")
                .setPhone("7878232386") //Add Dynamic Contact
                .setProductName("Purchase Order")
                .setFirstName("Sagar") //Add Dynamic Name
                .setEmail("khatrisagar2@gmail.com") //Add Dynamic Email
                .setsUrl(ConstantUrl.SURL)
                .setfUrl(ConstantUrl.FURL)
                .setUdf1("")
                .setUdf2("")
                .setUdf3("")
                .setUdf4("")
                .setUdf5("")
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(ConstantUrl.DEBUG)
                .setKey(ConstantUrl.MERCHANT_KEY)
                .setMerchantId(ConstantUrl.MERCHANT_ID);
        try {
            PayUmoneySdkInitializer.PaymentParam mPaymentParams = builder.build();
            calculateHashInServer(mPaymentParams);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Double convertStringToDouble(String str) {
        return Double.parseDouble(str);
    }

    private void calculateHashInServer(final PayUmoneySdkInitializer.PaymentParam mPaymentParams) {
        ProgressUtils.showLoadingDialog(this);
        String url = ConstantUrl.MONEY_HASH;
        StringRequest request = new StringRequest(Request.Method.POST, url,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String merchantHash = "";

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            merchantHash = jsonObject.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ProgressUtils.cancelLoading();

                        if (merchantHash.isEmpty() || merchantHash.equals("")) {
                            Toast.makeText(StartPaymentActivity.this, "Could not generate hash", Toast.LENGTH_SHORT).show();
                        } else {
                            mPaymentParams.setMerchantHash(merchantHash);
                            PayUmoneyFlowManager.startPayUMoneyFlow(mPaymentParams, StartPaymentActivity.this, R.style.PayUMoney, true);
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(StartPaymentActivity.this, "Connect to internet Volley", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StartPaymentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        ProgressUtils.cancelLoading();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return mPaymentParams.getParams();
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {

            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
            ResultModel resultModel = data.getParcelableExtra(PayUmoneyFlowManager.ARG_RESULT);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //showAlert("Payment Successful");
                    Toast.makeText(this, "Payment Successfully", Toast.LENGTH_SHORT).show();
                    Log.d("RESPONSE_TRANSACTION",transactionResponse.getTransactionDetails());
                    //new addShipping().execute();
                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.CANCELLED)) {
                    showAlert("Payment Cancelled");
                } else if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.FAILED)) {
                    showAlert("Payment Failed");
                }

            } else if (resultModel != null && resultModel.getError() != null) {
                Toast.makeText(this, "Error check log", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Both objects are null", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_CANCELED) {
            showAlert("Payment Cancelled New "+data.toString());
        }
    }

    private void showAlert(String msg) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(msg);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}