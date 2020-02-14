package huni.techtown.org.jarvice.ui.Fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import huni.techtown.org.jarvice.R;

public class TabLayoutHome extends Fragment {
    private static final String TAG = TabLayoutHome.class.getSimpleName();

    public TabLayoutHome() {
    }

    public static TabLayoutHome newInstance() {
        TabLayoutHome fragment = new TabLayoutHome();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_main_home, container, false);
//
//
//        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));


//        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(getActivity(), 8), true));
//        recyclerView.setHasFixedSize(true);
//
//        List<ShopProduct> items = DataGenerator.getShoppingProduct(getActivity());
//        Collections.shuffle(items);
//
//        //set data and list adapter
//        AdapterGridShopProductCard mAdapter = new AdapterGridShopProductCard(getActivity(), items);
//        recyclerView.setAdapter(mAdapter);
//
//        // on item list clicked
//        mAdapter.setOnItemClickListener(new AdapterGridShopProductCard.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, ShopProduct obj, int position) {
//                Snackbar.make(root, "Item " + obj.title + " clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });
//
//        mAdapter.setOnMoreButtonClickListener(new AdapterGridShopProductCard.OnMoreButtonClickListener() {
//            @Override
//            public void onItemClick(View view, ShopProduct obj, MenuItem item) {
//                Snackbar.make(root, obj.title + " (" + item.getTitle() + ") clicked", Snackbar.LENGTH_SHORT).show();
//            }
//        });

        return root;
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
}