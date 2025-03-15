package com.maneger.appbanhang.model.EventBus;

import com.maneger.appbanhang.model.SanPhamMoi;

public class SuaXoaEven {
    SanPhamMoi sanPhamMoi;

    public SuaXoaEven(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }

    public SanPhamMoi getSanPhamMoi() {
        return sanPhamMoi;
    }

    public void setSanPhamMoi(SanPhamMoi sanPhamMoi) {
        this.sanPhamMoi = sanPhamMoi;
    }
}

