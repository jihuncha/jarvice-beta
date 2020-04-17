package huni.techtown.org.jarvice.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import huni.techtown.org.jarvice.JarviceSettings;
import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesListItems;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;
import huni.techtown.org.jarvice.database.TBL_WEEKLY_SALES;
import huni.techtown.org.jarvice.ui.adapter.AdapterPieChartList;
import huni.techtown.org.jarvice.ui.popup.LoadingActivity;
import huni.techtown.org.jarvice.ui.utils.DateUtils;
import huni.techtown.org.jarvice.ui.utils.Tools;

/**
 * AnalysisDetailActivity
 * 가게분석에서 진입한 차트 영역
 *
 *
* */
public class AnalysisDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = AnalysisDetailActivity.class.getSimpleName();

    private Context mContext;
    private Handler mHandler;

    private RelativeLayout rl_main_analysis_back;
    private TextView tv_main_analysis_title;
    private ImageView iv_main_analysis_back;

    private WeeklySalesObject inputData;

    private String sellYear;
    private String sellWeek;

    //일간 데이터
    private TBL_DAILY_SALES mTblDailySales;
    private ArrayList<DailySalesObject> dailyDataList;

    //주간 데이터
    private TBL_WEEKLY_SALES mTblWeeklySales;
    private ArrayList<WeeklySalesObject> weeklyDataList;

    //매출 통계 - 막대 데이터
    private ArrayList<BarEntry> barEntrySellDaily;
    private ArrayList<BarEntry> barEntrySellWeek;
    //매출 통계 - 막대 데이터 라벨
    private ArrayList<String> barEntrySellDailyLabels;
    private ArrayList<String> barEntrySellWeekLabels;

    //매출 통계 - line data
    private ArrayList<Entry> barEntrySellDailyLine;

    private LinearLayout ll_main_analysis_sales;

    private TextView tv_main_analysis_sales_daily;
    private TextView tv_main_analysis_sales_week;

    private ViewPager sales_bar_chart;
    private WeeklySaleCombinedChartAdapter combinedChartAdapter;

    private CombinedChart combinedChart;
    private BarDataSet weeklySalesBarDataSet;
    private BarData weeklySalesBarData;
    private LineDataSet weeklySalesLineDataSet;
    private LineData weeklySalesLineData;

    //방문 통계
    private TextView tv_main_analysis_visit_daily;
    private TextView tv_main_analysis_visit_week;

    //방문 통계 - 막대 데이터
    private ArrayList<BarEntry> barEntryVisitDaily;
    private ArrayList<BarEntry> barEntryVisitWeek;
    //방문 통계 - 막대 데이터 라벨
    //TODO 라벨은 상단거 그대로 써도 되지않나...?? barEntrySellDailyLabels
    private ArrayList<String> barEntryVisitDailyLabels;
    private ArrayList<String> barEntryVisitWeekLabels;

    private ViewPager visit_bar_chart;
    private WeeklyVisitBarChartAdapter weeklyVisitBarChartAdapter;

    private BarChart visitBarChart;
    private BarDataSet dailyVisitBarDataSet;
    private BarData dailyVisitBarData;

    //카테고리 별 매출
    private PieChart categoryPieChart;
    private ArrayList<PieEntry> categorySellEntry;

    //카테고리 하단 색상 및 항목
    private ArrayList<Integer> categoryDetailPercentList;
    //리사이클러뷰에 전달할 list
    private ArrayList<DailySalesList> insertListOfSellsData;

    private RecyclerView rv_category_detail;
    private AdapterPieChartList mAdapterPieChartList;

    //주간 날짜에 해당되는 일간 데이터를 전부 가져와서 list 처리한다..
    private TBL_MY_SALES mTblMySales;
    private ArrayList<List<SalesObject>> dailyDataSalesList;
    private ArrayList<SalesObject> dailyDataSalesListReal;

    //최종 카테고리별로 나눈 결과
    private HashMap<String, List<DailySalesListItems>> lastRawDataHash;

    //TODO - 이게 맞을까?? 카테고리별 음식 Entry
    private ArrayList<BarEntry> barEntryCategory1;
    private ArrayList<BarEntry> barEntryCategory2;
    private ArrayList<BarEntry> barEntryCategory3;
    private ArrayList<BarEntry> barEntryCategory4;
    private ArrayList<BarEntry> barEntryCategory5;
    private ArrayList<BarEntry> barEntryCategory6;
    private ArrayList<BarEntry> barEntryCategory7;

    private ArrayList<String> barEntryCategoryString1;
    private ArrayList<String> barEntryCategoryString2;
    private ArrayList<String> barEntryCategoryString3;
    private ArrayList<String> barEntryCategoryString4;
    private ArrayList<String> barEntryCategoryString5;
    private ArrayList<String> barEntryCategoryString6;
    private ArrayList<String> barEntryCategoryString7;

    //최종 카테고리를 위한 viewpager 두개 생성 (1. 메뉴를 위한것 2.메뉴에 따른 내부 항목
    private RecyclerView rv_category_menu;

    private ViewPager vp_category_detail;
    private HotCategoryMenuChartAdapter hotCategoryMenuChartAdapter;

    private BarChart hotMenuBarChart;
    private BarDataSet hotMenuBarDataSet;
    private BarData hotMenuBarData;

    private ArrayList<String> categoryMenuString;
    private CategoryMenuAdapter categoryMenuAdapter;

    private int [] color = { };
    private int [] colorOriginal = {};

    //TODO viewpager setitem 을위함..
    private boolean checkFirstTime = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);

        mContext = this;
        mHandler = new Handler();

        LoadingActivity.show(mContext,"AnalysisDetailActivity - onCreate");

        //색상 설정
        color = new int[]{
                getResources().getColor(R.color.color_fb7a63), getResources().getColor(R.color.color_fcc849),
                getResources().getColor(R.color.color_6bd67c), getResources().getColor(R.color.color_c7abf6),
                getResources().getColor(R.color.color_839afe), getResources().getColor(R.color.color_81c6fc),
                getResources().getColor(R.color.color_4993cd)
        };

        colorOriginal = new int[] {
                R.color.color_fb7a63, R.color.color_fcc849,
                R.color.color_6bd67c, R.color.color_c7abf6,
                R.color.color_839afe, R.color.color_81c6fc,
                R.color.color_4993cd
        };

        Log.d(TAG, "check : " + JarviceSettings.getInstance(mContext).getMondaySellAvg());

        if (getIntent() != null) {
            inputData = (WeeklySalesObject) getIntent().getSerializableExtra("inputObject");
            Log.d(TAG,"inputdata : " + inputData);
        } else {
            Toast.makeText(mContext, "해당 데이터가 없습니다. \n관리자에 문의 바랍니다.", Toast.LENGTH_LONG).show();
            finish();
        }

        rl_main_analysis_back = (RelativeLayout) findViewById(R.id.rl_main_analysis_back);
        rl_main_analysis_back.setOnClickListener(this);
        iv_main_analysis_back = (ImageView) findViewById(R.id.iv_main_analysis_back);
        iv_main_analysis_back.setOnClickListener(this);

        sellYear = inputData.getSellYear();
        sellWeek = inputData.getSellWeek();
