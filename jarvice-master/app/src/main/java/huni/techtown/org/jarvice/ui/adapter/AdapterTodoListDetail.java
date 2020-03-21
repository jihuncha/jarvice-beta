package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.CurrentManager;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.HelperTodoListItems;
import huni.techtown.org.jarvice.common.data.HelperTodoListObject;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODO_LIST;
import huni.techtown.org.jarvice.ui.widget.CustomDialog;

/**
 * 오늘의 할일 상세 목록
 *  *
 *
 * */

public class AdapterTodoListDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterTodoListDetail.class.getSimpleName();

    private List<HelperTodoListItems> insertData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    private TBL_HELPER_TODO_LIST mTblHelperTodoList = null;

    //상위 view 업데이트를 위함..
    private OnNotifyDataChanged onNotifyDataChanged;

    public interface OnItemClickListener {
        void onItemClick(View view, DailySalesList obj, int position);
    }

    public interface OnNotifyDataChanged{
        void onEnd();
    }

    public void setOnNotifyDataChanged (final OnNotifyDataChanged onNotifyDataChanged) {
        this.onNotifyDataChanged = onNotifyDataChanged;
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterTodoListDetail(Context context, List<HelperTodoListItems> insertData) {
        this.insertData = insertData;
        mContext = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_main_helper_todolist_detail_parent;
        public LinearLayout ll_main_helper_todolist_detail;
        public CheckBox cb_main_helper_todolist_detail_check;
        public TextView tv_main_helper_todolist_detail_name;
        public ImageView iv_main_helper_todolist_detail_settings;

        public OriginalViewHolder(View v) {
            super(v);
            ll_main_helper_todolist_detail_parent = (LinearLayout) v.findViewById(R.id.ll_main_helper_todolist_detail_parent);
            ll_main_helper_todolist_detail = (LinearLayout) v.findViewById(R.id.ll_main_helper_todolist_detail);
            cb_main_helper_todolist_detail_check = (CheckBox) v.findViewById(R.id.cb_main_helper_todolist_detail_check);
            tv_main_helper_todolist_detail_name = (TextView) v.findViewById(R.id.tv_main_helper_todolist_detail_name);
            iv_main_helper_todolist_detail_settings = (ImageView) v.findViewById(R.id.iv_main_helper_todolist_detail_settings);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper_todolist_detail, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder viewHolder = (OriginalViewHolder) holder;

            if (insertData == null) {
                Log.d(TAG, "hereispr");
            }
            final HelperTodoListItems items = insertData.get(position);

            mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();

            viewHolder.tv_main_helper_todolist_detail_name.setText(insertData.get(position).getItemName());

            if (insertData.get(position).getItemCheck() == 1) {
                viewHolder.cb_main_helper_todolist_detail_check.setChecked(true);
            } else {
                viewHolder.cb_main_helper_todolist_detail_check.setChecked(false);
            }

            viewHolder.iv_main_helper_todolist_detail_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initiatePopupWindow(view, viewHolder, position);

                }
            });
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

    //TODO 너무복잡하다..
    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    private PopupWindow initiatePopupWindow(final View view, final OriginalViewHolder viewHolder, final int position) {

        try {
            mInflater = (LayoutInflater) mContext.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = mInflater.inflate(R.layout.custom_option_popup_real_todo, null);

            //수정
            final TextView tvModify = (TextView) layout.findViewById(R.id.tv_modify);
            tvModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDropdown.dismiss();

//                    //클릭시 db 다시 가져온다. (한번 수정했을경우가 있을수 있다.)
                    mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();

                    //기존 text
                    final TextView textInput = (TextView) viewHolder.tv_main_helper_todolist_detail_name;
                    final String input = textInput.getText().toString().trim();
                    Log.d(TAG, "input - " + input);

                    final CustomDialog cd = new CustomDialog(mContext, 1, 1);
                    cd.show();

                    //show 하고 가져와야함.
                    final EditText editText = (EditText) cd.findViewById(R.id.et_custom_dialog_input);
                    editText.setText(input);
                    //커서를 맨뒤로,
                    editText.setSelection(editText.length());

                    cd.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                        @Override
                        public void onNagativeClick() {
                            cd.dismiss();
                        }

                        @Override
                        public void onPositiveClick() {
                            //변경할 text
                            String output = editText.getText().toString().trim();

                            if (input.equals(output)) {
                                Toast.makeText(mContext, R.string.dialog_modify_err_message, Toast.LENGTH_LONG).show();
                                return;
                            }

                            //아이템 네임만 변경
                            insertData.get(position).setItemName(output);

                            //객체를 생성한다.
                            HelperTodoListObject inputData = new HelperTodoListObject();
                            inputData.setTodoTitle(insertData.get(position).getItemTitle());
                            inputData.setId(insertData.get(position).getId());
                            inputData.setTodoWork(insertData.get(position).getItemName());
                            inputData.setTodoCheck(insertData.get(position).getItemCheck());
                            inputData.setTodoDate(insertData.get(position).getTodoDate());
                            inputData.setTodoColumn(0);

                            mTblHelperTodoList.updateTodoListDetail(inputData);

                            textInput.setText(output);
                            notifyDataSetChanged();

                            cd.dismiss();

                        }
                    });
                }
            });

            //삭제
            final TextView tvDelete = (TextView) layout.findViewById(R.id.tv_delete);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //클릭시 db 다시 가져온다. (한번 수정했을경우가 있을수 있다.)
                    mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();

                    final CustomDialog cd = new CustomDialog(mContext, 1, 2);
                    cd.show();

                    cd.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                        @Override
                        public void onNagativeClick() {
                            mDropdown.dismiss();
                            cd.dismiss();
                        }

                        @Override
                        public void onPositiveClick() {
                            //객체를 생성한다. - id만 가져온다.
                            HelperTodoListObject inputData = new HelperTodoListObject();
                            inputData.setId(insertData.get(position).getId());

                            //db제거
                            mTblHelperTodoList.deleteTodoListDetail(inputData);

                            //view 제거
                            insertData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            notifyDataSetChanged();

                            mDropdown.dismiss();
                            cd.dismiss();

                            //상위 view 업데이트..
                            if (onNotifyDataChanged != null) {
                                onNotifyDataChanged.onEnd();
                            }
                        }
                    });
                }
            });

            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout, FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);

            //TODO 팝업 위치문제..
