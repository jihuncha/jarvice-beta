package huni.techtown.org.jarvice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String TAG = DatabaseHelper.class.getSimpleName();

    public static final int SQLITE_STATUS_CREATE = 1;
    public static final int SQLITE_STATUS_UPGRADE = 2;
    public static final int SQLITE_STATUS_NONE = 3;
    public static final int SQLITE_STATUS_OPEN = 4;

    private int status = SQLITE_STATUS_NONE;

    /*** DB 상수 정의 *******************************/
    public static final int    DB_VERSION = 4;

    public static final String DB_NAME = "jarvice.db";

    public Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");
        status = SQLITE_STATUS_CREATE;
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade() - oldVersion: " + oldVersion + ", newVersion: " + newVersion);
        //oldVersion
        switch (oldVersion) {
            case 1:
                upgrade1ToX(db);
                break;

            case 2:
                switch (newVersion) {
                    case 8: // 2 --> 8
                        upgrade2To3(db);
                        upgradeXTo4(db);
                        upgradeXTo5(db);
                        upgradeXTo6(db);
                        upgradeXTo7(db);
                        upgradeXTo8(db);
                        break;
                }
                break;

            case 3:
                switch (newVersion) {
                    case 8: // 3 --> 8
                        upgradeXTo4(db);
                        upgradeXTo5(db);
                        upgradeXTo6(db);
                        upgradeXTo7(db);
                        upgradeXTo8(db);
                        break;
                }
                break;

            case 4:
                switch (newVersion) {
                    case 8: // 4 --> 8
                        upgradeXTo5(db);
                        upgradeXTo6(db);
                        upgradeXTo7(db);
                        upgradeXTo8(db);
                        break;
                }
                break;
//
//            case 5:
//                switch (newVersion) {
//                    case 8: // 5 --> 8
//                        upgradeXTo6(db);
//                        upgradeXTo7(db);
//                        upgradeXTo8(db);
//                        break;
//                }
//                break;
//
//            case 6:
//                switch (newVersion) {
//                    case 8: // 6 --> 8
//                        upgradeXTo7(db);
//                        upgradeXTo8(db);
//                        break;
//                }
//                break;
//
//            case 7:
//                switch (newVersion) {
//                    case 8: // 7 --> 8
//                        upgradeXTo8(db);
//                        break;
//                }
//                break;
        }

    }

    public void upgrade1ToX(SQLiteDatabase db) {
        Log.d(TAG, "upgrade1ToX()");
        dropTables(db);
        createTables(db);
    }

    public void upgrade2To3(SQLiteDatabase db) {
        Log.d(TAG, "upgrade2To3()");
    }

    public void upgradeXTo4(SQLiteDatabase db) {
        Log.d(TAG, "upgradeXTo4()");
    }

    public void upgradeXTo5(SQLiteDatabase db) {
        Log.d(TAG, "upgradeXTo5()");
    }

    public void upgradeXTo6(SQLiteDatabase db) {
        Log.d(TAG, "upgradeXTo6()");
    }

    public void upgradeXTo7(SQLiteDatabase db) {
        Log.d(TAG, "upgradeXTo7()");
    }

    public void upgradeXTo8(SQLiteDatabase db) {
        Log.d(TAG, "upgradeXTo8()");
    }

    /**
     * 전체 TABLE을 생성한다.
     * @param db
     */
    private void createTables(SQLiteDatabase db) {
//        db.execSQL(TBL_MY_SALES.DROP);
        db.execSQL(TBL_MY_SALES.CREATE);
        db.execSQL(TBL_MONTH_SALES.CREATE);
        db.execSQL(TBL_DAILY_SALES.CREATE);
        db.execSQL(TBL_WEEKLY_SALES.CREATE);
//        db.execSQL(TBL_GROUP_MEMBER.CREATE);
//        db.execSQL(TBL_CALL_HISTORY.CREATE);
//        db.execSQL(TBL_CALL_HISTORY_MEMBER.CREATE);
//        db.execSQL(TBL_CHANNEL.CREATE);
//        db.execSQL(TBL_CHANNEL_TALK.CREATE);
//
//        db.execSQL(TBL_COMPANY.CREATE);
//        db.execSQL(TBL_DEPARTMENT.CREATE);
//        db.execSQL(TBL_DEPARTMENT_MEMBER.CREATE);
//
//        db.execSQL(TBL_CHANNEL_HISTORY.CREATE);
//        db.execSQL(TBL_CALL_RECORD.CREATE);
//
//        db.execSQL(TBL_PTT_HISTORY.CREATE);
//        db.execSQL(TBL_ONE_MEMBER.CREATE);
//
//        db.execSQL(TBL_ROOM_IN_OUT.CREATE);
    }

    /**
     * 전체 TABLE을 삭제한다.
     * @param db
     */
    private void dropTables(SQLiteDatabase db) {
        db.execSQL(TBL_MY_SALES.DROP);
        db.execSQL(TBL_MONTH_SALES.DROP);
        db.execSQL(TBL_DAILY_SALES.DROP);
        db.execSQL(TBL_WEEKLY_SALES.DROP);
//        db.execSQL(TBL_GROUP_MEMBER.DROP);
//        db.execSQL(TBL_CALL_HISTORY.DROP);
//        db.execSQL(TBL_CALL_HISTORY_MEMBER.DROP);
//        db.execSQL(TBL_CHANNEL.DROP);
//        db.execSQL(TBL_CHANNEL_TALK.DROP);
//
//        db.execSQL(TBL_COMPANY.DROP);
//        db.execSQL(TBL_DEPARTMENT.DROP);
//        db.execSQL(TBL_DEPARTMENT_MEMBER.DROP);
//
//        db.execSQL(TBL_CHANNEL_HISTORY.DROP);
//        db.execSQL(TBL_CALL_RECORD.DROP);
//
//        db.execSQL(TBL_PTT_HISTORY.DROP);
//        db.execSQL(TBL_ONE_MEMBER.DROP);
//
//        db.execSQL(TBL_ROOM_IN_OUT.DROP);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        Log.d(TAG, "onOpen");
        if (status == SQLITE_STATUS_NONE) {
            status = SQLITE_STATUS_OPEN;
        }
    }
}