//        2019년 23주차 리포트
        tv_main_analysis_title = (TextView) findViewById(R.id.tv_main_analysis_title);
        tv_main_analysis_title.setText(sellYear + "년 " + sellWeek +"주차 리포트");

        //해당 요일별 비교를 위한 DailyData를 가져온다
        mTblDailySales = DatabaseManager.getInstance(mContext).getDailySales();

        dailyDataList = new ArrayList<DailySalesObject>();
        dailyDataList.addAll(mTblDailySales.getDailyDataForAnalysis(sellYear, sellWeek));

        //TODO 0번째가 일요일이 아닌 월요일로 하기 위한 세팅..
        DailySalesObject sundayData = new DailySalesObject();
        if (dailyDataList.size() != 0) {
            sundayData = dailyDataList.get(0);
            dailyDataList.remove(0);
            dailyDataList.add(sundayData);
        } else {
            Log.e(TAG, "sundayData NULL!");
        }


        Log.d(TAG, "DailyDataList - " + dailyDataList);

        //매출 통계 데이터 생성
        barEntrySellDaily = new ArrayList<BarEntry>();
        barEntrySellWeek = new ArrayList<BarEntry>();
        barEntrySellDailyLabels = new ArrayList<String>();
        barEntrySellWeekLabels = new ArrayList<String>();
        barEntrySellDailyLine = new ArrayList<Entry>();

        addSalesData();

