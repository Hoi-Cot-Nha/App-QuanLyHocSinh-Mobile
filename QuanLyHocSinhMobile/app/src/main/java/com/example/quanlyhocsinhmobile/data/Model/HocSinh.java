package com.example.quanlyhocsinhmobile.data.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "HocSinh",
        foreignKeys = {
                @ForeignKey(entity = Lop.class, parentColumns = "MaLop", childColumns = "MaLop", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = DoiTuongUuTien.class, parentColumns = "maDT", childColumns = "MaDT", onDelete = ForeignKey.SET_NULL, onUpdate = ForeignKey.CASCADE)
        },
        indices = {@Index("MaLop"), @Index("MaDT")})
public class HocSinh {
    @PrimaryKey
    @NonNull
    @androidx.room.ColumnInfo(name = "MaHS")
    private String maHS;

    @androidx.room.ColumnInfo(name = "HoTen")
    private String hoTen;

    @androidx.room.ColumnInfo(name = "NgaySinh")
    private String ngaySinh;

    @androidx.room.ColumnInfo(name = "GioiTinh")
    private String gioiTinh;

    @androidx.room.ColumnInfo(name = "DiaChi")
    private String diaChi;

    @androidx.room.ColumnInfo(name = "MaLop")
    private String maLop;

    @androidx.room.ColumnInfo(name = "MaDT")
    private String maDT;

    public HocSinh() {}

    @NonNull
    public String getMaHS() {
        return maHS;
    }

    public void setMaHS(@NonNull String maHS) {
        this.maHS = maHS;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getMaLop() {
        return maLop;
    }

    public void setMaLop(String maLop) {
        this.maLop = maLop;
    }

    public String getMaDT() {
        return maDT;
    }

    public void setMaDT(String maDT) {
        this.maDT = maDT;
    }
}
