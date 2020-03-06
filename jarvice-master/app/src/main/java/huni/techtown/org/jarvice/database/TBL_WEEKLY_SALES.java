package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.WeeklySalesObject;

/**
 *
 * 주간 데이터
 *
 * */
public class TBL_WEEKLY_SALES extends TABLE<WeeklySalesObject> {
    private String TAG = TBL_WEEKLY_SALES.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_weekly_sales";

    /*** 21개 ***/
    /*** Column 이름 정의 **********************/
    public static final String ID					            = "_id";
    public static final String SELL_YEAR		                = "sell_year";                  // 년
    public static final String SELL_WEEK		                = "sell_week";                  // 몇번쨰주?
    public static final String START_WEEK			            = "start_week";                 // 시작주
    public static final String END_WEEK			                = "end_week";                   // 끝주
    public static final String SELL_REAL			            = "sell_real";                  // 실매출

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
            "(" +
                ID						+ " INTEGER NOT NULL UNIQUE PRIMARY KEY," +
                SELL_YEAR			    + " TEXT," +
                SELL_WEEK               + " TEXT," +
                START_WEEK				+ " TEXT," +
                END_WEEK			    + " TEXT," +
                SELL_REAL			    + " TEXT " +
                ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, SELL_YEAR, SELL_WEEK, START_WEEK, END_WEEK, SELL_REAL;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.SELL_YEAR               = c.getColumnIndex(SELL_YEAR);
        idx.SELL_WEEK               = c.getColumnIndex(SELL_WEEK);
        idx.START_WEEK              = c.getColumnIndex(START_WEEK);
        idx.END_WEEK                = c.getColumnIndex(END_WEEK);
        idx.SELL_REAL               = c.getColumnIndex(SELL_REAL);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_WEEKLY_SALES(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }

    /**
     * 오브젝트를 DailySalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(WeeklySalesObject o) {
        ContentValues values = new ContentValues();

        values.put(ID,                      o.getId());
        values.put(SELL_YEAR,               o.getSellYear());
        values.put(SELL_WEEK,               o.getSellWeek());
        values.put(START_WEEK,              o.getStartWeek());
        values.put(END_WEEK,                o.getEndWeek());
        values.put(SELL_REAL,               o.getSellReal());

        return values;
    }

    @Override
    public List<WeeklySalesObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<WeeklySalesObject> list = new ArrayList<WeeklySalesObject>();
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
    public WeeklySalesObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        WeeklySalesObject o = new WeeklySalesObject();
        if (idx.ID != -1) o.setId(c.getLong(idx.ID));
        if (idx.SELL_YEAR != -1) o.setSellYear(c.getString(idx.SELL_YEAR));
        if (idx.SELL_WEEK != -1) o.setSellWeek(c.getString(idx.SELL_WEEK));
        if (idx.START_WEEK != -1) o.setStartWeek(c.getString(idx.START_WEEK));
        if (idx.END_WEEK != -1) o.setEndWeek(c.getString(idx.END_WEEK));
        if (idx.SELL_REAL != -1) o.setSellReal(c.getString(idx.SELL_REAL));

        return o;
    }

    public void insert(List<WeeklySalesObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (WeeklySalesObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<WeeklySalesObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (WeeklySalesObject contact : addList) {
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

    public List<WeeklySalesObject> getList() {
        return select(null, null, null, null, null);
    }

    private String getInString(List<Long> delList) {
        StringBuffer buffer_select = new StringBuffer("in (");
        for (int i=0;i<delList.size();i++) {
            buffer_select.append(delList.get(i));
            if (i == delList.size() - 1) {
                buffer_select.append(")");
            } else {
                buffer_select.append(",");
            }
        }
        return buffer_select.toString();
    }


    public List<WeeklySalesObject> getWeeklyLastDataCheck(String year, String weekCheck) {

        String sql = "SELECT * FROM " + tableName + " WHERE " +
                SELL_YEAR + " == " + year +  " AND " + SELL_WEEK + " == " + weekCheck + " ORDER BY " + ID + " DESC LIMIT 3";


        return select_raw(sql);
    }

    public List<WeeklySalesObject> getWeeklyLastData(String id) {

        String sql = "SELECT * FROM " + tableName + " WHERE " + ID + " <= " + id + " ORDER BY " + ID + " DESC LIMIT 3";

        return select_raw(sql);
    }
}
