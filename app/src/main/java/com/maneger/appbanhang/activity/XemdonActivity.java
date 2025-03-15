package com.maneger.appbanhang.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maneger.appbanhang.R;
import com.maneger.appbanhang.adapter.DonHangAdapter;
import com.maneger.appbanhang.model.Donhang;
import com.maneger.appbanhang.model.EventBus.DonHangEvent;
import com.maneger.appbanhang.model.MesageData;
import com.maneger.appbanhang.model.Message;
import com.maneger.appbanhang.model.Notification;
import com.maneger.appbanhang.retrofit.ApiBanHang;
import com.maneger.appbanhang.retrofit.ApiPusNotification;
import com.maneger.appbanhang.retrofit.Authorization;
import com.maneger.appbanhang.retrofit.RetrofitClient;
import com.maneger.appbanhang.retrofit.RetrofitClientNoti;
import com.maneger.appbanhang.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;

public class XemdonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    RecyclerView redonhang;
    Toolbar toolbar1;
    Donhang donhang;
    int tinhtrang;
    AlertDialog dialog;
    AppCompatButton button1;
    Notification notification;
    OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_xemdon);

        initview();
        initToolbar();
        getOder();

    }

    private void getOder() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        compositeDisposable.add(apiBanHang.xemDonhang(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(donhangModels -> {
                    DonHangAdapter  adapter = new DonHangAdapter(getApplicationContext(), donhangModels.getResult());
                    redonhang.setAdapter(adapter);
                        },throwable -> {
                         Toast.makeText(getApplicationContext(), throwable.getMessage(),   Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    private void initToolbar() {
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }

    private void initview() {
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        redonhang = findViewById(R.id.recycerview_donhang);
        toolbar1 = findViewById(R.id.toolbar_donhang);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        redonhang.setLayoutManager(layoutManager);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void evenDonHang(DonHangEvent event){
        if (event != null){
            donhang = event.getDonhang();
            showCustumDialog();
        }
    }

    private void showCustumDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_donhang, null);
        Spinner spinner = view.findViewById(R.id.spinner_dialog);
        button1 = view.findViewById(R.id.dongy_dialog);
        List<String> list = new ArrayList<>();
        list.add("Đơn hàng đang được xử lí");
        list.add("Đơn hàng đã xác nhận");
        list.add("Đơn hàng đang trên đường vận chuyển");
        list.add("Giao thành công");
        list.add("Đã hủy");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, list);
        spinner.setAdapter(adapter);
        spinner.setSelection(donhang.getTrangthai());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tinhtrang = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CapNhatDonHang();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        dialog = builder.create();
        dialog.show();
    }

    private void CapNhatDonHang() {
        compositeDisposable.add(apiBanHang.updatedonhang(donhang.getId(), tinhtrang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(messageModel -> {
                            getOder();
                            PushNotiToUser();
                            dialog.dismiss();
                }, throwable -> {

                }
                ));
    }
    private void PushNotiToUser() {
        if (Utils.tokenSend != null){
            client = new OkHttpClient.Builder()
                    .addInterceptor(new Authorization(Utils.tokenSend))
                    .build();
        }
        //getToken
        compositeDisposable.add(apiBanHang.gettoken(0, donhang.getIduser())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userModel -> {
                            if (userModel.isSuccess()) {
                                for(int i =0; i<userModel.getResult().size(); i++){
                                    notification = new Notification("Thong bao",trangthaidon(tinhtrang));
                                    Message message = new Message(userModel.getResult().get(i).getToken(), notification);
                                    MesageData mesageData = new MesageData(message);
                                    ApiPusNotification apiPusNotification = RetrofitClientNoti.getInstance(client).create(ApiPusNotification.class);
                                    compositeDisposable.add(apiPusNotification.sendNofitication(mesageData)
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(notiResponse -> {
                                                    },throwable -> {
                                                        Log.d("logg",throwable.getMessage());
                                                    }
                                            ));
                                }

                            }
                        }, throwable -> {
                            Log.d("loog", throwable.getMessage());
                        }
                ));

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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}