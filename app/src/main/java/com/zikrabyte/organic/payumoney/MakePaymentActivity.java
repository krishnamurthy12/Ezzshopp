package com.zikrabyte.organic.payumoney;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.zikrabyte.organic.R;
import com.zikrabyte.organic.activities.CartActivity;
import com.zikrabyte.organic.activities.CheckOutActivity;
import com.zikrabyte.organic.activities.HomeActivity;
import com.zikrabyte.organic.activities.OrderHistoryActivity;

import java.util.HashMap;

public class MakePaymentActivity extends AppCompatActivity {
    private String postData;
    private WebView mWebView;

    private LinearLayout progressBar;

    private HashMap<String, String> params = new HashMap<>();
    private int ENV = 1;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payupayment);

        Intent intent = getIntent();

        ENV = intent.getIntExtra(PayUMoneyConstants.ENVIRONMENT, PayUMoneyConstants.ENV_PRODUCTION);
        params = (HashMap<String, String>) intent.getSerializableExtra(PayUMoneyConstants.PARAMS);

        url = PayUMoneyConstants.production_url;

        progressBar = (LinearLayout) findViewById(R.id.progressBar);

        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setDomStorageEnabled(true);

        /*mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {
                setProgress(progress * 100);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);


                }

            }
        });*/
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int progress) {

                if (view.getUrl().contains("/success")) {
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else if (view.getUrl().contains("/failure")){
                    Toast.makeText(MakePaymentActivity.this,"Canceled payment",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                setProgress(progress * 100);
                if (progress == 100) {
                    progressBar.setVisibility(View.GONE);
                    mWebView.setVisibility(View.VISIBLE);


                }
                Log.d("URLWEB",view.getUrl());




            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {
        });

        mWebView.getSettings().setJavaScriptEnabled(true);

        mWebView.addJavascriptInterface(new PayU(this), "PayU");

        postData = PayuMoneyUtils.urlEncodeUTF8(params);

        byte[] encodedData = postData.getBytes();
        Log.e("Pay u Pg postData", postData+"");
        Log.e("Pay u Pg encodedData", encodedData+"");
        mWebView.postUrl(url, encodedData);

    }


    public class PayU {
        Context mContext;
        Intent intent;

        PayU(Context c) {
            mContext = c;
            intent = new Intent();
        }

        @JavascriptInterface
        public void onSuccess(final String result) {
            intent.putExtra("result", result);
            Log.e("Pay u Pg onsuccess", result);
            setResult(RESULT_OK, intent);
            finish();
        }

        @JavascriptInterface
        public void onFailure(final String result) {


            intent.putExtra("result", result);
            Log.e("Pay u Pg onfailure", result);
            setResult(RESULT_CANCELED, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(MakePaymentActivity.this,"Canceled payment",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), CartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
