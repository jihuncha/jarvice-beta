package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.HelperNotificationObject;
import huni.techtown.org.jarvice.common.data.HelperTodoListDefaultObject;
import huni.techtown.org.jarvice.common.data.HelperTodoListItems;
import huni.techtown.org.jarvice.common.data.HelperTodoListObject;
import huni.techtown.org.jarvice.database.TBL_HELPER_NOTIFICATION;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODO_LIST;
import huni.techtown.org.jarvice.database.TBL_HELPER_TODO_LIST_DEFAULT;
import huni.techtown.org.jarvice.ui.adapter.AdapterNotification;
import huni.techtown.org.jarvice.ui.adapter.AdapterTodoList;
import huni.techtown.org.jarvice.ui.popup.LoadingActivity;
import huni.techtown.org.jarvice.ui.utils.Tools;
import huni.techtown.org.jarvice.ui.widget.CustomDialog;

public class TabLayoutHelper extends Fragment implements View.OnClickListener {
    private static final String TAG = TabLayoutHelper.class.getSimpleName();

    private Handler mHandler;
    private Context mContext;

    private String todayDate = null;

    //상단 날짜 선택 영역
    private TextView main_helper_title_date;
    private RelativeLayout rl_main_helper_title_left;
    private ImageView iv_main_helper_title_left;
    private RelativeLayout rl_main_helper_title_right;
    private ImageView iv_main_helper_title_right;

    //공지-예약영역
    private ImageView iv_notification_plus;
    private RecyclerView rv_main_helper_notice;
    private AdapterNotification notiAdapter;

    private ArrayList<HelperNotificationObject> notificationList;

    private ItemTouchHelper mItemTouchHelper;

    //todoList
    private ImageView iv_main_helper_todolist_modify;
    private ImageView iv_main_helper_todolist_add;

    private RecyclerView rv_main_helper_todolist;
    private AdapterTodoList todoListAdapter;

    private ArrayList<HelperTodoListObject> todoListList;
    private ArrayList<HelperTodoListDefaultObject> todoListDefaultList;

    //정제하여 보낼 데이터
    private ArrayList<HelperTodoListObject> todoListResult;

    public TabLayoutHelper() {}

    public static TabLayoutHelper newInstance() {
        TabLayoutHelper fragment = new TabLayoutHelper();
        return fragment;
    }

    private TBL_HELPER_NOTIFICATION mTblHelperNotification = null;
    private TBL_HELPER_TODO_LIST mTblHelperTodoList = null;
    private TBL_HELPER_TODO_LIST_DEFAULT mTblHelperTodoDefaultList = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_main_helper, container, false);

        mContext = container.getContext();
        mHandler = new Handler();

        //오늘 날짜를 체크하여 textview에 반영한다.
        Calendar calendar = Calendar.getInstance();
        long todayTimeCheck = calendar.getTimeInMillis();
        main_helper_title_date = (TextView) root.findViewById(R.id.main_helper_title_date);
        todayDate = Tools.getFormattedDateTitle(todayTimeCheck);

        if (todayDate != null) {
            main_helper_title_date.setText(todayDate);
        } else {
            main_helper_title_date.setText("알 수 없음");
        }

        main_helper_title_date.setOnClickListener(this);

        rl_main_helper_title_left = (RelativeLayout) root.findViewById(R.id.rl_main_helper_title_left);
        rl_main_helper_title_left.setOnClickListener(this);
        iv_main_helper_title_left = (ImageView) root.findViewById(R.id.iv_main_helper_title_left);
        iv_main_helper_title_left.setOnClickListener(this);

        rl_main_helper_title_right = (RelativeLayout) root.findViewById(R.id.rl_main_helper_title_right);
        rl_main_helper_title_right.setOnClickListener(this);
        iv_main_helper_title_right = (ImageView) root.findViewById(R.id.iv_main_helper_title_right);
        iv_main_helper_title_right.setOnClickListener(this);

        iv_notification_plus = (ImageView) root.findViewById(R.id.iv_notification_plus);
        iv_notification_plus.setOnClickListener(this);

        rv_main_helper_notice = (RecyclerView) root.findViewById(R.id.rv_main_helper_notice);

        insertNotificationData("onCreate");

        //TODO 좌우 스크롤 이벤트를 위해 추가
