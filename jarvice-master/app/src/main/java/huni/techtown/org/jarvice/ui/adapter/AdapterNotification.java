package huni.techtown.org.jarvice.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.HelperNotificationObject;
import huni.techtown.org.jarvice.database.TBL_HELPER_NOTIFICATION;
import huni.techtown.org.jarvice.ui.helper.SwipeItemTouchHelper;
import huni.techtown.org.jarvice.ui.widget.CustomDialog;

public class AdapterNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SwipeItemTouchHelper.SwipeHelperAdapter {
    private static final String TAG = AdapterNotification.class.getSimpleName();

    private Context mContext;
    private List<HelperNotificationObject> insertData;
    private OnItemClickListener mOnItemClickListener;

    private TBL_HELPER_NOTIFICATION mTblHelperNotification = null;

    public AdapterNotification(Context context, List<HelperNotificationObject> items) {
        mContext = context;
        this.insertData = items;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements SwipeItemTouchHelper.TouchViewHolder{
        public LinearLayout ll_main_helper_notification_parent;
        public ImageView iv_main_helper_notification_color;
        public EditText et_main_helper_notification_info;
        public RelativeLayout rv_main_helper_notification_settings;
        public ImageView iv_main_helper_notification_settings;

        public OriginalViewHolder(View v) {
            super(v);
            ll_main_helper_notification_parent = (LinearLayout) v.findViewById(R.id.ll_main_helper_notification_parent);
            iv_main_helper_notification_color = (ImageView) v.findViewById(R.id.iv_main_helper_notification_color);
            et_main_helper_notification_info = (EditText) v.findViewById(R.id.et_main_helper_notification_info);
            rv_main_helper_notification_settings = (RelativeLayout) v.findViewById(R.id.rv_main_helper_notification_settings);
            iv_main_helper_notification_settings = (ImageView) v.findViewById(R.id.iv_main_helper_notification_settings);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(mContext.getResources().getColor(R.color.grey_5));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper_notification, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder viewHolder = (OriginalViewHolder) holder;

            final HelperNotificationObject inputData = insertData.get(position);

            mTblHelperNotification = DatabaseManager.getInstance(mContext).getHelperNotification();

            viewHolder.et_main_helper_notification_info.setText(inputData.getNotiInfo());
            viewHolder.et_main_helper_notification_info.setFocusableInTouchMode(false);
//            viewHolder.et_main_helper_notification_info.requestFocus();

            viewHolder.et_main_helper_notification_info.setEnabled(false);
            if (viewHolder.et_main_helper_notification_info.hasFocus()) {
                Log.d(TAG, "ddd");
            }

            viewHolder.et_main_helper_notification_info.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            viewHolder.rv_main_helper_notification_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.iv_main_helper_notification_settings.performClick();
                }
            });

            viewHolder.iv_main_helper_notification_settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    showSingleChoiceDialog();


//                    onMoreButtonClick(view);

                    initiatePopupWindow(view, viewHolder, position);
                }
            });

//            view.rv_main_helper_notification_info.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                }
//            });

//            viewHolder.et_main_helper_notification_info.onD






//            view.checkBox1.setOnCheckedChangeListener(null);

//            view.checkBox1.setChecked(check.isSelected());

//            view.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                    check.setSelected(b);
//                }
//            });

//            view.button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    int count;
//                    count = getItemCount();

////                    Log.d(TAG, "countCheck : " + count);
//                    Log.d(TAG, "check : " + check.getId());
//                    HelperNotificationObject test = new HelperNotificationObject("2019-01-01","바꾼거", 0);
//                    check.setNotiInfo("바꾼거");
////                    mTblHelperNotification.updateNotification(test);
////                    inserData.add(test);
//
//                    mTblHelperNotification.updateNotification(check);
//
//
//
//                    notifyDataSetChanged();
//                }
//            });

        }
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.d(TAG,"onScrollStateChanged");
//                for (Social s : items_swiped) {
//                    int index_removed = items.indexOf(s);
//                    if (index_removed != -1) {
//                        items.remove(index_removed);
//                        notifyItemRemoved(index_removed);
//                    }
//                }
//                items_swiped.clear();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return insertData.size();
    }

    @Override
    public void onItemDismiss(int position) {

    }

