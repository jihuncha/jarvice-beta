package huni.techtown.org.jarvice.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import huni.techtown.org.jarvice.R;

public class TabLayoutHelper extends Fragment {
    private static final String TAG = TabLayoutHelper.class.getSimpleName();

    public TabLayoutHelper() {
    }

    public static TabLayoutHelper newInstance() {
        TabLayoutHelper fragment = new TabLayoutHelper();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.tab_main_helper, container, false);
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
}