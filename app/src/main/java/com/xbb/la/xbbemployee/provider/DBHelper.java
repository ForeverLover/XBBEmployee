package com.xbb.la.xbbemployee.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xbb.la.modellibrary.config.Constant;

public class DBHelper extends SQLiteOpenHelper {
    private final static String debugTAG = "xmppchatclient";
    private final static String DB_NAME = "xbbEmployee.db";
    private final static int VERSION = 1;

    public DBHelper(Context context, String name, CursorFactory factory,
                    int version) {
        super(context, name, factory, version);
    }

    public DBHelper(Context context, String currentname) {

        super(context, /*currentname + "&" + */Constant.DB.DB_NAME, null, VERSION);
        Log.i("db---DBHelper", "name:" + currentname);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Constant.DB.TABLE_POSITION + "("
                + Constant.DB.PositionColumns._ID + " integer primary key,"
                + Constant.DB.PositionColumns.POSITION_LAT + " text, "
                + Constant.DB.PositionColumns.POSITION_LNG+ " text,"
                + Constant.DB.PositionColumns.POSITION_ORDERID + " text,"
                + Constant.DB.PositionColumns.POSITION_UID + " text)");

        db.execSQL("create table " + Constant.DB.TABLE_TRANSACTION + "("
                + Constant.DB.TransactionColumns._ID + " integer primary key autoincrement,"
                + Constant.DB.TransactionColumns.ORDER_ID + " text, "
                + Constant.DB.TransactionColumns.EMPLOYEE_ID + " text,"
                + Constant.DB.TransactionColumns.ALBUM_NORMAL_BEFORE + " text,"
                + Constant.DB.TransactionColumns.ALBUM_DAMAGE_BEFORE + " text,"
                + Constant.DB.TransactionColumns.ALBUM_NORMAL_AFTER + " text,"
                + Constant.DB.TransactionColumns.ALBUM_DAMAGE_AFTER + " text,"
                + Constant.DB.TransactionColumns.EMPLOYEE_RECOMMAND + " text,"
                + Constant.DB.TransactionColumns.START_TIME + " text,"
                + Constant.DB.TransactionColumns.END_TIME + " text,"
                + Constant.DB.TransactionColumns.UPLOAD + " integer,"
                + Constant.DB.TransactionColumns.EMPLOYEE_SUGGEST + " integer,"
                + Constant.DB.TransactionColumns.CURRENT_STEP + " integer)");


        db.execSQL("create table " + Constant.DB.TABLE_DIY + "("
                + Constant.DB.DIYColumns._ID + " integer primary key,"
                + Constant.DB.DIYColumns.DIY_ID + " text, "
                + Constant.DB.DIYColumns.DIY_NAME + " text,"
                + Constant.DB.DIYColumns.DIY_SELECTED_IMG + " text,"
                + Constant.DB.DIYColumns.DIY_UNSELECT_IMG + " text)");
        db.execSQL("create table " + Constant.DB.TABLE_REMINDER + "("
                + Constant.DB.ReminderColumns._ID + " integer primary key,"
                + Constant.DB.ReminderColumns.NOTICE_ID + " text, "
                + Constant.DB.ReminderColumns.NOTICE_TITLE + " text,"
                + Constant.DB.ReminderColumns.NOTICE_CONTENT + " text,"
                + Constant.DB.ReminderColumns.NOTICE_THUMB + " text)");
        db.execSQL("create table " + Constant.DB.TABLE_RECOMMAND + "("
                + Constant.DB.RecommandColumns._ID + " integer primary key,"
                + Constant.DB.RecommandColumns.RECOMMAND_DIY_ID + " text, "
                + Constant.DB.RecommandColumns.RECOMMAND_ORDER_ID + " text, "
                + Constant.DB.RecommandColumns.RECOMMAND_EMPLOYEE_ID + " text, "
                + Constant.DB.RecommandColumns.RECOMMAND_DIY_NAME + " text,"
                + Constant.DB.RecommandColumns.RECOMMAND_DIY_WIMG + " text,"
                + Constant.DB.RecommandColumns.RECOMMAND_DIY_XIMG + " text,"
                + Constant.DB.RecommandColumns.RECOMMAND_INTRO_ALBUM + " text,"
                + Constant.DB.RecommandColumns.RECOMMAND_REMARK + " text)");

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Constant.DB.TABLE_POSITION);
            db.execSQL("DROP TABLE IF EXISTS " + Constant.DB.TABLE_TRANSACTION);
            db.execSQL("DROP TABLE IF EXISTS " + Constant.DB.TABLE_DIY);
            db.execSQL("DROP TABLE IF EXISTS " + Constant.DB.TABLE_REMINDER);
            db.execSQL("DROP TABLE IF EXISTS " + Constant.DB.TABLE_RECOMMAND);
            // Create tables
            onCreate(db);
            Log.i("", "重新建表成功");
        }

    }

}
