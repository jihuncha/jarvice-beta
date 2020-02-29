package huni.techtown.org.jarvice.common;

import android.content.Context;
import android.util.Log;

import java.util.List;

import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.database.DatabaseHelper;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_MONTH_SALES;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;

public class DatabaseManager {
    public static final String TAG = DatabaseManager.class.getSimpleName();

    private volatile static DatabaseManager sInstance;

    private static Context mContext = null;
    private static DatabaseHelper mHelper = null;

    //입력받은 총 데이터
    private TBL_MY_SALES mTblMySales;

    private TBL_DAILY_SALES mTblDailySales;

    private TBL_MONTH_SALES mTblMonthSales;

    private DatabaseManager(Context context) {
        mHelper = new DatabaseHelper(context);
        mContext = context;

        mTblMySales = new TBL_MY_SALES(mHelper.getWritableDatabase());
        mTblDailySales = new TBL_DAILY_SALES(mHelper.getWritableDatabase());
        mTblMonthSales = new TBL_MONTH_SALES(mHelper.getWritableDatabase());
    }

    public static DatabaseManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (DatabaseManager.class) {
                if (sInstance == null) {
                    sInstance = new DatabaseManager(context);
                }
            }
        }
        return sInstance;
    }

    public static DatabaseHelper getDatabaseHelper(){
        if(mHelper == null){
            mHelper = new DatabaseHelper(mContext);
        }
        return mHelper;
    }


    public TBL_MY_SALES getMySales() {
        return mTblMySales;
    }

    public TBL_DAILY_SALES getDailySales() {
        return mTblDailySales;
    }



    /**
     * 테이블을 몽땅 truncate 시킨다.
     */
    public void truncate() {
        Log.d(TAG, "truncate()");
        mTblMySales.truncate();
        mTblDailySales.truncate();
        mTblMonthSales.truncate();
    }

    public List<SalesObject> getDateSalesObject(String input, String f) {
        Log.d(TAG, "getChannelHistory() - f: " + f + ", sid: " + input);
        String newSql = "SELECT * FROM " + TBL_MY_SALES.TABLE_NAME
                + " WHERE " + TBL_MY_SALES.SELL_DATE + "=" + "'" + input + "'";

        return mTblMySales.select_raw(newSql);
    }

    public List<SalesObject> getTodaysSell(String test, String f) {
        Log.d(TAG, "getChannelHistory() - f: " + f + ", sid: " + test);
        String newSql = "SELECT SUM(" +TBL_MY_SALES.SELL +")"+ " FROM " + TBL_MY_SALES.TABLE_NAME
                + " WHERE " + TBL_MY_SALES.SELL_DATE + "=" + "'" + test + "'";

        return mTblMySales.select_raw(newSql);
    }

    public int getMemberCount(String gogo) {
        return mTblMySales.getMemberCount(gogo);
    }

    public int getSumCount(String gogo) {
        return mTblMySales.getSum(gogo);
    }

    public List<DailySalesObject> getDailCheck(String gogo) {
        return mTblDailySales.getDailyData(gogo);
    }

    public List<DailySalesObject> getLastData() {
        return mTblDailySales.getLastData();
    }

//    public List<Sa>
}
