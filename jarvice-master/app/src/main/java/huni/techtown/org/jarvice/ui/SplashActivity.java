package huni.techtown.org.jarvice.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_MONTH_SALES;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;

public class SplashActivity  extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private Context mContext;
    private Handler mHandler;

    //Database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

//    DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("ZONES");
    //원본 데이터
    DatabaseReference rawDataReference = databaseReference.child("rawData");
    //일일 데이터
    DatabaseReference dailyDataReference = databaseReference.child("testTwo");

    private TBL_MY_SALES tblMySalesDb = null;
    private TBL_DAILY_SALES tblDailySalesDb = null;

    private List<SalesObject> salesDataList = new ArrayList<SalesObject>();
    private List<DailySalesObject> DailySalesDataList = new ArrayList<DailySalesObject>();

    //로딩시간
    private long        mNextTime = 0;

    public interface onDownloadEnd {
        void onRawDataEnd();
        void onDailyDataEnd();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        mContext = this;
        mHandler = new Handler();

        mNextTime = System.currentTimeMillis() + 2000;

        Stetho.initializeWithDefaults(mContext);

        //DB 삭제
        DatabaseManager.getInstance(mContext).truncate();

        //데이터 베이스 인스턴스 생성
        DatabaseManager.getInstance(mContext);

        tblMySalesDb = DatabaseManager.getInstance(mContext).getMySales();
        tblDailySalesDb = DatabaseManager.getInstance(mContext).getDailySales();

        final onDownloadEnd mListener = new onDownloadEnd() {
            @Override
            public void onRawDataEnd() {
                Log.e(TAG, "onRawDataEnd ");

                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tblMySalesDb.insertForSync(salesDataList);
                        CurrentManager.getInstance(mContext).rawDataFinish(true, "end1");
                    }
                });
                t1.start();

                if (CurrentManager.getInstance(mContext).dailyDataCheck()) {
                    Log.d(TAG, "onRawDataEnd - goToMainActivity");
                    goToMainActivity();
                }
            }

            @Override
            public void onDailyDataEnd() {
                Log.e(TAG, "onDailyDataEnd ");
                Thread t1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        tblDailySalesDb.insertForSync(DailySalesDataList);

                        CurrentManager.getInstance(mContext).dailyDataFinish(true, "end2");
                    }
                });

                t1.start();

                if (CurrentManager.getInstance(mContext).rawDataCheck()) {
                    Log.d(TAG, "onDailyDataEnd - goToMainActivity");
                    goToMainActivity();
                }
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

                    DailySalesDataList.add(insertData);
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
}
