package com.example.quanlyhocsinhmobile.data.Model;

import androidx.room.Embedded;

public class LichThiDisplay {
    @Embedded
    private LichThi lichThi;
    private String tenMH;
    private String tenPhong;

    public LichThiDisplay() {}

    public LichThi getLichThi() { return lichThi; }
    public void setLichThi(LichThi lichThi) { this.lichThi = lichThi; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }
}
