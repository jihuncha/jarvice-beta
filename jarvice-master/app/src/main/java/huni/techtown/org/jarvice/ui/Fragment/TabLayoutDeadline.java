package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.CurrentManager;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesListItems;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.ui.adapter.AdapterListExpand;
import huni.techtown.org.jarvice.ui.adapter.AdapterPieChartList;
import huni.techtown.org.jarvice.ui.utils.DateUtils;
import huni.techtown.org.jarvice.ui.utils.Tools;

public class TabLayoutDeadline extends Fragment implements View.OnClickListener {
    private static final String TAG = TabLayoutDeadline.class.getSimpleName();

    private Handler mHandler;
    private Context mContext;

    //타이틀
    private TextView tv_main_deadline_company_title;
    private TextView tv_main_deadline_company_date;

    //막대 그래프
    private BarChart barChart;

    private ViewPager deadlineSalesBarChart;
    private DeadlineViewpagerAdapter deadlineViewpagerAdapter;

    // 일/주/월 로 나누기
    private int barChartPosition;
    private TextView tv_main_deadline_barchart_day;
    private TextView tv_main_deadline_barchart_week;
    private TextView tv_main_deadline_barchart_month;

    //막대 데이터
    private ArrayList<BarEntry> barEntryData;
    //막대 데이터 라벨
    private ArrayList<String> barEntryLabels ;
    private BarDataSet barDataSet;
    private BarData barData;

    //원 그래프
    private PieChart pieChart;
    private DailySalesObject pieChartData;

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

    public TabLayoutDeadline() {
    }

    public static TabLayoutDeadline newInstance() {
        TabLayoutDeadline fragment = new TabLayoutDeadline();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_main_deadline, container, false);
        Log.d(TAG, "onCreateView");

        mContext = container.getContext();
        mHandler = new Handler();

        //데이터가져오기..!!
        pieChartData = DatabaseManager.getInstance(mContext).getLastData().get(0);

        tv_main_deadline_company_title = root.findViewById(R.id.tv_main_deadline_company_title);
        tv_main_deadline_company_date = root.findViewById(R.id.tv_main_deadline_company_date);

        Log.d(TAG, "Test : " + Tools.getDayOfWeek(pieChartData.getSellDate()));

        String getLastDataDate = pieChartData.getSellDate().replace("-" , ".");
        String getDayOfWeek = Tools.getDayOfWeek(pieChartData.getSellDate());
        tv_main_deadline_company_date.setText(getLastDataDate + " "
                + getDayOfWeek + " " + mContext.getResources().getString(R.string.main_deadline_title_date));

        //Bar Chart
        String dateString = CurrentManager.getInstance(mContext).toFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        Log.d(TAG, "test22 : " + dateString);
        Log.d(TAG, "test : " + DatabaseManager.getInstance(mContext).getDailCheck("2019-10-20"));

        barEntryData = new ArrayList<>();
        barEntryLabels = new ArrayList<String>();

        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        deadlineSalesBarChart = (ViewPager) root.findViewById(R.id.deadline_sales_bar_chart);
        deadlineViewpagerAdapter = new DeadlineViewpagerAdapter();
        deadlineSalesBarChart.setAdapter(deadlineViewpagerAdapter);
        deadlineSalesBarChart.addOnPageChangeListener(viewPagerPageChangeListener);

        //view 유지하는 갯수 양쪽 2개씩으로 수정
        deadlineSalesBarChart.setOffscreenPageLimit(2);

        //일/주/월
        barChartPosition = 0;
        tv_main_deadline_barchart_day = root.findViewById(R.id.tv_main_deadline_barchart_day);
        tv_main_deadline_barchart_week = root.findViewById(R.id.tv_main_deadline_barchart_week);
        tv_main_deadline_barchart_month = root.findViewById(R.id.tv_main_deadline_barchart_month);

        tv_main_deadline_barchart_day.setOnClickListener(this);
        tv_main_deadline_barchart_week.setOnClickListener(this);
        tv_main_deadline_barchart_month.setOnClickListener(this);

        //Pie Chart
        Log.e(TAG, "testsd : " + DatabaseManager.getInstance(mContext).getLastData());

        pieChart = (PieChart) root.findViewById(R.id.piechart);

        lastRawData = new ArrayList<>();

        Log.e(TAG, " test 3434 : " + DatabaseManager.getInstance(mContext).getDateSalesObject(pieChartData.getSellDate(),"MainActivity - onCreate"));

        //해당 날짜에 있는 rawdata 목록.
        lastRawData.addAll(DatabaseManager.getInstance(mContext).getDateSalesObject(pieChartData.getSellDate(),"MainActivity - onCreate"));

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


