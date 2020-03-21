package huni.techtown.org.jarvice.common;

import android.content.Context;
import android.util.Log;

import java.util.List;

import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.MonthSalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.database.DatabaseHelper;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_HELPER_NOTIFICATION;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODAY_PEOPLE;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODO_LIST;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODO_LIST_DEFAULT;
import huni.techtown.org.jarvice.database.TBL_MONTH_SALES;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;
import huni.techtown.org.jarvice.database.TBL_WEEKLY_SALES;

public class DatabaseManager {
    public static final String TAG = DatabaseManager.class.getSimpleName();

    private volatile static DatabaseManager sInstance;

    private static Context mContext = null;
    private static DatabaseHelper mHelper = null;

    //입력받은 총 데이터
    private TBL_MY_SALES mTblMySales;

    private TBL_DAILY_SALES mTblDailySales;

    private TBL_MONTH_SALES mTblMonthSales;

    private TBL_WEEKLY_SALES mTblWeeklySales;

    private TBL_HELPER_NOTIFICATION mTblHelperNotification;

//    private TBL_HELPER_TODAY_PEOPLE mTblHelperTodayPeople;

    private TBL_HELPER_TODO_LIST mTblHelperTodoList;

    private TBL_HELPER_TODO_LIST_DEFAULT mTblHelperTodoListDefault;

    private DatabaseManager(Context context) {
        mHelper = new DatabaseHelper(context);
        mContext = context;

        mTblMySales = new TBL_MY_SALES(mHelper.getWritableDatabase());
        mTblDailySales = new TBL_DAILY_SALES(mHelper.getWritableDatabase());
        mTblMonthSales = new TBL_MONTH_SALES(mHelper.getWritableDatabase());
        mTblWeeklySales = new TBL_WEEKLY_SALES(mHelper.getWritableDatabase());

        mTblHelperNotification = new TBL_HELPER_NOTIFICATION(mHelper.getWritableDatabase());
//        mTblHelperTodayPeople = new TBL_HELPER_TODAY_PEOPLE(mHelper.getWritableDatabase());
        mTblHelperTodoList = new TBL_HELPER_TODO_LIST(mHelper.getWritableDatabase());
        mTblHelperTodoListDefault = new TBL_HELPER_TODO_LIST_DEFAULT(mHelper.getWritableDatabase());
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

    public TBL_WEEKLY_SALES getWeeklySales() { return mTblWeeklySales;}

    public TBL_MONTH_SALES getMonthlySales() { return mTblMonthSales;}

    public TBL_HELPER_NOTIFICATION getHelperNotification() { return mTblHelperNotification;}

//    public TBL_HELPER_TODAY_PEOPLE getHelperTodayPeople() { return mTblHelperTodayPeople;}

    public TBL_HELPER_TODO_LIST getHelperTodoList() { return mTblHelperTodoList;}

    public TBL_HELPER_TODO_LIST_DEFAULT getHelperTodoListDefault() { return mTblHelperTodoListDefault;}


    /**
     * 테이블을 몽땅 truncate 시킨다.
     */
    public void truncate() {
        Log.d(TAG, "truncate()");
        mTblMySales.truncate();
        mTblDailySales.truncate();
        mTblMonthSales.truncate();
        mTblWeeklySales.truncate();

        //TODO 여긴 아직 지우면안된다.
        mTblHelperNotification.truncate();
        mTblHelperTodoList.truncate();
        mTblHelperTodoListDefault.truncate();
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

    public List<DailySalesObject> getDailyCheck(String date) {
        return mTblDailySales.getDailyData(date);
    }


    /**
     *
     * 일간데이터 가져오기
     * 기준점이 되는 데이터.
     * id 정렬하여 실매출이 0이 아닌 가장 최신데이터를 가져온다
     *
     * */
    public List<DailySalesObject> getLastData() {
        return mTblDailySales.getLastData();
    }

    /**
     *
     * 주간데이터 가져오기
     * 1. getWeeklyLastDataCheck -> 로 해당 년/Week로 아이디 체크
     * 2. getWeeklyLastData -> 로 데이터 가져오기
     *
     * */

    public List<WeeklySalesObject> getWeeklyLastDataCheck(String year, String weekCheck) {
        return mTblWeeklySales.getWeeklyLastDataCheck(year, weekCheck);
    }

    public List<WeeklySalesObject> getWeeklyLastData(String input) {
        return mTblWeeklySales.getWeeklyLastData(input);
    }

    /**
     *
     * 월간데이터 가져오기
     * 1. getMonthlyLastDataCheck -> 로 해당 년/월 아이디 체크
     * 2. getMonthlyLastData -> 로 데이터 가져오기
     *
     * */

    public List<MonthSalesObject> getMonthlyLastDataCheck(String year, String month) {
        return mTblMonthSales.getMonthlyLastDataCheck(year, month);
    }

    public List<MonthSalesObject> getMonthlyLastData(String input) {
        return mTblMonthSales.getMonthlyLastData(input);
    }

}
