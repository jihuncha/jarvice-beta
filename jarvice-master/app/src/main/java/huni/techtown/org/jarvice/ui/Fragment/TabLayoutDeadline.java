package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesListItems;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.ui.adapter.AdapterListExpand;
import huni.techtown.org.jarvice.ui.adapter.AdapterPieChartList;
import huni.techtown.org.jarvice.ui.utils.DateUtils;
import huni.techtown.org.jarvice.ui.utils.Tools;

import static com.github.mikephil.charting.components.XAxis.XAxisPosition.TOP_INSIDE;

/**
 * 마감내역
 * 가장 중요한화면
 * 당일 매출/주간 매출/월간 매출
 * 판매량 상세 데이터
 * 방문자 수
 * 오늘의 메뉴(?)
* */

public class TabLayoutDeadline extends Fragment implements View.OnClickListener {
    private static final String TAG = TabLayoutDeadline.class.getSimpleName();

    private Handler mHandler;
    private Context mContext;

    //타이틀
    private TextView tv_main_deadline_company_title;
    private TextView tv_main_deadline_company_date;

    //daily/weekly/monthly 데이터가 몇개 있는지 체크
    private int checkDaily;
    private int checkWeekly;
    private int checkMonthly;

    private String weeklyId;
    private String monthlyId;

    //막대 그래프
    private BarChart barChart;

    private ViewPager deadlineSalesBarChart;
    private DeadlineViewpagerAdapter deadlineViewpagerAdapter;

    // 일/주/월 로 나누기
    private TextView tv_main_deadline_barchart_day;
    private TextView tv_main_deadline_barchart_week;
    private TextView tv_main_deadline_barchart_month;

    //막대 데이터
    private ArrayList<BarEntry> barEntryDaily;
    private ArrayList<BarEntry> barEntryWeekly;
    private ArrayList<BarEntry> barEntryMonthly;
    //막대 데이터 라벨
    private ArrayList<String> barEntryDailyLabels;
    private ArrayList<String> barEntryWeeklyLabels;
    private ArrayList<String> barEntryMonthlyLabels;

    //막대 데이터 - 매출
    private BarDataSet barDataSet;
    private BarData barData;

    //원 그래프
    private PieChart pieChart;
    private DailySalesObject pieChartData;

    private String getLastDataDate;
    private String getDayOfWeek;

    //총매출액
    private TextView tvSellRealResult;

    //그래프 아래 상세 금액/percent 표기
    private RecyclerView rvSellListColor;
    private AdapterPieChartList mAdapterPieChartList;

    //그래프 하단에 판매량
    private RecyclerView rvSellListDetail;
    private AdapterListExpand mAdapterListExpand;
    private List<DailySalesList> insertListOfSellsData;

    //상세 표기
    private  List<SalesObject> lastRawData;
    private  HashMap<String, List<DailySalesListItems>> lastRawDataHash;

    //방문 차트
    private ViewPager visitBarChart;
    private VisitViewpagerAdapter visitViewpagerAdapter;

    private ArrayList<BarEntry> barEntryDinner;
    private ArrayList<String> barEntryDinnerLabels;
    private ArrayList<BarEntry> barEntryLunch;
    private ArrayList<String> barEntryLunchLabels;

    //막대 데이터 - 방문자수
    private BarChart visitChart;
    private BarDataSet barDataVisitSet;
    private BarData barDataVisit;

    // 점심/저녁 으로 나누기
    private TextView tv_main_deadline_barchart_lunch;
    private TextView tv_main_deadline_barchart_dinner;

    //막대 데이터 - Top 메뉴
    private ViewPager topMenuBarChart;
    private TopMenuViewpagerAdapter topMenuViewpagerAdapter;

    private ArrayList<BarEntry> barEntryTopSell;
    private ArrayList<String> barEntryTopSellLabelsSample;
    private ArrayList<String> barEntryTopSellLabels;
    private ArrayList<BarEntry> barEntryTopOrder;
    private ArrayList<String> barEntryTopOrderLabelsSample;
    private ArrayList<String> barEntryTopOrderLabels;

    private ArrayList<DailySalesListItems> topMenuList;
    private ArrayList<DailySalesListItems> topMenuSellRankList;
    private ArrayList<DailySalesListItems> topMenuOrderRankList;

    private ArrayList<DailySalesListItems> test;

    private BarChart topMenuChart;
    private BarDataSet barDataTopMenuSet;
    private BarData barDataTopMenu;

    // 매출액/주문수 로 나누기
    private TextView tv_main_deadline_sell_rank;
    private TextView tv_main_deadline_order_rank;

    private LinearLayout ll_test;

    public TabLayoutDeadline() {
    }

    public static TabLayoutDeadline newInstance() {
        TabLayoutDeadline fragment = new TabLayoutDeadline();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        final View root = inflater.inflate(R.layout.tab_main_deadline, container, false);

        mContext = container.getContext();
        mHandler = new Handler();

        //날짜 선정 -> 0 번쨰가 가장 최근
        Log.d(TAG, "getLastData - All :  " + DatabaseManager.getInstance(mContext).getLastData());

        if (DatabaseManager.getInstance(mContext).getLastData().size() != 0) {
            pieChartData = DatabaseManager.getInstance(mContext).getLastData().get(0);
        } else {
            Toast.makeText(mContext,"해당 데이터 제공 불가능", Toast.LENGTH_LONG).show();
            return root;
        }

        Log.d(TAG, "pieChartData - date : " + pieChartData.getSellDate());

        checkDaily = DatabaseManager.getInstance(mContext).getLastData().size();

        tv_main_deadline_company_title = root.findViewById(R.id.tv_main_deadline_company_title);
        tv_main_deadline_company_date = root.findViewById(R.id.tv_main_deadline_company_date);

        if (pieChartData != null) {
            getLastDataDate = pieChartData.getSellDate().replace("-" , ".");
            getDayOfWeek = Tools.getDayOfWeek(pieChartData.getSellDate());

        } else {
            getLastDataDate = mContext.getResources().getString(R.string.unknown_result);
            getDayOfWeek = mContext.getResources().getString(R.string.unknown_result);
        }

        //타이틀 생성
        tv_main_deadline_company_date.setText(getLastDataDate + " "
                + getDayOfWeek + " " + mContext.getResources().getString(R.string.main_deadline_title_date));

        //월간 데이터 탐색
        String[] checkDataFromDate = pieChartData.getSellDate().split("-");
        Log.d(TAG, "checkDataFromDate : " + checkDataFromDate);
        if (DatabaseManager.getInstance(mContext).getMonthlyLastDataCheck(checkDataFromDate[0] , checkDataFromDate[1]) != null) {
            monthlyId = "" +  DatabaseManager.getInstance(mContext).getMonthlyLastDataCheck(checkDataFromDate[0] , checkDataFromDate[1]).get(0).getId();
        } else {
            Log.e(TAG, "monthlyId null!!");
        }

        if (monthlyId != null && monthlyId != "") {
            checkMonthly = DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).size();
        }

        //주간 데이터 탐색
        String weekCheck = pieChartData.getSellWeek();
        String year = checkDataFromDate[0];

