package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.HelperTodayPeopleObject;

/**
 *
 * 오늘의 사람 테이블
 *
 * */
public class TBL_HELPER_TODAY_PEOPLE extends TABLE <HelperTodayPeopleObject> {
    private String TAG = TBL_HELPER_TODAY_PEOPLE.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_helper_today_people";

    /*** Column 이름 정의 **********************/
    public static final String ID					            = "_id";
    public static final String PEOPLE_DATE		                = "people_date";                // 날짜 - 년월일로
    public static final String NOTI_PEOPLE                      = "noti_people";                // 업무하는 사람
    public static final String NOTI_WORK                        = "noti_work";                  // 업무 내용
    public static final String NOTI_TIME                        = "noti_time";                  // 업무 시간

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
            "(" +
            ID						    + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            PEOPLE_DATE                 + " TEXT," +
            NOTI_PEOPLE                 + " TEXT," +
            NOTI_WORK                   + " TEXT," +
            NOTI_TIME                   + " TEXT " +
            ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, PEOPLE_DATE, NOTI_PEOPLE, NOTI_WORK, NOTI_TIME;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                          = c.getColumnIndex(ID);
        idx.PEOPLE_DATE                 = c.getColumnIndex(PEOPLE_DATE);
        idx.NOTI_PEOPLE                 = c.getColumnIndex(NOTI_PEOPLE);
        idx.NOTI_WORK                   = c.getColumnIndex(NOTI_WORK);
        idx.NOTI_TIME                   = c.getColumnIndex(NOTI_TIME);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_HELPER_TODAY_PEOPLE(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

    /**
     * 오브젝트를 DailySalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(HelperTodayPeopleObject o) {
        ContentValues values = new ContentValues();

//        values.put(ID,                      o.getId());
        values.put(PEOPLE_DATE,             o.getPeopleDate());
        values.put(NOTI_PEOPLE,             o.getNotiPeople());
        values.put(NOTI_WORK,               o.getNotiWork());
        values.put(NOTI_TIME,               o.getNotiTime());

        return values;
    }

    @Override
    public List<HelperTodayPeopleObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<HelperTodayPeopleObject> list = new ArrayList<HelperTodayPeopleObject>();
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
    public HelperTodayPeopleObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        HelperTodayPeopleObject o = new HelperTodayPeopleObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.PEOPLE_DATE != -1) o.setPeopleDate(c.getString(idx.PEOPLE_DATE));
        if (idx.NOTI_PEOPLE != -1) o.setNotiPeople(c.getString(idx.NOTI_PEOPLE));
        if (idx.NOTI_WORK != -1) o.setNotiWork(c.getString(idx.NOTI_WORK));
        if (idx.NOTI_TIME != -1) o.setNotiTime(c.getString(idx.NOTI_TIME));

        return o;
    }

    public void insert(List<HelperTodayPeopleObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (HelperTodayPeopleObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<HelperTodayPeopleObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (HelperTodayPeopleObject contact : addList) {
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

    public List<HelperTodayPeopleObject> getList() {
        return select(null, null, null, null, null);
    }

    public boolean updateTodayPeople(HelperTodayPeopleObject input) {
        Log.v(TABLE_NAME, "updateNotification() - input: " + input);
        String where = ID + " = " + input.getId();
        return update(input, where, null);
    }

    public int deleteTodayPeople(HelperTodayPeopleObject input) {
        Log.v(TABLE_NAME, "updateNotification() - input: " + input);
        String where = ID + " = " + input.getId();
        return delete(where, null);
    }

}
