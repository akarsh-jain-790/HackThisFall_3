package com.example.shoparoundsup.adapters;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoparoundsup.R;
import com.example.shoparoundsup.model.InventoryItemModel;

import java.util.List;

public class AddItemRVAdapter extends RecyclerView.Adapter<AddItemRVAdapter.myViewHolder> {

    List<InventoryItemModel> itemList;

    public AddItemRVAdapter(List<InventoryItemModel> passedListItem){
        this.itemList = passedListItem;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inventory_item_view, parent, false);

        myViewHolder holder = new myViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(myViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.itemName.setText(itemList.get(position).getName());
        holder.itemPrice.setText(itemList.get(position).getPrice());
        holder.image.setImageURI(Uri.parse(itemList.get(position).getItemImage()));
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAt(position);
            }
        });

    }

    private void removeAt(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, removeBtn;
        ImageView image;
        public myViewHolder(View view){
            super(view);
            itemName = view.findViewById(R.id.itemName);
            itemPrice = view.findViewById(R.id.itemPrice);
            removeBtn = view.findViewById(R.id.removeBtn);
            image = view.findViewById(R.id.itemImage);
        }
    }
}

