package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.common.data.WeeklySalesObject;
import huni.techtown.org.jarvice.ui.AnalysisDetailActivity;
import huni.techtown.org.jarvice.ui.utils.Tools;
import huni.techtown.org.jarvice.ui.utils.ViewAnimation;


/**
 * 원 그래프 하단 아이템 상세목록 펼치기
 *
 *
* */
public class AdapterAnalysisList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterAnalysisList.class.getSimpleName();

    private List<WeeklySalesObject> inputData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterAnalysisList(Context context, List<WeeklySalesObject> inputData) {
        mContext = context;
        this.inputData = inputData;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_main_analysis_container;
        public TextView tv_main_analysis_title;
        public TextView tv_main_analysis_date;


        public OriginalViewHolder(View v) {
            super(v);
            ll_main_analysis_container = (LinearLayout) v.findViewById(R.id.ll_main_analysis_container);
            tv_main_analysis_title = (TextView) v.findViewById(R.id.tv_main_analysis_title);
            tv_main_analysis_date = (TextView) v.findViewById(R.id.tv_main_analysis_date);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis_list, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder viewHolder = (OriginalViewHolder) holder;

            final WeeklySalesObject weeklySalesObject = inputData.get(position);

            if (inputData.get(position).getSellYear() == null || inputData.get(position).getSellYear().equals("")) {
                viewHolder.tv_main_analysis_title.setText("알수없음");
                viewHolder.tv_main_analysis_date.setText("알수없음");
            } else {
                viewHolder.tv_main_analysis_title.setText(weeklySalesObject.getSellYear() + "년 " + weeklySalesObject.getSellWeek() + "주차 리포트");
                viewHolder.tv_main_analysis_date.setText(weeklySalesObject.getSellYear() + "." + Tools.deleteSpace(weeklySalesObject.getStartWeek())
                        + "~" + Tools.deleteSpace(weeklySalesObject.getEndWeek()));
            }

            viewHolder.ll_main_analysis_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (inputData.get(position).getSellYear() == null || inputData.get(position).getSellYear().equals("")) {
                        Toast.makeText(mContext, "해당 데이터가 없습니다. \n관리자에 문의 바랍니다.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(mContext, AnalysisDetailActivity.class);
                    intent.putExtra("inputObject", weeklySalesObject);
                    mContext.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return inputData.size();
    }

}