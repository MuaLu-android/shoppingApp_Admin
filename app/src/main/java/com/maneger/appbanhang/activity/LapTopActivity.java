package com.maneger.appbanhang.activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.maneger.appbanhang.R;
import com.maneger.appbanhang.adapter.DienThoaiAdapter;
import com.maneger.appbanhang.model.SanPhamMoi;
import com.maneger.appbanhang.retrofit.ApiBanHang;
import com.maneger.appbanhang.retrofit.RetrofitClient;
import com.maneger.appbanhang.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
public class LapTopActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page = 1;
    int loai;
    DienThoaiAdapter adapter;
    List<SanPhamMoi> sanPhamMoilist;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isloading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lap_top);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai", 1);
        AnhXa();
        ActoinToolBar();
        getData(page);
        addEvenload();
    }
    private void addEvenload() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isloading == false){
                    if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanPhamMoilist.size()-1){
                        isloading = true;
                        loadMore();
                    }
                }
            }
        });
    }
    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanPhamMoilist.add(null);
                adapter.notifyItemInserted(sanPhamMoilist.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sanPhamMoilist.remove(sanPhamMoilist.size()-1);
                adapter.notifyItemRemoved(sanPhamMoilist.size());
                page = page+1;
                getData(page);
                adapter.notifyDataSetChanged();
                isloading = false;
            }
        }, 2000);
    }
    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getSanPham(page, loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()){
                                if (adapter == null){
                                    sanPhamMoilist = sanPhamMoiModel.getResult();
                                    adapter = new DienThoaiAdapter(getApplicationContext(), sanPhamMoilist);
                                    recyclerView.setAdapter(adapter);
                                }else{
                                    int vitri = sanPhamMoilist.size()-1;
                                    int soluong = sanPhamMoiModel.getResult().size();
                                    for (int i= 0; i<soluong; i++){
                                        sanPhamMoilist.add(sanPhamMoiModel.getResult().get(i));
                                    }
                                    adapter.notifyItemRangeChanged(vitri,soluong);
                                }
                            }else {
                                Toast.makeText(getApplicationContext(), "Het du lieu", Toast.LENGTH_LONG).show();
                                isloading = true;
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "khong co internet", Toast.LENGTH_SHORT).show();
                        }
                ));
    }
    private void ActoinToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void AnhXa() {
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycerview_dt);
        linearLayoutManager = new LinearLayoutManager(this, linearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        sanPhamMoilist = new ArrayList<>();
    }
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}