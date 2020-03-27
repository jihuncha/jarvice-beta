package huni.techtown.org.jarvice.ui.Fragment;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import huni.techtown.org.jarvice.R;
import huni.techtown.org.jarvice.ui.MainActivity;
import huni.techtown.org.jarvice.ui.utils.Tools;

/**
* HOME 화면 구현
*
* */
public class TabLayoutHome extends Fragment implements View.OnClickListener {
    private static final String TAG = TabLayoutHome.class.getSimpleName();

    private Context mContext;

    //하단 cardView 의 아래 dots 사용을위해
    private static final int MAX_STEP = 3;

    private ViewPager mainFunctionViewPager;
    private MyViewPagerAdapter myViewPagerAdapter;

    private LinearLayout dotsLayout;

    //View 관련
    private TextView tvMainHomeFunctionFirstTitle;
    private TextView tvMainHomeFunctionSecondTitle;
    private TextView tvMainHomeFunctionThirdTitle;

    private EditText etMainHomeCbtInput;

    //하단 뷰페이저 위치
    public static int MAIN_FUNCTION_POSITION = 0;

    private String about_title_array[] = new String[3];
    private String about_description_array[] = new String[3];

    private int bg_images_array[] = {
            R.drawable.image_1,
            R.drawable.image_2,
            R.drawable.image_3
    };

    public TabLayoutHome() {
    }

    public static TabLayoutHome newInstance() {
        TabLayoutHome fragment = new TabLayoutHome();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_main_home, container, false);

        mContext = container.getContext();

        tvMainHomeFunctionFirstTitle = (TextView) root.findViewById(R.id.tv_main_home_function_first_title);
        tvMainHomeFunctionSecondTitle = (TextView) root.findViewById(R.id.tv_main_home_function_second_title);
        tvMainHomeFunctionThirdTitle = (TextView) root.findViewById(R.id.tv_main_home_function_third_title);

        //하단 카드뷰 부분
        //TODO 프래그먼트는 구지다..
        about_title_array[0] = mContext.getString(R.string.main_home_function_tab_first);
        about_title_array[1] = mContext.getString(R.string.main_home_function_tab_second);
        about_title_array[2] = mContext.getString(R.string.main_home_function_tab_third);

        about_description_array[0] =  mContext.getString(R.string.main_home_function_tab_first_detail);
        about_description_array[1] =  mContext.getString(R.string.main_home_function_tab_second_detail);
        about_description_array[2] =  mContext.getString(R.string.main_home_function_tab_third_detail);

        mainFunctionViewPager = (ViewPager) root.findViewById(R.id.tab_home_cardview_viewpager);
        dotsLayout = (LinearLayout) root.findViewById(R.id.layoutDots);

