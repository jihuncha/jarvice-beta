package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.ui.utils.Tools;


/**
 * 원 그래프 하단 목록
 *
 *
* */
public class AdapterPieChartList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterPieChartList.class.getSimpleName();

    private List<DailySalesList> inserData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DailySalesList obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterPieChartList(Context context, List<DailySalesList> insertData) {
        this.inserData = insertData;
        mContext = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_main_deadline_sell_list_color;
        public TextView tv_main_deadline_sell_list_color_category;
        public TextView tv_main_deadline_sell_list_color_money;
        public TextView tv_main_deadline_sell_list_color_percent;

        public OriginalViewHolder(View v) {
            super(v);
            iv_main_deadline_sell_list_color = (ImageView) v.findViewById(R.id.iv_main_deadline_sell_list_color);
            tv_main_deadline_sell_list_color_category = (TextView) v.findViewById(R.id.tv_main_deadline_sell_list_color_category);
            tv_main_deadline_sell_list_color_money = (TextView) v.findViewById(R.id.tv_main_deadline_sell_list_color_money);
            tv_main_deadline_sell_list_color_percent = (TextView) v.findViewById(R.id.tv_main_deadline_sell_list_color_percent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sell_list_color, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final DailySalesList dso = inserData.get(position);
            GradientDrawable imageColor = (GradientDrawable) view.iv_main_deadline_sell_list_color.getDrawable();
            imageColor.setColor(mContext.getResources().getColor(dso.getColor()));
            view.tv_main_deadline_sell_list_color_category.setText(dso.getCategoryName());
            view.tv_main_deadline_sell_list_color_money.setText(Tools.decimalFormat(dso.getCategoryRealSell()));
            view.tv_main_deadline_sell_list_color_percent.setText(dso.getCategorySellPer());
            view.tv_main_deadline_sell_list_color_percent.setTextColor(mContext.getResources().getColor(dso.getColor()));
        }
    }


    @Override
    public int getItemCount() {
        return inserData.size();
    }

}