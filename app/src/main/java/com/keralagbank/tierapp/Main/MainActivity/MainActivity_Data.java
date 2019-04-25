package com.keralagbank.tierapp.Main.MainActivity;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;

import com.keralagbank.tierapp.Common.DatabaseHelper;
import com.keralagbank.tierapp.Common.WebServiceCall;
import com.keralagbank.tierapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * Created by Hiran on 02-03-2018.
 */

public class MainActivity_Data {

    String strOutPut;
    boolean blFinish = false;
    DatabaseHelper dbHelp;

    public String CallWS(final String param1, final String param2, String param3, final Context ctxAct)
    {

        WebServiceCall wsMUS = new WebServiceCall(ctxAct, new WebServiceCall.AsyncResponse() {
            @Override
            public void onFinished(String output) {
                Activity actMain = (Activity)ctxAct;
                EditText txtRslt = actMain.findViewById(R.id.txtResult);
                txtRslt.setText(output);
                dbHelp = new DatabaseHelper(ctxAct);
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                boolean updateData = dbHelp.updateWSLog(date,
                        param1,
                        param2,
                        output);
            }
        });
        try{

            wsMUS.execute(param1,param2,param3);

        }catch (Exception e)
        {
            return e.getMessage();
        }
       return  "Success";
    }
}