        if (DatabaseManager.getInstance(mContext).getWeeklyLastDataCheck(year, weekCheck).size() == 0) {
            weeklyId = mContext.getResources().getString(R.string.unknown_result);
            checkWeekly = 0;
        } else {
            weeklyId = "" +  DatabaseManager.getInstance(mContext).getWeeklyLastDataCheck(year, weekCheck).get(0).getId();
            if (weeklyId != null && weeklyId != "") {
                checkWeekly = DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).size();
            }
        }

        //객체 생성
        barEntryDaily = new ArrayList<>();
        barEntryWeekly = new ArrayList<>();
        barEntryMonthly = new ArrayList<>();
        barEntryDailyLabels = new ArrayList<String>();
        barEntryWeeklyLabels = new ArrayList<String>();
        barEntryMonthlyLabels = new ArrayList<String >();

        barEntryDinner = new ArrayList<>();
        barEntryDinnerLabels = new ArrayList<String>();
        barEntryLunch = new ArrayList<>();
        barEntryLunchLabels = new ArrayList<String>();

        //bar chart 데이터들 삽입
        addValuesForBarChart();
        addValuesForBarChartVisit();

        //막대 그래프 (매출 그래프)
        deadlineSalesBarChart = (ViewPager) root.findViewById(R.id.deadline_sales_bar_chart);
        deadlineViewpagerAdapter = new DeadlineViewpagerAdapter();
        deadlineSalesBarChart.setAdapter(deadlineViewpagerAdapter);
        deadlineSalesBarChart.addOnPageChangeListener(viewPagerPageChangeListener);

        //view 유지하는 갯수 양쪽 2개씩으로 수정
        deadlineSalesBarChart.setOffscreenPageLimit(2);

        //일/주/월
        tv_main_deadline_barchart_day = root.findViewById(R.id.tv_main_deadline_barchart_day);
        tv_main_deadline_barchart_week = root.findViewById(R.id.tv_main_deadline_barchart_week);
        tv_main_deadline_barchart_month = root.findViewById(R.id.tv_main_deadline_barchart_month);

        tv_main_deadline_barchart_day.setOnClickListener(this);
        tv_main_deadline_barchart_week.setOnClickListener(this);
        tv_main_deadline_barchart_month.setOnClickListener(this);

        //Pie Chart
        pieChart = (PieChart) root.findViewById(R.id.piechart);

        lastRawData = new ArrayList<>();

        //해당 날짜에 있는 rawdata 목록.
        Log.d(TAG, "rawData : " + DatabaseManager.getInstance(mContext).getDateSalesObject(pieChartData.getSellDate(),TAG + "- onCreateView"));
        lastRawData.addAll(DatabaseManager.getInstance(mContext).getDateSalesObject(pieChartData.getSellDate(),TAG + "- onCreateView"));

        //해당 raw 데이터들을 카테고리별로 나눈다.
        lastRawDataHash = new HashMap<>();

        //pieChart 하단 부분 및 list 부분 처리를위한 객체 생성 - initSellList
        insertListOfSellsData = new ArrayList<DailySalesList>();

        //원 그래프 작성
        initPieChart();

        // 판매 항목 상세 보기 객체 생성
        initSellDetailList();

        //원 그래프 하단 판매 항목 객체 생성
        initSellList();
        Log.d(TAG, "insertListOfSellsData all - " + insertListOfSellsData);

        //총매출 text 지정
        tvSellRealResult = (TextView) root.findViewById(R.id.tv_sell_real_result);
        tvSellRealResult.setText(Tools.decimalFormat(pieChartData.getSellReal()));

        rvSellListColor = (RecyclerView) root.findViewById(R.id.rv_sell_list_color);
        rvSellListColor.setLayoutManager(new LinearLayoutManager(mContext));
        rvSellListColor.setHasFixedSize(true);

        //Pie Chart Text Area
        mAdapterPieChartList = new AdapterPieChartList(mContext, insertListOfSellsData);
        mAdapterPieChartList.setOnItemClickListener(new AdapterPieChartList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DailySalesList obj, int position) {
                Log.d(TAG, "mAdapterPieChartList - onItemClick : " + position);
            }
        });
        rvSellListColor.setAdapter(mAdapterPieChartList);

        rvSellListDetail = (RecyclerView)root.findViewById(R.id.rv_sell_list_detail);
        rvSellListDetail.setLayoutManager(new LinearLayoutManager(mContext));
        rvSellListDetail.setHasFixedSize(true);

        //Pie Chart Bottom Area
        mAdapterListExpand = new AdapterListExpand(mContext, insertListOfSellsData);
        mAdapterListExpand.setOnItemClickListener(new AdapterListExpand.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DailySalesList obj, int position) {
                Log.d(TAG, "mAdapterListExpand - onItemClick : " + position);
            }
        });
        rvSellListDetail.setAdapter(mAdapterListExpand);

        //방문 차트
        visitBarChart = (ViewPager) root.findViewById(R.id.visit_bar_chart);
        visitViewpagerAdapter = new VisitViewpagerAdapter();
        visitBarChart.setAdapter(visitViewpagerAdapter);
        visitBarChart.addOnPageChangeListener(bottomViewPagerPageChangeListener);

        tv_main_deadline_barchart_lunch = root.findViewById(R.id.tv_main_deadline_barchart_lunch);
        tv_main_deadline_barchart_dinner = root.findViewById(R.id.tv_main_deadline_barchart_dinner);

        tv_main_deadline_barchart_lunch.setOnClickListener(this);
        tv_main_deadline_barchart_dinner.setOnClickListener(this);

        //어제의 TOP 메뉴
        initTopMenuList();

        topMenuBarChart = (ViewPager) root.findViewById(R.id.top_menu_bar_chart);
        topMenuViewpagerAdapter = new TopMenuViewpagerAdapter();
        topMenuBarChart.setAdapter(topMenuViewpagerAdapter);
        topMenuBarChart.addOnPageChangeListener(topMenuViewPagerPageChangeListener);

        tv_main_deadline_sell_rank = root.findViewById(R.id.tv_main_deadline_sell_rank);
        tv_main_deadline_order_rank = root.findViewById(R.id.tv_main_deadline_order_rank);

        tv_main_deadline_sell_rank.setOnClickListener(this);
        tv_main_deadline_order_rank.setOnClickListener(this);

        ll_test = (LinearLayout) root.findViewById(R.id.ll_test);
        Drawable testDraw = getResources().getDrawable(R.drawable.circular_background_graph_new);
