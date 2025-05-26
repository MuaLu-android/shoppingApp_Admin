package com.maneger.appbanhang.retrofit;
import com.maneger.appbanhang.model.DonhangModels;
import com.maneger.appbanhang.model.LoaiSpModel;
import com.maneger.appbanhang.model.MessageModel;
import com.maneger.appbanhang.model.SanPhamMoiModel;
import com.maneger.appbanhang.model.UserModel;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
public interface ApiBanHang {
    @GET("getloaisp1.php")
    Observable<LoaiSpModel> getLoaiSp();
    @GET("getspmoi1.php")
    Observable<SanPhamMoiModel> getSpMoi();
    @POST("chitiet1.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> getSanPham(
            @Field("page") int page,
            @Field("loai") int loai
    );
    @POST("dangki1.php")
    @FormUrlEncoded
    Observable<UserModel> dangki(
            @Field("email") String email,
            @Field("pass") String pass,
            @Field("username") String username,
            @Field("mobile") String mobile,
            @Field("uid") String uid
    );
    @POST("dangnhap1.php")
    @FormUrlEncoded
    Observable<UserModel> dangnhap(
            @Field("email") String email,
            @Field("pass") String pass
    );
    @POST("donhang1.php")
    @FormUrlEncoded
    Observable<UserModel> creatOder(
            @Field("sdt") String sdt,
            @Field("email") String eamil,
            @Field("tongtien") String tongtien,
            @Field("iduser") int iduser,
            @Field("diachi") String diachi,
            @Field("soluong") int soluong,
            @Field("chitiet") String chitiet
    );
    @POST("xemdonhang1.php")
    @FormUrlEncoded
    Observable<DonhangModels> xemDonhang(
            @Field("iduser") int id
    );
    @POST("timkiem1.php")
    @FormUrlEncoded
    Observable<SanPhamMoiModel> search(
            @Field("search") String search
    );
    @POST("xoa1.php")
    @FormUrlEncoded
    Observable<MessageModel> xoaSanPham(
            @Field("id") int id
    );
    @POST("insertsp1.php")
    @FormUrlEncoded
    Observable<MessageModel> insertSp(
            @Field("tensp") String tensp,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int loai
    );
    @POST("updatesp1.php")
    @FormUrlEncoded
    Observable<MessageModel> updatesp(
            @Field("tensp") String tensp,
            @Field("giasp") String giasp,
            @Field("hinhanh") String hinhanh,
            @Field("mota") String mota,
            @Field("loai") int idloai,
            @Field("id") int id
    );
    @POST("updatetoken1.php")
    @FormUrlEncoded
    Observable<MessageModel> updateToken(
            @Field("id") int id,
            @Field("token") String token
    );
    @POST("upload1.php")
    Call<MessageModel> uploadFile(
            @Part MultipartBody.Part file);
    @POST("updatedonhang1.php")
    @FormUrlEncoded
    Observable<MessageModel> updatedonhang(
            @Field("id") int id,
            @Field("trangthai") int trangthai
    );
    @POST("gettoken1.php")
    @FormUrlEncoded
    Observable<UserModel> gettoken(
            @Field("status") int status,
            @Field("iduser") int iduser
    );
}