//        ll_main_analysis_sales = (LinearLayout) findViewById(R.id.ll_main_analysis_sales);
//        ll_main_analysis_sales.setBackground(getResources().getDrawable(R.drawable.circular_background_graph_selector));

        tv_main_analysis_sales_daily = (TextView) findViewById(R.id.tv_main_analysis_sales_daily);
        tv_main_analysis_sales_daily.setOnClickListener(this);
        tv_main_analysis_sales_week = (TextView) findViewById(R.id.tv_main_analysis_sales_week);
        tv_main_analysis_sales_week.setOnClickListener(this);

        sales_bar_chart = (ViewPager) findViewById(R.id.sales_bar_chart);
        combinedChartAdapter = new WeeklySaleCombinedChartAdapter();
        sales_bar_chart.setAdapter(combinedChartAdapter);
        sales_bar_chart.addOnPageChangeListener(CombinedPageChangeListener);

        //방문통계
        tv_main_analysis_visit_daily = (TextView) findViewById(R.id.tv_main_analysis_visit_daily);
        tv_main_analysis_visit_daily.setOnClickListener(this);
        tv_main_analysis_visit_week = (TextView) findViewById(R.id.tv_main_analysis_visit_week);
        tv_main_analysis_visit_week.setOnClickListener(this);

        barEntryVisitDaily = new ArrayList<BarEntry>();
        barEntryVisitWeek = new ArrayList<BarEntry>();
        barEntryVisitDailyLabels = new ArrayList<String>();
        barEntryVisitWeekLabels = new ArrayList<String>();

        addVisitData();

        visit_bar_chart = (ViewPager) findViewById(R.id.visit_bar_chart);
        weeklyVisitBarChartAdapter = new WeeklyVisitBarChartAdapter();
        visit_bar_chart.setAdapter(weeklyVisitBarChartAdapter);
        visit_bar_chart.addOnPageChangeListener(visitBarChartListener);

        //카테고리 별 매출
        //카테고리 매출을 위해 주간 데이터를 가져온다.
        mTblWeeklySales = DatabaseManager.getInstance(mContext).getWeeklySales();
        weeklyDataList = new ArrayList<WeeklySalesObject>();
        weeklyDataList.addAll(mTblWeeklySales.getWeeklyDataForAnalysis(sellYear, sellWeek));

        addCategoryData();
        categoryPieChart = (PieChart) findViewById(R.id.category_piechart);

        initPieChart();

        //카테고리 별 매출 상세 항목
        //퍼센트담는곳
        categoryDetailPercentList = new ArrayList<Integer>();

        initCategorySellList();

        rv_category_detail = (RecyclerView) findViewById(R.id.rv_category_detail);
        rv_category_detail.setLayoutManager(new LinearLayoutManager(mContext));
        rv_category_detail.setHasFixedSize(true);

        insertListOfSellsData = new ArrayList<DailySalesList>();
        categoryMenuString = new ArrayList<String>();

        initColorList();

        mAdapterPieChartList = new AdapterPieChartList(mContext, insertListOfSellsData);
        mAdapterPieChartList.setOnItemClickListener(new AdapterPieChartList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DailySalesList obj, int position) {
                Log.d(TAG, "mAdapterPieChartList - onItemClick : " + position);
            }

        });
        rv_category_detail.setAdapter(mAdapterPieChartList);

        //카테고리 별 인기메뉴 통계
        dailyDataSalesList = new ArrayList<List<SalesObject>>();
        dailyDataSalesListReal = new ArrayList<SalesObject>();
        lastRawDataHash = new HashMap<>();

        mTblMySales = DatabaseManager.getInstance(mContext).getMySales();

        for (int i = 0; i < dailyDataList.size(); i++) {
            dailyDataSalesList.add(mTblMySales.getRangeDailyData(dailyDataList.get(i).getSellDate()));
        }

        for (int i = 0; i < dailyDataSalesList.size(); i++) {
            dailyDataSalesListReal.addAll(dailyDataSalesList.get(i));
        }

        Log.d(TAG, "dailyDataSalesListReal : " + dailyDataSalesListReal.toString());

        initCategoryDetailList();

        Log.d(TAG, "hana : " + lastRawDataHash.toString());

        //카테고리 별 인기 메뉴의 타이틀.
        rv_category_menu = (RecyclerView) findViewById(R.id.rv_category_menu);
        rv_category_menu.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
        //TODO 이거때문에 항목 삭제시 view 갱신이 안됬음 -> false 로 변경...
        rv_category_menu.setHasFixedSize(false);
        categoryMenuAdapter = new CategoryMenuAdapter(categoryMenuString);
        rv_category_menu.setAdapter(categoryMenuAdapter);

        //카테고리 별 인기 메뉴
        vp_category_detail = (ViewPager) findViewById(R.id.vp_category_detail);
        hotCategoryMenuChartAdapter = new HotCategoryMenuChartAdapter();
        vp_category_detail.setAdapter(hotCategoryMenuChartAdapter);
        vp_category_detail.addOnPageChangeListener(hotCategoryBarChartListener);

        //findViewHolderForAdapterPosition 가 바로하면 죽는 현상때문에 postdelay
        setPosition();

        LoadingActivity.hide(mContext,"AnalysisDetailActivity - onCreate");
    }

    public void setPosition(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                vp_category_detail.setCurrentItem(0);
                hotCategoryMenuChartAdapter.notifyDataSetChanged();
                categoryMenuAdapter.notifyDataSetChanged();
                checkFirstTime = false;
            }
        },50);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_analysis_back:
            case R.id.rl_main_analysis_back:
                finish();
                break;

            case R.id.tv_main_analysis_sales_daily:
                sales_bar_chart.setCurrentItem(0);

                tv_main_analysis_sales_daily.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_sales_daily.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_sales_week.setBackground(null);
                tv_main_analysis_sales_week.setTypeface(Typeface.DEFAULT);

                break;

            case R.id.tv_main_analysis_sales_week:
                sales_bar_chart.setCurrentItem(1);

                tv_main_analysis_sales_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_sales_week.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_sales_daily.setBackground(null);
                tv_main_analysis_sales_daily.setTypeface(Typeface.DEFAULT);
                break;

            case R.id.tv_main_analysis_visit_daily:
                visit_bar_chart.setCurrentItem(0);

                tv_main_analysis_visit_daily.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_visit_daily.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_visit_week.setBackground(null);
                tv_main_analysis_visit_week.setTypeface(Typeface.DEFAULT);

                break;

            case R.id.tv_main_analysis_visit_week:
                visit_bar_chart.setCurrentItem(1);

                tv_main_analysis_visit_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_visit_week.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_visit_daily.setBackground(null);
                tv_main_analysis_visit_daily.setTypeface(Typeface.DEFAULT);

                break;
        }
    }

    public void addSalesData() {
        Log.d(TAG, "addSalesData - dailyDataList : " + dailyDataList.toString());

        for (int i = 0; i < dailyDataList.size(); i++) {
            if (dailyDataList.get(i).getSellReal().equals("0")
                    || dailyDataList.get(i).getSellReal() == null
                    || dailyDataList.get(i).getSellReal().isEmpty()) {
                Log.e(TAG, "addSalesData - Empty String!!");
                barEntrySellDaily.add(new BarEntry(i + 1, 0));
            } else {
                Log.d(TAG, "addSalesData - add Data : " + dailyDataList.get(i).getSellReal());
                barEntrySellDaily.add(new BarEntry(i + 1, Float.parseFloat(Tools.deleteComma(dailyDataList.get(i).getSellReal()))));
            }
        }
        Log.d(TAG, "check1 : " + dailyDataList.size());
        Log.d(TAG, "barEntrySellDaily : " + barEntrySellDaily.toString());

        barEntrySellWeekLabels.add("월");
        barEntrySellWeekLabels.add("화");
        barEntrySellWeekLabels.add("수");
        barEntrySellWeekLabels.add("목");
        barEntrySellWeekLabels.add("금");
        barEntrySellWeekLabels.add("토");
        barEntrySellWeekLabels.add("일");

        Log.d(TAG, "barEntrySellWeekLabels - " + barEntrySellWeekLabels.toString());
        Log.d(TAG, "check2 : " + barEntrySellWeekLabels.size());

        //Linedata
        barEntrySellDailyLine.add(new Entry(0.5f, JarviceSettings.getInstance(mContext).getMondaySellAvg()));
        barEntrySellDailyLine.add(new Entry(1.5f, JarviceSettings.getInstance(mContext).getTuesdaySellAvg()));
        barEntrySellDailyLine.add(new Entry(2.5f, JarviceSettings.getInstance(mContext).getWednesdayySellAvg()));
        barEntrySellDailyLine.add(new Entry(3.5f, JarviceSettings.getInstance(mContext).getThursdaySellAvg()));
        barEntrySellDailyLine.add(new Entry(4.5f, JarviceSettings.getInstance(mContext).getFridaySellAvg()));
        barEntrySellDailyLine.add(new Entry(5.5f, JarviceSettings.getInstance(mContext).getSaturdaySellAvg()));
        barEntrySellDailyLine.add(new Entry(6.5f, JarviceSettings.getInstance(mContext).getSundaySellAvg()));

        Log.d(TAG, "barEntrySellDailyLine - " + barEntrySellDailyLine.toString());
        Log.d(TAG, "check3 : " + barEntrySellDailyLine.size());

    }

    public void addVisitData() {
        Log.d(TAG, "addVisitData - dailyDataList : " + dailyDataList.toString());

        for (int i = 0; i < dailyDataList.size(); i++) {
            if (dailyDataList.get(i).getDinnerVisitTotal().equals("0")
                    || dailyDataList.get(i).getDinnerVisitTotal() == null
                    || dailyDataList.get(i).getDinnerVisitTotal().isEmpty()) {
                Log.e(TAG, "addVisitData - Empty String!!");
                barEntryVisitDaily.add(new BarEntry(i + 1, 0));
            } else {
                Log.d(TAG, "addVisitData - add Data : " + dailyDataList.get(i).getDinnerVisitTotal());
                barEntryVisitDaily.add(new BarEntry(i + 1, Float.parseFloat(Tools.deleteComma(dailyDataList.get(i).getDinnerVisitTotal()))));
            }
        }

        Log.d(TAG, "barEntryVisitDaily : " + barEntryVisitDaily);

    }

    public void addCategoryData() {
        Log.d(TAG, "addCategoryData - weeklyDataList : " + weeklyDataList.toString());

        if (weeklyDataList.size() == 0) {
            Log.e(TAG, "weeklyDataList NULL!!");
            return;
        }

        categorySellEntry = new ArrayList<PieEntry>();
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellFood()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellBeer()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellCock()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellLiquor()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellDrink()))));

        Log.d(TAG, "categorySellEntry : " + categorySellEntry);

    }

    public void addCategoryDetailData() {
        Log.d(TAG, "addCategoryDetailData");

        if (categoryMenuString.size() == 0) {
            Log.e(TAG, "addCategoryDetailData NULL!!");
            return;
        }

//        for (int i = 0; i < categoryMenuString.size(); i++) {
//            switch (i) {
////                case 0 :             barEntryCategory1.add(new BarEntry())
//            }
//
//        }

    }

    public void initSellBarChart(int position){
        Log.d(TAG, "initSellBarChart");

        weeklySalesLineData = new LineData();

        weeklySalesLineDataSet = new LineDataSet(barEntrySellDailyLine, "평균 매출");
        weeklySalesLineDataSet.setColors(getResources().getColor(R.color.color_fb7a63));
        weeklySalesLineDataSet.setDrawCircles(false);
        weeklySalesLineDataSet.setLineWidth(2.5f);
//        weeklySalesLineDataSet.setCircleColor(Color.rgb(240, 238, 70));
//        weeklySalesLineDataSet.setCircleRadius(5f);
//        weeklySalesLineDataSet.setFillColor(Color.rgb(240, 238, 70));
//        weeklySalesLineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        weeklySalesLineDataSet.setDrawValues(false);
//        weeklySalesLineDataSet.setValueTextSize(10f);
//        weeklySalesLineDataSet.setValueTextColor(Color.rgb(240, 238, 70));

        weeklySalesLineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        weeklySalesLineData.addDataSet(weeklySalesLineDataSet);

        weeklySalesBarDataSet = new BarDataSet(barEntrySellDaily, null);
        weeklySalesBarData = new BarData(weeklySalesBarDataSet);

        //막대 그래프의 너비
        weeklySalesBarData.setBarWidth(0.3f);
        weeklySalesBarData.setDrawValues(false);

        //막대 그래프의 색을 지정해준다.
        weeklySalesBarDataSet.setColor(getResources().getColor(R.color.color_e6e9f7));

        //x축 설정
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        xAxis.setYOffset(9.5f);
        xAxis.setTextColor(getResources().getColor(R.color.color_909090));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
//        xAxis.setAxisMinimum(-0.01f);
//        xAxis.setAxisMaximum(7.0f);

        combinedChart.setExtraBottomOffset(24.5f);
        xAxis.setLabelCount(barEntrySellWeekLabels.size());
        xAxis.setValueFormatter(new CombinedChartFormmater(combinedChart));
//        xAxis.setAxisMaximum(7f);
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);


        //하단 차트 정보 (막대 색갈별 정보)
        Legend legend = combinedChart.getLegend();
        legend.setEnabled(false);
        legend.setFormSize(8);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.color_222222));

                //막대 그래프 상단 수치text 크기
