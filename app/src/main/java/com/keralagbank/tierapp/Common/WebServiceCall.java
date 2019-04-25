package com.keralagbank.tierapp.Common;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.ProgressBar;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.Proxy;


/**
 * Created by cbit on 13/1/17.
 */

public class WebServiceCall extends AsyncTask<String, Void, String>{

    private static final String URL_NAMESPACE = "http://tempuri.org/";
    private static final String URL_MAIN_REQ = "http://117.247.183.17:8300/";
    private static final String URL_SOAP_ACTION = "http://tempuri.org/mus";
    private static final String METHOD_NAME = "mus";

    public static final String CALL_MODE_VALUE = "MIATEST";
    public static final String CALL_SOURCE_VALUE = "EN";

    private static final String G_SOURCE = "gsource";
    private static final String G_CALL_TYPE = "gcalltype";
    private static final String G_INPUT = "ginput";

    private ProgressDialog progressDialog;

    private Context context;


    public interface AsyncResponse{
        void onFinished(String output);
    }

    private AsyncResponse instance = null;

    public WebServiceCall(Context ctx, AsyncResponse response){
        this.instance = response;
        this.context = ctx;
    }

    public WebServiceCall(Context ctx, AsyncResponse response, boolean show){
        this.instance = response;
        this.context = ctx;
    }


    public String getResponse(String param1, String param2, String param3){
//System.out.println(param);

//        Log.d("value_to_pass","");

        SoapObject request = new SoapObject(URL_NAMESPACE, METHOD_NAME);
        request.addProperty(G_SOURCE, param1);
        request.addProperty(G_CALL_TYPE, param2);
        request.addProperty(G_INPUT, param3);
//        Log.d("REQUEST",q);
//        System.out.println("final req"+request);
        SoapSerializationEnvelope envelope = getSoapSerializationEnvelope(request);
        HttpTransportSE ht = getHttpTransportSE();
        String data = " ";
        try {
            ht.call(URL_SOAP_ACTION, envelope);
//            System.out.println("response - "+envelope.getResponse());
            SoapPrimitive resultsString = (SoapPrimitive) envelope.getResponse();
            data = resultsString.toString();

        }catch (Exception e){
            try{
//               System.out.println("response - "+envelope.getResponse());
                SoapObject resultObject = (SoapObject) envelope.getResponse();
                data = resultObject.toString();
            }catch (Exception ex){
                ex.printStackTrace();
                data = "ERROR" + ex.getMessage();
            }
            e.printStackTrace();
        }

        return data;
    }


    private HttpTransportSE getHttpTransportSE() {
//        myX509TrustManager.allowAllSSL();
        HttpTransportSE ht = new HttpTransportSE(Proxy.NO_PROXY, URL_MAIN_REQ, 90000);

        ht.debug = true;
        ht.setXmlVersionTag("<!--?xml version=\"1.0\" encoding= \"UTF-8\" ?-->");
        return ht;
    }


    private SoapSerializationEnvelope getSoapSerializationEnvelope(SoapObject request) {
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.setOutputSoapObject(request);
        return envelope;
    }

    @Override
    protected void onPreExecute(){
      //  if(showProgress) {
        if (Build.VERSION.SDK_INT > 21) {     progressDialog=ProgressDialog.show(context,null,null);
            progressDialog.setContentView(new ProgressBar(context));
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            progressDialog.setProgressDrawable(new ColorDrawable(Color.WHITE));
        }
        else
        {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }
        progressDialog.setCancelable(false);

    }

    @Override
    protected String doInBackground(String... strings) {

        return getResponse(strings[0],strings[1],strings[2]);
    }

    @Override
    protected void onPostExecute(String result){

            progressDialog.dismiss();
        if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }

        instance.onFinished(result);
    }
}