        myViewPagerAdapter = new MyViewPagerAdapter();
        mainFunctionViewPager.setAdapter(myViewPagerAdapter);
        mainFunctionViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        Log.d(TAG, "mainFunctionViewPager - position : " + mainFunctionViewPager.getCurrentItem());
        MAIN_FUNCTION_POSITION = mainFunctionViewPager.getCurrentItem();
        switch (MAIN_FUNCTION_POSITION) {
            case 0 :
                tvMainHomeFunctionFirstTitle.setTextColor(getResources().getColor(R.color.color_ffffff));
                tvMainHomeFunctionFirstTitle.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                tvMainHomeFunctionFirstTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));

                break;
            case 1:
                tvMainHomeFunctionSecondTitle.setTextColor(getResources().getColor(R.color.color_ffffff));
                tvMainHomeFunctionSecondTitle.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                tvMainHomeFunctionSecondTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));

                break;
            case 2:
                tvMainHomeFunctionThirdTitle.setTextColor(getResources().getColor(R.color.color_ffffff));
                tvMainHomeFunctionThirdTitle.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                tvMainHomeFunctionThirdTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));

                break;
        }

        tvMainHomeFunctionFirstTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainFunctionViewPager.setCurrentItem(0);
            }
        });

        tvMainHomeFunctionSecondTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainFunctionViewPager.setCurrentItem(1);
            }
        });

        tvMainHomeFunctionThirdTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainFunctionViewPager.setCurrentItem(2);
            }
        });

        etMainHomeCbtInput = (EditText) root.findViewById(R.id.et_main_home_cbt_input);
        etMainHomeCbtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    etMainHomeCbtInput.setPadding(0, Tools.dpToPx(mContext, 6),0,Tools.dpToPx(mContext, 9));
                } else {
                    etMainHomeCbtInput.setPadding(Tools.dpToPx(mContext, 38),Tools.dpToPx(mContext, 6),Tools.dpToPx(mContext, 38),Tools.dpToPx(mContext, 9));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO view가 완성된 이후에 동작한다..
        bottomProgressDots(0);
    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) getView().findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(getActivity());
            //가로세로 길이를 dp 로 변경해주는 로직
            int length = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8, getResources().getDisplayMetrics());
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6, getResources().getDisplayMetrics());

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(length, length));
            params.setMargins(margin, 0, margin, 0);

            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.color_d8d8d8), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.color_4263ff), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

    }

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_tab_home_cardview, container, false);
            ((TextView) view.findViewById(R.id.tv_cardview_title)).setText(about_title_array[position]);
            ((TextView) view.findViewById(R.id.tv_cardview_description)).setText(about_description_array[position]);
            ((ImageView) view.findViewById(R.id.ig_cardview_img)).setImageResource(bg_images_array[position]);

            ((ImageView) view.findViewById(R.id.ig_cardview_img)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "MyViewPagerAdapter - onClick position : " + position);

                    switch (position) {
                        case 0 :
                            ((MainActivity)getActivity()).setPagerFragment(1);
                            break;
                        case 1:
                            ((MainActivity)getActivity()).setPagerFragment(2);
                            break;
                        case 2:
                            ((MainActivity)getActivity()).setPagerFragment(3);
                            break;
                    }
                }
            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return super.getItemPosition(object);
        }


    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            Log.d(TAG, "onPageSelected : " + position);

            //TODO background가 selector로 안바뀜..??
            switch (position) {
                case 0 :
                    tvMainHomeFunctionFirstTitle.setTextColor(getResources().getColor(R.color.color_ffffff));
                    tvMainHomeFunctionFirstTitle.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                    tvMainHomeFunctionFirstTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));


                    tvMainHomeFunctionSecondTitle.setTextColor(getResources().getColor(R.color.color_707594));
                    tvMainHomeFunctionSecondTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                    tvMainHomeFunctionSecondTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));

                    tvMainHomeFunctionThirdTitle.setTextColor(getResources().getColor(R.color.color_707594));
                    tvMainHomeFunctionThirdTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                    tvMainHomeFunctionThirdTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));

                    break;
                case 1 :
                    tvMainHomeFunctionSecondTitle.setTextColor(getResources().getColor(R.color.color_ffffff));
                    tvMainHomeFunctionSecondTitle.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                    tvMainHomeFunctionSecondTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));

                    tvMainHomeFunctionFirstTitle.setTextColor(getResources().getColor(R.color.color_707594));
                    tvMainHomeFunctionFirstTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                    tvMainHomeFunctionFirstTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));

                    tvMainHomeFunctionThirdTitle.setTextColor(getResources().getColor(R.color.color_707594));
                    tvMainHomeFunctionThirdTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                    tvMainHomeFunctionThirdTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));

                    break;
                case 2 :
                    tvMainHomeFunctionThirdTitle.setTextColor(getResources().getColor(R.color.color_ffffff));
                    tvMainHomeFunctionThirdTitle.setBackground(getResources().getDrawable(R.drawable.circular_focus));
                    tvMainHomeFunctionThirdTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_bold));

                    tvMainHomeFunctionFirstTitle.setTextColor(getResources().getColor(R.color.color_707594));
                    tvMainHomeFunctionFirstTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                    tvMainHomeFunctionFirstTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));

                    tvMainHomeFunctionSecondTitle.setTextColor(getResources().getColor(R.color.color_707594));
                    tvMainHomeFunctionSecondTitle.setBackground(getResources().getDrawable(R.drawable.circular_regular));
                    tvMainHomeFunctionSecondTitle.setTypeface(ResourcesCompat.getFont(mContext, R.font.notosans_regular));
                    break;
            }

            bottomProgressDots(position);

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };
}