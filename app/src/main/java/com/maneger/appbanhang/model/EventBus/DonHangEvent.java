package com.maneger.appbanhang.model.EventBus;

import com.maneger.appbanhang.model.Donhang;

public class DonHangEvent {
    Donhang donhang;

    public DonHangEvent(Donhang donhang) {
        this.donhang = donhang;
    }

    public Donhang getDonhang() {
        return donhang;
    }

    public void setDonhang(Donhang donhang) {
        this.donhang = donhang;
    }
}