//        barDataTopMenuSet.setValueTextSize(11);
//        barDataTopMenuSet.setValueTextColor(getResources().getColor(R.color.color_222222));
//        //소수점 제거를 위한 데이터 포맷 설정
//        barDataTopMenuSet.setValueFormatter(new DeleteDecimal());
//
//        topMenuChart.animateY(3000);
//        topMenuChart.setExtraBottomOffset(24.5f);
//
//
//        //x축 라벨 갯수
//        //라벨 커스텀을 위함...(x축 라벨)
//        switch (position) {
//            case 0:
//                xAxis.setLabelCount(barEntryTopSellLabels.size());
//                xAxis.setValueFormatter(new TopMenuSellFormatter(topMenuChart));
//                break;
//            case 1:
//                xAxis.setLabelCount(barEntryTopOrderLabels.size());
//                xAxis.setValueFormatter(new TopMenuOrderFormatter(topMenuChart));
//                break;
//        }
//
        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = combinedChart.getAxisLeft();
        YAxis YRAXis = combinedChart.getAxisRight();
//
        //활성화
        YLAxis.setDrawLabels(true);
        YLAxis.setDrawAxisLine(false);
        //gridline 은 옆에 선
        YLAxis.setDrawGridLines(true);
        YLAxis.setAxisMinimum(0);
//        if (position == 0) {
//            YLAxis.setAxisMaximum(500000);
//        } else {
//            YLAxis.setAxisMaximum(40);
//        }
        YLAxis.setGranularity(10);
        YLAxis.setTextColor(getResources().getColor(R.color.color_909090));
        YLAxis.setTextSize(12);

        //비활성화
        YRAXis.setDrawLabels(false);
        YRAXis.setDrawAxisLine(false);
        YRAXis.setDrawGridLines(false);

        // 각종 이벤트 방지.
        combinedChart.setTouchEnabled(false);
        combinedChart.setDragEnabled(false);
        combinedChart.setScaleEnabled(false);
        combinedChart.setPinchZoom(false);
        combinedChart.setDoubleTapToZoomEnabled(false);

        //하단 상세 설명(?) 안보이게 설정
        combinedChart.getDescription().setEnabled(false);

        CombinedData data = new CombinedData();
        data.setData(weeklySalesLineData);
        data.setData(weeklySalesBarData);
        xAxis.setAxisMaximum(data.getXMax() + 0.25f);

