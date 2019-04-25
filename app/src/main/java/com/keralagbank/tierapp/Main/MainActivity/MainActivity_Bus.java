package com.keralagbank.tierapp.Main.MainActivity;

import android.content.Context;

/**
 * Created by Hiran on 02-03-2018.
 */

public class MainActivity_Bus implements iMainActivity_Bus
{
    @Override
    public String CallWebservice(String data1, String data2, String data3, Context ctxAct) {

        MainActivity_Data objData = new MainActivity_Data();
        return objData.CallWS(data1,data2,data3,ctxAct);
    }
}
