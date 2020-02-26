package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.DailySalesObject;
import huni.techtown.org.jarvice.ui.utils.Tools;
import huni.techtown.org.jarvice.ui.utils.ViewAnimation;

public class AdapterListExpand extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterListExpand.class.getSimpleName();

    private DailySalesObject inserData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
//        void onItemClick(View view, Social obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterListExpand(Context context, DailySalesObject insertData) {
        this.inserData = insertData;
        mContext = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public ImageButton bt_expand;
        public View lyt_expand;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_expand, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            List<String> dataList = new ArrayList<String>();
            //TODO 이 노가다를 해야하나??더 좋은 방법이...??
            if (!inserData.getSellFood().equals("0")) {
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_food));
            }

            if (!inserData.getSellBeer().equals("0")) {
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_beer));
            }

            if (!inserData.getSellCock().equals("0")) {
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_cock));
            }

            if (!inserData.getSellLiquor().equals("0")) {
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_liquor));
            }

            if (!inserData.getSellDrink().equals("0")) { ;
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_drink));
            }

            if (!inserData.getSellLunch().equals("0")) {
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_lunch));
            }

            if (!inserData.getSellDelivery().equals("0")) {
                dataList.add(mContext.getResources().getString(R.string.main_deadline_pie_graph_delivery));
            }

            Log.d(TAG, "testdddd : " + dataList.toString());
//            final Social p = items.get(position);
//            view.name.setText(p.name);
//            Tools.displayImageOriginal(ctx, view.image, p.image);
//            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (mOnItemClickListener != null) {
////                        mOnItemClickListener.onItemClick(view, items.get(position), position);
//                    }
//                }
//            });
//
//            view.bt_expand.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    boolean show = toggleLayoutExpand(!p.expanded, v, view.lyt_expand);
////                    items.get(position).expanded = show;
//                }
//            });


            // void recycling view
//            if(p.expanded){
//                view.lyt_expand.setVisibility(View.VISIBLE);
//            } else {
//                view.lyt_expand.setVisibility(View.GONE);
//            }
//            Tools.toggleArrow(p.expanded, view.bt_expand, false);

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
        return 2;
//        return 0;
    }

}