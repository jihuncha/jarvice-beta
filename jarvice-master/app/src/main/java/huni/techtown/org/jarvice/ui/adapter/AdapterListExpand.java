package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
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
import huni.techtown.org.jarvice.ui.utils.Tools;
import huni.techtown.org.jarvice.ui.utils.ViewAnimation;


/**
 * 원 그래프 하단 아이템 상세목록 펼치기
 *
 *
* */
public class AdapterListExpand extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterListExpand.class.getSimpleName();

    private List<DailySalesList> insertData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DailySalesList obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListExpand(Context context, List<DailySalesList> insertData) {
        this.insertData = insertData;
        mContext = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public View main_deadline_sell_item_parent;
        public TextView main_deadline_sell_item_title;
        public TextView main_deadline_sell_item_money;
        public ImageButton bt_expand;
        public View ll_main_deadline_sell_list_detail;
        public RecyclerView rv_main_deadline_sell_list_detail;
        public TextView tv_main_deadline_sell_list_detail_empty;

        public OriginalViewHolder(View v) {
            super(v);
            main_deadline_sell_item_parent = (View) v.findViewById(R.id.main_deadline_sell_item_parent);
            main_deadline_sell_item_title = (TextView) v.findViewById(R.id.main_deadline_sell_item_title);
            main_deadline_sell_item_money = (TextView) v.findViewById(R.id.main_deadline_sell_item_money);
            bt_expand = (ImageButton) v.findViewById(R.id.bt_expand);
            ll_main_deadline_sell_list_detail = (View) v.findViewById(R.id.ll_main_deadline_sell_list_detail);
            rv_main_deadline_sell_list_detail = (RecyclerView) v.findViewById(R.id.rv_main_deadline_sell_list_detail);
            tv_main_deadline_sell_list_detail_empty = (TextView) v.findViewById(R.id.tv_main_deadline_sell_list_detail_empty);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sell_list_detail, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final DailySalesList dso = insertData.get(position);

            view.main_deadline_sell_item_title.setText(dso.getCategoryName());
            view.main_deadline_sell_item_money.setText(Tools.decimalFormat(dso.getCategoryRealSell()));

            AdapterListDetail itemListDataAdapter = new AdapterListDetail(mContext, insertData.get(position).getItemList());

            if (insertData.get(position).getItemList() == null) {
                view.tv_main_deadline_sell_list_detail_empty.setVisibility(View.VISIBLE);
                view.rv_main_deadline_sell_list_detail.setVisibility(View.GONE);
            } else {
                view.tv_main_deadline_sell_list_detail_empty.setVisibility(View.GONE);
                view.rv_main_deadline_sell_list_detail.setVisibility(View.VISIBLE);
            }

            view.rv_main_deadline_sell_list_detail.setHasFixedSize(true);
            view.rv_main_deadline_sell_list_detail.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            view.rv_main_deadline_sell_list_detail.setAdapter(itemListDataAdapter);

            view.main_deadline_sell_item_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        Log.d(TAG, "test7 : " + insertData.get(position).toString());
                        mOnItemClickListener.onItemClick(view, insertData.get(position), position);
                    }
                }
            });

            //하위 아이템 보여주기
            view.bt_expand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!dso.expanded, v, view.ll_main_deadline_sell_list_detail);
                    insertData.get(position).expanded = show;
                }
            });

            view.main_deadline_sell_item_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view.bt_expand.performClick();
                }
            });


            if(dso.expanded){
                view.ll_main_deadline_sell_list_detail.setVisibility(View.VISIBLE);
            } else {
                view.ll_main_deadline_sell_list_detail.setVisibility(View.GONE);
            }
            Tools.toggleArrow(dso.expanded, view.bt_expand, false);

        }
    }

    private boolean toggleLayoutExpand(boolean show, View view, View lyt_expand) {
        Tools.toggleArrow(show, view);
        if (show) {
            ViewAnimation.expand(lyt_expand);
        } else {
            ViewAnimation.collapse(lyt_expand);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return insertData.size();
    }

}