//        testDraw.setAlpha(120);
//        ll_test.setBackground(testDraw);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addValuesForBarChart() {
        Log.d(TAG, "addValuesForBarChart");
        //일간
        switch (checkDaily) {
            case 3 :
                barEntryDaily.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getLastData().get(2).getSellReal()))));
                barEntryDaily.add(new BarEntry(2, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getLastData().get(1).getSellReal()))));
                barEntryDaily.add(new BarEntry(3, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getLastData().get(0).getSellReal()))));

                barEntryDailyLabels.add(Tools.changeForBarChart(DatabaseManager.getInstance(mContext).getLastData().get(2).getSellDate()) +
                        DatabaseManager.getInstance(mContext).getLastData().get(2).getSellDayOfWeek());
                barEntryDailyLabels.add(Tools.changeForBarChart(DatabaseManager.getInstance(mContext).getLastData().get(1).getSellDate()) +
                        DatabaseManager.getInstance(mContext).getLastData().get(1).getSellDayOfWeek());
                barEntryDailyLabels.add(Tools.changeForBarChart(DatabaseManager.getInstance(mContext).getLastData().get(0).getSellDate()) +
                        DatabaseManager.getInstance(mContext).getLastData().get(0).getSellDayOfWeek());

                break;

            case 2 :
                barEntryDaily.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getLastData().get(1).getSellReal()))));
                barEntryDaily.add(new BarEntry(2, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getLastData().get(0).getSellReal()))));

                barEntryDailyLabels.add(Tools.changeForBarChart(DatabaseManager.getInstance(mContext).getLastData().get(1).getSellDate()) +
                        DatabaseManager.getInstance(mContext).getLastData().get(1).getSellDayOfWeek());
                barEntryDailyLabels.add(Tools.changeForBarChart(DatabaseManager.getInstance(mContext).getLastData().get(0).getSellDate()) +
                        DatabaseManager.getInstance(mContext).getLastData().get(0).getSellDayOfWeek());

                break;
            case 1 :
                barEntryDaily.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getLastData().get(0).getSellReal()))));
                barEntryDaily.add(new BarEntry(2, 0));

                barEntryDailyLabels.add(Tools.changeForBarChart(DatabaseManager.getInstance(mContext).getLastData().get(0).getSellDate()) +
                        DatabaseManager.getInstance(mContext).getLastData().get(0).getSellDayOfWeek());
                barEntryDailyLabels.add("데이터없음");

                break;
        }

        //주간
        switch (checkWeekly) {
            case 3 :
                barEntryWeekly.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(2).getSellReal()))));
                barEntryWeekly.add(new BarEntry(2, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(1).getSellReal()))));
                barEntryWeekly.add(new BarEntry(3, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getSellReal()))));

                barEntryWeeklyLabels.add(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(2).getStartWeek() + " ~ "
                                            + DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(2).getEndWeek());
                barEntryWeeklyLabels.add(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(1).getStartWeek() + " ~ "
                        + DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(1).getEndWeek());
                barEntryWeeklyLabels.add(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getStartWeek() + " ~ "
                        + DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getEndWeek());

                break;

            case 2 :
                barEntryWeekly.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(1).getSellReal()))));
                barEntryWeekly.add(new BarEntry(2, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getSellReal()))));

                barEntryWeeklyLabels.add(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(1).getStartWeek() + " ~ "
                        + DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(1).getEndWeek());
                barEntryWeeklyLabels.add(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getStartWeek() + " ~ "
                        + DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getEndWeek());

                break;
            case 1 :
                barEntryWeekly.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getSellReal()))));
                barEntryWeekly.add(new BarEntry(2, 0));

                barEntryWeeklyLabels.add(DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getStartWeek() + " ~ "
                        + DatabaseManager.getInstance(mContext).getWeeklyLastData(weeklyId).get(0).getEndWeek());
                barEntryWeeklyLabels.add("데이터없음");

                break;

            case 0 :
                break;
        }

        //월간
        switch (checkMonthly) {
            case 3 :
                barEntryMonthly.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(2).getSellReal()))));
                barEntryMonthly.add(new BarEntry(2, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(1).getSellReal()))));
                barEntryMonthly.add(new BarEntry(3, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(0).getSellReal()))));

                barEntryMonthlyLabels.add(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(2).getSellMonth() + "월");
                barEntryMonthlyLabels.add(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(1).getSellMonth() + "월");
                barEntryMonthlyLabels.add(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(0).getSellMonth() + "월");

                break;

            case 2 :
                barEntryMonthly.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(1).getSellReal()))));
                barEntryMonthly.add(new BarEntry(2, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(0).getSellReal()))));

                barEntryMonthlyLabels.add(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(1).getSellMonth() + "월");
                barEntryMonthlyLabels.add(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(0).getSellMonth() + "월");

                break;
            case 1 :
                barEntryMonthly.add(new BarEntry(1, Float.parseFloat(Tools.deleteComma(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(0).getSellReal()))));
                barEntryMonthly.add(new BarEntry(2, 0));

                barEntryMonthlyLabels.add(DatabaseManager.getInstance(mContext).getMonthlyLastData(monthlyId).get(0).getSellMonth() + "월");
                barEntryMonthlyLabels.add("데이터없음");

                break;
        }

    }

    public void addValuesForBarChartVisit() {
        Log.d(TAG, "addValuesForBarChartVisit");

        //TODO 추후 구현...
        //점심
        barEntryLunch.add(new BarEntry(1, 0));
        barEntryLunch.add(new BarEntry(2, 0));
        barEntryLunch.add(new BarEntry(3, 0));
        barEntryLunch.add(new BarEntry(4, 0));
        barEntryLunch.add(new BarEntry(5, 0));
        barEntryLunch.add(new BarEntry(6, 0));
        barEntryLunch.add(new BarEntry(7, 0));
        barEntryLunchLabels.add("10");
        barEntryLunchLabels.add("11");
        barEntryLunchLabels.add("12");
        barEntryLunchLabels.add("13");
        barEntryLunchLabels.add("14");
        barEntryLunchLabels.add("15");
        barEntryLunchLabels.add("16");

        //저녁
        if (pieChartData.getDinnerVisitFive() == null || pieChartData.getDinnerVisitFive().isEmpty()) {
            barEntryDinner.add(new BarEntry(1, 0));
        } else {
            barEntryDinner.add(new BarEntry(1, Float.parseFloat(pieChartData.getDinnerVisitFive())));
        }
        barEntryDinnerLabels.add("5");

        if (pieChartData.getDinnerVisitSix() == null || pieChartData.getDinnerVisitSix().isEmpty()) {
            barEntryDinner.add(new BarEntry(2, 0));
        } else {
            barEntryDinner.add(new BarEntry(2, Float.parseFloat(pieChartData.getDinnerVisitSix())));
        }
        barEntryDinnerLabels.add("6");

        if (pieChartData.getDinnerVisitSeven() == null || pieChartData.getDinnerVisitSeven().isEmpty()) {
            barEntryDinner.add(new BarEntry(3, 0));
        } else {
            barEntryDinner.add(new BarEntry(3, Float.parseFloat(pieChartData.getDinnerVisitSeven())));
        }
        barEntryDinnerLabels.add("7");

        if (pieChartData.getDinnerVisitEight() == null || pieChartData.getDinnerVisitEight().isEmpty()) {
            barEntryDinner.add(new BarEntry(4, 0));
        } else {
            barEntryDinner.add(new BarEntry(4, Float.parseFloat(pieChartData.getDinnerVisitEight())));
        }
        barEntryDinnerLabels.add("8");

        if (pieChartData.getDinnerVisitNine() == null || pieChartData.getDinnerVisitNine().isEmpty()) {
            barEntryDinner.add(new BarEntry(5, 0));
        } else {
            barEntryDinner.add(new BarEntry(5, Float.parseFloat(pieChartData.getDinnerVisitNine())));
        }
        barEntryDinnerLabels.add("9");

        if (pieChartData.getDinnerVisitTen() == null || pieChartData.getDinnerVisitTen().isEmpty()) {
            barEntryDinner.add(new BarEntry(6, 0));
        } else {
            barEntryDinner.add(new BarEntry(6, Float.parseFloat(pieChartData.getDinnerVisitTen())));
        }
        barEntryDinnerLabels.add("10");

        if (pieChartData.getDinnerVisitEleven() == null || pieChartData.getDinnerVisitEleven().isEmpty()) {
            barEntryDinner.add(new BarEntry(7, 0));
        } else {
            barEntryDinner.add(new BarEntry(7, Float.parseFloat(pieChartData.getDinnerVisitEleven())));
        }
        barEntryDinnerLabels.add("11");

        if (pieChartData.getDinnerVisitTwelve() == null || pieChartData.getDinnerVisitTwelve().isEmpty()) {
            barEntryDinner.add(new BarEntry(8, 0));
        } else {
            barEntryDinner.add(new BarEntry(8, Float.parseFloat(pieChartData.getDinnerVisitTwelve())));
        }
        barEntryDinnerLabels.add("12");

        if (pieChartData.getDinnerVisitOne() == null || pieChartData.getDinnerVisitOne().isEmpty()) {
            barEntryDinner.add(new BarEntry(9, 0));
        } else {
            barEntryDinner.add(new BarEntry(9, Float.parseFloat(pieChartData.getDinnerVisitOne())));
        }
        barEntryDinnerLabels.add("1");

        Log.d(TAG, "addValuesForBarChartVisit - barEntryDinner : " + barEntryDinner.toString());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_main_deadline_barchart_day :
                tv_main_deadline_barchart_day.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_barchart_day.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_barchart_week.setBackground(null);
                tv_main_deadline_barchart_month.setBackground(null);
                tv_main_deadline_barchart_week.setTypeface(Typeface.DEFAULT);
                tv_main_deadline_barchart_month.setTypeface(Typeface.DEFAULT);
                deadlineSalesBarChart.setCurrentItem(0);

                break;

            case R.id.tv_main_deadline_barchart_week :
                tv_main_deadline_barchart_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_barchart_week.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_barchart_day.setBackground(null);
                tv_main_deadline_barchart_month.setBackground(null);
                tv_main_deadline_barchart_day.setTypeface(Typeface.DEFAULT);
                tv_main_deadline_barchart_month.setTypeface(Typeface.DEFAULT);
                deadlineSalesBarChart.setCurrentItem(1);

                break;

            case R.id.tv_main_deadline_barchart_month :
                tv_main_deadline_barchart_month.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_barchart_month.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_barchart_day.setBackground(null);
                tv_main_deadline_barchart_week.setBackground(null);
                tv_main_deadline_barchart_day.setTypeface(Typeface.DEFAULT);
                tv_main_deadline_barchart_week.setTypeface(Typeface.DEFAULT);
                deadlineSalesBarChart.setCurrentItem(2);

                break;

            case R.id.tv_main_deadline_barchart_lunch :
                tv_main_deadline_barchart_lunch.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_barchart_lunch.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_barchart_dinner.setBackground(null);
                tv_main_deadline_barchart_dinner.setTypeface(Typeface.DEFAULT);

                visitBarChart.setCurrentItem(0);
                break;

            case R.id.tv_main_deadline_barchart_dinner :
                tv_main_deadline_barchart_dinner.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_barchart_dinner.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_barchart_lunch.setBackground(null);
                tv_main_deadline_barchart_lunch.setTypeface(Typeface.DEFAULT);

                visitBarChart.setCurrentItem(1);
                break;

            case R.id.tv_main_deadline_sell_rank :
                tv_main_deadline_sell_rank.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_sell_rank.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_order_rank.setBackground(null);
                tv_main_deadline_order_rank.setTypeface(Typeface.DEFAULT);

                topMenuBarChart.setCurrentItem(0);
                break;

            case R.id.tv_main_deadline_order_rank :
                tv_main_deadline_order_rank.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                tv_main_deadline_order_rank.setTypeface(Typeface.DEFAULT_BOLD);

                tv_main_deadline_sell_rank.setBackground(null);
                tv_main_deadline_sell_rank.setTypeface(Typeface.DEFAULT);

                topMenuBarChart.setCurrentItem(1);
                break;
        }
    }

    public void initBarChart(int position, int size) {
        Log.d(TAG, "initBarChart");

        switch (position) {
            case 0 :
                barDataSet = new BarDataSet(barEntryDaily, null);
                barData = new BarData(barDataSet);
                break;
            case 1 :
                barDataSet = new BarDataSet(barEntryWeekly, null);
                barData = new BarData(barDataSet);
                break;
            case 2 :
                barDataSet = new BarDataSet(barEntryMonthly, null);
                barData = new BarData(barDataSet);
                break;
        }

        //막대 그래프의 너비
        barData.setBarWidth(0.3f);

        //막대 그래프의 색을 지정해준다.
        switch (size) {
            case 3 :
                barDataSet.setColors(getResources().getColor(R.color.color_e6e9f7),
                        getResources().getColor(R.color.color_e6e9f7),
                        getResources().getColor(R.color.color_4263ff));

                List<Integer> colorList = new ArrayList<Integer>();

                colorList.add(getResources().getColor(R.color.color_222222));
                colorList.add(getResources().getColor(R.color.color_222222));
                colorList.add(getResources().getColor(R.color.color_4263ff));
                barDataSet.setValueTextColors(colorList);
                break;
            case 2 :
                barDataSet.setColors(getResources().getColor(R.color.color_e6e9f7),
                        getResources().getColor(R.color.color_4263ff));

                List<Integer> colorListTwo = new ArrayList<Integer>();

                colorListTwo.add(getResources().getColor(R.color.color_222222));
                colorListTwo.add(getResources().getColor(R.color.color_4263ff));
                barDataSet.setValueTextColors(colorListTwo);
                break;
            case 1 :
                barDataSet.setColors(getResources().getColor(R.color.color_4263ff));

                List<Integer> colorListOne = new ArrayList<Integer>();

                colorListOne.add(getResources().getColor(R.color.color_4263ff));
                barDataSet.setValueTextColors(colorListOne);
                break;

        }

        //막대 그래프 상단 수치text 크기
        barDataSet.setValueTextSize(13);

        barChart.animateY(3000);
        barChart.setExtraBottomOffset(24.5f);

        //x축 설정
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14);
        xAxis.setYOffset(9.5f);
        xAxis.setTextColor(getResources().getColor(R.color.color_909090));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        //x축 라벨 갯수
        xAxis.setLabelCount(size);
        if (size == 1) {
            xAxis.setLabelCount(2);
        }

        Log.d(TAG, "check size : " + size + ", but why?" + xAxis.getLabelCount());

        // 라벨 커스텀을 위함...(x축 라벨)
        switch (position) {
            case 0:
                xAxis.setValueFormatter(new MyDailyFormatter(barChart));
                break;
            case 1:
                xAxis.setValueFormatter(new MyWeeklyFormatter(barChart));
                break;
            case 2:
                xAxis.setValueFormatter(new MyMonthlyFormatter(barChart));
                break;
        }

        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = barChart.getAxisLeft();
        YAxis YRAXis = barChart.getAxisRight();

        //비활성화
        YLAxis.setDrawLabels(false);
        YLAxis.setDrawAxisLine(false);
        //gridline 은 옆에 선
        YLAxis.setDrawGridLines(true);

        //비활성화
        YRAXis.setDrawLabels(false);
        YRAXis.setDrawAxisLine(false);
        YRAXis.setDrawGridLines(false);

        // 각종 이벤트 방지.
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
        barChart.setPinchZoom(false);
        barChart.setDoubleTapToZoomEnabled(false);

        //하단 차트 정보 (막대 색갈별 정보) 안보이게 조정
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        //하단 상세 설명(?) 안보이게 설정
        barChart.getDescription().setEnabled(false);

        barChart.setData(barData);
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    /**
     *
     *  원 그래프 그리기.
     *
     * */
    public void initPieChart() {
        Log.d(TAG, "initPieChart");

        if (pieChartData == null) {
            Log.e(TAG, "processPieData NULL!!");
            return;
        }

        Log.d(TAG, "processPieData - " + pieChart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellFood()),getResources().getString(R.string.main_deadline_pie_graph_food)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellBeer()),getResources().getString(R.string.main_deadline_pie_graph_beer)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellCock()),getResources().getString(R.string.main_deadline_pie_graph_cock)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellLiquor()),getResources().getString(R.string.main_deadline_pie_graph_liquor)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellDrink()),getResources().getString(R.string.main_deadline_pie_graph_drink)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellLunch()),getResources().getString(R.string.main_deadline_pie_graph_lunch)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellDelivery()),getResources().getString(R.string.main_deadline_pie_graph_delivery)));

        Description description = new Description();
        description.setText(" "); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);
        //하단 상세 설명(?) 안보이게 설정
        pieChart.getDescription().setEnabled(false);
        pieChart.setNoDataText("test");

        //하단 차트 정보 (막대 색갈별 정보) 안보이게 조정
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

