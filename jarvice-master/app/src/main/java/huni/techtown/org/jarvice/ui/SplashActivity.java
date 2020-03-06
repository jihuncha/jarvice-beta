package huni.techtown.org.jarvice.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.stetho.Stetho;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.JarviceConfig;
import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.CurrentManager;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.MonthSalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_MONTH_SALES;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;
import huni.techtown.org.jarvice.database.TBL_WEEKLY_SALES;

/**
 * splash -> data를 받아옴
 * 문제점 : firebase 에서 데이터받아오는것은 mainThread에서만 처리가능함
 * 앱 데이터베이스에 집어넣는 처리를 listener를 통하여 thread 돌려서 구현
 *
* */
public class SplashActivity  extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Context mContext;
    private Handler mHandler;

    //Database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    //원본 데이터
    DatabaseReference rawDataReference = databaseReference.child("rawData");
    //일간 데이터
    DatabaseReference dailyDataReference = databaseReference.child("dailyData");
    //주간 데이터
    DatabaseReference weeklyDataReference = databaseReference.child("weeklyData");
    //월간 데이터
    DatabaseReference monthlyDataReference = databaseReference.child("monthlyData");

    private TBL_MY_SALES tblMySalesDb = null;
    private TBL_DAILY_SALES tblDailySalesDb = null;
    private TBL_WEEKLY_SALES tblWeeklySalesDb = null;
    private TBL_MONTH_SALES tblMonthlySalesDb = null;

    private List<SalesObject> salesDataList = new ArrayList<SalesObject>();
    private List<DailySalesObject> dailySalesDataList = new ArrayList<DailySalesObject>();
    private List<WeeklySalesObject> weeklySalesDataList = new ArrayList<WeeklySalesObject>();
    private List<MonthSalesObject> monthlySalesDataList = new ArrayList<MonthSalesObject>();

    private ProgressBar pb;

    //로딩시간
    private long        mNextTime = 0;

    public interface onDownloadEnd {
        void onRawDataEnd();
        void onDailyDataEnd();
        void onWeeklyDataEnd();
        void onMonthlyDataEnd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        mHandler = new Handler();

        //TODO 의미가있을까..?
        mNextTime = System.currentTimeMillis() + 2000;

        Stetho.initializeWithDefaults(mContext);

        //DB 삭제
        DatabaseManager.getInstance(mContext).truncate();

        //데이터 베이스 인스턴스 생성
        DatabaseManager.getInstance(mContext);

        pb = (ProgressBar) findViewById(R.id.splash_progress_image);

        tblMySalesDb = DatabaseManager.getInstance(mContext).getMySales();
        tblDailySalesDb = DatabaseManager.getInstance(mContext).getDailySales();
        tblWeeklySalesDb = DatabaseManager.getInstance(mContext).getWeeklySales();
        tblMonthlySalesDb = DatabaseManager.getInstance(mContext).getMonthlySales();

        final onDownloadEnd mListener = new onDownloadEnd() {
            @Override
            public void onRawDataEnd() {
                Log.e(TAG, "onRawDataEnd ");

                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tblMySalesDb.insertForSync(salesDataList);
                        CurrentManager.getInstance(mContext).rawDataFinish(true, "end1");

                        if (CurrentManager.getInstance(mContext).dailyDataCheck()
                                && CurrentManager.getInstance(mContext).weeklyDataCheck()
                                && CurrentManager.getInstance(mContext).monthlyDataCheck()) {
                            Log.d(TAG, "onRawDataEnd - goToMainActivity");
                            goToMainActivity();
                        }
                    }
                });
                t1.start();
            }

            @Override
            public void onDailyDataEnd() {
                Log.e(TAG, "onDailyDataEnd ");
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tblDailySalesDb.insertForSync(dailySalesDataList);

                        CurrentManager.getInstance(mContext).dailyDataFinish(true, "end2");

                        if (CurrentManager.getInstance(mContext).rawDataCheck()
                                && CurrentManager.getInstance(mContext).weeklyDataCheck()
                                && CurrentManager.getInstance(mContext).monthlyDataCheck()) {
                            Log.d(TAG, "onDailyDataEnd - goToMainActivity");
                            goToMainActivity();
                        }
                    }
                });

                t1.start();
            }

            @Override
            public void onWeeklyDataEnd() {
                Log.e(TAG, "onWeeklyDataEnd ");
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tblWeeklySalesDb.insertForSync(weeklySalesDataList);

                        CurrentManager.getInstance(mContext).weeklyDataFinish(true, "end3");

                        if (CurrentManager.getInstance(mContext).rawDataCheck()
                                && CurrentManager.getInstance(mContext).dailyDataCheck()
                                && CurrentManager.getInstance(mContext).monthlyDataCheck()) {
                            Log.d(TAG, "onWeeklyDataEnd - goToMainActivity");
                            goToMainActivity();
                        }
                    }
                });

                t1.start();
            }

            @Override
            public void onMonthlyDataEnd() {
                Log.e(TAG, "onMonthlyDataEnd ");
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tblMonthlySalesDb.insertForSync(monthlySalesDataList);

                        CurrentManager.getInstance(mContext).monthlyDataFinish(true, "end4");

                        if (CurrentManager.getInstance(mContext).rawDataCheck()
                                && CurrentManager.getInstance(mContext).dailyDataCheck()
                                && CurrentManager.getInstance(mContext).weeklyDataCheck()) {
                            Log.d(TAG, "onMonthlyDataEnd - goToMainActivity");
                            goToMainActivity();
                        }
                    }
                });

                t1.start();

            }
        };

        //원본데이터 가져오기
        rawDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "rawData : " + dataSnapshot.getValue());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    SalesObject insertData = snapshot.getValue(SalesObject.class);

                    Log.d(TAG, "onDataChange - rawData : " + insertData);

                    salesDataList.add(insertData);
                }

                mListener.onRawDataEnd();
                Log.d(TAG, "onDataChange - finish!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "error : " + databaseError.toString());

                Toast myToast = Toast.makeText(mContext.getApplicationContext(), "error!!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        //daily데이터 가져오기
        dailyDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "dailyDataReference : " + dataSnapshot.getValue());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DailySalesObject insertData = snapshot.getValue(DailySalesObject.class);

                    Log.d(TAG, "onDataChange - dailyData : " + insertData);

                    dailySalesDataList.add(insertData);
                }

                mListener.onDailyDataEnd();
                Log.d(TAG, "onDataChange - finish!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "error : " + databaseError.toString());

                Toast myToast = Toast.makeText(mContext.getApplicationContext(), "error!!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        //주간데이터
        weeklyDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    WeeklySalesObject insertData = snapshot.getValue(WeeklySalesObject.class);

                    Log.d(TAG, "onDataChange - WeeklySalesObject : " + insertData);

                    weeklySalesDataList.add(insertData);
                }

                mListener.onWeeklyDataEnd();
                Log.d(TAG, "onDataChange - finish!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "error : " + databaseError.toString());

                Toast myToast = Toast.makeText(mContext.getApplicationContext(), "error!!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

        //월간데이터
        monthlyDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MonthSalesObject insertData = snapshot.getValue(MonthSalesObject.class);

                    Log.d(TAG, "onDataChange - MonthSalesObject : " + insertData);

                    monthlySalesDataList.add(insertData);
                }

                mListener.onMonthlyDataEnd();
                Log.d(TAG, "onDataChange - finish!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "error : " + databaseError.toString());

                Toast myToast = Toast.makeText(mContext.getApplicationContext(), "error!!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

    }

    private void goToMainActivity() {
        Log.d(TAG, "goToMainActivity()");

        post(mGotoMain, "goToMainActivity()");
    }

    private void post(Runnable runnable, String f) {
        long delayed = mNextTime - System.currentTimeMillis();
        Log.d(TAG, "[" + f + "] post() - delayed: " + delayed);
        if (delayed > 0) {
            Log.d(TAG, "post - postDelayed");
            mHandler.postDelayed(runnable, delayed);
        } else {
            mHandler.post(runnable);
        }
    }

    private Runnable mGotoMain = new Runnable() {
        @Override
        public void run() {

            //Intent 정보를 카피하여 전달..
            JarviceConfig.startMainActivity(mContext, getIntent(), null, true, "goToMainActivity");
            overridePendingTransition(0,0);

            //종료
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");

    }
}
