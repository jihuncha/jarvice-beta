package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.CurrentManager;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.ui.adapter.AdapterListExpand;
import huni.techtown.org.jarvice.ui.utils.LineItemDecoration;

public class TabLayoutDeadline extends Fragment {
    private static final String TAG = TabLayoutDeadline.class.getSimpleName();

    private Handler mHandler;
    private Context mContext;

    private ViewPager deadlineSalesBarChart;
    private DeadlineViewpagerAdapter deadlineViewpagerAdapter;
    //막대 그래프
    private BarChart barChart;

    // 일/주/월 로 나누기
    private int barChartPosition;

    //데이터
    private ArrayList<BarEntry> barEntryData;
    //데이터 라벨
    private ArrayList<String> barEntryLabels ;
    private BarDataSet barDataSet ;
    private BarData barData;

    private boolean firstTime;

    private PieChart pieChart;
    private DailySalesObject pieChartData;

    private RecyclerView rvSellList;
    private AdapterListExpand mAdapter;
    private List<DailySalesList> insertData;

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

        //Bar Chart
        String dateString = CurrentManager.getInstance(mContext).toFormatString(System.currentTimeMillis(), "yyyy-MM-dd");
        Log.d(TAG, "test22 : " + dateString);
        Log.d(TAG, "test : " + DatabaseManager.getInstance(mContext).getDailCheck("2019-10-20"));

        barEntryData = new ArrayList<>();
        barEntryLabels = new ArrayList<String>();
        firstTime = false;

        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        deadlineSalesBarChart = (ViewPager) root.findViewById(R.id.deadline_sales_bar_chart);
        deadlineViewpagerAdapter = new DeadlineViewpagerAdapter();
        deadlineSalesBarChart.setAdapter(deadlineViewpagerAdapter);
        deadlineSalesBarChart.addOnPageChangeListener(viewPagerPageChangeListener);

        //view 유지하는 갯수 양쪽 2개씩으로 수정
        deadlineSalesBarChart.setOffscreenPageLimit(2);

        //Pie Chart
        Log.e(TAG, "testsd : " + DatabaseManager.getInstance(mContext).getLastData());

        pieChartData = DatabaseManager.getInstance(mContext).getLastData().get(0);
        pieChart = (PieChart)root.findViewById(R.id.piechart);

        initPieChart();

        rvSellList = (RecyclerView)root.findViewById(R.id.rv_sell_list);
        rvSellList.setLayoutManager(new LinearLayoutManager(mContext));
//        rvSellList.addItemDecoration(new LineItemDecoration(mContext, LinearLayout.VERTICAL));
        rvSellList.setHasFixedSize(true);

        insertData = new ArrayList<DailySalesList>();
        initSellList();
        mAdapter = new AdapterListExpand(mContext, pieChartData);
        rvSellList.setAdapter(mAdapter);

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
                getResources().getColor(R.color.purple_500), getResources().getColor(R.color.amber_900)
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
//                    initBarChart();
//
//                    barChart.setVisibility(View.VISIBLE);
//
//                    barChart2.setVisibility(View.GONE);
//                    barChart3.setVisibility(View.GONE);
                    break;

                case 1:


//                    barChart.setVisibility(View.GONE);
//
//                    barChart2.setVisibility(View.VISIBLE);
//                    barChart3.setVisibility(View.GONE);
//                    initWeekChart();

                    break;

                case 2:
//                    barChart.setVisibility(View.GONE);
//
//                    barChart2.setVisibility(View.GONE);
//                    barChart3.setVisibility(View.VISIBLE);
//                    initMonthChart();
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

    public void initSellList() {
        Log.d(TAG, "initSellList");

        //TODO 이 노가다를 해야하나??더 좋은 방법이...??

        if (!pieChartData.getSellFood().equals("0")) {
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_food));
            dsl.setCategoryRealSell(pieChartData.getSellReal());
            insertData.add(dsl);
        }

        if (!pieChartData.getSellBeer().equals("0")) {
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_beer));
            dsl.setCategoryRealSell(pieChartData.getSellBeer());
            insertData.add(dsl);
        }

        if (!pieChartData.getSellCock().equals("0")) {
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_cock));
            dsl.setCategoryRealSell(pieChartData.getSellCock());
            insertData.add(dsl);
        }

        if (!pieChartData.getSellLiquor().equals("0")) {
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_liquor));
            dsl.setCategoryRealSell(pieChartData.getSellLiquor());
            insertData.add(dsl);
        }

        if (!pieChartData.getSellDrink().equals("0")) { ;
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_drink));
            dsl.setCategoryRealSell(pieChartData.getSellDrink());
            insertData.add(dsl);
        }

        if (!pieChartData.getSellLunch().equals("0")) {
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_lunch));
            dsl.setCategoryRealSell(pieChartData.getSellLunch());
            insertData.add(dsl);
        }

        if (!pieChartData.getSellDelivery().equals("0")) {
            DailySalesList dsl = new DailySalesList();
            dsl.setSellDate(pieChartData.getSellDate());
            dsl.setCategoryName(getResources().getString(R.string.main_deadline_pie_graph_delivery));
            dsl.setCategoryRealSell(pieChartData.getSellDelivery());
            insertData.add(dsl);
        }

        Log.d(TAG, "testddd : " + insertData.toString());

    }
}