//            mDropdown.showAsDropDown(view, -400, 5);
            mDropdown.showAsDropDown(view);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }

    //add
    public void add(int position, HelperTodoListObject input) {
        Log.d(TAG, "add - input :  " + input);
        //TODO 디비를 다시 가져옴..(???) 가끔 null 뜸..
        //TODO 순서 데이터베이스에 넣고 -> 화면에 그려준다.
        mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();

        mTblHelperTodoList.insert(input);

//        //TODO input 의 아이디가 없음
//        //TODO 리스트 에서 마지막 걸로 아이디 세팅 (마지막으로 집어넣은것이기떄문..)
//        input.setId(mTblHelperTodoList.getList().get(mTblHelperTodoList.getList().size()- 1).getId());

        HelperTodoListItems inputData = new HelperTodoListItems();

        inputData.setId(input.getId());
        inputData.setItemTitle(input.getTodoTitle());
        inputData.setTodoDate(input.getTodoDate());
        inputData.setTodoColumn(input.getTodoColumn());
        inputData.setItemName(input.getTodoWork());
        inputData.setItemCheck(input.getTodoCheck());

//        if (insertData == null) {
//            insertData = new ArrayList<HelperTodoListItems>();
//            insertData.add(0,inputData);
//            notifyItemInserted(0);
//            notifyItemRangeChanged(0,1);
//            notifyDataSetChanged();
//
//            if (onNotifyDataChanged != null) {
//                onNotifyDataChanged.onEnd();
//            }
//
//        }


        Log.d(TAG, "position check : " + position);

        insertData.add(position, inputData);
        notifyItemInserted(position);
        notifyDataSetChanged();
    }

}