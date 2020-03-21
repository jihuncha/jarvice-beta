package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.HelperNotificationObject;

/**
 *
 * 공지 테이블
 *
 * */
public class TBL_HELPER_NOTIFICATION extends TABLE <HelperNotificationObject> {
    private String TAG = TBL_HELPER_NOTIFICATION.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_helper_notification";

    /*** Column 이름 정의 **********************/
    public static final String ID					            = "_id";
    public static final String NOTI_DATE		                = "noti_date";                  // 날짜 - 년월일로
    public static final String NOTI_CHECK                       = "noti_check";                 // 체크여부
    public static final String NOTI_INFO                        = "noti_info";                  // 노티 내용

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
            "(" +
            ID						+ " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NOTI_DATE               + " TEXT," +
            NOTI_CHECK              + " TEXT," +
            NOTI_INFO               + " INTEGER " +
            ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, NOTI_DATE, NOTI_CHECK, NOTI_INFO;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.NOTI_DATE               = c.getColumnIndex(NOTI_DATE);
        idx.NOTI_CHECK              = c.getColumnIndex(NOTI_CHECK);
        idx.NOTI_INFO               = c.getColumnIndex(NOTI_INFO);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_HELPER_NOTIFICATION(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

    /**
     * 오브젝트를 DailySalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(HelperNotificationObject o) {
        ContentValues values = new ContentValues();

        //autoIncrement의 경우 id를 set하지말자!!
//        values.put(ID,                      o.getId());
        values.put(NOTI_DATE,               o.getNotiDate());
        values.put(NOTI_CHECK,              o.getNotiCheck());
        values.put(NOTI_INFO,               o.getNotiInfo());

        return values;
    }

    @Override
    public List<HelperNotificationObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<HelperNotificationObject> list = new ArrayList<HelperNotificationObject>();
        try {
            if (c.moveToFirst()) {
                INDEX idx = cursorToIndex(c);
                do {
                    list.add(fetchCursor2Object(idx, c));
                }
                while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 커서를 받아 리스트로 반환한다.
     * @param c
     * @return
     * @throws Exception
     */
    public HelperNotificationObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        HelperNotificationObject o = new HelperNotificationObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.NOTI_DATE != -1) o.setNotiDate(c.getString(idx.NOTI_DATE));
        if (idx.NOTI_CHECK != -1) o.setNotiCheck(c.getInt(idx.NOTI_CHECK));
        if (idx.NOTI_INFO != -1) o.setNotiInfo(c.getString(idx.NOTI_INFO));

        return o;
    }

    public void insert(List<HelperNotificationObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (HelperNotificationObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<HelperNotificationObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (HelperNotificationObject contact : addList) {
                if (insert(contact) > 0) {
                    count++;
                }
            }
            db.setTransactionSuccessful();
            Log.d(TABLE_NAME, "insertForSync() - count: " + count);
            return count;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            db.endTransaction();
        }

        Log.d(TABLE_NAME, "insertForSync() - count: " + -1);

        return -1;
    }

    public List<HelperNotificationObject> getList() {
        return select(null, null, null, null, null);
    }

    public boolean updateNotification(HelperNotificationObject input) {
        Log.v(TABLE_NAME, "updateNotification() - input: " + input);
        String where = ID + " = " + input.getId();
        return update(input, where, null);
    }

    public int deleteNotification(HelperNotificationObject input) {
        Log.v(TABLE_NAME, "updateNotification() - input: " + input);
        String where = ID + " = " + input.getId();
        return delete(where, null);
    }

    public List<HelperNotificationObject> getListDate(String inputDate) {
        Log.v(TABLE_NAME, "getListDate() - inputDate: " + inputDate);
        String whereClause = "SELECT * FROM " + tableName + " WHERE " + NOTI_DATE + " = '" + inputDate + "'";
        return select_raw(whereClause);
    }

}
