package com.keralagbank.tierapp.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by frank on 12-02-2018.
 */

public class CommonFunctions {

    public Date String_dd_mm_yyyy_ToDate (String strDate){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date convertedDate = new Date();
        try {

            convertedDate = dateFormat.parse(strDate);

        } catch (ParseException e) {

            e.printStackTrace();

        }

        return convertedDate;
    }

    public String Date_ToString_dd_mm_yyyy (Date dtDate){

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = dateFormat.format(dtDate);

        return strDate;
    }

    public String Date_ToString_yyyy_mm_dd (Date dtDate){

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(dtDate);

        return strDate;
    }

    public String dd_mm_yyyy_to_yyyy_mm_dd (String strDate){

        return strDate.substring(6,10) + "-" + strDate.substring(3,5) + "-" + strDate.substring(0,2);

    }

    public String yyyy_mm_dd_to_dd_mm_yyyy (String strDate){

        return strDate.substring(8,10) + "-" + strDate.substring(5,7) + "-" + strDate.substring(0,4);

    }

    public String Int_ToString (int intValue){

        //return String.valueOf(intValue);
        return Integer.toString(intValue);

    }

    public int String_ToInt (String strValue){

        return Integer.parseInt(strValue);

    }

}