        Log.e(TAG, "result all = " + insertListOfSellsData);
        //총매출 text 지정
        tvSellRealResult = (TextView) root.findViewById(R.id.tv_sell_real_result);
        tvSellRealResult.setText(Tools.decimalFormat(pieChartData.getSellReal()));

        rvSellListColor = (RecyclerView) root.findViewById(R.id.rv_sell_list_color);
        rvSellListColor.setLayoutManager(new LinearLayoutManager(mContext));
        rvSellListColor.setHasFixedSize(true);

        //Pie Chart Text Area
        mAdapterPieChartList = new AdapterPieChartList(mContext, insertListOfSellsData);
        // on item list clicked
        mAdapterPieChartList.setOnItemClickListener(new AdapterPieChartList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DailySalesList obj, int position) {
                Log.d(TAG, "Test656 : " + position);
            }
        });
        rvSellListColor.setAdapter(mAdapterPieChartList);

        rvSellListDetail = (RecyclerView)root.findViewById(R.id.rv_sell_list_detail);
        rvSellListDetail.setLayoutManager(new LinearLayoutManager(mContext));
        rvSellListDetail.setHasFixedSize(true);

        //Pie Chart Bottom Area
        mAdapterListExpand = new AdapterListExpand(mContext, insertListOfSellsData);
        // on item list clicked
        mAdapterListExpand.setOnItemClickListener(new AdapterListExpand.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DailySalesList obj, int position) {
                Log.d(TAG, "Test656 : " + position);
            }
        });
        rvSellListDetail.setAdapter(mAdapterListExpand);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initBarChart() {
        Log.d(TAG, "initBarChart");

        barDataSet = new BarDataSet(barEntryData, null);
        barData = new BarData(barDataSet);

        //막대 그래프의 너비
        barData.setBarWidth(0.3f);

        //막대 그래프의 색을 지정해준다.
        barDataSet.setColors(getResources().getColor(R.color.color_e6e9f7));
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
        xAxis.setLabelCount(3);

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

        // 라벨 커스텀을 위함...(x축 라벨)
        xAxis.setValueFormatter(new MyCustomFormatter(barChart));

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

    public void AddValuesToBARENTRY(){

        barEntryData.add(new BarEntry(1, 872000));
        barEntryData.add(new BarEntry(2, 1057000));
        barEntryData.add(new BarEntry(3, 716000));

    }

    public void AddValuesToBarEntryLabels(){

        barEntryLabels.add("04.10 수");
        barEntryLabels.add("04.11 목");
        barEntryLabels.add("04.12 금");

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
        }
    }


    public class MyCustomFormatter extends ValueFormatter {
        private final BarLineChartBase<?> chart;

        public MyCustomFormatter(BarLineChartBase<?> chart) {
            this.chart = chart;
        }

        @Override
        public String getFormattedValue(float value) {
//            for (int i =0; i < barEntryData.size(); i++) {
//                return "" + barEntryLabels.get(i);
//            }
//            return "" + barEntryLabels.get((int)value);
            return barEntryLabels.get((int)value - 1);
        }

    }


    /**
     *
     *  원 그래프 그리기.
     *
     * */
    public void initPieChart() {
        if (pieChartData == null) {
            Log.e(TAG, "processPieData - data is null!!");
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
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellDrink()),getResources().getString(R.string.main_deadline_pie_graph_drink)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellLiquor()),getResources().getString(R.string.main_deadline_pie_graph_liquor)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellLunch()),getResources().getString(R.string.main_deadline_pie_graph_lunch)));
        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellDelivery()),getResources().getString(R.string.main_deadline_pie_graph_delivery)));
//        yValues.add(new PieEntry(Integer.parseInt(pieChartData.getSellDrink()),R.string.main_deadline_pie_graph_drink));

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
        int [] color={ getResources().getColor(R.color.color_ef6754), getResources().getColor(R.color.color_24609e),
                getResources().getColor(R.color.color_ffbe37), getResources().getColor(R.color.color_08b3a2),
                getResources().getColor(R.color.purple_500), getResources().getColor(R.color.brown_400),
                getResources().getColor(R.color.light_blue_900)
        };

//        Log.d(TAG, pieChart.)

//        dataSet.setColors(R.color.color_ef6754, R.color.color_24609e,
//                R.color.color_ffbe37, R.color.color_08b3a2, R.color.color_24609e);

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

            initBarChart();

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
        dsl1.setColor(R.color.color_ef6754);
        dsl1.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_food)));
        insertListOfSellsData.add(dsl1);
