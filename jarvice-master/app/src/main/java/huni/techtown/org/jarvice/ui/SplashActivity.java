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

    private TBL_MY_SALES tblMySales;

    //로딩시간
    private long        mNextTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        mHandler = new Handler();

        mNextTime = System.currentTimeMillis() + 2000;

        Stetho.initializeWithDefaults(this);

        //데이터 베이스 인스턴스 생성
        DatabaseManager.getInstance(this);
        final List<SalesObject> salesDataList = new ArrayList<SalesObject>();
        final TBL_MY_SALES tblMySalesDb = DatabaseManager.getInstance(mContext).getMySales();

        final List<DailySalesObject> DailySalesDataList = new ArrayList<DailySalesObject>();
        final TBL_DAILY_SALES tblDailySalesDb = DatabaseManager.getInstance(mContext).getDailySales();

        tblMySalesDb.truncate();
        tblDailySalesDb.truncate();

        //원본데이터 가져오기
        rawDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "rawData : " + dataSnapshot.getValue());
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    SalesObject insertData = snapshot.getValue(SalesObject.class);

                    Log.d(TAG, insertData.toString());
                    salesDataList.add(insertData);
                }

                tblMySalesDb.insertForSync(salesDataList);
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

//                    SalesObject insertData = snapshot.getValue(SalesObject.class);
                    DailySalesObject insertData = snapshot.getValue(DailySalesObject.class);

                    Log.d(TAG, "daily : " + insertData.toString());
                    DailySalesDataList.add(insertData);
                }

                tblDailySalesDb.insertForSync(DailySalesDataList);
                Log.d(TAG, "onDataChange - finish!");
                goToMainActivity();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "error : " + databaseError.toString());

                Toast myToast = Toast.makeText(mContext.getApplicationContext(), "error!!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });

//        // 데이터 변동 못읽어오는 녀석.
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d(TAG ,"Count "+ dataSnapshot.getChildrenCount());
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "Single ValueEventListener : " + snapshot.getValue());
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, " error : " + databaseError.getMessage());
//            }
//        });
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
