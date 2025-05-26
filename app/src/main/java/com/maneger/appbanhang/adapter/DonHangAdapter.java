package com.maneger.appbanhang.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.maneger.appbanhang.Interface.ImageClickListener;
import com.maneger.appbanhang.Interface.ItemClickListener;
import com.maneger.appbanhang.R;
import com.maneger.appbanhang.model.Donhang;
import com.maneger.appbanhang.model.EventBus.DonHangEvent;
import org.greenrobot.eventbus.EventBus;
import java.util.List;
public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<Donhang> listdonhang;
    public DonHangAdapter(Context context, List<Donhang> listdinhang) {
        this.context = context;
        this.listdonhang = listdinhang;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_donhang, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Donhang donhang = listdonhang.get(position);
        holder.txtdonhang.setText("Đơn hàng: "+ donhang.getId());
        holder.trangthai.setText(trangthaidon(donhang.getTrangthai() ));
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                holder.reChitiet.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(donhang.getItem().size());
        ChiTietAdapter chiTietAdapter = new ChiTietAdapter(context,donhang.getItem());
        holder.reChitiet.setLayoutManager(layoutManager);
        holder.reChitiet.setAdapter(chiTietAdapter);
        holder.reChitiet.setRecycledViewPool(viewPool);
        holder.setListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int pos, boolean islongClick) {
                if (islongClick){
                    EventBus.getDefault().postSticky(new DonHangEvent(donhang));
                }
            }
        });
    }
    private String trangthaidon(int statusu){
        String result = "Giao thành công";
        switch (statusu){
            case 0:
                result = "Dơn hàng đang đươc xử lí";
                break;
            case 1:
                result = "Đơn hàng đã xác nhận";
                break;
            case 2:
                result = "Dơn hàng đang trên đường vân chuyển";
                break;
            case 4:
                result = "Đã hủy";
                break;
            case 5:
                result = "";
                break;
        }
        return result;
    }
    @Override
    public int getItemCount() {
        return listdonhang.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView txtdonhang, trangthai;
        RecyclerView reChitiet;
        ItemClickListener listener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtdonhang = itemView.findViewById(R.id.item_donhang);
            trangthai = itemView.findViewById(R.id.tinhtrang);
            reChitiet = itemView.findViewById(R.id.item_donhang_recyclerview);
            itemView.setOnLongClickListener(this);
        }
        public void setListener(ItemClickListener listener) {
            this.listener = listener;
        }
        @Override
        public boolean onLongClick(View view) {
            listener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }
}
