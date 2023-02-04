package com.shoparound.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shoparound.R;
import com.shoparound.adapter.ShopAdapter;
import com.shoparound.adapter.SliderAdapter;
import com.shoparound.model.SliderModel;
import com.shoparound.model.VendorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import www.sanju.motiontoast.MotionToast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    public static List<VendorModel> vendorModelsObj = new ArrayList<>();
    RecyclerView recyclerView;
    ShopAdapter shopAdapter;

    private FirebaseFirestore db;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    ImageView shop_image_IV;
    TextView shop_name_TV, shop_category_TV, shop_rating_TV;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //  Checking Internet Connection Status
        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected == false) {
            MotionToast.Companion.darkColorToast(getActivity(),
                    "No Internet Connection!",
                    "Please check your Internet connection and try again.",
                    MotionToast.TOAST_NO_INTERNET,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(getContext(),R.font.regular));
        }
        else{
            // Initializing recyclerView for ShopAdapter
            recyclerView = view.findViewById(R.id.shops_RV);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
            shopAdapter = new ShopAdapter(vendorModelsObj, getActivity());
            recyclerView.setAdapter(shopAdapter);
            vendorModelsObj.clear();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Vendors").document("First City").collection("Clothes")
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            List<DocumentSnapshot> snapshots = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : snapshots) {
                                VendorModel vendor = snapshot.toObject(VendorModel.class);
                                vendorModelsObj.add(vendor);
                            }
                            shopAdapter.notifyDataSetChanged();
                        }
                    });

            shop_image_IV = view.findViewById(R.id.shop_image_IV);
            shop_name_TV = view.findViewById(R.id.shop_name_TV);
            shop_category_TV = view.findViewById(R.id.shop_category_TV);
            shop_rating_TV = view.findViewById(R.id.shop_rating_TV);

            // Setting Slider
            viewPager2 = view.findViewById(R.id.viewPagerImageSlider);

            List<SliderModel> sliderItems = new ArrayList<>();
            sliderItems.add(new SliderModel(R.drawable.ic_verify_otp));
            sliderItems.add(new SliderModel(R.drawable.ic_login));

            viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));

            viewPager2.setClipToPadding(false);
            viewPager2.setClipChildren(false);
            viewPager2.setOffscreenPageLimit(3);
            viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

            CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
            compositePageTransformer.addTransformer(new MarginPageTransformer(40));
            compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                @Override
                public void transformPage(@NonNull View page, float position) {
                    float r = 1 - Math.abs(position);
                    page.setScaleY(0.85f + r * 0.15f);
                }
            });

            viewPager2.setPageTransformer(compositePageTransformer);

            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    sliderHandler.removeCallbacks(sliderRunnable);
                    sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
                }
            });
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }
}