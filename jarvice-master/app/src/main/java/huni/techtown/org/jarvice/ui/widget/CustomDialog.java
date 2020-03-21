package huni.techtown.org.jarvice.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import huni.techtown.org.jarvice.R;

public class CustomDialog extends Dialog implements View.OnClickListener{
    public static final String TAG = CustomDialog.class.getSimpleName();

    public interface OnDialogClickListener {
        public void onNagativeClick();
        public void onPositiveClick();
    }

    private Context mContext;
    private int category;               // 공지예약인지 todolist인지 - 0 : 공지예약 1 : todolist
    private int inputCheck;             // 추가 수정 삭제 판별 - 0 : add 1 : modify 2 : delete

    private InputMethodManager imm;

    private OnDialogClickListener mOnDialogClickListener;

    private TextView tv_custom_dialog_title;

    private EditText et_custom_dialog_input;

    private TextView tv_dialog_cancel;
    private TextView tv_dialog_confirm;

    public void setOnItemClickListener(OnDialogClickListener listener) {
        this.mOnDialogClickListener = listener;
    }

    public CustomDialog(Context context, int category, int inputCheck) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.mContext = context;
        this.category = category;
        this.inputCheck = inputCheck;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //뒤에 화면 dim 처리
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.3f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.custom_dialog_view);

        //화면 밖에 클릭시 화면 내려가도록
        RelativeLayout root = (RelativeLayout) findViewById(R.id.rl_root);
        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_custom_dialog_title = (TextView) findViewById(R.id.tv_custom_dialog_title);

        et_custom_dialog_input = (EditText) findViewById(R.id.et_custom_dialog_input);
        if (inputCheck != 2) {
            et_custom_dialog_input.setVisibility(View.VISIBLE);
        } else {
            et_custom_dialog_input.setVisibility(View.GONE);
        }

        tv_dialog_cancel = (TextView) findViewById(R.id.tv_dialog_cancel);
        tv_dialog_cancel.setOnClickListener(this);

        tv_dialog_confirm = (TextView) findViewById(R.id.tv_dialog_confirm);
        tv_dialog_confirm.setOnClickListener(this);

        switch (category) {
            case 0 :
                if (inputCheck == 0) {
                    tv_custom_dialog_title.setText(mContext.getResources().getString(R.string.dialog_title_notification_add));
                    tv_dialog_confirm.setText(mContext.getResources().getString(R.string.dialog_confirm));
                }

                if (inputCheck == 1) {
                    tv_custom_dialog_title.setText(mContext.getResources().getString(R.string.dialog_title_notification_add));
                    tv_dialog_confirm.setText(mContext.getResources().getString(R.string.dialog_modify));
                }

                if (inputCheck == 2) {
                    tv_custom_dialog_title.setText(mContext.getResources().getString(R.string.dialog_title_notification_delete));
                    tv_dialog_confirm.setText(mContext.getResources().getString(R.string.dialog_confirm));
                }

                break;

            case 1 :
                if (inputCheck == 0) {
                    tv_custom_dialog_title.setText(mContext.getResources().getString(R.string.dialog_title_todolist_add));
                    tv_dialog_confirm.setText(mContext.getResources().getString(R.string.dialog_confirm));
                }

                if (inputCheck == 1) {
                    tv_custom_dialog_title.setText(mContext.getResources().getString(R.string.dialog_title_todolist_add));
                    tv_dialog_confirm.setText(mContext.getResources().getString(R.string.dialog_modify));
                }

                if (inputCheck == 2) {
                    tv_custom_dialog_title.setText(mContext.getResources().getString(R.string.dialog_title_todolist_delete));
                    tv_dialog_confirm.setText(mContext.getResources().getString(R.string.dialog_confirm));
                }

                break;
        }

        //키보드 띄우기
        if (et_custom_dialog_input.getVisibility() == View.VISIBLE) {
            imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);

            //위치 설정 및 키보드가 올라감/내려감에 따라 화면 위치 조정
            getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            getWindow().setGravity(Gravity.CENTER);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }

    }

    @Override
    public void dismiss() {
		super.dismiss();

		if (imm != null) {
		    //팝업이 닫히면서 키보드창도 닫혀야한다.
            imm.hideSoftInputFromWindow(et_custom_dialog_input.getWindowToken(), 0);
        }
}


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_dialog_cancel :
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onNagativeClick();
                }

                break;

            case R.id.tv_dialog_confirm :
                if (mOnDialogClickListener != null) {
                    mOnDialogClickListener.onPositiveClick();
                }

                break;
        }
    }


}
