package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.animation.Easing;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import huni.techtown.org.jarvice.R;

public class TabLayoutDeadline extends Fragment {
    private static final String TAG = TabLayoutDeadline.class.getSimpleName();

    private Handler mHandler;
    private Context mContext;

    private ViewPager deadlineSalesBarChart;
    private DeadlineViewpagerAdapter deadlineViewpagerAdapter;
    //막대 그래프
    private BarChart barChart;

    private BarChart barChart2;

    private BarChart barChart3;

    //데이터
    private ArrayList<BarEntry> barEntryData;
    //데이터 라벨
    private ArrayList<String> barEntryLabels ;
    private BarDataSet barDataSet ;
    private BarData barData;

    private boolean firstTime;

    PieChart pieChart;

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

//        barChart = (BarChart) root.findViewById(R.id.chart1);
        pieChart = (PieChart)root.findViewById(R.id.piechart);

//        initBarChart();
        initPieChart();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void initBarChart() {

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
//        barChart.setoffscr


//        Legend legend = chart.getLegend();
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);


//        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
//        recyclerView.setHasFixedSize(true);
//
//        List<ShopProduct> items = DataGenerator.getShoppingProduct(getActivity());
//        Collections.shuffle(items);
//
//        //set data and list adapter
//        AdapterGridShopProductCard mAdapter = new AdapterGridShopProductCard(getActivity(), items);
//        recyclerView.setAdapter(mAdapter);
//
//        // on item list clicked
//        mAdapter.setOnItemClickListener(new AdapterGridShopProductCard.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, ShopProduct obj, int position) {
//                Snackbar.make(root, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//
//        mAdapter.setOnMoreButtonClickListener(new AdapterGridShopProductCard.OnMoreButtonClickListener() {
//            @Override
//            public void onItemClick(View view, ShopProduct obj, MenuItem item) {
//                Snackbar.make(root, obj.title + " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });

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
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(false);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);

        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();

        yValues.add(new PieEntry(34f,"Japen"));
        yValues.add(new PieEntry(23f,"USA"));
        yValues.add(new PieEntry(14f,"UK"));
        yValues.add(new PieEntry(35f,"India"));
        yValues.add(new PieEntry(40f,"Russia"));
        yValues.add(new PieEntry(40f,"Korea"));

        Description description = new Description();
        description.setText("세계 국가"); //라벨
        description.setTextSize(15);
        pieChart.setDescription(description);

//        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션

        PieDataSet dataSet = new PieDataSet(yValues,"Countries");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

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

//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initBarChart();
//
//                    container.addView(view);
//                }
//            });

                    initBarChart();
//
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

    public void initWeekChart() {
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        barDataSet = new BarDataSet(barEntryData, null);
        barData = new BarData(barDataSet);

        //막대 그래프의 너비
        barData.setBarWidth(0.3f);

        //막대 그래프의 색을 지정해준다.
        barDataSet.setColors(getResources().getColor(R.color.color_e6e9f7));
        //막대 그래프 상단 수치text 크기
        barDataSet.setValueTextSize(13);

        barChart2.setData(barData);
//        barChart2.animateY(3000);
        barChart2.setExtraBottomOffset(24.5f);

        //x축 설정
        XAxis xAxis = barChart2.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14);
        xAxis.setYOffset(9.5f);
        xAxis.setTextColor(getResources().getColor(R.color.color_909090));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);

        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = barChart2.getAxisLeft();
        YAxis YRAXis = barChart2.getAxisRight();

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
        xAxis.setValueFormatter(new MyCustomFormatter(barChart2));

        // 각종 이벤트 방지.
        barChart2.setTouchEnabled(false);
        barChart2.setDragEnabled(false);
        barChart2.setScaleEnabled(false);
        barChart2.setPinchZoom(false);
        barChart2.setDoubleTapToZoomEnabled(false);

        //하단 차트 정보 (막대 색갈별 정보) 안보이게 조정
        Legend legend = barChart2.getLegend();
        legend.setEnabled(false);

        //하단 상세 설명(?) 안보이게 설정
        barChart2.getDescription().setEnabled(false);
    }

    public void initMonthChart() {
        AddValuesToBARENTRY();
        AddValuesToBarEntryLabels();

        barDataSet = new BarDataSet(barEntryData, null);
        barData = new BarData(barDataSet);

        //막대 그래프의 너비
        barData.setBarWidth(0.3f);

        //막대 그래프의 색을 지정해준다.
        barDataSet.setColors(getResources().getColor(R.color.color_e6e9f7));
        //막대 그래프 상단 수치text 크기
        barDataSet.setValueTextSize(13);

        barChart3.setData(barData);
        barChart3.animateY(3000);
        barChart3.setExtraBottomOffset(24.5f);

        //x축 설정
        XAxis xAxis = barChart3.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(14);
        xAxis.setYOffset(9.5f);
        xAxis.setTextColor(getResources().getColor(R.color.color_909090));
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(3);

        //Y축 설정 (왼쪽 / 오른쪽)
        YAxis YLAxis = barChart3.getAxisLeft();
        YAxis YRAXis = barChart3.getAxisRight();

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
        xAxis.setValueFormatter(new MyCustomFormatter(barChart3));

        // 각종 이벤트 방지.
        barChart3.setTouchEnabled(false);
        barChart3.setDragEnabled(false);
        barChart3.setScaleEnabled(false);
        barChart3.setPinchZoom(false);
        barChart3.setDoubleTapToZoomEnabled(false);

        //하단 차트 정보 (막대 색갈별 정보) 안보이게 조정
        Legend legend = barChart3.getLegend();
        legend.setEnabled(false);

        //하단 상세 설명(?) 안보이게 설정
        barChart3.getDescription().setEnabled(false);
    }
}