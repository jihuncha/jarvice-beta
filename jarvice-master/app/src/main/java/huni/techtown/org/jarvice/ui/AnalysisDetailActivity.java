package huni.techtown.org.jarvice.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;
import huni.techtown.org.jarvice.ui.utils.Tools;

/**
 * MainActivity
 * Tab layout 클릭시 각 Fragment view 로 이동
 *
 *
* */
public class AnalysisDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = AnalysisDetailActivity.class.getSimpleName();

    private Context mContext;
    private Handler mHandler;

    private ImageView iv_main_analysis_back;

    private WeeklySalesObject inputData;

    private String sellYear;
    private String sellWeek;

    private TBL_DAILY_SALES mTblDailySales;
    private ArrayList<DailySalesObject> dailyDataList;

    //매출 통계 - 막대 데이터
    private ArrayList<BarEntry> barEntrySellDaily;
    private ArrayList<BarEntry> barEntrySellWeek;
    //매출 통계 - 막대 데이터 라벨
    private ArrayList<String> barEntrySellDailyLabels;
    private ArrayList<String> barEntrySellWeekLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);

        mContext = this;
        mHandler = new Handler();

        if (getIntent() != null) {
            inputData = (WeeklySalesObject) getIntent().getSerializableExtra("inputObject");
            Log.d(TAG,"inputdata : " + inputData);
        } else {
            Toast.makeText(mContext, "해당 데이터가 없습니다. \n관리자에 문의 바랍니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        iv_main_analysis_back = (ImageView) findViewById(R.id.iv_main_analysis_back);
        iv_main_analysis_back.setOnClickListener(this);

        sellYear = inputData.getSellYear();
        sellWeek = inputData.getSellWeek();

        //해당 요일별 비교를 위한 DailyData를 가져온다
        mTblDailySales = DatabaseManager.getInstance(mContext).getDailySales();

        dailyDataList = new ArrayList<DailySalesObject>();
        dailyDataList.addAll(mTblDailySales.getDailyDataForAnalysis(sellYear, sellWeek));
        Log.d(TAG, "DailyDataList - " + dailyDataList);

        //매출 통계 데이터 생성
        barEntrySellDaily = new ArrayList<BarEntry>();
        barEntrySellWeek = new ArrayList<BarEntry>();
        barEntrySellDailyLabels = new ArrayList<String>();
        barEntrySellWeekLabels = new ArrayList<String>();

        addSalesData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_analysis_back :
                finish();
                break;
        }
    }

    public void addSalesData() {
        Log.d(TAG, "addSalesData");

        Log.d(TAG, "test2 : " + dailyDataList.get(4).getSellReal());

        for (int i = 0; i < dailyDataList.size(); i++) {
            barEntrySellDaily.add(new BarEntry(i + 1, Float.parseFloat(Tools.deleteComma(dailyDataList.get(i).getSellReal()))));
        }

        Log.d(TAG, "ddd : " + barEntrySellDaily.toString());
    }
}