//        }

//        if (!pieChartData.getSellBeer().equals("0")) {
        DailySalesList dsl2 = new DailySalesList();
        dsl2.setSellDate(pieChartData.getSellDate());
        dsl2.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_beer));
        dsl2.setCategoryRealSell(pieChartData.getSellBeer());
        dsl2.setCategorySellPer(pieChartData.getSellBeerPercent());
        dsl2.setColor(R.color.color_24609e);
        dsl2.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_beer)));
        insertListOfSellsData.add(dsl2);
//        }

//        if (!pieChartData.getSellCock().equals("0")) {
        DailySalesList dsl3 = new DailySalesList();
        dsl3.setSellDate(pieChartData.getSellDate());
        dsl3.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_cock));
        dsl3.setCategoryRealSell(pieChartData.getSellCock());
        dsl3.setCategorySellPer(pieChartData.getSellCockPercent());
        dsl3.setColor(R.color.color_ffbe37);
        dsl3.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_cock)));
        insertListOfSellsData.add(dsl3);
//        }

//        if (!pieChartData.getSellLiquor().equals("0")) {
        DailySalesList dsl4 = new DailySalesList();
        dsl4.setSellDate(pieChartData.getSellDate());
        dsl4.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_liquor));
        dsl4.setCategoryRealSell(pieChartData.getSellLiquor());
        dsl4.setCategorySellPer(pieChartData.getSellLiquorPercent());
        dsl4.setColor(R.color.color_08b3a2);
        dsl4.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_liquor)));
        insertListOfSellsData.add(dsl4);
//        }

//        if (!pieChartData.getSellDrink().equals("0")) { ;
        DailySalesList dsl5 = new DailySalesList();
        dsl5.setSellDate(pieChartData.getSellDate());
        dsl5.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_drink));
        dsl5.setCategoryRealSell(pieChartData.getSellDrink());
        dsl5.setCategorySellPer(pieChartData.getSellDrinkPercent());
        dsl5.setColor(R.color.purple_500);
        dsl5.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_drink)));
        insertListOfSellsData.add(dsl5);
//        }

//        if (!pieChartData.getSellLunch().equals("0")) {
        DailySalesList dsl6 = new DailySalesList();
        dsl6.setSellDate(pieChartData.getSellDate());
        dsl6.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_lunch));
        dsl6.setCategoryRealSell(pieChartData.getSellLunch());
        dsl6.setCategorySellPer(pieChartData.getSellLunchPercent());
        dsl6.setColor(R.color.brown_400);
        dsl6.setItemList(lastRawDataHash.get(getResources().getString(R.string.main_deadline_pie_graph_lunch)));
        insertListOfSellsData.add(dsl6);
//        }

//        if (!pieChartData.getSellDelivery().equals("0")) {
        DailySalesList dsl7 = new DailySalesList();
        dsl7.setSellDate(pieChartData.getSellDate());
        dsl7.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_delivery));
        dsl7.setCategoryRealSell(pieChartData.getSellDelivery());
        dsl7.setCategorySellPer(pieChartData.getSellDeliveryPercent());
        dsl7.setColor(R.color.light_blue_900);
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
            if (lastRawData.get(i).getCategory().equals("푸드")){
                food.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals("주류")){
                beer.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals("칵테일")){
                cock.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals("양주")){
                liquor.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals("드링크")){
                drink.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals("런치")){
                lunch.add(lastRawData.get(i));
            }

            if (lastRawData.get(i).getCategory().equals("배달")){
                delivery.add(lastRawData.get(i));
            }
        }

        if (food.size() != 0) {
//            makeListItems(food);
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_food), makeListItems(food));
        }

        if (beer.size() != 0) {
//            makeListItems(beer);
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_beer), makeListItems(beer));
        }

        if (cock.size() != 0) {
//            makeListItems(cock);
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_cock), makeListItems(cock));
        }

        if (liquor.size() != 0) {
//            makeListItems(liquor);
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_liquor), makeListItems(liquor));
        }

        if (drink.size() != 0) {
//            makeListItems(drink);
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_drink), makeListItems(drink));
        }

        if (lunch.size() != 0) {
//            makeListItems(lunch);
            lastRawDataHash.put(getResources().getString(R.string.main_deadline_pie_graph_lunch), makeListItems(lunch));
        }

        if (delivery.size() != 0) {
//            makeListItems(delivery);
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
        //같은 카테고리에서 같은 종류의 음식이 있는지 체크하는로직
        List<String> check  = new ArrayList<String>();
        List<DailySalesListItems> result = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i ++) {
            //상품명이 다른것이 잇을경우
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
}