//        combinedChart.setExtraLeftOffset(10f);
        combinedChart.setData(data);
        combinedChart.notifyDataSetChanged();
        combinedChart.invalidate();
    }

    public void initVisitBarChart(int position){
        Log.d(TAG, "initVisitBarChart");

        switch (position) {
            case 0 :
                dailyVisitBarDataSet = new BarDataSet(barEntryVisitDaily, getResources().getString(R.string.main_deadline_visit_labels));
                dailyVisitBarData = new BarData(dailyVisitBarDataSet);
                break;
            case 1 :
//                barDataVisitSet = new BarDataSet(barEntryDinner, getResources().getString(R.string.main_deadline_visit_labels));
//                barDataVisit = new BarData(barDataVisitSet);
                break;
        }

        //막대 그래프의 너비
        dailyVisitBarData.setBarWidth(0.3f);

        //막대 그래프의 색을 지정해준다.
        dailyVisitBarDataSet.setColor(getResources().getColor(R.color.color_4263ff));

        //막대 그래프 상단 수치text 크기
        dailyVisitBarDataSet.setValueTextSize(11);
        dailyVisitBarDataSet.setValueTextColor(getResources().getColor(R.color.color_222222));
        //소수점 제거를 위한 데이터 포맷 설정
        dailyVisitBarDataSet.setValueFormatter(new DeleteDecimal());

        visitBarChart.animateY(1500);
        visitBarChart.setExtraBottomOffset(24.5f);

        //x축 설정
        XAxis xAxis = visitBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        xAxis.setYOffset(9.5f);
        xAxis.setTextColor(getResources().getColor(R.color.color_909090));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        //x축 라벨 갯수
        //라벨 커스텀을 위함...(x축 라벨)
        switch (position) {
            case 0:
                xAxis.setLabelCount(barEntrySellWeekLabels.size());
                xAxis.setValueFormatter(new CombinedChartFormmater(visitBarChart));
                break;
            case 1:
//                xAxis.setLabelCount(barEntryDinnerLabels.size());
//                xAxis.setValueFormatter(new MyDinnerFormatter(visitBarChart));
                break;
        }

        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = visitBarChart.getAxisLeft();
        YAxis YRAXis = visitBarChart.getAxisRight();

        //활성화
        YLAxis.setDrawLabels(true);
        YLAxis.setDrawAxisLine(false);
        //gridline 은 옆에 선
        YLAxis.setDrawGridLines(true);
        YLAxis.setAxisMinimum(0);
        YLAxis.setAxisMaximum(40);
        YLAxis.setGranularity(10);
        YLAxis.setTextColor(getResources().getColor(R.color.color_909090));
        YLAxis.setTextSize(12);

        //비활성화
        YRAXis.setDrawLabels(false);
        YRAXis.setDrawAxisLine(false);
        YRAXis.setDrawGridLines(false);

        // 각종 이벤트 방지.
        visitBarChart.setTouchEnabled(false);
        visitBarChart.setDragEnabled(false);
        visitBarChart.setScaleEnabled(false);
        visitBarChart.setPinchZoom(false);
        visitBarChart.setDoubleTapToZoomEnabled(false);

        //하단 차트 정보 (막대 색갈별 정보)
        Legend legend = visitBarChart.getLegend();
        legend.setEnabled(true);
        legend.setFormSize(8);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.color_222222));

        //하단 상세 설명(?) 안보이게 설정
        visitBarChart.getDescription().setEnabled(false);

        visitBarChart.setData(dailyVisitBarData);
        visitBarChart.notifyDataSetChanged();
        visitBarChart.invalidate();
    }

    /**
     *
     *  원 그래프 그리기.
     *
     * */
    public void initPieChart() {
        Log.d(TAG, "initPieChart");

        if (categorySellEntry.size() == 0) {
            Log.e(TAG, "categorySellEntry NULL!!");
            return;
        }

        categoryPieChart.setUsePercentValues(true);
        categoryPieChart.getDescription().setEnabled(false);
        categoryPieChart.setExtraOffsets(5,10,5,5);

        categoryPieChart.setDragDecelerationFrictionCoef(0.95f);

        categoryPieChart.setDrawHoleEnabled(false);
        categoryPieChart.setHoleColor(Color.WHITE);
        categoryPieChart.setTransparentCircleRadius(61f);

        Description description = new Description();
        description.setText(" "); //라벨
        description.setTextSize(15);
        categoryPieChart.setDescription(description);
        //하단 상세 설명(?) 안보이게 설정
        categoryPieChart.getDescription().setEnabled(false);
        categoryPieChart.setNoDataText("test");

        //하단 차트 정보 (막대 색갈별 정보) 안보이게 조정
        Legend legend = categoryPieChart.getLegend();
        legend.setEnabled(false);

//        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(categorySellEntry," ");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
//        int [] color={ getResources().getColor(R.color.color_fb7a63), getResources().getColor(R.color.color_fcc849),
//                getResources().getColor(R.color.color_6bd67c), getResources().getColor(R.color.color_c7abf6),
//                getResources().getColor(R.color.color_839afe), getResources().getColor(R.color.color_81c6fc),
//                getResources().getColor(R.color.color_4993cd)
//        };

        dataSet.setColors(color);
        categoryPieChart.setDrawMarkers(false);
        categoryPieChart.setDrawEntryLabels(false);
        categoryPieChart.setTouchEnabled(false);

        PieData data = new PieData((dataSet));
        //value 값 숨기기.
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        data.setDrawValues(false);

        categoryPieChart.setData(data);
    }

    public class CombinedChartFormmater extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public CombinedChartFormmater(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
//            Log.d(TAG, "value : " + value);
            if ((int)value - 1 < 0) {
                return barEntrySellWeekLabels.get(0);
            } else {
                return barEntrySellWeekLabels.get((int)value - 1);
            }

        }
    }

    /**
     * CombinedChart 차트 영역
     * 매출 통계 영역
     */
    public class WeeklySaleCombinedChartAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public WeeklySaleCombinedChartAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem : position - " + position);
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.item_analysis_combined_chart, container, false);

            combinedChart = (CombinedChart) view.findViewById(R.id.chart1);

            //position에 따라 그리는 값을 달리한다.
            switch (position) {
                case 0 :
//                    initTopMenuChart(position);
                    initSellBarChart(position);
                    break;
                case 1 :
//                    initTopMenuChart(position);
                    break;
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

    }

    ViewPager.OnPageChangeListener CombinedPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            switch (position) {
                case 0 :
                    tv_main_analysis_sales_daily.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_analysis_sales_daily.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_analysis_sales_week.setBackground(null);
                    tv_main_analysis_sales_week.setTypeface(Typeface.DEFAULT);

                    break;

                case 1:
                    tv_main_analysis_sales_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_analysis_sales_week.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_analysis_sales_daily.setBackground(null);
                    tv_main_analysis_sales_daily.setTypeface(Typeface.DEFAULT);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * BarChart 차트 영역
     * 방문 통계 영역
     */
    public class WeeklyVisitBarChartAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public WeeklyVisitBarChartAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem : position - " + position);
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.item_visit_bar_chart_analysis, container, false);

            visitBarChart = (BarChart) view.findViewById(R.id.visit_chart);

            //position에 따라 그리는 값을 달리한다.
            switch (position) {
                case 0 :
                    initVisitBarChart(position);
                    break;
                case 1 :
                    break;
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

    }

    ViewPager.OnPageChangeListener visitBarChartListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            switch (position) {
                case 0 :
                    tv_main_analysis_visit_daily.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_analysis_visit_daily.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_analysis_visit_week.setBackground(null);
                    tv_main_analysis_visit_week.setTypeface(Typeface.DEFAULT);

                    break;

                case 1:
                    tv_main_analysis_visit_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_analysis_visit_week.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_analysis_visit_daily.setBackground(null);
                    tv_main_analysis_visit_daily.setTypeface(Typeface.DEFAULT);
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * BarChart 차트 영역
     * 카테고리 별 인기 메뉴
     */
    public class HotCategoryMenuChartAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private TextView tv_yaxis_title;

        public HotCategoryMenuChartAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem : position - " + position);
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.item_hot_category_bar_chart_analysis, container, false);

            hotMenuBarChart = (BarChart) view.findViewById(R.id.hot_menu_chart);
            tv_yaxis_title = (TextView) view.findViewById(R.id.tv_yaxis_title);

            tv_yaxis_title.setText(categoryMenuString.get(position));

            //position에 따라 그리는 값을 달리한다.
            switch (position) {
                case 0 :
                    break;

                case 1 :
                    break;

                case 2 :
                    break;

                case 3 :
                    break;

                case 4 :
                    break;

                case 5 :
                    break;

                case 6 :
                    break;

                case 7 :
                    break;
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return categoryMenuString.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }

    }

    ViewPager.OnPageChangeListener hotCategoryBarChartListener = new ViewPager.OnPageChangeListener() {

        private TextView title;
        private TextView lastTitle;
        private int lastPosition = 0;

        @Override
        public void onPageSelected(final int position) {
            Log.d(TAG, "hotCategoryBarChartListener - position : " + position + " lastPosition : " + lastPosition);

            title = (TextView) rv_category_menu.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tv_category_menu_title);
            title.setTextColor(getResources().getColor(R.color.color_ffffff));
            title.setBackground(getResources().getDrawable(R.drawable.circular_focus));
            title.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));

            if (lastPosition != position) {
                lastTitle = (TextView) rv_category_menu.findViewHolderForAdapterPosition(lastPosition).itemView.findViewById(R.id.tv_category_menu_title);
                lastTitle.setTextColor(getResources().getColor(R.color.color_707594));
                lastTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                lastTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));
                lastPosition = position;
            }

            categoryMenuAdapter.notifyDataSetChanged();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    //메뉴 title 어댑터
    public class CategoryMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private ArrayList<String> inputData;

        public CategoryMenuAdapter(ArrayList<String> inputData) {
            this.inputData = inputData;
        }

        public class OriginalViewHolder extends RecyclerView.ViewHolder {
            public RelativeLayout rl_category_menu_container;
            public TextView tv_category_menu_title;

            public OriginalViewHolder(View v) {
                super(v);
                rl_category_menu_container = (RelativeLayout) v.findViewById(R.id.rl_category_menu_container);
                tv_category_menu_title = (TextView) v.findViewById(R.id.tv_category_menu_title);
            }
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder vh;
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis_menu, parent, false);
            vh = new OriginalViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof OriginalViewHolder) {
                final OriginalViewHolder viewHolder = (OriginalViewHolder) holder;

                //text 설정
                viewHolder.tv_category_menu_title.setText(inputData.get(position));
                if (position == 0 && checkFirstTime) {
                    viewHolder.tv_category_menu_title.setTextColor(getResources().getColor(R.color.color_ffffff));
                    viewHolder.tv_category_menu_title.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                    viewHolder.tv_category_menu_title.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));
                }

                viewHolder.tv_category_menu_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        vp_category_detail.setCurrentItem(position);
                    }
                });

                viewHolder.rl_category_menu_container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.tv_category_menu_title.performClick();
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return inputData.size();
        }
    }


    //소수점 제거를 위함
    public class DeleteDecimal extends ValueFormatter {
        private DecimalFormat mFormat;

        public DeleteDecimal() {
            mFormat = new DecimalFormat("#");
        }

        @Override
        public String getFormattedValue(float value) {
            return mFormat.format(value);

        }
    }

    //객체 생성하여 리스트 작성
    public void initColorList() {
        Log.d(TAG, "initColorList");

        //TODO 이 노가다를 해야하나??더 좋은 방법이...??
        //TODO 정책 수정 -> 결국 7개 다보내야함

        for (int i = 0; i < categoryDetailPercentList.size(); i++) {
            DailySalesList input = new DailySalesList();
            switch (i) {
                case 0 :
                    input.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_food));
                    if (weeklyDataList.get(0).getSellFood() == null
                            || weeklyDataList.get(0).getSellFood() == " "
                            || weeklyDataList.get(0).getSellFood().equals("0")) {
                        input.setCategoryRealSell("0");
                    } else {
                        input.setCategoryRealSell(weeklyDataList.get(0).getSellFood());

                        categoryMenuString.add(getResources().getString(R.string.main_deadline_pie_graph_food));
                    }
//                    input.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_food)));
                    break;
                case 1 :
                    input.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_beer));
                    if (weeklyDataList.get(0).getSellBeer() == null
                            || weeklyDataList.get(0).getSellBeer() == " "
                            || weeklyDataList.get(0).getSellBeer().equals("0")) {
                        input.setCategoryRealSell("0");
                    } else {
                        input.setCategoryRealSell(weeklyDataList.get(0).getSellBeer());

                        categoryMenuString.add(getResources().getString(R.string.main_deadline_pie_graph_beer));
                    }

