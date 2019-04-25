package com.keralagbank.tierapp.Common;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by frank on 12-02-2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDay.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TAG = "Franklin";

    CommonFunctions myCF;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table param_data_types (pdt_date date,pdt_data_type_id int,pdt_data_type_desc text,pdt_data_capture_type int,pdt_display_order int)");
        db.execSQL("create index param_data_types_idx1 on param_data_types (pdt_data_type_id)");
        db.execSQL("create table param_data_items (pdi_date date,pdi_data_type_id int,pdi_data_item_id int,pdi_data_item_desc text,pdi_data_item_unit text,pdi_display_order int)");
        db.execSQL("create index param_data_items_idx1 on param_data_items (pdi_data_type_id,pdi_data_item_id)");
        db.execSQL("create table data_table (dt_date date,dt_data_type_id int,dt_data_item_id int,dt_value text)");
        db.execSQL("create index data_table_idx1 on data_table (dt_date,dt_data_type_id,dt_data_item_id)");
        db.execSQL("create table ws_result_log (ws_date datetime,ws_in1 int,ws_in2 int,ws_op text)");
        db.execSQL("create index ws_result_log_idx1 on ws_result_log (ws_date)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean updateDataType (String gDate, int gDataID, String gDataIDDesc, String gCaptureType, int gDisplayOrder)
    {

        myCF = new CommonFunctions();

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            // Checking the count of Data ID to know whether the same already exists in DB

            Cursor res = db.rawQuery ("select count(1) from param_data_types where pdt_data_type_id = ?",new String[] {myCF.Int_ToString(gDataID)});
            res.moveToFirst();
            int recordCount = res.getInt(0);
            Log.i(TAG, "DatabaseHelper/updateDataType/Checking whether the Data Type ID already exists >> Count - " + myCF.Int_ToString(recordCount));

            if (recordCount == 0) {

                // If no record exists, inserting data

                ContentValues contentValues = new ContentValues();
                contentValues.put("pdt_date",gDate);
                contentValues.put("pdt_data_type_id",gDataID);
                contentValues.put("pdt_data_type_desc",gDataIDDesc);
                contentValues.put("pdt_data_capture_type",gCaptureType);
                contentValues.put("pdt_display_order",gDisplayOrder);
                long result = db.insert("param_data_types",null,contentValues);
                if (result == -1){
                    Log.i(TAG, "DatabaseHelper/updateDataType/DB Insert failed");
                    return false;
                }else{
                    Log.i(TAG, "DatabaseHelper/updateDataType/DB Insert Success");
                    return true;
                }

            } else {

                // If a record exists, updating data

                ContentValues contentValues = new ContentValues();
                contentValues.put("pdt_date",gDate);
                contentValues.put("pdt_data_type_desc",gDataIDDesc);
                contentValues.put("pdt_data_capture_type",gCaptureType);
                contentValues.put("pdt_display_order",gDisplayOrder);
                db.update("param_data_types",contentValues,"pdt_data_type_id = ?",new String[]{myCF.Int_ToString(gDataID)});
                Log.i(TAG, "DatabaseHelper/updateDataType/Data Type updated");
                return true;

            }


        } catch (Exception e) {

            Log.i(TAG, "DatabaseHelper/updateDataType/error - " + e.getMessage());
            return false;

        }

    }

    public boolean updateDataItem (String gDate, int gDataTypeID, int gDataItemID, String gDataItemDesc, String gDataItemUnit, int gDisplayOrder){

        myCF = new CommonFunctions();

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            // Validating whether the Data Type ID exists

            Cursor res = db.rawQuery ("select count(1) from param_data_types where pdt_data_type_id = ?",new String[] {myCF.Int_ToString(gDataTypeID)});
            res.moveToFirst();
            int recordCount = res.getInt(0);
            if (recordCount == 0){
                Log.i(TAG, "DatabaseHelper/updateDataItem/entered datatypeid do not exists");
                return false;
            }

            // Checking the count of Data ID to know whether the same already exists in DB

            res = db.rawQuery ("select count(1) from param_data_items where pdi_data_type_id = ? and pdi_data_item_id = ?",new String[] {myCF.Int_ToString(gDataTypeID),myCF.Int_ToString(gDataItemID)});
            res.moveToFirst();
            recordCount = res.getInt(0);
            Log.i(TAG, "DatabaseHelper/updateDataItem/Checking whether the Data Type Item exists under the Data Type ID >> Count - " + myCF.Int_ToString(recordCount));

            if (recordCount == 0) {

                // If no record exists, inserting data

                ContentValues contentValues = new ContentValues();
                contentValues.put("pdi_date",gDate);
                contentValues.put("pdi_data_type_id",gDataTypeID);
                contentValues.put("pdi_data_item_id",gDataItemID);
                contentValues.put("pdi_data_item_desc",gDataItemDesc);
                contentValues.put("pdi_data_item_unit",gDataItemUnit);
                contentValues.put("pdi_display_order",gDisplayOrder);
                long result = db.insert("param_data_items",null,contentValues);
                if (result == -1){
                    Log.i(TAG, "DatabaseHelper/updateDataItem/DB Insert failed");
                    return false;
                }else{
                    Log.i(TAG, "DatabaseHelper/updateDataItem/DB Insert Success");
                    return true;
                }

            } else {

                // If a record exists, updating data

                ContentValues contentValues = new ContentValues();
                contentValues.put("pdi_date",gDate);
                contentValues.put("pdi_data_item_desc",gDataItemDesc);
                contentValues.put("pdi_data_item_unit",gDataItemUnit);
                contentValues.put("pdi_display_order",gDisplayOrder);
                db.update("param_data_items",contentValues,"pdi_data_type_id = ? and pdi_data_item_id = ?",new String[]{myCF.Int_ToString(gDataTypeID),myCF.Int_ToString(gDataItemID)});
                Log.i(TAG, "DatabaseHelper/updateDataItem/Data Item updated");
                return true;

            }


        } catch (Exception e) {

            Log.i(TAG, "DatabaseHelper/updateDataItem/error - " + e.getMessage());
            return false;

        }
    }

    public boolean updateData (String gDate, int gDataTypeID, int gDataItemID, String gData){

        myCF = new CommonFunctions();

        try {

            String sql = "";
            SQLiteDatabase db = this.getWritableDatabase();

            // Validating whether the Data Type ID exists

            Cursor res = db.rawQuery ("select count(1) from param_data_types where pdt_data_type_id = ?",new String[] {myCF.Int_ToString(gDataTypeID)});
            res.moveToFirst();
            int recordCount = res.getInt(0);
            if (recordCount == 0){
                Log.i(TAG, "DatabaseHelper/updateData/entered datatypeid do not exists");
                return false;
            }

            // Validating whether the Data Type ID + Data Item ID exists

            res = db.rawQuery ("select count(1) from param_data_items where pdi_data_type_id = ? and pdi_data_item_id = ?",new String[] {myCF.Int_ToString(gDataTypeID),myCF.Int_ToString(gDataItemID)});
            res.moveToFirst();
            recordCount = res.getInt(0);
            if (recordCount == 0){
                Log.i(TAG, "DatabaseHelper/updateData/entered datatypeid + dataitemid combination do not exists");
                return false;
            }

            // Validating whether a record already exists for the entry

            sql = "select count(1) from data_table where dt_date =  ? and dt_data_type_id = ? and dt_data_item_id = ?";
            Log.i(TAG, sql);
            res = db.rawQuery (sql,new String[] {gDate,myCF.Int_ToString(gDataTypeID),myCF.Int_ToString(gDataItemID)});
            res.moveToFirst();
            recordCount = res.getInt(0);
            Log.i(TAG, "DatabaseHelper/updateData/Checking whether an entry exists. Count - " + myCF.Int_ToString(recordCount));

            // Validating whether the Data Type ID + Data Item ID exists

            if (recordCount == 0) {

                // If no record exists, inserting data

                ContentValues contentValues = new ContentValues();
                contentValues.put("dt_date",gDate);
                contentValues.put("dt_data_type_id",gDataTypeID);
                contentValues.put("dt_data_item_id",gDataItemID);
                contentValues.put("dt_value",gData);
                long result = db.insert("data_table",null,contentValues);
                if (result == -1){
                    Log.i(TAG, "DatabaseHelper/updateData/DB Insert failed");
                    return false;
                }else{
                    Log.i(TAG, "DatabaseHelper/updateData/DB Insert Success");
                    return true;
                }

            } else {

                // If a record exists, updating data

                ContentValues contentValues = new ContentValues();
                contentValues.put("dt_value",gData);
                db.update("data_table",contentValues,"dt_date = ? and dt_data_type_id = ? and dt_data_item_id = ?", new String[]{gDate,myCF.Int_ToString(gDataTypeID),myCF.Int_ToString(gDataItemID)});
                Log.i(TAG, "DatabaseHelper/updateDataItem/Data Item updated");
                return true;

            }


        } catch (Exception e) {

            Log.i(TAG, "DatabaseHelper/updateData/error - " + e.getMessage());
            return false;

        }

    }

    public boolean updateWSLog (String gDate, String gInput1, String gInput2, String gOutput){

        myCF = new CommonFunctions();

        try {

            String sql = "";
            Cursor res;
            SQLiteDatabase db = this.getWritableDatabase();
            sql = "SELECT name FROM sqlite_master WHERE type='table' AND name = 'ws_result_log'";
            res = db.rawQuery(sql,null);
            if(res.getCount() == 0){
                db.execSQL("create table ws_result_log (ws_date datetime,ws_in1 int,ws_in2 int,ws_op text)");
                db.execSQL("create index ws_result_log_idx1 on ws_result_log (ws_date)");
            }
            else {
                res.moveToFirst();
                sql = res.getString(0);
            }

                // If no record exists, inserting data

                ContentValues contentValues = new ContentValues();
                contentValues.put("ws_date",gDate);
                contentValues.put("ws_in1",gInput1);
                contentValues.put("ws_in2",gInput2);
                contentValues.put("ws_op",gOutput);
                long result = db.insert("ws_result_log",null,contentValues);
                if (result == -1){
                    Log.i(TAG, "DatabaseHelper/updateData/DB Insert failed");
                    return false;
                }else{
                    Log.i(TAG, "DatabaseHelper/updateData/DB Insert Success");
                    return true;
                }

        } catch (Exception e) {

            Log.i(TAG, "DatabaseHelper/updateData/error - " + e.getMessage());
            return false;

        }

    }

    public String getDataTypeDesc (int gDataTypeID){

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select pdt_data_type_desc from param_data_types where pdt_data_type_id =  ?";
        Cursor res = db.rawQuery (sql,new String[] {myCF.Int_ToString(gDataTypeID)});
        res.moveToFirst();
        return res.getString(0);

    }

    public String getDataItemDesc (int gDataItemID){

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select pdi_data_item_desc from param_data_items where pdi_data_item_id =  ?";
        Cursor res = db.rawQuery (sql,new String[] {myCF.Int_ToString(gDataItemID)});
        res.moveToFirst();
        return res.getString(0);

    }

    public Cursor getDataTypeData (String gDataTypeID){

        String sql = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        if (gDataTypeID=="ALL"){

            sql = "select pdt_date,pdt_data_type_id,pdt_data_type_desc,pdt_data_capture_type,pdt_display_order from param_data_types order by pdt_display_order";
            res = db.rawQuery (sql,null);
        }else{

            sql = "select pdt_date,pdt_data_type_id,pdt_data_type_desc,pdt_data_capture_type,pdt_display_order from param_data_types where pdt_data_type_id = ?";
            res = db.rawQuery (sql,new String[] {gDataTypeID});
        }

        return res;

    }

    public Cursor getDataItemData (String gDataTypeID,String gDataItemID){

        String sql = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        if (gDataItemID=="ALL"){

            sql = "select pdi_date,pdi_data_type_id,pdi_data_item_id,pdi_data_item_desc,pdi_data_item_unit,pdi_display_order from param_data_items where pdi_data_type_id = ? order by pdi_display_order";
            res = db.rawQuery (sql,new String[] {gDataTypeID});

        }else{

            sql = "select pdi_date,pdi_data_type_id,pdi_data_item_id,pdi_data_item_desc,pdi_data_item_unit,pdi_display_order from param_data_items where pdi_data_type_id = ? and pdi_data_item_id = ?";
            res = db.rawQuery (sql,new String[] {gDataTypeID,gDataItemID});

        }

        return res;

    }

    public Cursor getData (String gDate,String gDataTypeID){

        String sql = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        sql = "select dt_date,dt_data_type_id,dt_data_item_id,dt_value from data_table join param_data_items on pdi_data_item_id = dt_data_item_id where dt_date = ? and dt_data_type_id = ? order by pdi_display_order";
        res = db.rawQuery (sql,new String[] {gDate,gDataTypeID});


        return res;

    }

    public String getDataTypeID (String gDataTypeDesc){

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select pdt_data_type_id from param_data_types where pdt_data_type_desc =  ?";
        Cursor res = db.rawQuery (sql,new String[] {gDataTypeDesc});
        res.moveToFirst();
        return res.getString(0);

    }

}