//        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues," ");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        int [] color={ getResources().getColor(R.color.color_fb7a63), getResources().getColor(R.color.color_fcc849),
                getResources().getColor(R.color.color_6bd67c), getResources().getColor(R.color.color_c7abf6),
                getResources().getColor(R.color.color_839afe), getResources().getColor(R.color.color_81c6fc),
                getResources().getColor(R.color.color_4993cd)
        };

        dataSet.setColors(color);
        pieChart.setDrawMarkers(false);
        pieChart.setDrawEntryLabels(false);
        pieChart.setTouchEnabled(false);

        PieData data = new PieData((dataSet));
        //value 값 숨기기.
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);
        data.setDrawValues(false);

        pieChart.setData(data);
    }


    //방문자 차트
    public void initVisitChart(int position) {
        Log.d(TAG, "initVisitChart");

        Log.d(TAG, "check : " + barEntryDinner.toString());

        switch (position) {
            case 0 :
                barDataVisitSet = new BarDataSet(barEntryLunch, getResources().getString(R.string.main_deadline_visit_labels));
                barDataVisit = new BarData(barDataVisitSet);
                break;
            case 1 :
                barDataVisitSet = new BarDataSet(barEntryDinner, getResources().getString(R.string.main_deadline_visit_labels));
                barDataVisit = new BarData(barDataVisitSet);
                break;
        }

        //막대 그래프의 너비
        barDataVisit.setBarWidth(0.3f);

        //막대 그래프의 색을 지정해준다.
        barDataVisitSet.setColor(getResources().getColor(R.color.color_4263ff));

        //막대 그래프 상단 수치text 크기
        barDataVisitSet.setValueTextSize(11);
        barDataVisitSet.setValueTextColor(getResources().getColor(R.color.color_222222));
        //소수점 제거를 위한 데이터 포맷 설정
        barDataVisitSet.setValueFormatter(new DeleteDecimal());

        visitChart.animateY(3000);
        visitChart.setExtraBottomOffset(24.5f);

        //x축 설정
        XAxis xAxis = visitChart.getXAxis();
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
                xAxis.setLabelCount(barEntryLunchLabels.size());
                xAxis.setValueFormatter(new MyLunchFormatter(visitChart));
                break;
            case 1:
                xAxis.setLabelCount(barEntryDinnerLabels.size());
                xAxis.setValueFormatter(new MyDinnerFormatter(visitChart));
                break;
        }

        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = visitChart.getAxisLeft();
        YAxis YRAXis = visitChart.getAxisRight();

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
        visitChart.setTouchEnabled(false);
        visitChart.setDragEnabled(false);
        visitChart.setScaleEnabled(false);
        visitChart.setPinchZoom(false);
        visitChart.setDoubleTapToZoomEnabled(false);

        //하단 차트 정보 (막대 색갈별 정보)
        Legend legend = visitChart.getLegend();
        legend.setEnabled(true);
        legend.setFormSize(8);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.color_222222));

        //하단 상세 설명(?) 안보이게 설정
        visitChart.getDescription().setEnabled(false);

        visitChart.setData(barDataVisit);
        visitChart.notifyDataSetChanged();
        visitChart.invalidate();
    }

    //top Menu 차트
    public void initTopMenuChart(int position) {
        Log.d(TAG, "initTopMenuChart");

        switch (position) {
            case 0 :
                barDataTopMenuSet = new BarDataSet(barEntryTopSell, getResources().getString(R.string.main_deadline_top_menu_sell_rank));
                barDataTopMenu = new BarData(barDataTopMenuSet);
                break;
            case 1 :
                barDataTopMenuSet = new BarDataSet(barEntryTopOrder, getResources().getString(R.string.main_deadline_top_menu_order_rank));
                barDataTopMenu = new BarData(barDataTopMenuSet);
                break;
        }


        //막대 그래프의 너비
        barDataTopMenu.setBarWidth(0.3f);
        barDataTopMenu.setDrawValues(true);
        barDataTopMenu.setValueFormatter(new ValueFormatter() {

        });

        //막대 그래프의 색을 지정해준다.
        barDataTopMenuSet.setColor(getResources().getColor(R.color.color_4263ff));

        //막대 그래프 상단 수치text 크기
        barDataTopMenuSet.setValueTextSize(11);
        barDataTopMenuSet.setValueTextColor(getResources().getColor(R.color.color_222222));
        //소수점 제거를 위한 데이터 포맷 설정
        barDataTopMenuSet.setValueFormatter(new DeleteDecimal());
//        barDataTopMenuSet.setva

        topMenuChart.animateY(3000);
        topMenuChart.setExtraBottomOffset(24.5f);

        //x축 설정
        XAxis xAxis = topMenuChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12);
        xAxis.setYOffset(9.5f);
        xAxis.setTextColor(getResources().getColor(R.color.color_909090));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        //TODO 위치..
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setPosition(TOP_INSIDE);

        //x축 라벨 갯수
        //라벨 커스텀을 위함...(x축 라벨)
        switch (position) {
            case 0:
                xAxis.setLabelCount(barEntryTopSellLabels.size());
                xAxis.setValueFormatter(new TopMenuSellFormatter(topMenuChart));
                break;
            case 1:
                xAxis.setLabelCount(barEntryTopOrderLabels.size());
                xAxis.setValueFormatter(new TopMenuOrderFormatter(topMenuChart));
                break;
        }

        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = topMenuChart.getAxisLeft();
        YAxis YRAXis = topMenuChart.getAxisRight();

        //활성화
        YLAxis.setDrawLabels(true);
        YLAxis.setDrawAxisLine(false);
        //gridline 은 옆에 선
        YLAxis.setDrawGridLines(true);
        YLAxis.setAxisMinimum(0);
        if (position == 0) {
            YLAxis.setAxisMaximum(500000);
        } else {
            YLAxis.setAxisMaximum(40);
        }
        YLAxis.setGranularity(10);
        YLAxis.setTextColor(getResources().getColor(R.color.color_909090));
        YLAxis.setTextSize(12);

        //비활성화
        YRAXis.setDrawLabels(false);
        YRAXis.setDrawAxisLine(false);
        YRAXis.setDrawGridLines(false);

        // 각종 이벤트 방지.
        topMenuChart.setTouchEnabled(false);
        topMenuChart.setDragEnabled(false);
        topMenuChart.setScaleEnabled(false);
        topMenuChart.setPinchZoom(false);
        topMenuChart.setDoubleTapToZoomEnabled(false);

        //하단 차트 정보 (막대 색갈별 정보)
        Legend legend = topMenuChart.getLegend();
        legend.setEnabled(false);
        legend.setFormSize(8);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setTextSize(13);
        legend.setTextColor(getResources().getColor(R.color.color_222222));

        //하단 상세 설명(?) 안보이게 설정
        topMenuChart.getDescription().setEnabled(false);

        topMenuChart.setData(barDataTopMenu);
        topMenuChart.notifyDataSetChanged();
        topMenuChart.invalidate();
    }

    public class MyDailyFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public MyDailyFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            //라벨값으로..
            //TODO value 가 왜 float인지..?
            if ((int)value - 1 < 0) {
                return barEntryWeeklyLabels.get(0);
            } else {
                return barEntryDailyLabels.get((int)value - 1);
            }
        }

    }

    public class MyWeeklyFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public MyWeeklyFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
            if ((int)value - 1 < 0) {
                return barEntryWeeklyLabels.get(0);
            } else {
                return barEntryWeeklyLabels.get((int)value - 1);
            }
        }
    }

    public class MyMonthlyFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public MyMonthlyFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