//        ItemTouchHelper.Callback callback = new SwipeItemTouchHelper(notiAdapter);
//        mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(rv_main_helper_notice);


        //todolist영역
        iv_main_helper_todolist_modify = (ImageView) root.findViewById(R.id.iv_main_helper_todolist_modify);
        iv_main_helper_todolist_modify.setOnClickListener(this);
        iv_main_helper_todolist_add = (ImageView) root.findViewById(R.id.iv_main_helper_todolist_add);
        iv_main_helper_todolist_add.setOnClickListener(this);

        rv_main_helper_todolist = (RecyclerView) root.findViewById(R.id.rv_main_helper_todolist);
        insertTodoListData("onCreate");

        return root;
    }


    //달력 선택기능
    private void datePicker() {
        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy.MM.dd");

        //오늘 날짜값 대입
        Date result = null;
        try {
            result = transFormat.parse(todayDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(result);
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        //날짜를 선택할 경우 해당 날짜로 견병
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        long date_ship_millis = calendar.getTimeInMillis();

                        todayDate = Tools.getFormattedDateTitle(date_ship_millis);
                        main_helper_title_date.setText(todayDate);
                    }
                },
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)
        );
        //set dark theme
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        //오늘 날짜 이후부터 선택가능하도록 설정
//        datePicker.setMinDate(currentCalendar);
        datePicker.show(getActivity().getFragmentManager(), "Select Date");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_main_helper_title_left :
            case R.id.rl_main_helper_title_left :
//                Intent intentLeft = new Intent(mContext, LoadingActivity.class);
//                startActivity(intentLeft);
                LoadingActivity.show(mContext,"iv_main_helper_title_left");

                String leftResult = Tools.handleDateChange(todayDate, -1);
                todayDate = leftResult;
                Log.d(TAG, "iv_main_helper_title_left - onclick : " + todayDate);
                main_helper_title_date.setText(todayDate);
                insertNotificationData("iv_main_helper_title_left");
                insertTodoListData("iv_main_helper_title_left");

                LoadingActivity.hide(mContext,"iv_main_helper_title_left");

                break;

            case R.id.iv_main_helper_title_right :
            case R.id.rl_main_helper_title_right :
//                Intent intentRight = new Intent(mContext, LoadingActivity.class);
//                startActivity(intentRight);
                LoadingActivity.show(mContext,"iv_main_helper_title_right");

                String rightResult = Tools.handleDateChange(todayDate, 1);
                todayDate = rightResult;
                Log.d(TAG, "iv_main_helper_title_right - onclick : " + todayDate);
                main_helper_title_date.setText(todayDate);
                insertNotificationData("iv_main_helper_title_right");
                insertTodoListData("iv_main_helper_title_right");

                LoadingActivity.hide(mContext,"iv_main_helper_title_right");

                break;

            case R.id.main_helper_title_date :
                //TODO 일단 주석..
