package huni.techtown.org.jarvice.ui.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.DailySalesList;
import huni.techtown.org.jarvice.common.data.HelperTodoListItems;
import huni.techtown.org.jarvice.common.data.HelperTodoListObject;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODO_LIST;
import huni.techtown.org.jarvice.ui.utils.Tools;
import huni.techtown.org.jarvice.ui.utils.ViewAnimation;
import huni.techtown.org.jarvice.ui.widget.CustomDialog;


/**
 * 오늘의 할일 목록
 *
 *
* */
public class AdapterTodoList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = AdapterTodoList.class.getSimpleName();

    private List<HelperTodoListObject> insertData;

    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    private TBL_HELPER_TODO_LIST mTblHelperTodoList = null;

    private String todayDate;

    //helper 에서 편집화면 눌렀을때 편집 아이콘 보여주는설정
    private Boolean visible = false;

    public interface OnItemClickListener {
        void onItemClick(View view, HelperTodoListObject obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public AdapterTodoList(Context context, List<HelperTodoListObject> insertData, String todayDate) {
        this.insertData = insertData;
        mContext = context;
        this.todayDate = todayDate;
    }


    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout ll_main_helper_todolist_parent;
        public TextView tv_main_helper_todolist_title;
        public ImageView iv_main_helper_todolist_modify;
        public ImageView iv_main_helper_todolist_delete;
        public LinearLayout ll_main_helper_todolist_detail_title;
        public LinearLayout ll_main_helper_todolist_detail;
        public RecyclerView rv_main_helper_todolist_detail;
        public LinearLayout ll_main_helper_todolist_add;
        public TextView tv_main_helper_todolist_add;
        public TextView tv_main_helper_todolist_empty;

        public OriginalViewHolder(View v) {
            super(v);
            ll_main_helper_todolist_parent = (LinearLayout) v.findViewById(R.id.ll_main_helper_todolist_parent);
            tv_main_helper_todolist_title = (TextView) v.findViewById(R.id.tv_main_helper_todolist_title);
            iv_main_helper_todolist_modify = (ImageView) v.findViewById(R.id.iv_main_helper_todolist_modify);
            iv_main_helper_todolist_delete = (ImageView) v.findViewById(R.id.iv_main_helper_todolist_delete);
            ll_main_helper_todolist_detail_title = (LinearLayout) v.findViewById(R.id.ll_main_helper_todolist_detail_title);
            ll_main_helper_todolist_detail = (LinearLayout) v.findViewById(R.id.ll_main_helper_todolist_detail);
            rv_main_helper_todolist_detail = (RecyclerView) v.findViewById(R.id.rv_main_helper_todolist_detail);
            ll_main_helper_todolist_add = (LinearLayout) v.findViewById(R.id.ll_main_helper_todolist_add);
            tv_main_helper_todolist_add = (TextView) v.findViewById(R.id.tv_main_helper_todolist_add);
            tv_main_helper_todolist_empty = (TextView) v.findViewById(R.id.tv_main_helper_todolist_empty);
        }

        public ImageView getImage() {
            return this.iv_main_helper_todolist_modify;
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper_todolist, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder viewHolder = (OriginalViewHolder) holder;

            final HelperTodoListObject helperObject = insertData.get(position);

            mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();

            viewHolder.tv_main_helper_todolist_title.setText(insertData.get(position).getTodoTitle());

            Log.d(TAG, "ttt : " + insertData.get(position).getTodoColumn());
            final AdapterTodoListDetail itemListDataAdapter = new AdapterTodoListDetail(mContext, insertData.get(position).getItemList());

            viewHolder.rv_main_helper_todolist_detail.setHasFixedSize(true);
            viewHolder.rv_main_helper_todolist_detail.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            viewHolder.rv_main_helper_todolist_detail.setAdapter(itemListDataAdapter);

            //view 업데이트를 위함
            itemListDataAdapter.setOnNotifyDataChanged(new AdapterTodoListDetail.OnNotifyDataChanged() {
                @Override
                public void onEnd() {
                    Log.d(TAG, "setOnNotifyDataChanged - onEnd");

                    notifyDataSetChanged();
                }
            });

            viewHolder.tv_main_helper_todolist_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean show = toggleLayoutExpand(!helperObject.expanded,
                            viewHolder.ll_main_helper_todolist_detail_title, viewHolder.ll_main_helper_todolist_detail,
                            viewHolder.ll_main_helper_todolist_add);
                    insertData.get(position).expanded = show;
                }
            });

            if (helperObject.expanded) {
                viewHolder.ll_main_helper_todolist_detail_title.setVisibility(View.VISIBLE);
                viewHolder.ll_main_helper_todolist_detail.setVisibility(View.VISIBLE);
                viewHolder.ll_main_helper_todolist_add.setVisibility(View.VISIBLE);
            } else {
                viewHolder.ll_main_helper_todolist_detail_title.setVisibility(View.GONE);
                viewHolder.ll_main_helper_todolist_detail.setVisibility(View.GONE);
                viewHolder.ll_main_helper_todolist_add.setVisibility(View.GONE);
            }

            viewHolder.ll_main_helper_todolist_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, insertData.get(position),position);
                    }
                }
            });

            if (visible) {
                viewHolder.iv_main_helper_todolist_modify.setVisibility(View.VISIBLE);
                viewHolder.iv_main_helper_todolist_delete.setVisibility(View.VISIBLE);
            } else {
                viewHolder.iv_main_helper_todolist_modify.setVisibility(View.GONE);
                viewHolder.iv_main_helper_todolist_delete.setVisibility(View.GONE);
            }


            viewHolder.iv_main_helper_todolist_modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final TextView title = (TextView) viewHolder.tv_main_helper_todolist_title;

                    final CustomDialog cd = new CustomDialog(mContext, 1, 1);
                    cd.show();

                    //show 하고 가져와야함.
                    final EditText editText = (EditText) cd.findViewById(R.id.et_custom_dialog_input);
                    editText.setText(title.getText().toString().trim());
                    //커서를 맨뒤로,
                    editText.setSelection(editText.length());

                    cd.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                        @Override
                        public void onNagativeClick() {
                            cd.dismiss();
                        }

                        @Override
                        public void onPositiveClick() {
                            Log.d(TAG, "test2");
                            //변경할 text
                            String output = editText.getText().toString().trim();

                            if (title.getText().toString().trim().equals(output)) {
                                Toast.makeText(mContext, R.string.dialog_modify_err_message, Toast.LENGTH_LONG).show();
                                return;
                            }

                            //TODO 순서 주의
                            insertData.get(position).setTodoTitle(output);
                            mTblHelperTodoList.updateTodoList(output, title.getText().toString().trim());
                            title.setText(output);

                            notifyDataSetChanged();

                            cd.dismiss();

                        }
                    });
                }
            });

            viewHolder.iv_main_helper_todolist_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialog cd = new CustomDialog(mContext, 1, 2);
                    cd.show();

                    cd.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                        @Override
                        public void onNagativeClick() {
                            cd.dismiss();
                        }

                        @Override
                        public void onPositiveClick() {
                            //db제거
                            mTblHelperTodoList.deleteTodoList(insertData.get(position).getTodoTitle());

                            //view 제거
                            insertData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            notifyDataSetChanged();

                            cd.dismiss();
                        }
                    });
                }
            });

            //listdetail 을 add 한다.
            viewHolder.ll_main_helper_todolist_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final CustomDialog customDialog = new CustomDialog(mContext, 1,0);

                    customDialog.show();

                    customDialog.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                        @Override
                        public void onNagativeClick() {
                            customDialog.dismiss();
                        }

                        @Override
                        public void onPositiveClick() {
                            EditText editText = (EditText) customDialog.findViewById(R.id.et_custom_dialog_input);
                            String inputText = editText.getText().toString();

                            //클릭 한 순간 데이터를 다시 가져옴
                            mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();
                            Log.d(TAG, "mTblHelperTodoList - size : " + mTblHelperTodoList.getList().size());

                            HelperTodoListObject lastObject = new HelperTodoListObject();
                            //todolist가 하나라도 존재 할 경우 -> 존재하는 todolist 중에 마지막 객체를 가져온다 (id떄문)
                            if (mTblHelperTodoList.getList().size() > 0) {
                                lastObject = mTblHelperTodoList.getList().get(mTblHelperTodoList.getList().size() - 1);
                            }

                            //id 설정을 위한 num
                            long num = 0;
                            if (lastObject != null) {
                                num = lastObject.getId();
                            }

                            String dateChange = Tools.changeComma(todayDate);

                            //객체생성
                            HelperTodoListObject inputData = new HelperTodoListObject();
                            inputData.setTodoTitle(insertData.get(position).getTodoTitle());
                            inputData.setId(num);
                            inputData.setTodoWork(inputText);
                            inputData.setTodoCheck(0);
                            inputData.setTodoDate(dateChange);
                            inputData.setTodoColumn(0);

                            viewHolder.rv_main_helper_todolist_detail.getAdapter().notifyDataSetChanged();

                            int result = itemListDataAdapter.getItemCount();

                            //TODO 여기 너무 임시방편코드....
                            //TODO adatper 가 null이되는 경우 발생
                            //어댑터가 null이 아니면서 항목이 하나라도 있는 경우에는 평소처럼 add.
                            if (itemListDataAdapter != null && itemListDataAdapter.getItemCount() != 0) {
                                itemListDataAdapter.add(result, inputData);
                                itemListDataAdapter.notifyDataSetChanged();
                            } else {
                                //최초 add 할경우는 다르게한다.
                                Log.e(TAG, "ll_main_helper_todolist_add - first Time!");

                                //list 객체 생성
                                HelperTodoListItems inputItem = new HelperTodoListItems();

                                inputItem.setId(inputData.getId());
                                inputItem.setItemTitle(inputData.getTodoTitle());
                                inputItem.setTodoDate(inputData.getTodoDate());
                                inputItem.setTodoColumn(inputData.getTodoColumn());
                                inputItem.setItemName(inputData.getTodoWork());
                                inputItem.setItemCheck(inputData.getTodoCheck());

                                ArrayList<HelperTodoListItems> inputItemList = new ArrayList<HelperTodoListItems>();
                                inputItemList.add(inputItem);

                                insertData.get(position).setItemList(inputItemList);
                                final AdapterTodoListDetail itemListDataAdapter = new AdapterTodoListDetail(mContext, insertData.get(position).getItemList());

                                //어댑터 새로 생성 -> view 생성
                                viewHolder.rv_main_helper_todolist_detail.setHasFixedSize(true);
                                viewHolder.rv_main_helper_todolist_detail.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                                viewHolder.rv_main_helper_todolist_detail.setAdapter(itemListDataAdapter);

                                // database 에 반영.
                                mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();
//                                mTblHelperTodoList.deleteTodoListNoColumn(inputData.getTodoTitle());
//                                mTblHelperTodoList.insert(inputData);
                                mTblHelperTodoList.updateTodoListDetail(inputData);
                            }

                            notifyDataSetChanged();

                            customDialog.dismiss();
                        }
                    });
                }
            });

        }
    }

    private boolean toggleLayoutExpand(boolean show, View expandLayout, View expandSecondLayout, View expandThirdLayout) {
        if (show) {
            ViewAnimation.expand(expandLayout);
            ViewAnimation.expand(expandSecondLayout);
            ViewAnimation.expand(expandThirdLayout);
        } else {
            ViewAnimation.collapse(expandLayout);
            ViewAnimation.collapse(expandSecondLayout);
            ViewAnimation.collapse(expandThirdLayout);
        }
        return show;
    }

    @Override
    public int getItemCount() {
        return insertData.size();
    }

    public void showModifyView() {
        Log.d(TAG, "showModifyView - visible : " + visible);

        if (visible) {
            visible = false;
        } else {
            visible = true;
        }

        notifyDataSetChanged();

    }

    //add
    public void add(int position, HelperTodoListObject input) {
        Log.d(TAG, "add - input :  " + input);
        //TODO 디비를 다시 가져옴..(???) 가끔 null 뜸..
        //TODO 순서 데이터베이스에 넣고 -> 화면에 그려준다.
        mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();

        mTblHelperTodoList.insert(input);

        //TODO input 의 아이디가 없음
        //TODO 리스트 에서 마지막 걸로 아이디 세팅 (마지막으로 집어넣은것이기떄문..)
        input.setId(mTblHelperTodoList.getList().get(mTblHelperTodoList.getList().size()- 1).getId());

        insertData.add(position, input);
        notifyItemInserted(position);
//        notifyItemRangeChanged(position, insertData.size());
        notifyDataSetChanged();

    }


}