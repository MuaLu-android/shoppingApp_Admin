package com.maneger.appbanhang.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.maneger.appbanhang.R;
import com.maneger.appbanhang.databinding.ActivityThemBinding;
import com.maneger.appbanhang.model.MessageModel;
import com.maneger.appbanhang.model.SanPhamMoi;
import com.maneger.appbanhang.retrofit.ApiBanHang;
import com.maneger.appbanhang.retrofit.RetrofitClient;
import com.maneger.appbanhang.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemActivity extends AppCompatActivity {
    Spinner spinner;
    int loai   = 0;
    ActivityThemBinding binding;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    String mediaPath;
    SanPhamMoi sanPhamSua;
    boolean flag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityThemBinding.inflate(getLayoutInflater());
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);
        setContentView(binding.getRoot());
        initView();
        initData();
        Intent intent = getIntent();
        sanPhamSua = (SanPhamMoi) intent.getSerializableExtra("Sua");
        if (sanPhamSua == null){
            //them moi
            flag = false;
        }else {
            //sua
            flag = true;
            binding.btnthemsp.setText("Sửa sản phẩm");
            //show data
            binding.motasp.setText(sanPhamSua.getMota());
            binding.giasp.setText(sanPhamSua.getGiasp());
            binding.tensp.setText(sanPhamSua.getTensp());
            binding.hinhanhsp.setText(sanPhamSua.getHinhanh());
            binding.spinnerLoai.setSelection(sanPhamSua.getLoai());
        }

    }

    private void initView() {
        spinner = findViewById(R.id.spinner_loai);
    }

    private void initData() {
        List<String> stringList = new ArrayList<>();
        stringList.add("Vui  lòng  chọn loại");
        stringList.add("Loại 1");
        stringList.add("Loại 2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.btnthemsp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag ==  false){
                    themsanpham();
                }else {
                    suaSanPham();
                }

            }
        });
        binding.imagehinhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.with(ThemActivity.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
    }

    private void suaSanPham() {
        String str_tensp = binding.tensp.getText().toString().trim();
        String str_giasp = binding.giasp.getText().toString().trim();
        String str_mota = binding.motasp.getText().toString().trim();
        String str_hinhanh = binding.hinhanhsp.getText().toString().trim();
        if (TextUtils.isEmpty(str_tensp)|| TextUtils.isEmpty(str_giasp)||   TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||loai == 0){
            Toast.makeText(getApplicationContext(), "Vui long  nhập đủ thông  tin", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.updatesp(str_tensp, str_giasp, str_hinhanh, str_mota, loai, sanPhamSua.getId())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                    ));

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();
            if (fileUri != null) {
                mediaPath = getMediaPath(fileUri); // Chuyển đổi Uri thành đường dẫn thực
                uploadMultipleFiles(); // Gọi phương thức tải lên tệp
                Log.d("log12", "onActivityResult: " + mediaPath);
            } else {
                Toast.makeText(getApplicationContext(), "Không tìm thấy hình ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Không có hình ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void themsanpham() {
        String str_tensp = binding.tensp.getText().toString().trim();
        String str_giasp = binding.giasp.getText().toString().trim();
        String str_mota = binding.motasp.getText().toString().trim();
        String str_hinhanh = binding.hinhanhsp.getText().toString().trim();
        if (TextUtils.isEmpty(str_tensp)|| TextUtils.isEmpty(str_giasp)||   TextUtils.isEmpty(str_hinhanh)||TextUtils.isEmpty(str_mota)||loai == 0){
            Toast.makeText(getApplicationContext(), "Vui long  nhập đủ thông  tin", Toast.LENGTH_SHORT).show();
        }else {
            compositeDisposable.add(apiBanHang.insertSp(str_tensp, str_giasp, str_hinhanh, str_mota, (loai))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            messageModel -> {
                                if (messageModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getApplicationContext(), messageModel.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            },
                            throwable -> {
                                    Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                             }
                    ));

    }
}
private String getMediaPath(Uri uri){
    String result;
    Cursor cursor = getContentResolver().query(uri, null, null, null, null);
    if (cursor == null){
        result = uri.getPath();
    }else
    {
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        result = cursor.getString(index);
        cursor.close();
    }
    return result;
}
private void uploadMultipleFiles() {
    File file = new File(mediaPath);
    RequestBody requestBody1 = RequestBody.create(MediaType.parse("*/*"), file);
    MultipartBody.Part fileToUpload1 = MultipartBody.Part.createFormData("file", file.getName(), requestBody1);
    Call<MessageModel> call = apiBanHang.uploadFile(fileToUpload1);
    call.enqueue(new Callback<MessageModel>() {
        @Override
        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
            MessageModel serverResponse = response.body();
            if (serverResponse != null) {
                if (serverResponse.isSuccess()) {
                    Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.v("Response", serverResponse.toString());
                }

        }

        @Override
        public void onFailure(Call<MessageModel> call, Throwable t) {
                Log.d("log", t.getMessage());
            }
    });
}
    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}