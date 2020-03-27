package huni.techtown.org.jarvice.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.common.data.MonthSalesObject;

/**
 *
 * 월 매출 테이블
 *
 * */
public class TBL_MONTH_SALES extends TABLE <MonthSalesObject> {
    private String TAG = TBL_MONTH_SALES.class.getSimpleName();

    /*** Table 이름 정의 *******************/
    public static final String TABLE_NAME = "tbl_month_sales";

    /*** Column 이름 정의 **********************/
    public static final String ID					= "_id";
    public static final String SELL_YEAR		    = "sell_year";            // 년
    public static final String SELL_MONTH			= "sell_month";           // 월
    public static final String SELL_REAL			= "sell_real";            // 실매출
    public static final String SELL_CARD			= "sell_card";            // 카드매출
    public static final String SELL_CASH			= "sell_cash";            // 현금매출

    /*** Table 생성 쿼리 **********************/
    public static final String CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " " +
                "(" +
                ID						    + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                SELL_YEAR			        + " TEXT," +
                SELL_MONTH				    + " TEXT," +
                SELL_REAL			        + " TEXT," +
                SELL_CARD			        + " TEXT," +
                SELL_CASH			        + " TEXT " +
                ");";

    /*** Table 생성 쿼리 **********************/
    public static final String DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /*** INDEX 정의 **********************/
    protected static class INDEX {
        public int ID, SELL_YEAR, SELL_MONTH, SELL_REAL, SELL_CARD, SELL_CASH;
    }

    protected static INDEX cursorToIndex(Cursor c) throws Exception {
        INDEX idx = new INDEX();

        idx.ID                      = c.getColumnIndex(ID);
        idx.SELL_YEAR               = c.getColumnIndex(SELL_YEAR);
        idx.SELL_MONTH              = c.getColumnIndex(SELL_MONTH);
        idx.SELL_REAL               = c.getColumnIndex(SELL_REAL);
        idx.SELL_CARD               = c.getColumnIndex(SELL_CARD);
        idx.SELL_CASH               = c.getColumnIndex(SELL_CASH);

        return idx;
    }

    /*** 생성자 *************************/
    public TBL_MONTH_SALES(SQLiteDatabase db) {
        super(db, TABLE_NAME);
    }


    /**
     * 오브젝트를 SalesObject 변환한다.
     * @param o
     * @return
     */
    @Override
    public ContentValues fetchObject2Values(MonthSalesObject o) {
        ContentValues values = new ContentValues();

        values.put(ID,                      o.getId());
        values.put(SELL_YEAR,               o.getSellYear());
        values.put(SELL_MONTH,              o.getSellMonth());
        values.put(SELL_REAL,               o.getSellReal());
        values.put(SELL_CARD,               o.getSellCard());
        values.put(SELL_CASH,               o.getSellCash());

        return values;
    }

    @Override
    public List<MonthSalesObject> fetchCursor2List(Cursor c) throws Exception {
        ArrayList<MonthSalesObject> list = new ArrayList<MonthSalesObject>();
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
    public MonthSalesObject fetchCursor2Object(INDEX idx, Cursor c) throws Exception {
        MonthSalesObject o = new MonthSalesObject();
        if (idx.ID != -1)           o.setId(c.getLong(idx.ID));
        if (idx.SELL_YEAR != -1)    o.setSellYear(c.getString(idx.SELL_YEAR));
        if (idx.SELL_MONTH != -1)   o.setSellMonth(c.getString(idx.SELL_MONTH));
        if (idx.SELL_REAL != -1)    o.setSellReal(c.getString(idx.SELL_REAL));
        if (idx.SELL_CARD != -1)    o.setSellCard(c.getString(idx.SELL_CARD));
        if (idx.SELL_CASH != -1)    o.setSellCash(c.getString(idx.SELL_CASH));

        return o;
    }

    public void insert(List<MonthSalesObject> list, String f) {
        Log.d(TABLE_NAME, "insert() - f: " + f);
        List<ContentValues> valueList = new ArrayList<>();
        for (MonthSalesObject o : list) {
            Log.d(TABLE_NAME, "insert()");
            valueList.add(fetchObject2Values(o));
        }
        fastInsert(valueList);
    }

    public int insertForSync(List<MonthSalesObject> addList) {
        try {
            int count = 0;
            db.beginTransaction();
            for (MonthSalesObject contact : addList) {
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

    public List<MonthSalesObject> getList() {
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

    public List<MonthSalesObject> getMonthlyLastDataCheck(String year, String month) {
        Log.d(TAG, "year : " + year + ", month : " + month);
        String sql = "SELECT * FROM " + tableName + " WHERE " +
                SELL_YEAR + " == " + year + " AND " + SELL_MONTH + " == " + month + " ORDER BY " + ID + " DESC LIMIT 3";

//        String sql = "SELECT * FROM " + tableName + " ORDER BY " +
//                SELL_YEAR + " == " + year +  " AND " + SELL_MONTH + " == " + month + ID + " DESC LIMIT 3";

        return select_raw(sql);
    }

    public List<MonthSalesObject> getMonthlyLastData(String id) {
//        String sql = "SELECT * FROM " + tableName + " WHERE " +
//                SELL_YEAR + " == " + year +  " AND " + SELL_MONTH + " == " + month + " ORDER BY " + ID + " DESC LIMIT 3";

        String sql = "SELECT * FROM " + tableName + " WHERE " + ID + " <= " + id + " ORDER BY " + ID + " DESC LIMIT 3";

        return select_raw(sql);
    }

}
