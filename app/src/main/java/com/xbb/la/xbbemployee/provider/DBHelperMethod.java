package com.xbb.la.xbbemployee.provider;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xbb.la.modellibrary.bean.DIYProduct;
import com.xbb.la.modellibrary.bean.Recommand;
import com.xbb.la.modellibrary.bean.Reminder;
import com.xbb.la.modellibrary.bean.Transaction;
import com.xbb.la.modellibrary.config.Constant;
import com.xbb.la.modellibrary.utils.StringUtil;
import com.xbb.la.xbbemployee.config.BaseApplication;
import com.xbb.la.xbbemployee.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目:XBBEmployee
 * 作者：Templar
 * 创建时间：2015/11/7 11:05
 * 描述：
 */

public class DBHelperMethod {

    private static DBHelperMethod sInstance = null;

    private static String TAG = DBHelperMethod.class.getCanonicalName();

    private static SQLiteDatabase db;

    public DBHelperMethod() {
        db = new DBHelper(BaseApplication.getContext(), SharePreferenceUtil.getsharepre(BaseApplication.getContext())).getReadableDatabase();
        Log.i("fds", db.getPath());
    }

    public synchronized static DBHelperMethod getInstance() {
//        if (sInstance == null) {
//            synchronized (DBHelperMethod.class) {
        if (sInstance == null) {
            sInstance = new DBHelperMethod();
        }
//            }
//        }
        return sInstance;
    }

    public void deletePosition(String orderid,String uid){
        if (StringUtil.isEmpty(orderid) || StringUtil.isEmpty(uid))
            return ;
        int count = db.delete(Constant.DB.TABLE_POSITION, Constant.DB.PositionColumns.POSITION_ORDERID + "=? and " + Constant.DB.PositionColumns.POSITION_UID + " = ?", new String[]{
                orderid, uid
        });



    }

