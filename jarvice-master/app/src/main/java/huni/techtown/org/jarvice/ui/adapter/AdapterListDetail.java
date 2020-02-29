package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesListItems;
import huni.techtown.org.jarvice.ui.utils.Tools;
import huni.techtown.org.jarvice.ui.utils.ViewAnimation;

public class AdapterListDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterListDetail.class.getSimpleName();

    private List<DailySalesListItems> insertData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DailySalesList obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListDetail(Context context, List<DailySalesListItems> insertData) {
        this.insertData = insertData;
        mContext = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView main_deadline_sell_item_title_detail;
        public TextView main_deadline_sell_item_money_detail;

        public OriginalViewHolder(View v) {
            super(v);
            main_deadline_sell_item_title_detail = v.findViewById(R.id.main_deadline_sell_item_title_detail);
            main_deadline_sell_item_money_detail = v.findViewById(R.id.main_deadline_sell_item_money_detail);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sell_list_bottom_detail, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final DailySalesListItems items = insertData.get(position);

            String inputTitle = items.getItemName() + " " + items.getItemCount() + "건";
            Log.d(TAG, "Test : " + inputTitle);
            SpannableStringBuilder ssb = new SpannableStringBuilder(inputTitle);
//            span.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_0c4bff)), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.color_4263ff)),
                    items.getItemName().length(), inputTitle.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            view.main_deadline_sell_item_title_detail.setText(ssb);
            view.main_deadline_sell_item_money_detail.setText(Tools.decimalFormat(items.getItemRealSell()));

        }
    }


    @Override
    public int getItemCount() {
        if (insertData != null) {
            return insertData.size();
        } else {
            return 0;
        }
    }

}