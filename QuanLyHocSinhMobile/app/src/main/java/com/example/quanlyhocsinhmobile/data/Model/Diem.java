package com.example.quanlyhocsinhmobile.data.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(tableName = "Diem",
        primaryKeys = {"maHS", "maMH", "hocKy"},
        foreignKeys = {
                @ForeignKey(entity = HocSinh.class, parentColumns = "MaHS", childColumns = "maHS", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = MonHoc.class, parentColumns = "MaMH", childColumns = "maMH", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        },
        indices = {@Index("maMH")})
public class Diem {
    @NonNull
    private String maHS;

    @NonNull
    private String maMH;

    @NonNull
    private int hocKy;

    private Double diem15p;  
    private Double diem1Tiet;
    private Double diemGiuaKy;
    private Double diemCuoiKy;    
    private Double diemTongKet;

    public Diem() {}

    @Ignore
    public Diem(String maHS, String maMH, int hocKy, Double diem15p, Double diem1Tiet, Double diemGiuaKy, Double diemCuoiKy) {
        this.maHS = maHS;
        this.maMH = maMH;
        this.hocKy = hocKy;
        this.diem15p = diem15p;
        this.diem1Tiet = diem1Tiet;
        this.diemGiuaKy = diemGiuaKy;
        this.diemCuoiKy = diemCuoiKy;
    }

    @NonNull
    public String getMaHS() { return maHS; }
    public void setMaHS(@NonNull String maHS) { this.maHS = maHS; }

    @NonNull
    public String getMaMH() { return maMH; }
    public void setMaMH(@NonNull String maMH) { this.maMH = maMH; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public Double getDiem15p() { return diem15p; }
    public void setDiem15p(Double diem15p) { this.diem15p = diem15p; }

    public Double getDiem1Tiet() { return diem1Tiet; }
    public void setDiem1Tiet(Double diem1Tiet) { this.diem1Tiet = diem1Tiet; }

    public Double getDiemGiuaKy() { return diemGiuaKy; }
    public void setDiemGiuaKy(Double diemGiuaKy) { this.diemGiuaKy = diemGiuaKy; }

    public Double getDiemCuoiKy() { return diemCuoiKy; }
    public void setDiemCuoiKy(Double diemCuoiKy) { this.diemCuoiKy = diemCuoiKy; }

    public Double getDiemTongKet() { return diemTongKet; }
    public void setDiemTongKet(Double diemTongKet) { this.diemTongKet = diemTongKet; }

    public double calculateDiemTongKet() {
        double d15 = (diem15p != null) ? diem15p : 0;
        double d1t = (diem1Tiet != null) ? diem1Tiet : 0;
        double dgk = (diemGiuaKy != null) ? diemGiuaKy : 0;
        double dck = (diemCuoiKy != null) ? diemCuoiKy : 0;
        return (d15 + d1t + dgk * 2 + dck * 3) / 7.0;
    }
}
