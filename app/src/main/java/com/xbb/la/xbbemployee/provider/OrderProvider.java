package com.xbb.la.xbbemployee.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/6 17:35
 * 描述：订单数据库相关
 */

public class OrderProvider extends ContentProvider{
    private static final String AUTHOTITY = OrderProvider.class.getCanonicalName();

    /**
     * 联系人表
     */
    private static final String ORDER_TABLE = "order";

    /**
     * 联系人数据库
     */
    private static final String DB_NAME = "order.db";

    /**
     * 联系人数据库版本
     */
    private static final int DB_VERSION = 1;

    /**
     * 联系人uri
     */
    public static final Uri ORDER_URI = Uri.parse("content://" + AUTHOTITY + "/" + ORDER_TABLE);

    private SQLiteOpenHelper dbHelper;

    private SQLiteDatabase db;

    private static UriMatcher URI_MATCHER;

    /**
     * UriMatcher 匹配值
     */
    public static final int CONTACTS = 1;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHOTITY, ORDER_TABLE, CONTACTS);
    }
    @Override
    public boolean onCreate() {
        dbHelper = new OrderDatabaseHelper(getContext());
        return (dbHelper == null) ? false : true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Cursor cursor = null;
        switch (URI_MATCHER.match(uri)) {
            case CONTACTS:
                qb.setTables(ORDER_TABLE);
                cursor = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                //

                break;
        }
        if (cursor != null)
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db = dbHelper.getWritableDatabase();
        Uri result = null;
        switch (URI_MATCHER.match(uri)) {
            case CONTACTS:
                long rowId = db.insert(ORDER_TABLE,null, values);
                result = ContentUris.withAppendedId(uri, rowId);
                break;
            default:
                //
                break;
        }
        if (result != null)
            getContext().getContentResolver().notifyChange(result, null);
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int count = 0;
        switch (URI_MATCHER.match(uri)) {
            case CONTACTS:
                count = db.delete(ORDER_TABLE, selection, selectionArgs);
                break;
            default:
                //
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        db = dbHelper.getWritableDatabase();
        int count = 0;
        switch (URI_MATCHER.match(uri)) {
            case CONTACTS:
                count = db.update(ORDER_TABLE, values, selection, selectionArgs);
                break;
            default:
                //
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    /**
     * 订单数据库
     */
    private class OrderDatabaseHelper extends SQLiteOpenHelper {
        public OrderDatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + ORDER_TABLE + " ("
                    + OrderColumns._ID + " INTEGER PRIMARY KEY , "
                   /* + OrderColumns.AVATAR + " BLOB, "*/
                    + OrderColumns.ORDER_ID + " TEXT, "
                   /* + OrderColumns.ORDER_NO + " TEXT, "*/
                    + OrderColumns.ORDER_NO + " TEXT);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + ORDER_TABLE);
            onCreate(db);
        }
    }

    /**
     * 订单列
     */
    public static class OrderColumns implements BaseColumns {
        /**
         * 订单id
         */
        public static final String ORDER_ID = "orderId";
        /**
         * 订单编号
         */
        public static final String ORDER_NO = "orderNo";


    }
}
