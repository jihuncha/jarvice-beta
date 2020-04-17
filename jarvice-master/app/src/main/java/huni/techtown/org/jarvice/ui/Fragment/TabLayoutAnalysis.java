package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.ui.adapter.AdapterAnalysisList;

public class TabLayoutAnalysis extends Fragment {
    private static final String TAG = TabLayoutAnalysis.class.getSimpleName();

    private Handler mHandler;
    private Context mContext;

    private List<WeeklySalesObject> inputWeeklyDataList;

    private RecyclerView rv_main_analysis_list;
    private AdapterAnalysisList adapterAnalysisList;

    public TabLayoutAnalysis() {
    }

    public static TabLayoutAnalysis newInstance() {
        TabLayoutAnalysis fragment = new TabLayoutAnalysis();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_main_analysis, container, false);

        mContext = container.getContext();
        mHandler = new Handler();

        rv_main_analysis_list = root.findViewById(R.id.rv_main_analysis_list);

        inputWeeklyDataList = new ArrayList<WeeklySalesObject>();
        inputWeeklyDataList.addAll(DatabaseManager.getInstance(mContext).getWeeklySales().getList());

        //최근목록을 상위에 해야하기 때문에 순서를 reverse 해준다
        Collections.reverse(inputWeeklyDataList);

        Log.d(TAG, "inputWeeklyDataList - " + inputWeeklyDataList);
//        Log.d(TAG, "inputWeeklyDataList - last : " + inputWeeklyDataList.get(inputWeeklyDataList.size() -1 ));

        rv_main_analysis_list = (RecyclerView)root.findViewById(R.id.rv_main_analysis_list);
        rv_main_analysis_list.setLayoutManager(new LinearLayoutManager(mContext));
        rv_main_analysis_list.setHasFixedSize(true);

        adapterAnalysisList = new AdapterAnalysisList(mContext, inputWeeklyDataList);
        rv_main_analysis_list.setAdapter(adapterAnalysisList);


        return root;
    }
}