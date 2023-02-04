package com.shoparound.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shoparound.R;
import com.shoparound.model.VendorModel;

import java.util.List;
import java.util.Objects;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder> {
    private List<VendorModel> localDataSet;
    private Context context;

    public ShopAdapter(List<VendorModel> vendors, Context context) {
        this.localDataSet = vendors;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        VendorModel vendor = localDataSet.get(position);
        holder.name.setText(vendor.getName());
        holder.interest.setText("Interest");
        holder.image.setImageURI(Uri.parse(vendor.getShopImage()));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView interest;
        private final ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shop_name_TV);
            interest = itemView.findViewById(R.id.shop_category_TV);
            image = itemView.findViewById(R.id.shop_image_IV);
        }
    }
}