//            Log.d(TAG, "getFormattedValue - " + value);

            if ((int)value - 1 < 0) {
                return barEntryMonthlyLabels.get(0);
            } else {
                return barEntryMonthlyLabels.get((int)value - 1);
            }

        }
    }

    public class MyLunchFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public MyLunchFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
//            Log.d(TAG, "getFormattedValue - " + value);

            if ((int)value - 1 < 0) {
                return barEntryLunchLabels.get(0);
            } else {
                return barEntryLunchLabels.get((int)value - 1);
            }

        }
    }

    public class MyDinnerFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public MyDinnerFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {

            if ((int)value - 1 < 0) {
                return barEntryDinnerLabels.get(0);
            } else {
                return barEntryDinnerLabels.get((int)value - 1);
            }

        }
    }

    public class TopMenuSellFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public TopMenuSellFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
//            Log.d(TAG, "getFormattedValue - " + value);

            if ((int)value - 1 < 0) {
                return barEntryTopSellLabelsSample.get(0);
            } else {
                return barEntryTopSellLabelsSample.get((int)value - 1);
            }

        }
    }

    public class TopMenuOrderFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public TopMenuOrderFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
//            Log.d(TAG, "getFormattedValue - " + value);

            if ((int)value - 1 < 0) {
                return barEntryTopOrderLabelsSample.get(0);
            } else {
                return barEntryTopOrderLabelsSample.get((int)value - 1);
            }

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

    /**
     * BarChart 영역
     * View pager adapter
     */
    public class DeadlineViewpagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public DeadlineViewpagerAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem : position - " + position);
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.item_deadline_bar_chart, container, false);

            barChart = (BarChart) view.findViewById(R.id.chart1);

            //position에 따라 그리는 값을 달리한다.
            //size에 따라 비춰지는 x.y축을 달리한다.
            switch (position) {
                case 0 :
                    initBarChart(position, checkDaily);
                    break;
                case 1 :
                    initBarChart(position, checkWeekly);
                    break;
                case 2 :
                    initBarChart(position, checkMonthly);
                    break;
            }

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return 3;
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

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(final int position) {
            Log.d(TAG, "onPageSelected : " + position);

//            //TODO background가 selector로 안바뀜..??
            switch (position) {
                case 0 :
                    tv_main_deadline_barchart_day.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_barchart_day.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_barchart_week.setBackground(null);
                    tv_main_deadline_barchart_month.setBackground(null);
                    tv_main_deadline_barchart_week.setTypeface(Typeface.DEFAULT);
                    tv_main_deadline_barchart_month.setTypeface(Typeface.DEFAULT);
                    break;

                case 1:
                    tv_main_deadline_barchart_week.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_barchart_week.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_barchart_day.setBackground(null);
                    tv_main_deadline_barchart_month.setBackground(null);
                    tv_main_deadline_barchart_day.setTypeface(Typeface.DEFAULT);
                    tv_main_deadline_barchart_month.setTypeface(Typeface.DEFAULT);
                    break;

                case 2:
                    tv_main_deadline_barchart_month.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_barchart_month.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_barchart_day.setBackground(null);
                    tv_main_deadline_barchart_week.setBackground(null);
                    tv_main_deadline_barchart_day.setTypeface(Typeface.DEFAULT);
                    tv_main_deadline_barchart_week.setTypeface(Typeface.DEFAULT);
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
     * 방문자 차트 영역
     * View pager adapter
     */
    public class VisitViewpagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public VisitViewpagerAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem : position - " + position);
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.item_visit_bar_chart, container, false);

            visitChart = (BarChart) view.findViewById(R.id.visit_chart);

            //position에 따라 그리는 값을 달리한다.
            switch (position) {
                case 0 :
                    initVisitChart(position);
                    break;
                case 1 :
                    initVisitChart(position);
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

    //  visitviewpager change listener
    ViewPager.OnPageChangeListener bottomViewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            switch (position) {
                case 0 :
                    tv_main_deadline_barchart_lunch.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_barchart_lunch.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_barchart_dinner.setBackground(null);
                    tv_main_deadline_barchart_dinner.setTypeface(Typeface.DEFAULT);

                    break;

                case 1:
                    tv_main_deadline_barchart_dinner.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_barchart_dinner.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_barchart_lunch.setBackground(null);
                    tv_main_deadline_barchart_lunch.setTypeface(Typeface.DEFAULT);
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
     * TopMenu 차트 영역
     * View pager adapter
     */
    public class TopMenuViewpagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public TopMenuViewpagerAdapter() {
        }

        @Override
        public Object instantiateItem(final ViewGroup container, int position) {
            Log.d(TAG, "instantiateItem : position - " + position);
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View view = layoutInflater.inflate(R.layout.item_top_menu_bar_chart, container, false);

            topMenuChart = (BarChart) view.findViewById(R.id.top_menu_chart);

            //position에 따라 그리는 값을 달리한다.
            switch (position) {
                case 0 :
                    initTopMenuChart(position);
                    break;
                case 1 :
                    initTopMenuChart(position);
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

    //  visitviewpager change listener
    ViewPager.OnPageChangeListener topMenuViewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            switch (position) {
                case 0 :
                    tv_main_deadline_sell_rank.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_sell_rank.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_order_rank.setBackground(null);
                    tv_main_deadline_order_rank.setTypeface(Typeface.DEFAULT);

                    break;

                case 1:
                    tv_main_deadline_order_rank.setBackground(mContext.getResources().getDrawable(R.drawable.barchart_circular_background));
                    tv_main_deadline_order_rank.setTypeface(Typeface.DEFAULT_BOLD);

                    tv_main_deadline_sell_rank.setBackground(null);
                    tv_main_deadline_sell_rank.setTypeface(Typeface.DEFAULT);
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

    //객체 생성하여 리스트 작성
    public void initSellList() {
        Log.d(TAG, "initSellList");

        //TODO 이 노가다를 해야하나??더 좋은 방법이...??
        //TODO 정책 수정 -> 결국 7개 다보내야함

//        if (!pieChartData.getSellFood().equals("0")) {
        DailySalesList dsl1 = new DailySalesList();
        dsl1.setSellDate(pieChartData.getSellDate());
        dsl1.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_food));
        dsl1.setCategoryRealSell(pieChartData.getSellFood());
        dsl1.setCategorySellPer(pieChartData.getSellFoodPercent());
        dsl1.setColor(R.color.color_fb7a63);
        dsl1.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_food)));
        insertListOfSellsData.add(dsl1);
//        }

//        if (!pieChartData.getSellBeer().equals("0")) {
        DailySalesList dsl2 = new DailySalesList();
        dsl2.setSellDate(pieChartData.getSellDate());
        dsl2.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_beer));
        dsl2.setCategoryRealSell(pieChartData.getSellBeer());
        dsl2.setCategorySellPer(pieChartData.getSellBeerPercent());
        dsl2.setColor(R.color.color_fcc849);
        dsl2.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_beer)));
        insertListOfSellsData.add(dsl2);
