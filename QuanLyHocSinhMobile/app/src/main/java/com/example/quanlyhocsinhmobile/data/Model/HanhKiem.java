package com.example.quanlyhocsinhmobile.data.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Embedded;

@Entity(tableName = "HanhKiem",
        primaryKeys = {"maHS", "hocKy", "namHoc"},
        foreignKeys = {@ForeignKey(entity = HocSinh.class,
                parentColumns = "MaHS",
                childColumns = "maHS",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)})
public class HanhKiem {
    @NonNull
    private String maHS;
    @NonNull
    private int hocKy;
    @NonNull
    private String namHoc;
    private String xepLoai;
    private String nhanXet;

    public HanhKiem() {}

    @NonNull
    public String getMaHS() { return maHS; }
    public void setMaHS(@NonNull String maHS) { this.maHS = maHS; }

    public int getHocKy() { return hocKy; }
    public void setHocKy(int hocKy) { this.hocKy = hocKy; }

    @NonNull
    public String getNamHoc() { return namHoc; }
    public void setNamHoc(@NonNull String namHoc) { this.namHoc = namHoc; }

    public String getXepLoai() { return xepLoai; }
    public void setXepLoai(String xepLoai) { this.xepLoai = xepLoai; }

    public String getNhanXet() { return nhanXet; }
    public void setNhanXet(String nhanXet) { this.nhanXet = nhanXet; }

    public static class Display {
        @Embedded
        private HanhKiem hanhKiem;
        private String tenHS;
        private String tenLop;

        public Display() {}

        public HanhKiem getHanhKiem() { return hanhKiem; }
        public void setHanhKiem(HanhKiem hanhKiem) { this.hanhKiem = hanhKiem; }

        public String getTenHS() { return tenHS; }
        public void setTenHS(String tenHS) { this.tenHS = tenHS; }

        public String getTenLop() { return tenLop; }
        public void setTenLop(String tenLop) { this.tenLop = tenLop; }
    }
}
