package com.example.quanlyhocsinhmobile.data.local.Model;
import androidx.annotation.NonNull;
import androidx.room.*;
@Entity(
        tableName = "GiaoVien",
        foreignKeys = {
                @ForeignKey(
                        entity = ToHopMon.class,
                        parentColumns = "maToHop",
                        childColumns = "maToHop",
                        onDelete = ForeignKey.SET_NULL,
                        onUpdate = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = MonHoc.class,
                        parentColumns = "MaMH", 
                        childColumns = "MaMH",  
                        onDelete = ForeignKey.SET_NULL,
                        onUpdate = ForeignKey.CASCADE
                )
        },
        indices = {@Index("maToHop"), @Index("MaMH")} 
)
public class GiaoVien {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "maGV")
    private String maGV;
    @NonNull
    @ColumnInfo(name = "hoTen")
    private String hoTen;
    private String ngaySinh;
    private String sdt;
    @ColumnInfo(name = "maToHop")
    private String maToHop;
    @ColumnInfo(name = "MaMH") 
    private String maMH;
    public GiaoVien(@NonNull String maGV, @NonNull String hoTen,
                    String ngaySinh, String sdt,
                    String maToHop, String maMH) {
        this.maGV = maGV;
        this.hoTen = hoTen;
        this.ngaySinh = ngaySinh;
        this.sdt = sdt;
        this.maToHop = maToHop; 
        this.maMH = maMH;       
    }
    public GiaoVien() {}
    public String getMaGV() { return maGV; }
    public void setMaGV(String maGV) { this.maGV = maGV; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(String ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getMaToHop() { return maToHop; }
    public void setMaToHop(String maToHop) { this.maToHop = maToHop; }
    public String getMaMH() { return maMH; }
    public void setMaMH(String maMH) { this.maMH = maMH; }
    public static class Display {
        @Embedded
        public GiaoVien giaoVien;
        @ColumnInfo(name = "TenMH") 
        public String tenMH;
        @ColumnInfo(name = "tenToHop") 
        public String tenToHop;
        public GiaoVien getGiaoVien() {
            return giaoVien;
        }
        public String getTenMH() {
            return tenMH;
        }
        public String getTenToHop() {
            return tenToHop;
        }
    }
}