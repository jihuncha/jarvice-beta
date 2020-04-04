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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import huni.techtown.org.jarvice.JarviceSettings;
import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.database.TBL_DAILY_SALES;
import huni.techtown.org.jarvice.database.TBL_WEEKLY_SALES;
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

    //카테고리 하단 순서
    private ArrayList<Integer> categoryDetailPercentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis_detail);

        mContext = this;
        mHandler = new Handler();

        Log.d(TAG, "check : " + JarviceSettings.getInstance(mContext).getMondaySellAvg());

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

        //TODO 0번째가 일요일이 아닌 월요일로 하기 위한 세팅..
        DailySalesObject sundayData = new DailySalesObject();
        sundayData = dailyDataList.get(0);
        dailyDataList.remove(0);
        dailyDataList.add(sundayData);

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

        //카테고리 별 매출 상세 항목
        categoryDetailPercentList = new ArrayList<Integer>();
        initCategorySellList();
        initPieChart();



    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_analysis_back :
                finish();
                break;

            case R.id.tv_main_analysis_sales_daily :
                sales_bar_chart.setCurrentItem(0);

                tv_main_analysis_sales_daily.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_sales_daily.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_sales_week.setBackground(null);
                tv_main_analysis_sales_week.setTypeface(Typeface.DEFAULT);

                break;

            case R.id.tv_main_analysis_sales_week :
                sales_bar_chart.setCurrentItem(1);

                tv_main_analysis_sales_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_sales_week.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_sales_daily.setBackground(null);
                tv_main_analysis_sales_daily.setTypeface(Typeface.DEFAULT);
                break;

            case R.id.tv_main_analysis_visit_daily :
                visit_bar_chart.setCurrentItem(0);

                tv_main_analysis_visit_daily.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_analysis_visit_daily.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_analysis_visit_week.setBackground(null);
                tv_main_analysis_visit_week.setTypeface(Typeface.DEFAULT);

                break;

            case R.id.tv_main_analysis_visit_week :
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
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellBear()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellCock()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellLiquor()))));
        categorySellEntry.add(new PieEntry(Integer.parseInt(Tools.deleteComma(weeklyDataList.get(0).getSellDrink()))));

        Log.d(TAG, "categorySellEntry : " + categorySellEntry);


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
        legend.setEnabled(true);
        legend.setFormSize(8);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.color_222222));
//        legend.

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

        visitBarChart.animateY(3000);
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
        int [] color={ getResources().getColor(R.color.color_fb7a63), getResources().getColor(R.color.color_fcc849),
                getResources().getColor(R.color.color_6bd67c), getResources().getColor(R.color.color_c7abf6),
                getResources().getColor(R.color.color_839afe), getResources().getColor(R.color.color_81c6fc),
                getResources().getColor(R.color.color_4993cd)
        };

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
            Log.d(TAG, "value : " + value);
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

        if (weeklyDataList.get(0).getSellBear() == null
                || weeklyDataList.get(0).getSellBear() == " "
                || weeklyDataList.get(0).getSellBear().equals("0")) {
            categoryDetailPercentList.add(0);
        } else {
            categoryDetailPercentList.add((int)
                    ((Float.parseFloat(Tools.deleteComma(weeklyDataList.get(0).getSellBear()))) * 100
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

        Log.d(TAG, "test : " + categoryDetailPercentList);

    }
}
