package com.example.quanlyhocsinhmobile.data.Model;

import androidx.room.Embedded;

public class DiemDisplay {
    @Embedded
    private Diem diem;
    private String tenHS;
    private String tenMH;
    private String tenLop;

    public DiemDisplay() {}

    public Diem getDiem() { return diem; }
    public void setDiem(Diem diem) { this.diem = diem; }

    public String getTenHS() { return tenHS; }
    public void setTenHS(String tenHS) { this.tenHS = tenHS; }

    public String getTenMH() { return tenMH; }
    public void setTenMH(String tenMH) { this.tenMH = tenMH; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }
}
