package com.example.quanlyhocsinhmobile.data.local.Model;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
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
    public static class Display {
        @Embedded
        private HocPhi hocPhi;
        private String tenHS;
        private String tenLop;
        public Display() {}
        @Ignore
        public Display(HocPhi hocPhi, String tenHS, String tenLop) {
            this.hocPhi = hocPhi;
            this.tenHS = tenHS;
            this.tenLop = tenLop;
        }
        public HocPhi getHocPhi() { return hocPhi; }
        public void setHocPhi(HocPhi hocPhi) { this.hocPhi = hocPhi; }
        public String getTenHS() { return tenHS; }
        public void setTenHS(String tenHS) { this.tenHS = tenHS; }
        public String getTenLop() { return tenLop; }
        public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    }
}