//                    input.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_beer)));
                    break;
                case 2 :
                    if (weeklyDataList.get(0).getSellCock() == null
                            || weeklyDataList.get(0).getSellCock() == " "
                            || weeklyDataList.get(0).getSellCock().equals("0")) {
                        input.setCategoryRealSell("0");
                    } else {
                        input.setCategoryRealSell(weeklyDataList.get(0).getSellCock());
                    }
                    input.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_cock));
//                    input.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_cock)));

                    categoryMenuString.add(getResources().getString(R.string.main_deadline_pie_graph_cock));
                    break;
                case 3 :
                    if (weeklyDataList.get(0).getSellLiquor() == null
                            || weeklyDataList.get(0).getSellLiquor() == " "
                            || weeklyDataList.get(0).getSellLiquor().equals("0")) {
                        input.setCategoryRealSell("0");
                    } else {
                        input.setCategoryRealSell(weeklyDataList.get(0).getSellLiquor());
                    }
                    input.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_liquor));
//                    input.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_liquor)));

                    categoryMenuString.add(getResources().getString(R.string.main_deadline_pie_graph_liquor));
                    break;
                case 4 :
                    if (weeklyDataList.get(0).getSellDrink() == null
                            || weeklyDataList.get(0).getSellDrink() == " "
                            || weeklyDataList.get(0).getSellDrink().equals("0")) {
                        input.setCategoryRealSell("0");
                    } else {
                        input.setCategoryRealSell(weeklyDataList.get(0).getSellDrink());
                    }
                    input.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_drink));
