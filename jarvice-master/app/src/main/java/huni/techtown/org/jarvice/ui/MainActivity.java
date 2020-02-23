package huni.techtown.org.jarvice.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.common.DatabaseManager;
import huni.techtown.org.jarvice.common.data.SalesObject;
import huni.techtown.org.jarvice.component.Tools;
import huni.techtown.org.jarvice.database.TBL_MY_SALES;
import huni.techtown.org.jarvice.ui.Fragment.TabLayoutAnalysis;
import huni.techtown.org.jarvice.ui.Fragment.TabLayoutDeadline;
import huni.techtown.org.jarvice.ui.Fragment.TabLayoutHelper;
import huni.techtown.org.jarvice.ui.Fragment.TabLayoutHome;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Context mContext;

    private TBL_MY_SALES tblMySales;
    private Button test;

    private CoordinatorLayout clParentView;
    private Toolbar toolbar;

    private ViewPager view_pager;
    private TabLayout main_tab_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tblMySales = DatabaseManager.getInstance(mContext).getMySales();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        int thisWeek = getWeekOfYear(sdf.format(new Date()));

        Log.d(TAG, "123:  " + thisWeek);

        clParentView = (CoordinatorLayout) findViewById(R.id.cl_parent_view);

        initToolbar();
        initComponent();
        setToolBarTitleClick();

        ArrayList<SalesObject> test = new ArrayList<SalesObject>();
        test.addAll(DatabaseManager.getInstance(mContext).getChannelHistory("2019-04-12","tt"));


        int sum = 0;
        Log.e(TAG, "length : " + test.size());
        for (int i = 0; i < test.size(); i++) {
            Log.d(TAG, "test : " + test.get(i).getSell());
            String result = "";
            if (test.get(i).getSell().contains(",")) {
                result = test.get(i).getSell().replace(",", "");
            } else {
                result = test.get(i).getSell();
            }
            sum += Integer.parseInt(result);
        }

        Log.e(TAG, "sum check : " + sum);

        Log.e(TAG, "" + DatabaseManager.getInstance(mContext).getSumCount("2019-04-12"));

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.main_app_name);
        //뒤로가기 버튼 보여주기 (false -> gone)
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Tools.setSystemBarColor(this);
        invalidateOptionsMenu();

    }

    private void initComponent() {
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        setupViewPager(view_pager);

        main_tab_layout = (TabLayout) findViewById(R.id.main_tab_layout);
        main_tab_layout.setupWithViewPager(view_pager);
    }

    //TODO toobar 타이틀만 onclick 하는 방법이 없어서 view 로 페이크
    //https://stackoverflow.com/questions/33287190/make-toolbar-title-clickable-and-respond-to-it?noredirect=1&lq=1
    private void setToolBarTitleClick() {
        toolbar.findViewById(R.id.tv_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_pager.setCurrentItem(0);
            }
        });

        getSupportActionBar().setTitle(null);
    }


    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(TabLayoutHome.newInstance(), getString(R.string.main_tab_home));
        adapter.addFragment(TabLayoutDeadline.newInstance(), getString(R.string.main_tab_deadline));
        adapter.addFragment(TabLayoutHelper.newInstance(), getString(R.string.main_tab_shop_assistant));
        adapter.addFragment(TabLayoutAnalysis.newInstance(), getString(R.string.main_tab_shop_analysis));
        viewPager.setAdapter(adapter);
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.test2 :
//                List<SalesObject> list = tblMySales.getList();
//                if (list != null) {
//                    for (SalesObject check : list) {
//                        Log.d(TAG, "company: " + check.toString());
//
//
//                    }
//                }
//                break;
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            this.moveTaskToBack(true);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private int getWeekOfYear(String date) {
        Calendar calendar = Calendar.getInstance();
        String[] dates = date.split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        calendar.set(year, month - 1, day);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

}
