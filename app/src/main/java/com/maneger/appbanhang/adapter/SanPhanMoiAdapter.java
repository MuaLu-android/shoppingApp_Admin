package com.maneger.appbanhang.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.maneger.appbanhang.Interface.ItemClickListener;
import com.maneger.appbanhang.R;
import com.maneger.appbanhang.activity.ChitietActivity;
import com.maneger.appbanhang.model.EventBus.SuaXoaEven;
import com.maneger.appbanhang.model.SanPhamMoi;
import com.maneger.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SanPhanMoiAdapter extends RecyclerView.Adapter<SanPhanMoiAdapter.MyViewHolder> {

    Context context;
    List<SanPhamMoi> array;

    public SanPhanMoiAdapter(Context context, List<SanPhamMoi> array) {
        this.context = context;
        this.array = array;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sp_moi, parent, false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SanPhamMoi sanPhamMoi = array.get(position);
        holder.txtten.setText(sanPhamMoi.getTensp());
        holder.txtgia.setText("Giá: "+sanPhamMoi.getGiasp()+ "VND");
        if (sanPhamMoi.getHinhanh().contains("http"))
        {
            Glide.with(context).load(sanPhamMoi.getHinhanh()).into(holder.imghinhanh);
        }else {
            String hinh = Utils.BASE_URL+ "images/"+sanPhamMoi.getHinhanh();
            Glide.with(context).load(hinh).into(holder.imghinhanh);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean islongClick) {
                if (!islongClick){
                    //Click
                    Intent intent = new Intent(context, ChitietActivity.class);
                    intent.putExtra("chitiet", sanPhamMoi);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    EventBus.getDefault().postSticky(new SuaXoaEven(sanPhamMoi));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, View.OnLongClickListener {
        TextView txtgia, txtten;
        ImageView imghinhanh;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtgia = itemView.findViewById(R.id.tensp_gia);
            txtten = itemView.findViewById(R.id.tensp_ten);
            imghinhanh = itemView.findViewById(R.id.tensp_image);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0,0, getAdapterPosition(), "sửa");
            contextMenu.add(0,0, getAdapterPosition(), "xóa");
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }
}
