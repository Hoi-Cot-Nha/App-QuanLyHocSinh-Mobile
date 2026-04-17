package com.example.quanlyhocsinhmobile.data.local.Model;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "TaiKhoan")
public class TaiKhoan {
    @PrimaryKey
    @NonNull
    private String tenDangNhap;
    @NonNull
    private String matKhau;
    private String quyen;
    private String maNguoiDung;
    public TaiKhoan() {}
    @NonNull
    public String getTenDangNhap() { return tenDangNhap; }
    public void setTenDangNhap(@NonNull String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    @NonNull
    public String getMatKhau() { return matKhau; }
    public void setMatKhau(@NonNull String matKhau) { this.matKhau = matKhau; }
    public String getQuyen() { return quyen; }
    public void setQuyen(String quyen) { this.quyen = quyen; }
    public String getMaNguoiDung() { return maNguoiDung; }
    public void setMaNguoiDung(String maNguoiDung) { this.maNguoiDung = maNguoiDung; }
}