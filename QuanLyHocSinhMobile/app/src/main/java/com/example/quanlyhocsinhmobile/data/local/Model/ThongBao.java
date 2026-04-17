package com.example.quanlyhocsinhmobile.data.local.Model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "ThongBao")
public class ThongBao {
    @PrimaryKey(autoGenerate = true)
    private int maTB;
    private String tieuDe;
    private String noiDung;
    private String ngayTao; 
    private String nguoiGui;
    public ThongBao() {}
    public int getMaTB() { return maTB; }
    public void setMaTB(int maTB) { this.maTB = maTB; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public String getNgayTao() { return ngayTao; }
    public void setNgayTao(String ngayTao) { this.ngayTao = ngayTao; }
    public String getNguoiGui() { return nguoiGui; }
    public void setNguoiGui(String nguoiGui) { this.nguoiGui = nguoiGui; }
}