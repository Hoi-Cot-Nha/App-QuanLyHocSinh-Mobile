package com.example.quanlyhocsinhmobile.data.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "LichThi",
        foreignKeys = {
                @ForeignKey(entity = MonHoc.class, parentColumns = "MaMH", childColumns = "maMH", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = PhongHoc.class, parentColumns = "maPhong", childColumns = "maPhong", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)
        },
        indices = {@Index("maMH"), @Index("maPhong")})
public class LichThi {
    @PrimaryKey(autoGenerate = true)
    private int maLT;
    private String tenKyThi;
    private String maMH;
    private String ngayThi;
    private String gioBatDau;
    private String gioKetThuc;
    private String maPhong;

    public LichThi() {}

    public int getMaLT() { return maLT; }
    public void setMaLT(int maLT) { this.maLT = maLT; }

    public String getTenKyThi() { return tenKyThi; }
    public void setTenKyThi(String tenKyThi) { this.tenKyThi = tenKyThi; }

    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }

    public String getNgayThi() { return ngayThi; }
    public void setNgayThi(String ngayThi) { this.ngayThi = ngayThi; }

    public String getGioBatDau() { return gioBatDau; }
    public void setGioBatDau(String gioBatDau) { this.gioBatDau = gioBatDau; }

    public String getGioKetThuc() { return gioKetThuc; }
    public void setGioKetThuc(String gioKetThuc) { this.gioKetThuc = gioKetThuc; }

    public String getMaPhong() { return maPhong; }
    public void setMaPhong(String maPhong) { this.maPhong = maPhong; }
}