//                    input.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_drink)));

                    categoryMenuString.add(getResources().getString(R.string.main_deadline_pie_graph_drink));
                    break;
                case 5 :
                    break;
                case 6 :
                    break;
            }
            input.setCategorySellPer(categoryDetailPercentList.get(i) + "%");
            input.setColor(colorOriginal[i]);
            insertListOfSellsData.add(input);
        }

//        DailySalesList dsl6 = new DailySalesList();
//        dsl6.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_lunch));
//        dsl6.setCategoryRealSell(pieChartData.getSellLunch());
//        dsl6.setCategorySellPer(pieChartData.getSellLunchPercent());
//        dsl6.setColor(R.color.color_81c6fc);
//        dsl6.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_lunch)));
//        insertListOfSellsData.add(dsl6);
//
//        DailySalesList dsl7 = new DailySalesList();
//        dsl7.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_delivery));
//        dsl7.setCategoryRealSell(pieChartData.getSellDelivery());
//        dsl7.setCategorySellPer(pieChartData.getSellDeliveryPercent());
//        dsl7.setColor(R.color.color_4993cd);
//        dsl7.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_delivery)));
//        insertListOfSellsData.add(dsl7);

        Log.d(TAG, "initColorList - result : " + insertListOfSellsData);

    }

    public void initCategorySellList() {
        Log.d(TAG, "initCategorySellList");

        if (weeklyDataList.size() == 0
            || weeklyDataList.get(0).getSellReal() == null
            || weeklyDataList.get(0).getSellReal() == " "
            || weeklyDataList.get(0).getSellReal().equals("0")) {
            Log.e(TAG, "weeklyDataList NULL!!");
            return;
        }

        if (weeklyDataList.get(0).getSellFood() == null
                || weeklyDataList.get(0).getSellFood() == " "
                || weeklyDataList.get(0).getSellFood().equals("0")) {
            categoryDetailPercentList.add(0);
        } else {
            categoryDetailPercentList.add((int)
                    ((Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellFood()))) * 100
                            / (Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellReal())))));
        }

        if (weeklyDataList.get(0).getSellBeer() == null
                || weeklyDataList.get(0).getSellBeer() == " "
                || weeklyDataList.get(0).getSellBeer().equals("0")) {
            categoryDetailPercentList.add(0);
        } else {
            categoryDetailPercentList.add((int)
                    ((Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellBeer()))) * 100
                            / (Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellReal())))));
        }

        if (weeklyDataList.get(0).getSellCock() == null
                || weeklyDataList.get(0).getSellCock() == " "
                || weeklyDataList.get(0).getSellCock().equals("0")) {
            categoryDetailPercentList.add(0);
        } else {
            categoryDetailPercentList.add((int)
                    ((Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellCock()))) * 100
                            / (Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellReal())))));
        }

        if (weeklyDataList.get(0).getSellLiquor() == null
                || weeklyDataList.get(0).getSellLiquor() == " "
                || weeklyDataList.get(0).getSellLiquor().equals("0")) {
            categoryDetailPercentList.add(0);
        } else {
            categoryDetailPercentList.add((int)
                    ((Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellLiquor()))) * 100
                            / (Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellReal())))));
        }

        if (weeklyDataList.get(0).getSellDrink() == null
                || weeklyDataList.get(0).getSellDrink() == " "
                || weeklyDataList.get(0).getSellDrink().equals("0")) {
            categoryDetailPercentList.add(0);
        } else {
            categoryDetailPercentList.add((int)
                    ((Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellDrink()))) * 100
                    / (Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellReal())))));
        }

        Log.d(TAG, "initCategorySellList - result : " + categoryDetailPercentList);

    }

    public void initCategoryDetailList() {
        Log.d(TAG, "initCategoryDetailList");
        DateUtils.d(TAG, "initCategoryDetailList - start");

        //TODO 이 노가다를 해야하나??더 좋은 방법이...??
        List<SalesObject> food = new ArrayList<SalesObject>();
        List<SalesObject> beer = new ArrayList<SalesObject>();
        List<SalesObject> cock = new ArrayList<SalesObject>();
        List<SalesObject> liquor = new ArrayList<SalesObject>();
        List<SalesObject> drink = new ArrayList<SalesObject>();
        List<SalesObject> lunch = new ArrayList<SalesObject>();
        List<SalesObject> delivery = new ArrayList<SalesObject>();

        for (int i = 0; i < dailyDataSalesListReal.size(); i++ ) {
            if (dailyDataSalesListReal.get(i).getCategory().equals("푸드")){
                food.add(dailyDataSalesListReal.get(i));
            }

            if (dailyDataSalesListReal.get(i).getCategory().equals("주류")){
                beer.add(dailyDataSalesListReal.get(i));
            }

            if (dailyDataSalesListReal.get(i).getCategory().equals("칵테일")){
                cock.add(dailyDataSalesListReal.get(i));
            }

            if (dailyDataSalesListReal.get(i).getCategory().equals("양주")){
                liquor.add(dailyDataSalesListReal.get(i));
            }

            if (dailyDataSalesListReal.get(i).getCategory().equals("드링크")){
                drink.add(dailyDataSalesListReal.get(i));
            }

            if (dailyDataSalesListReal.get(i).getCategory().equals("런치")){
                lunch.add(dailyDataSalesListReal.get(i));
            }

            if (dailyDataSalesListReal.get(i).getCategory().equals("배달")){
                delivery.add(dailyDataSalesListReal.get(i));
            }
        }

        if (food.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_food), makeListItems(food));
        }

        if (beer.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_beer), makeListItems(beer));
        }

        if (cock.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_cock), makeListItems(cock));
        }

        if (liquor.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_liquor), makeListItems(liquor));
        }

        if (drink.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_drink), makeListItems(drink));
        }

        if (lunch.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_lunch), makeListItems(lunch));
        }

        if (delivery.size() != 0) {
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_delivery), makeListItems(delivery));
        }

        Log.d(TAG, "initCategoryDetailList - hash : " + lastRawDataHash.toString());

        addCategoryDetailData();

        DateUtils.d(TAG, "initCategoryDetailList - end");
    }

    /**
     * 중복되는 아이템 체크를 위한 로직
     *
     * */
    public List<DailySalesListItems> makeListItems(List<SalesObject> inputList) {
        Log.d(TAG, "makeListItems - " + inputList.toString());

        //같은 카테고리에서 같은 종류의 음식이 있는지 체크하는로직
        List<String> check  = new ArrayList<String>();
        List<DailySalesListItems> result = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i ++) {
            //상품명이 다른 것이 있을 경우
            if (!check.contains(inputList.get(i).getProductName())) {
                check.add(inputList.get(i).getProductName());

                //새로운 아이템을 생성해준다.
                DailySalesListItems items = new DailySalesListItems();
                items.setItemName(inputList.get(i).getProductName());
                items.setItemCount(inputList.get(i).getProductCount());
                items.setItemRealSell(Tools.deleteComma(inputList.get(i).getRealSales()));

                //결과 값에 반영한다.
                result.add(items);
            } else {
                //기존에 있는 상품일 경우
                for (int j = 0; j < result.size(); j++) {
                    if (result.get(j).getItemName().equals(inputList.get(i).getProductName())) {
                        //상품갯수
                        String count = result.get(j).getItemCount();
                        //판매금액
                        String sales = result.get(j).getItemRealSell();
                        //파싱 이후 set해준다 (,때문에)
                        int resultCount = Integer.parseInt(Tools.deleteComma(count)) + Integer.parseInt(Tools.deleteComma(inputList.get(i).getProductCount()));
                        result.get(j).setItemCount("" + resultCount);
                        int resultSales = Integer.parseInt(Tools.deleteComma(sales)) + Integer.parseInt(Tools.deleteComma(inputList.get(i).getRealSales()));
                        result.get(j).setItemRealSell("" + resultSales);
                    }
                }
            }
        }

        //TODO 팔린 가격순으로 정렬
        Collections.sort(result, new Comparator<DailySalesListItems>() {
            @Override
            public int compare(DailySalesListItems t1, DailySalesListItems t2) {
                int ret = 0;
                if (Integer.parseInt(t1.getItemRealSell()) < Integer.parseInt(t2.getItemRealSell())) {
                    ret = 1;
                }

                if ((Integer.parseInt(t1.getItemRealSell()) == Integer.parseInt(t2.getItemRealSell()))) {
                    if (Integer.parseInt(t1.getItemCount()) < Integer.parseInt(t2.getItemCount())) {
                        ret = 1;
                    } else if (Integer.parseInt(t1.getItemCount()) == Integer.parseInt(t2.getItemCount())) {
                        ret = 0;
                    } else if (Integer.parseInt(t1.getItemCount()) > Integer.parseInt(t2.getItemCount())) {
                        ret = -1;
                    }
                }

                if (Integer.parseInt(t1.getItemRealSell()) > Integer.parseInt(t2.getItemRealSell())) {
                    ret = -1;
                }

                return ret;
            }
        });

        Log.d(TAG, "result check : " + result);

        return result;
    }
}