//                datePicker();
                break;

            case R.id.iv_notification_plus:
                final CustomDialog customDialog = new CustomDialog(mContext, 0,0);
                customDialog.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onNagativeClick() {
                        Log.d(TAG, "iv_notification_plus - onNagativeClick");
                        customDialog.dismiss();
                    }

                    @Override
                    public void onPositiveClick() {
                        Log.d(TAG, "iv_notification_plus - onPositiveClick");

                        EditText editText = (EditText) customDialog.findViewById(R.id.et_custom_dialog_input);
                        String inputText = editText.getText().toString();

                        //클릭 한 순간 데이터를 다시 가져옴
                        mTblHelperNotification = DatabaseManager.getInstance(mContext).getHelperNotification();
                        Log.d(TAG, "onPositiveClick - dataSize : " + mTblHelperNotification.getListDate(todayDate).size());
                        Log.d(TAG, "onPositiveClick - dataSizeAll : " + mTblHelperNotification.getList().size());

                        //해당 날짜의 데이터가 3개 이상인 경우는 공지를 더 생성할 수 없다.
                        if (mTblHelperNotification.getListDate(todayDate).size() >= 3) {
                            Log.d(TAG,"mTblHelperNotification.getList().size() >= 3");
                            Toast.makeText(mContext, mContext.getResources().getString(R.string.main_helper_notice_add_error) , Toast.LENGTH_SHORT).show();
                        } else {
                            //공지가 3개 이하인 경우는 추가한다.
                            HelperNotificationObject lastObject = new HelperNotificationObject();
                            //공지가 하나라도 존재 할 경우 -> 존재하는 공지 중에 마지막 공지를 가져온다 (id떄문)
                            //TODO 여기는 database 에 공지가 없는지 있는지 체크
                            if (mTblHelperNotification.getList().size() > 0) {
                                lastObject = mTblHelperNotification.getList().get(mTblHelperNotification.getList().size() - 1);
                            }

                            //id 설정을 위한 num
                            long num = 0;
                            if (lastObject != null) {
                                num = lastObject.getId();
                            }

                            //1. 날짜가져오기 - 형태 변경.
                            String inputDate = null;
                            if (todayDate != null && !todayDate.isEmpty()) {
                                inputDate = todayDate.replace(".", "-");
                            } else {
                                inputDate = "알수없음";
                            }

                            //2. 현재 아이템 갯수 가져오기 - recyclerview의
                            int result = notiAdapter.getItemCount();

                            Log.d(TAG, "result check : " + result);

                            if (inputText == null || inputText.equals("")) {
                                Toast.makeText(mContext, "입력값이 없을 경우 공지 추가가 불가능합니다.", Toast.LENGTH_SHORT).show();
                                customDialog.dismiss();
                                return;
                            }

                            if (mTblHelperNotification.getList().size() > 0) {
                                notiAdapter.add(result, new HelperNotificationObject(inputDate, inputText, 0));
                            } else {
                                notiAdapter.add(result, new HelperNotificationObject(1, inputDate, inputText, 0));
                            }

                            customDialog.dismiss();
                        }
                    }
                });

                customDialog.show();
                break;

                //todolist
            case R.id.iv_main_helper_todolist_modify :
                todoListAdapter.showModifyView();

                break;

            case R.id.iv_main_helper_todolist_add :
                final CustomDialog customDialogTodo = new CustomDialog(mContext, 1,0);
                customDialogTodo.setOnItemClickListener(new CustomDialog.OnDialogClickListener() {
                    @Override
                    public void onNagativeClick() {
                        Log.d(TAG, "iv_main_helper_todolist_add - onNagativeClick");
                        customDialogTodo.dismiss();
                    }

                    @Override
                    public void onPositiveClick() {
                        Log.d(TAG, "iv_main_helper_todolist_add - onPositiveClick");

                        EditText editText = (EditText) customDialogTodo.findViewById(R.id.et_custom_dialog_input);
                        String inputText = editText.getText().toString();

                        //클릭 한 순간 데이터를 다시 가져옴
                        mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();
                        Log.d(TAG, "mTblHelperTodoList - size : " + mTblHelperTodoList.getList().size());

                        HelperTodoListObject lastObject = new HelperTodoListObject();
                        //todolist가 하나라도 존재 할 경우 -> 존재하는 todolist 중에 마지막 객체를 가져온다 (id떄문)
                        //TODO 여기는 database 에 공지가 없는지 있는지 체크
                        if (mTblHelperTodoList.getList().size() > 0) {
                            lastObject = mTblHelperTodoList.getList().get(mTblHelperTodoList.getList().size() - 1);
                        }

                        //id 설정을 위한 num
                        long num = 0;
                        if (lastObject != null) {
                            num = lastObject.getId();
                        }

                        //1. 날짜가져오기 - 형태 변경.
                        String inputDate = null;
                        if (todayDate != null && !todayDate.isEmpty()) {
                            inputDate = todayDate.replace(".", "-");
                        } else {
                            inputDate = "알수없음";
                        }

                        //2. 현재 아이템 갯수 가져오기 - recyclerview의
                        int result = todoListAdapter.getItemCount();

                        Log.d(TAG, "result check : " + result);

                        if (inputText == null || inputText.equals("")) {
                            Toast.makeText(mContext, "입력값이 없을 경우 To Do List 추가가 불가능합니다.", Toast.LENGTH_SHORT).show();
                            customDialogTodo.dismiss();
                            return;
                        }

                        if (mTblHelperNotification.getList().size() > 0) {
                            todoListAdapter.add(result, new HelperTodoListObject(inputDate, inputText, 1));
                        } else {
                            todoListAdapter.add(result, new HelperTodoListObject(1, inputDate, inputText, 1));
                        }

                        customDialogTodo.dismiss();

                    }
                });

                customDialogTodo.show();


                break;
        }
    }

    public void insertNotificationData (String f) {
        Log.d(TAG, "insertNotificationData - " + f);

        //공지-예약영역
        notificationList = new ArrayList<HelperNotificationObject>();

        mTblHelperNotification = DatabaseManager.getInstance(mContext).getHelperNotification();

        //해당 날짜에 대한 데이터 입력
        notificationList.addAll(mTblHelperNotification.getListDate(Tools.changeComma(todayDate)));

        rv_main_helper_notice.setLayoutManager(new LinearLayoutManager(mContext));
        //TODO 이거때문에 항목 삭제시 view 갱신이 안됬음 -> false 로 변경...
        rv_main_helper_notice.setHasFixedSize(false);
        notiAdapter = new AdapterNotification(mContext, notificationList);
        notiAdapter.setOnItemClickListener(new AdapterNotification.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                Log.d(TAG, "notiAdapter - " + position);
            }
        });
        rv_main_helper_notice.setAdapter(notiAdapter);
    }

    public void insertTodoListData (String f) {
        Log.d(TAG, "insertTodoListData - " + f);

        todoListList = new ArrayList<HelperTodoListObject>();
        todoListDefaultList = new ArrayList<HelperTodoListDefaultObject>();

        //정제하여 보낼 데이터
        todoListResult = new ArrayList<HelperTodoListObject>();

        //DB 에서 데이터 get
        mTblHelperTodoList = DatabaseManager.getInstance(mContext).getHelperTodoList();
        mTblHelperTodoDefaultList = DatabaseManager.getInstance(mContext).getHelperTodoListDefault();

        Log.d(TAG, "day : " + todayDate);
        //1. 먼저 해당 날짜에 데이터가 있는지 체크 -> 없는 경우는 default 데이터를 가져온다.
        if (mTblHelperTodoList.getTodayList(Tools.changeComma(todayDate)).size() == 0
                || mTblHelperTodoList.getTodayList(Tools.changeComma(todayDate)) == null) {

            //default 데이터
            todoListDefaultList.addAll(mTblHelperTodoDefaultList.getList());
            //해당 날짜를 설정한다.
            for (int i = 0; i < todoListDefaultList.size(); i++) {
                todoListList.add(new HelperTodoListObject(Tools.changeComma(todayDate),
                        todoListDefaultList.get(i).getTodoTitle(),
                        todoListDefaultList.get(i).getTodoColumn(),
                        todoListDefaultList.get(i).getTodoCheck(),
                        todoListDefaultList.get(i).getTodoWork()));
            }
            //해당 날짜에 데이터 insert
            mTblHelperTodoList.insertForSync(todoListList);
        } else {
            todoListList.addAll(mTblHelperTodoList.getTodayList(Tools.changeComma(todayDate)));
        }

        //2. 해당 데이터를 정제한다.
        HashMap<String, List<HelperTodoListItems>> itemHash = new HashMap<String, List<HelperTodoListItems>>();
        List<String> check  = new ArrayList<String>();

        for (int i = 0; i < todoListList.size(); i++) {
            if (!check.contains(todoListList.get(i).getTodoTitle())) {
                check.add(todoListList.get(i).getTodoTitle());

                //새로운 아이템을 생성해준다.
                HelperTodoListItems items = new HelperTodoListItems();
                items.setId(todoListList.get(i).getId());
                items.setItemTitle(todoListList.get(i).getTodoTitle());
                items.setItemName(todoListList.get(i).getTodoWork());
                items.setItemCheck(todoListList.get(i).getTodoCheck());
                items.setTodoDate(todoListList.get(i).getTodoDate());

                //새로운 아이템을 담을 리스트 생성
                List<HelperTodoListItems> input = new ArrayList<HelperTodoListItems>();
                input.add(items);

                //결과 값에 반영한다.
                itemHash.put(todoListList.get(i).getTodoTitle(), input);
            } else {
                //기존에 있는 상품일 경우
                HelperTodoListItems items = new HelperTodoListItems();
                items.setId(todoListList.get(i).getId());
                items.setItemTitle(todoListList.get(i).getTodoTitle());
                items.setItemName(todoListList.get(i).getTodoWork());
                items.setItemCheck(todoListList.get(i).getTodoCheck());
                items.setTodoDate(todoListList.get(i).getTodoDate());

                //기존 리스트에 아이템을 새로 담는다.
                itemHash.get(todoListList.get(i).getTodoTitle()).add(items);
            }
        }

        Log.d(TAG, "insertTodoListData - itemHash : " + itemHash.toString());
        Log.d(TAG, "insertTodoListData - titleList : " + check.toString());

        //3. 건내줄 리스트에 대입한다.
        for (int i = 0; i < check.size(); i++) {
            HelperTodoListObject inputdata = new HelperTodoListObject();

            inputdata.setTodoTitle(check.get(i));

            if (itemHash.get(check.get(i)).size() != 0) {
                inputdata.setTodoColumn(0);
            } else {
                inputdata.setTodoColumn(1);
            }

            inputdata.setTodoDate(Tools.changeComma(todayDate));
            inputdata.setItemList(itemHash.get(check.get(i)));

            todoListResult.add(inputdata);
        }

        Log.d(TAG, "input - " + todoListResult.toString());

        //4. 리사이클러뷰에 전달
        rv_main_helper_todolist.setLayoutManager(new LinearLayoutManager(mContext));
        //TODO 이거때문에 항목 삭제시 view 갱신이 안됬음 -> false 로 변경...
        rv_main_helper_todolist.setHasFixedSize(false);
        todoListAdapter = new AdapterTodoList(mContext, todoListResult, todayDate);
        todoListAdapter.setOnItemClickListener(new AdapterTodoList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, HelperTodoListObject obj, int position) {
                Log.d(TAG, "onItemClick - " + position);
            }
        });

        rv_main_helper_todolist.setAdapter(todoListAdapter);
    }


    @Override
    public void onPause() {
        super.onPause();
    }
}