//        }

//        if (!pieChartData.getSellCock().equals("0")) {
        DailySalesList dsl3 = new DailySalesList();
        dsl3.setSellDate(pieChartData.getSellDate());
        dsl3.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_cock));
        dsl3.setCategoryRealSell(pieChartData.getSellCock());
        dsl3.setCategorySellPer(pieChartData.getSellCockPercent());
        dsl3.setColor(R.color.color_6bd67c);
        dsl3.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_cock)));
        insertListOfSellsData.add(dsl3);
//        }

//        if (!pieChartData.getSellLiquor().equals("0")) {
        DailySalesList dsl4 = new DailySalesList();
        dsl4.setSellDate(pieChartData.getSellDate());
        dsl4.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_liquor));
        dsl4.setCategoryRealSell(pieChartData.getSellLiquor());
        dsl4.setCategorySellPer(pieChartData.getSellLiquorPercent());
        dsl4.setColor(R.color.color_c7abf6);
        dsl4.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_liquor)));
        insertListOfSellsData.add(dsl4);
//        }

//        if (!pieChartData.getSellDrink().equals("0")) { ;
        DailySalesList dsl5 = new DailySalesList();
        dsl5.setSellDate(pieChartData.getSellDate());
        dsl5.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_drink));
        dsl5.setCategoryRealSell(pieChartData.getSellDrink());
        dsl5.setCategorySellPer(pieChartData.getSellDrinkPercent());
        dsl5.setColor(R.color.color_839afe);
        dsl5.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_drink)));
        insertListOfSellsData.add(dsl5);