    public long insertPosition(double latitude, double longitude,String orderId,String uid) {

        ContentValues cv = new ContentValues();
        cv.put(Constant.DB.PositionColumns.POSITION_UID, uid);
        cv.put(Constant.DB.PositionColumns.POSITION_LNG, longitude);
        cv.put(Constant.DB.PositionColumns.POSITION_LAT, latitude);
        cv.put(Constant.DB.PositionColumns.POSITION_ORDERID, orderId);

        long rowId = db.insert(Constant.DB.TABLE_POSITION, null, cv);
        return rowId;
    }

    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        return db.query(table, columns, selection, selectionArgs, groupBy,
                having, orderBy, limit);
    }

    public void updateSql(String sql) {
        db.execSQL(sql);
    }

    public boolean insertTransaction(int step, Transaction transaction) {
        if (transaction == null)
            return false;
        ContentValues contentValues;
        long result;
        int count;
        switch (step) {
            case 1:
                boolean flag = false;
                contentValues = new ContentValues();
                contentValues.put(Constant.DB.TransactionColumns.ORDER_ID, transaction.getOrderId());
                contentValues.put(Constant.DB.TransactionColumns.EMPLOYEE_ID, transaction.getEmployeeId());
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_NORMAL_BEFORE, transaction.getNormalAlbumBefore());
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_DAMAGE_BEFORE, transaction.getDamageAlbumBefore());
                contentValues.put(Constant.DB.TransactionColumns.START_TIME, System.currentTimeMillis());
                contentValues.put(Constant.DB.TransactionColumns.CURRENT_STEP, 1);
                contentValues.put(Constant.DB.TransactionColumns.UPLOAD, 0);

                contentValues.put(Constant.DB.TransactionColumns.ALBUM_DAMAGE_AFTER, "");
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_NORMAL_AFTER, "");
                contentValues.put(Constant.DB.TransactionColumns.EMPLOYEE_RECOMMAND, 0);
                contentValues.put(Constant.DB.TransactionColumns.END_TIME, 0);
                contentValues.put(Constant.DB.TransactionColumns.EMPLOYEE_SUGGEST, "");
                if (!(flag = updateTransaction(1, transaction, false))) {
                    Log.v(TAG, "first time to insert");
                    result = db.insert(Constant.DB.TABLE_TRANSACTION, null, contentValues);
                    Log.v(TAG, "result:" + result);
                    if (result != -1)
                        return true;
                } else {
                    Log.v(TAG, "not first time to insert");
                    return true;
                }
                break;
            case 2:
                return updateTransaction(2, transaction, true);
            case 3:
                return updateTransaction(3, transaction, true);
            case 4:
                return updateTransaction(4, transaction, true);
            case 5:
                return updateTransaction(5, transaction, true);
        }
        return false;
    }

    public boolean updateTransaction(int step, Transaction transaction, boolean isFirst) {
        if (transaction == null)
            return false;
        ContentValues contentValues = null;
        long result;
        int count;
        switch (step) {
            case 1:
                contentValues = new ContentValues();
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_NORMAL_BEFORE, transaction.getNormalAlbumBefore());
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_DAMAGE_BEFORE, transaction.getDamageAlbumBefore());
                count = db.update(Constant.DB.TABLE_TRANSACTION, contentValues, Constant.DB.TransactionColumns.ORDER_ID + " = ? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ? ", new String[]{
                        transaction.getOrderId(), transaction.getEmployeeId()
                });
                if (count > 0)
                    return true;
                break;
            case 2:
                contentValues = new ContentValues();
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_NORMAL_AFTER, transaction.getNormalAlbumAfter());
                contentValues.put(Constant.DB.TransactionColumns.ALBUM_DAMAGE_AFTER, transaction.getDamageAlbumAfter());
                if (isFirst)
                    contentValues.put(Constant.DB.TransactionColumns.CURRENT_STEP, 2);
                count = db.update(Constant.DB.TABLE_TRANSACTION, contentValues, Constant.DB.TransactionColumns.ORDER_ID + " = ? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ? ", new String[]{
                        transaction.getOrderId(), transaction.getEmployeeId()});
                Log.v(TAG, "end_transaction:" + contentValues.toString() + " orderId:" + transaction.getOrderId() + " userId:" + transaction.getEmployeeId());
                Log.v(TAG, "count:" + count);
                if (count > 0)
                    return true;
                break;
            case 3:
                contentValues = new ContentValues();
                contentValues.put(Constant.DB.TransactionColumns.EMPLOYEE_RECOMMAND, transaction.getRecommand());
                if (isFirst) {
                    contentValues.put(Constant.DB.TransactionColumns.END_TIME, System.currentTimeMillis());
                    contentValues.put(Constant.DB.TransactionColumns.CURRENT_STEP, 3);
                }
                count = db.update(Constant.DB.TABLE_TRANSACTION, contentValues, Constant.DB.TransactionColumns.ORDER_ID + " = ? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ?", new String[]{
                        transaction.getOrderId(), transaction.getEmployeeId()
                });
                if (count > 0)
                    return true;
                break;
            case 4:
                contentValues = new ContentValues();
                contentValues.put(Constant.DB.TransactionColumns.EMPLOYEE_SUGGEST, transaction.getSuggestion());
                if (!isFirst) {
                    contentValues.put(Constant.DB.TransactionColumns.CURRENT_STEP, 4);
                    contentValues.put(Constant.DB.TransactionColumns.END_TIME, System.currentTimeMillis());
                }
                count = db.update(Constant.DB.TABLE_TRANSACTION, contentValues, Constant.DB.TransactionColumns.ORDER_ID + "=? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ?", new String[]{
                        transaction.getOrderId(), transaction.getEmployeeId()
                });
                if (count > 0)
                    return true;
                break;
            case 5:
                contentValues = new ContentValues();
                if (!isFirst)
                    contentValues.put(Constant.DB.TransactionColumns.CURRENT_STEP, 5);
                count = db.update(Constant.DB.TABLE_TRANSACTION, contentValues, Constant.DB.TransactionColumns.ORDER_ID + "=? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ?", new String[]{
                        transaction.getOrderId(), transaction.getEmployeeId()
                });
                if (count > 0)
                    return true;
                break;

        }

        return false;
    }

    public boolean deleteTransaction(String orderId, String employeeId) {
        if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(employeeId))
            return false;
        int count = db.delete(Constant.DB.TABLE_TRANSACTION, Constant.DB.TransactionColumns.ORDER_ID + "=? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ?", new String[]{
                orderId, employeeId
        });
        if (count > 0)
            return true;
        return false;
    }

    public Transaction getTransaction(String orderId, String employeeId) {
        if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(employeeId))
            return null;
        Transaction transaction = null;
        Cursor cursor = db.query(Constant.DB.TABLE_TRANSACTION, null, Constant.DB.TransactionColumns.ORDER_ID + "=? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ?", new String[]{
                orderId, employeeId
        }, null, null, null);
        Log.v(TAG, "cursor.getCount()=" + cursor.getCount());
        while (cursor.moveToNext()) {
            transaction = new Transaction();
            transaction.setOrderId(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.ORDER_ID)));
            transaction.setDamageAlbumBefore(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.ALBUM_DAMAGE_BEFORE)));
            transaction.setNormalAlbumBefore(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.ALBUM_NORMAL_BEFORE)));
            transaction.setDamageAlbumAfter(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.ALBUM_DAMAGE_AFTER)));
            transaction.setNormalAlbumAfter(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.ALBUM_NORMAL_AFTER)));
            transaction.setRecommand(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.EMPLOYEE_RECOMMAND)));
            transaction.setSuggestion(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.EMPLOYEE_SUGGEST)));
            transaction.setStartTime(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.START_TIME)));
            transaction.setEndTime(cursor.getString(cursor.getColumnIndex(Constant.DB.TransactionColumns.END_TIME)));
            transaction.setCurrentStep(cursor.getInt(cursor.getColumnIndex(Constant.DB.TransactionColumns.CURRENT_STEP)));
            transaction.setEmployeeId(employeeId);
        }
        if (cursor != null && !cursor.isClosed())
            cursor.close();
        return transaction;
    }

    public boolean insertDIYProducts(List<DIYProduct> diyProducts) {
        if (diyProducts == null || diyProducts.isEmpty())
            return false;
        Log.v(TAG, "begin delete diy");
        clearTable(Constant.DB.TABLE_DIY, null, null);
        Log.v(TAG, "begin insert diy");
        for (DIYProduct diyProduct : diyProducts) {
            if (diyProduct != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constant.DB.DIYColumns.DIY_ID, diyProduct.getId());
                contentValues.put(Constant.DB.DIYColumns.DIY_NAME, diyProduct.getP_name());
                contentValues.put(Constant.DB.DIYColumns.DIY_SELECTED_IMG, diyProduct.getP_ximg());
                contentValues.put(Constant.DB.DIYColumns.DIY_UNSELECT_IMG, diyProduct.getP_wimg());

                db.insert(Constant.DB.TABLE_DIY, null, contentValues);

            }
        }
        return true;
    }

    public List<DIYProduct> getLocalDIYProductList() {
        Cursor cursor = db.query(Constant.DB.TABLE_DIY, null, null, null, null, null, null);
        List<DIYProduct> diyProductList = null;
        if (cursor != null) {
            Log.v(TAG, "visible-diy:" + cursor.getCount());
            diyProductList = new ArrayList<DIYProduct>();
            while (cursor.moveToNext()) {
                DIYProduct diyProduct = new DIYProduct();
                diyProduct.setP_ximg(cursor.getString(cursor.getColumnIndex(Constant.DB.DIYColumns.DIY_SELECTED_IMG)));
                diyProduct.setId(cursor.getString(cursor.getColumnIndex(Constant.DB.DIYColumns.DIY_ID)));
                diyProduct.setP_wimg(cursor.getString(cursor.getColumnIndex(Constant.DB.DIYColumns.DIY_UNSELECT_IMG)));
                diyProduct.setP_name(cursor.getString(cursor.getColumnIndex(Constant.DB.DIYColumns.DIY_NAME)));
                diyProductList.add(diyProduct);
            }
        }
        if (cursor != null && !cursor.isClosed())
            cursor.close();
        return diyProductList;
    }

    public boolean insertRemminders(List<Reminder> reminderList) {
        if (reminderList == null || reminderList.isEmpty())
            return false;
        Log.v(TAG, "begin delete diy");
        clearTable(Constant.DB.TABLE_REMINDER, null, null);
        Log.v(TAG, "begin insert diy");
        for (Reminder reminder : reminderList) {
            if (reminder != null) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(Constant.DB.ReminderColumns.NOTICE_TITLE, reminder.getTitle());
                contentValues.put(Constant.DB.ReminderColumns.NOTICE_CONTENT, reminder.getReminderText());
                contentValues.put(Constant.DB.ReminderColumns.NOTICE_THUMB, reminder.getThumb());

                db.insert(Constant.DB.TABLE_REMINDER, null, contentValues);

            }
        }
        return true;
    }

    public List<Reminder> getReminderList() {
        Cursor cursor = db.query(Constant.DB.TABLE_REMINDER, null, null, null, null, null, null);
        List<Reminder> reminderList = null;
        if (cursor != null) {
            Log.v(TAG, "visible-diy:" + cursor.getCount());
            reminderList = new ArrayList<Reminder>();
            while (cursor.moveToNext()) {
                Reminder reminder = new Reminder();
                reminder.setThumb(cursor.getString(cursor.getColumnIndex(Constant.DB.ReminderColumns.NOTICE_THUMB)));
                reminder.setId(cursor.getString(cursor.getColumnIndex(Constant.DB.ReminderColumns._ID)));
                reminder.setTitle(cursor.getString(cursor.getColumnIndex(Constant.DB.ReminderColumns.NOTICE_TITLE)));
                reminder.setReminderText(cursor.getString(cursor.getColumnIndex(Constant.DB.ReminderColumns.NOTICE_CONTENT)));
                reminderList.add(reminder);
            }
        }
        if (cursor != null && !cursor.isClosed())
            cursor.close();
        return reminderList;
    }

    public void clearTable(String table, String where, String[] whereArgs) {
        if (isTableExists(table))
            db.delete(table, where, whereArgs);
    }

    public boolean isTableExists(String table) {
        if (StringUtil.isEmpty(table))
            return false;
        Cursor cursor = db.rawQuery("select name from sqlite_master where type='table';", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            Log.i(TAG, name);
            if (table.equals(name))
                return true;

        }
        return false;
    }

    public List<Recommand> getRecommandList(String orderId, String userId) {
        if (StringUtil.isEmpty(orderId) || StringUtil.isEmpty(userId))
            return null;
        List<Recommand> recommandList = null;
        Cursor cursor = db.query(Constant.DB.TABLE_RECOMMAND, null, Constant.DB.RecommandColumns.RECOMMAND_ORDER_ID + "=? and " + Constant.DB.RecommandColumns.RECOMMAND_EMPLOYEE_ID + " = ?", new String[]{
                orderId, userId}, null, null, null, null);
        if (cursor != null) {
            Log.v(TAG, "visible-recommand:" + cursor.getCount());
            recommandList = new ArrayList<Recommand>();
            while (cursor.moveToNext()) {
                Recommand recommand = new Recommand();
                DIYProduct diy = new DIYProduct();
                diy.setP_name(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns.RECOMMAND_DIY_NAME)));
                diy.setP_ximg(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns.RECOMMAND_DIY_XIMG)));
                diy.setId(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns.RECOMMAND_DIY_ID)));
                diy.setP_wimg(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns.RECOMMAND_DIY_WIMG)));
                recommand.setRecommandId(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns._ID)));
                recommand.setSelectedDIYProduct(diy);
                recommand.setIntroAlbum(StringUtil.covertStringToStringList(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns.RECOMMAND_INTRO_ALBUM))));
                recommand.setRemark(cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns.RECOMMAND_REMARK)));
                recommandList.add(recommand);
            }
        }
        if (cursor != null && !cursor.isClosed())
            cursor.close();
        return recommandList;
    }

    public boolean insertRecommands(List<Recommand> recommandList, String userId, String orderId, boolean isInsert) {
        if (recommandList == null || recommandList.isEmpty() || StringUtil.isEmpty(userId) || StringUtil.isEmpty(orderId))
            return false;
//        Log.v(TAG, "begin delete recommand");
        clearTable(Constant.DB.TABLE_RECOMMAND, null, null);
        String recommandContent = "";
//        Log.v(TAG, "begin insert recommand");
        for (Recommand recommand : recommandList) {
           /* Transaction transaction = new Transaction();
            transaction.setEmployeeId(userId);
            transaction.setOrderId(orderId);
            transaction.setIntroAlbum(recommand.getIntroAlbum());
            transaction.setSelectedDIYProduct(recommand.getSelectedDIYProduct());
            transaction.setRemark(recommand.getRemark());
            insertTransaction(3, transaction);*/
            if (recommand != null) {
                ContentValues contentValues = new ContentValues();
                DIYProduct diy = recommand.getSelectedDIYProduct();
                if (diy != null) {
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_DIY_ID, diy.getId());
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_ORDER_ID, orderId);
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_EMPLOYEE_ID, userId);
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_DIY_NAME, diy.getP_name());
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_DIY_WIMG, diy.getP_wimg());
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_DIY_XIMG, diy.getP_ximg());
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_REMARK, recommand.getRemark());
                    contentValues.put(Constant.DB.RecommandColumns.RECOMMAND_INTRO_ALBUM, StringUtil.covertStringListToString(recommand.getIntroAlbum()));
                    db.insert(Constant.DB.TABLE_RECOMMAND, null, contentValues);
                }
            }
        }
        Cursor cursor = db.query(Constant.DB.TABLE_RECOMMAND, null, Constant.DB.RecommandColumns.RECOMMAND_ORDER_ID + "=? and " + Constant.DB.RecommandColumns.RECOMMAND_EMPLOYEE_ID + " = ?", new String[]{
                orderId, userId}, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                recommandContent = recommandContent + "~" + cursor.getString(cursor.getColumnIndex(Constant.DB.RecommandColumns._ID));
            }
        }
        if (recommandContent.endsWith("~"))
            recommandContent = recommandContent.substring(0, recommandContent.length() - 1);
        if (recommandContent.startsWith("~"))
            recommandContent = recommandContent.substring(1, recommandContent.length());
        Transaction transaction = new Transaction();
        transaction.setEmployeeId(userId);
        transaction.setOrderId(orderId);
        transaction.setRecommand(recommandContent);
        boolean flag;
        if (isInsert)
            flag = insertTransaction(3, transaction);
        else
            flag = updateTransaction(3, transaction, false);
        return flag;
    }

    public boolean skipRecommand(String orderId, String employeeId) {
        if (StringUtil.isEmpty(employeeId) || StringUtil.isEmpty(orderId))
            return false;
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.DB.TransactionColumns.CURRENT_STEP, 3);
        contentValues.put(Constant.DB.TransactionColumns.END_TIME, System.currentTimeMillis());
        int flag = db.update(Constant.DB.TABLE_TRANSACTION, contentValues, Constant.DB.TransactionColumns.ORDER_ID + " = ? and " + Constant.DB.TransactionColumns.EMPLOYEE_ID + " = ? ", new String[]{
                orderId, employeeId});
        Log.v(TAG, "flag=" + flag);
        if (flag > 0)
            return true;
        return false;

    }
}
