package ro.pub.cs.systems.eim.lab07.calculatorwebservice.network;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.R;
import ro.pub.cs.systems.eim.lab07.calculatorwebservice.general.Constants;

public class CalculatorWebServiceAsyncTask extends AsyncTask<String, Void, String> {

    private TextView resultTextView;

    public CalculatorWebServiceAsyncTask(TextView resultTextView) {
        this.resultTextView = resultTextView;
    }

    @Override
    protected String doInBackground(String... params) {
        String operator1 = params[0];
        String operator2 = params[1];
        String operation = params[2];
        int method = Integer.parseInt(params[3]);

        String result = null;

        // TODO exercise 4
        // signal missing values through error messages

        // create an instance of a HttpClient object
        HttpClient httpClient = new DefaultHttpClient();

        // get method used for sending request from methodsSpinner
        if (method == Constants.GET_OPERATION) {
            try {
                // 1. GET
                // a) build the URL into a HttpGet object (append the operators / operations to the Internet address)
                String getURL = Constants.GET_WEB_SERVICE_ADDRESS + "?";
                getURL += Constants.OPERATION_ATTRIBUTE + "=" + operation + "&";
                getURL += Constants.OPERATOR1_ATTRIBUTE + "=" + operator1 + "&";
                getURL += Constants.OPERATOR2_ATTRIBUTE + "=" + operator2;
                HttpGet httpGet = new HttpGet(getURL);
                // b) create an instance of a ResultHandler object
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                // c) execute the request, thus generating the result
                result = httpClient.execute(httpGet, responseHandler);
            } catch (Exception exception) {
                Log.e(Constants.TAG, exception.getMessage());
                if (Constants.DEBUG) {
                    exception.printStackTrace();
                }
            }
        } else {
            // 2. POST
            // a) build the URL into a HttpPost object
            HttpPost httpPost = new HttpPost(Constants.POST_WEB_SERVICE_ADDRESS);
            // b) create a list of NameValuePair objects containing the attributes and their values (operators, operation)
            List<NameValuePair> postParams = new ArrayList<NameValuePair>();
            postParams.add(new BasicNameValuePair(Constants.OPERATION_ATTRIBUTE, operation));
            postParams.add(new BasicNameValuePair(Constants.OPERATOR1_ATTRIBUTE, operator1));
            postParams.add(new BasicNameValuePair(Constants.OPERATOR2_ATTRIBUTE, operator2));
            // c) create an instance of a UrlEncodedFormEntity object using the list and UTF-8 encoding and attach it to the post request
            try {
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(postParams, HTTP.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
                // d) create an instance of a ResultHandler object
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                // e) execute the request, thus generating the result
                result = httpClient.execute(httpPost, responseHandler);
            } catch (Exception exception) {
                Log.e(Constants.TAG, exception.getMessage());
                if (Constants.DEBUG) {
                    exception.printStackTrace();
                }
            }
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // display the result in resultTextView
        resultTextView.setText(result);
    }

}
