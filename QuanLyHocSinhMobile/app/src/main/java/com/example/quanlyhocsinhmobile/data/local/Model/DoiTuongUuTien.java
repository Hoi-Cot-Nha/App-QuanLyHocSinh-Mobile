package com.example.quanlyhocsinhmobile.data.local.Model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "DoiTuongUuTien")
public class DoiTuongUuTien {
    @PrimaryKey
    @NonNull
    private String maDT;
    private String tenDT;
    private Double tiLeGiamHocPhi;
    public DoiTuongUuTien() {}
    @NonNull
    public String getMaDT() { return maDT; }
    public void setMaDT(@NonNull String maDT) { this.maDT = maDT; }
    public String getTenDT() { return tenDT; }
    public void setTenDT(String tenDT) { this.tenDT = tenDT; }
    public Double getTiLeGiamHocPhi() { return tiLeGiamHocPhi; }
    public void setTiLeGiamHocPhi(Double tiLeGiamHocPhi) { this.tiLeGiamHocPhi = tiLeGiamHocPhi; }
}