//        }

//        if (!pieChartData.getSellLunch().equals("0")) {
        DailySalesList dsl6 = new DailySalesList();
        dsl6.setSellDate(pieChartData.getSellDate());
        dsl6.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_lunch));
        dsl6.setCategoryRealSell(pieChartData.getSellLunch());
        dsl6.setCategorySellPer(pieChartData.getSellLunchPercent());
        dsl6.setColor(R.color.color_81c6fc);
        dsl6.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_lunch)));
        insertListOfSellsData.add(dsl6);
//        }

//        if (!pieChartData.getSellDelivery().equals("0")) {
        DailySalesList dsl7 = new DailySalesList();
        dsl7.setSellDate(pieChartData.getSellDate());
        dsl7.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_delivery));
        dsl7.setCategoryRealSell(pieChartData.getSellDelivery());
        dsl7.setCategorySellPer(pieChartData.getSellDeliveryPercent());
        dsl7.setColor(R.color.color_4993cd);
        dsl7.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_delivery)));
        insertListOfSellsData.add(dsl7);
//        }

    }


    public void initSellDetailList() {
        Log.d(TAG, "initSellDetailList");
        DateUtils.d(TAG, "initSellDetailList - start");

        //TODO 이 노가다를 해야하나??더 좋은 방법이...??
        List<SalesObject> food = new ArrayList<SalesObject>();
        List<SalesObject> beer = new ArrayList<SalesObject>();
        List<SalesObject> cock = new ArrayList<SalesObject>();
        List<SalesObject> liquor = new ArrayList<SalesObject>();
        List<SalesObject> drink = new ArrayList<SalesObject>();
        List<SalesObject> lunch = new ArrayList<SalesObject>();
        List<SalesObject> delivery = new ArrayList<SalesObject>();

        for (int i = 0; i < lastRawData.size(); i++ ) {
            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_food))){
                food.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_beer))){
                beer.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_cock))){
                cock.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_liquor))){
                liquor.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_drink))){
                drink.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_lunch))){
                lunch.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals(getResources().getString(R.string.main_deadline_pie_graph_delivery))){
                delivery.add(lastRawData.get(i));
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

        Log.d(TAG, "initSellDetailList - hash : " + lastRawDataHash.toString());
        DateUtils.d(TAG, "initSellDetailList - end");
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

        return result;
    }

    public void initTopMenuList() {
        Log.d(TAG, "initTopMenuList - " + lastRawDataHash.toString());

        ArrayList<List<DailySalesListItems>> topMenuListTemp;
        topMenuListTemp = new ArrayList<List<DailySalesListItems>>();

        //hash 를 arraylist 로 변경한다.
        topMenuListTemp.addAll(lastRawDataHash.values());

        topMenuList = new ArrayList<DailySalesListItems>();

        for (int i = 0; i < topMenuListTemp.size(); i++) {
            topMenuList.addAll(topMenuListTemp.get(i));
        }

        topMenuSellRankList = new ArrayList<DailySalesListItems>();
        topMenuSellRankList.addAll(topMenuList);

        //정렬
        Collections.sort(topMenuSellRankList, new Comparator<DailySalesListItems>() {
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

        Log.d(TAG, "topMenuSellRankList : " + topMenuSellRankList);

        topMenuOrderRankList = new ArrayList<DailySalesListItems>();
        topMenuOrderRankList.addAll(topMenuList);
        //정렬
        Collections.sort(topMenuOrderRankList, new Comparator<DailySalesListItems>() {
            @Override
            public int compare(DailySalesListItems t1, DailySalesListItems t2) {
                int ret = 0;
                if (Integer.parseInt(t1.getItemCount()) < Integer.parseInt(t2.getItemCount())) {
                    ret = 1;
                }

                if ((Integer.parseInt(t1.getItemCount()) == Integer.parseInt(t2.getItemCount()))) {
                    if (Integer.parseInt(t1.getItemRealSell()) < Integer.parseInt(t2.getItemRealSell())) {
                        ret = 1;
                    } else if (Integer.parseInt(t1.getItemRealSell()) == Integer.parseInt(t2.getItemRealSell())) {
                        ret = 0;
                    } else if (Integer.parseInt(t1.getItemRealSell()) > Integer.parseInt(t2.getItemRealSell())) {
                        ret = -1;
                    }
                }

                if (Integer.parseInt(t1.getItemCount()) > Integer.parseInt(t2.getItemCount())) {
                    ret = -1;
                }

                return ret;
            }
        });

        Log.d(TAG, "topMenuOrderRankList : " + topMenuOrderRankList);

        //Entry 생성
        makeTopMenuEntry();

    }

    public void makeTopMenuEntry() {
        Log.d(TAG, "makeTopMenuEntry");

        barEntryTopSellLabelsSample = new ArrayList<String>();
        barEntryTopSellLabels = new ArrayList<String>();
        test = new ArrayList<>();

        test.add(new DailySalesListItems("실험1", "1", "1000"));
//        test.add(new DailySalesListItems("실험2", "2", "1000"));

        //판매액 랭킹
        //항목이 5개 이상인 경우 5개로 설정
        if (topMenuSellRankList.size() > 5) {
            barEntryTopSellLabels.add("1");

            barEntryTopSellLabelsSample.add("1위 " + topMenuSellRankList.get(0).getItemName());

            //두번쨰항목
            if (topMenuSellRankList.get(0).getItemRealSell().equals(topMenuSellRankList.get(1).getItemRealSell())) {
                barEntryTopSellLabels.add("1");

                barEntryTopSellLabelsSample.add("1위 " + topMenuSellRankList.get(1).getItemName());
            } else {
                barEntryTopSellLabels.add("2");

                barEntryTopSellLabelsSample.add("2위 " + topMenuSellRankList.get(1).getItemName());
            }

            //세번쨰항목
            if (topMenuSellRankList.get(1).getItemRealSell().equals(topMenuSellRankList.get(2).getItemRealSell())) {
                barEntryTopSellLabels.add(barEntryTopSellLabels.get(1));

                barEntryTopSellLabelsSample.add(barEntryTopSellLabels.get(1) + "위 " + topMenuSellRankList.get(2).getItemName());
            } else {
                barEntryTopSellLabels.add("" + (Integer.parseInt(barEntryTopSellLabels.get(1)) + 1));

                barEntryTopSellLabelsSample.add((Integer.parseInt(barEntryTopSellLabels.get(1)) + 1) + "위 " + topMenuSellRankList.get(2).getItemName());
            }

            if (topMenuSellRankList.get(2).getItemRealSell().equals(topMenuSellRankList.get(3).getItemRealSell())) {
                barEntryTopSellLabels.add(barEntryTopSellLabels.get(2));

                barEntryTopSellLabelsSample.add(barEntryTopSellLabels.get(2) + "위 " + topMenuSellRankList.get(3).getItemName());
            } else {
                barEntryTopSellLabels.add("" + (Integer.parseInt(barEntryTopSellLabels.get(2)) + 1));

                barEntryTopSellLabelsSample.add((Integer.parseInt(barEntryTopSellLabels.get(2)) + 1) + "위 " + topMenuSellRankList.get(3).getItemName());
            }

            if (topMenuSellRankList.get(3).getItemRealSell().equals(topMenuSellRankList.get(4).getItemRealSell())) {
                barEntryTopSellLabels.add(barEntryTopSellLabels.get(3));

                barEntryTopSellLabelsSample.add(barEntryTopSellLabels.get(3) + "위 " + topMenuSellRankList.get(4).getItemName());
            } else {
                barEntryTopSellLabels.add("" + (Integer.parseInt(barEntryTopSellLabels.get(3)) + 1));

                barEntryTopSellLabelsSample.add((Integer.parseInt(barEntryTopSellLabels.get(3)) + 1) + "위 " + topMenuSellRankList.get(4).getItemName());
            }

        } else {
            //항목이 5개 이하인 경우
            if (topMenuSellRankList.size() == 0) {
                Log.e(TAG, "항목이 없습니다.");
            } else {
                barEntryTopSellLabels.add("1");

                barEntryTopSellLabelsSample.add("1위 " + topMenuSellRankList.get(0).getItemName());

                for (int i = 1; i < topMenuSellRankList.size(); i++) {
                    if (topMenuSellRankList.get(i - 1).getItemRealSell().equals(topMenuSellRankList.get(i).getItemRealSell())) {
                        barEntryTopSellLabels.add(barEntryTopSellLabels.get(i - 1));

                        barEntryTopSellLabelsSample.add(barEntryTopSellLabels.get(i - 1) + "위 " + topMenuSellRankList.get(i).getItemName());
                    } else {
                        barEntryTopSellLabels.add("" + (Integer.parseInt(barEntryTopSellLabels.get(i - 1)) + 1));

                        barEntryTopSellLabelsSample.add((Integer.parseInt(barEntryTopSellLabels.get(i - 1)) + 1) + "위 " + topMenuSellRankList.get(i).getItemName());
                    }
                }
            }
        }

        Log.d(TAG, "barEntryTopSellLabels : " + barEntryTopSellLabels.toString());

        //sellRank barEntry 생성
        barEntryTopSell = new ArrayList<BarEntry>();
        for (int i = 0; i < barEntryTopSellLabels.size(); i++) {
            barEntryTopSell.add(new BarEntry(i + 1 ,Integer.parseInt(topMenuSellRankList.get(barEntryTopSellLabels.size()-i-1).getItemRealSell())));
        }

        Log.d(TAG, "barEntryTopSell : " + barEntryTopSell.toString());

        //주문수 랭킹
        barEntryTopOrderLabelsSample = new ArrayList<String>();
        barEntryTopOrderLabels = new ArrayList<String>();
        if (topMenuOrderRankList.size() > 5) {
            barEntryTopOrderLabels.add("1");

            barEntryTopOrderLabelsSample.add("1위 " + topMenuOrderRankList.get(0).getItemName());

            //두번쨰항목
            if (topMenuOrderRankList.get(0).getItemCount().equals(topMenuOrderRankList.get(1).getItemCount())) {
                barEntryTopOrderLabels.add("1");

                barEntryTopOrderLabelsSample.add("1위 " + topMenuOrderRankList.get(1).getItemName());
            } else {
                barEntryTopOrderLabels.add("2");

                barEntryTopOrderLabelsSample.add("2위 " + topMenuOrderRankList.get(1).getItemName());
            }

            //세번쨰항목
            if (topMenuOrderRankList.get(1).getItemCount().equals(topMenuOrderRankList.get(2).getItemCount())) {
                barEntryTopOrderLabels.add(barEntryTopOrderLabels.get(1));

                barEntryTopOrderLabelsSample.add(barEntryTopOrderLabels.get(1) + "위 " + topMenuOrderRankList.get(2).getItemName());
            } else {
                barEntryTopOrderLabels.add("" + (Integer.parseInt(barEntryTopOrderLabels.get(1)) + 1));

                barEntryTopOrderLabelsSample.add((Integer.parseInt(barEntryTopOrderLabels.get(1)) + 1) + "위 " + topMenuOrderRankList.get(2).getItemName());
            }

            if (topMenuOrderRankList.get(2).getItemCount().equals(topMenuOrderRankList.get(3).getItemCount())) {
                barEntryTopOrderLabels.add(barEntryTopOrderLabels.get(2));

                barEntryTopOrderLabelsSample.add(barEntryTopOrderLabels.get(2) + "위 " + topMenuOrderRankList.get(3).getItemName());
            } else {
                barEntryTopOrderLabels.add("" + (Integer.parseInt(barEntryTopOrderLabels.get(2)) + 1));

                barEntryTopOrderLabelsSample.add((Integer.parseInt(barEntryTopOrderLabels.get(2)) + 1) + "위 " + topMenuOrderRankList.get(3).getItemName());
            }

            if (topMenuOrderRankList.get(3).getItemCount().equals(topMenuOrderRankList.get(4).getItemCount())) {
                barEntryTopOrderLabels.add(barEntryTopOrderLabels.get(3));

                barEntryTopOrderLabelsSample.add(barEntryTopOrderLabels.get(3) + "위 " + topMenuOrderRankList.get(4).getItemName());
            } else {
                barEntryTopOrderLabels.add("" + (Integer.parseInt(barEntryTopOrderLabels.get(3)) + 1));

                barEntryTopOrderLabelsSample.add((Integer.parseInt(barEntryTopOrderLabels.get(3)) + 1) + "위 " + topMenuOrderRankList.get(4).getItemName());
            }

        } else {
            //항목이 5개 이하인 경우
            if (topMenuOrderRankList.size() == 0) {
                Log.e(TAG, "항목이 없습니다.");
            } else {
                barEntryTopOrderLabels.add("1");

                barEntryTopOrderLabelsSample.add("1위 " + topMenuOrderRankList.get(0).getItemName());

                for (int i = 1; i < topMenuOrderRankList.size(); i++) {
                    if (topMenuOrderRankList.get(i - 1).getItemRealSell().equals(topMenuOrderRankList.get(i).getItemRealSell())) {
                        barEntryTopOrderLabels.add(barEntryTopOrderLabels.get(i - 1));

                        barEntryTopOrderLabelsSample.add(barEntryTopOrderLabels.get(i - 1) + "위 " + topMenuOrderRankList.get(i).getItemName());
                    } else {
                        barEntryTopOrderLabels.add("" + (Integer.parseInt(barEntryTopOrderLabels.get(i - 1)) + 1));

                        barEntryTopOrderLabelsSample.add((Integer.parseInt(barEntryTopOrderLabels.get(i - 1)) + 1) + "위 " + topMenuOrderRankList.get(i).getItemName());
                    }
                }
            }
        }

        Log.d(TAG, "barEntryTopOrderLabels : " + barEntryTopOrderLabels.toString());


        barEntryTopOrder = new ArrayList<BarEntry>();
        for (int i = 0; i < barEntryTopOrderLabels.size(); i++) {
            barEntryTopOrder.add(new BarEntry(i + 1 ,Integer.parseInt(topMenuOrderRankList.get(barEntryTopOrderLabels.size()-i-1).getItemCount())));
        }

        Log.d(TAG, "barEntryTopOrder : " + barEntryTopOrder.toString());

//        Collections.reverse(barEntryTopSell);
        Collections.reverse(barEntryTopSellLabels);
        Collections.reverse(barEntryTopSellLabelsSample);
//        Collections.reverse(barEntryTopOrder);
        Collections.reverse(barEntryTopOrderLabels);
        Collections.reverse(barEntryTopOrderLabelsSample);

        Log.d(TAG, "barEntryTopOrder22 : " + barEntryTopOrder.toString());
        Log.d(TAG, "dsadasd : " + barEntryTopOrderLabels);
    }
}