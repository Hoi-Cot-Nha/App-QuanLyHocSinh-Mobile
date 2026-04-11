package com.example.quanlyhocsinhmobile.data.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "HocPhi",
        foreignKeys = {@ForeignKey(entity = HocSinh.class,
                parentColumns = "MaHS",
                childColumns = "maHS",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index("maHS")})
public class HocPhi {
    @PrimaryKey(autoGenerate = true)
    private int maHP;
    private String maHS;
    private int hocKy;
    private String namHoc;
    private Double tongTien;
    private Double mienGiam;
    private Double phaiDong;
    private String trangThai;

    public HocPhi() {}

    public int getMaHP() { return maHP; }
    public void setMaHP(int maHP) { this.maHP = maHP; }

    public String getMaHS() { return maHS; }
    public void setMaHS(String maHS) { this.maHS = maHS; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    public String getNamHoc() { return namHoc; }
    public void setNamHoc(String namHoc) { this.namHoc = namHoc; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public Double getMienGiam() { return mienGiam; }
    public void setMienGiam(Double mienGiam) { this.mienGiam = mienGiam; }

    public Double getPhaiDong() { return phaiDong; }
    public void setPhaiDong(Double phaiDong) { this.phaiDong = phaiDong; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