//    private void showSingleChoiceDialog() {
////        ArrayList<String> ar = new ArrayList<String>();
////        ar.add(mContext.getResources().getString(R.string.main_helper_notice_settings_modify));
////        ar.add(mContext.getResources().getString(R.string.main_helper_notice_settings_delete));
////        CustomOptionPopup popup = new CustomOptionPopup(mContext, ar);
////        popup.setOnItemClickListener(mPopupItemClickListener);
////        popup.show();
//        mContext.ge
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        String [] itemList = {mContext.getResources().getString(R.string.main_helper_notice_settings_modify),
//                mContext.getResources().getString(R.string.main_helper_notice_settings_delete),
//                mContext.getResources().getString(R.string.main_helper_notice_settings_alarm)};
//
//        builder.setItems(itemList, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//            }
//        });
//
//
////        builder.setNegativeButton("ddddd", null);
//        builder.show();
//    }

    private void onMoreButtonClick(final View view) {
        Context wrapper = new ContextThemeWrapper(mContext, R.style.PopupStyle);
        PopupMenu popupMenu = new PopupMenu(wrapper, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_modify :
                        break;
                    case R.id.action_delete :
                        break;
                    case R.id.action_alarm :
                        break;
                }
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_product_more);
        popupMenu.show();
    }

    //TODO 너무복잡하다..
    private PopupWindow mDropdown = null;
    LayoutInflater mInflater;
    private PopupWindow initiatePopupWindow(final View view, final OriginalViewHolder viewHolder, final int position) {

        try {
            mInflater = (LayoutInflater) mContext.getApplicationContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = mInflater.inflate(R.layout.custom_option_popup_real, null);

            //수정
            final TextView tvModify = (TextView) layout.findViewById(R.id.tv_modify);
            tvModify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTblHelperNotification = DatabaseManager.getInstance(mContext).getHelperNotification();

                    mDropdown.dismiss();

                    //기존 text
                    final EditText et = (EditText) viewHolder.et_main_helper_notification_info;
                    final String input = et.getText().toString().trim();
                    Log.d(TAG, "input - " + input);

                    final CustomDialog cd = new CustomDialog(mContext, 0, 1);
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

                            insertData.get(position).setNotiInfo(output);
                            et.setText(output);
                            mTblHelperNotification.updateNotification(insertData.get(position));

                            notifyDataSetChanged();

                            cd.dismiss();

                        }
                    });
                }
            });

//            //                            final String beforeText = et.getText().toString().trim();
//
////                            viewHolder.et_main_helper_notification_info.setFocusableInTouchMode(true);
////                            viewHolder.et_main_helper_notification_info.setEnabled(true);
////                            viewHolder.et_main_helper_notification_info.requestFocus();
//            //커서를 맨 끝으로 위치하게함
////                            viewHolder.et_main_helper_notification_info.setSelection(viewHolder.et_main_helper_notification_info.length());
//
//            //Multi line 으로 액션을 변경하기 위해서 rawinputtype을 지정해줘야한다.
////                            viewHolder.et_main_helper_notification_info.setImeOptions(EditorInfo.IME_ACTION_DONE);
////                            viewHolder.et_main_helper_notification_info.setRawInputType(InputType.TYPE_CLASS_TEXT);
//
//            //강제로 키보드 띄우기
////                            final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
////                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//
//            viewHolder.et_main_helper_notification_info.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                    Log.d(TAG, "dsad : " + charSequence.toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    //라인수 제한
////                                    if (viewHolder.et_main_helper_notification_info.getLineCount() > 2) {
////                                        viewHolder.et_main_helper_notification_info.setText(beforeText);
////                                        viewHolder.et_main_helper_notification_info.setSelection(viewHolder.et_main_helper_notification_info.length());
////                                    }
//                }
//            });
//
//            viewHolder.et_main_helper_notification_info.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                    switch (i) {
//                        case EditorInfo.IME_ACTION_DONE:
//                            break;
//
//                    }
////                            if (i == EditorInfo.IME_ACTION_DONE) {
////                                Log.d(TAG, "test");
////                                viewHolder.et_main_helper_notification_info.setFocusableInTouchMode(false);
////                                viewHolder.et_main_helper_notification_info.setEnabled(false);
////
////                            }
//
//                    return false;

            //삭제
            final TextView tvDelete = (TextView) layout.findViewById(R.id.tv_delete);
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTblHelperNotification = DatabaseManager.getInstance(mContext).getHelperNotification();

                    final CustomDialog cd = new CustomDialog(mContext, 0, 2);
                    cd.show();

                    cd.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                        @Override
                        public void onNagativeClick() {
                            mDropdown.dismiss();
                            cd.dismiss();
                        }

                        @Override
                        public void onPositiveClick() {
                            //db제거
                            mTblHelperNotification.deleteNotification(insertData.get(position));

                            //view 제거
                            insertData.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, getItemCount());
                            notifyDataSetChanged();

                            mDropdown.dismiss();
                            cd.dismiss();
                        }
                    });
                }
            });

            layout.measure(View.MeasureSpec.UNSPECIFIED,
                    View.MeasureSpec.UNSPECIFIED);
            mDropdown = new PopupWindow(layout,FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,true);
//            Drawable background = mContext.getResources().getDrawable(android.R.drawable.editbox_dropdown_dark_frame);
//            mDropdown.setBackgroundDrawable(background);

            //TODO 팝업 위치문제..
//            mDropdown.showAsDropDown(view, -400, 5);
            mDropdown.showAsDropDown(view);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return mDropdown;

    }

    public void add(int position, HelperNotificationObject input) {
        Log.d(TAG, "add - input :  " + input);
        //TODO 디비를 다시 가져옴..(???) 가끔 null 뜸..
        //TODO 순서 데이터베이스에 넣고 -> 화면에 그려준다.
        mTblHelperNotification = DatabaseManager.getInstance(mContext).getHelperNotification();

        mTblHelperNotification.insert(input);

        //TODO input 의 아이디가 없음
        //TODO 리스트 에서 마지막 걸로 아이디 세팅 (마지막으로 집어넣은것이기떄문..)
        input.setId(mTblHelperNotification.getList().get(mTblHelperNotification.getList().size()- 1).getId());

        insertData.add(position, input);
        notifyItemInserted(position);
        notifyDataSetChanged();

    }

}