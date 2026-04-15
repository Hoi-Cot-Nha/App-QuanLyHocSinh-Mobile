package com.example.quanlyhocsinhmobile.data.local.Model;

import androidx.annotation.NonNull;
import androidx.room.*;

@Entity(tableName = "Lop",
        foreignKeys = @ForeignKey(
                entity = GiaoVien.class,
                parentColumns = "maGV",
                childColumns = "maGVCN",
                onDelete = ForeignKey.SET_NULL,
                onUpdate = ForeignKey.CASCADE
        ),
        indices = {@Index("maGVCN")})
public class Lop {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "MaLop")
    private String maLop;

    @NonNull
    @ColumnInfo(name = "TenLop")
    private String tenLop;

    private String nienKhoa;
    private String maGVCN;

    public Lop() {}

    @Ignore
    public Lop(@NonNull String maLop, @NonNull String tenLop, String nienKhoa, String maGVCN) {
        this.maLop = maLop;
        this.tenLop = tenLop;
        this.nienKhoa = nienKhoa;
        this.maGVCN = maGVCN;
    }

    public String getMaLop() { return maLop; }
    public void setMaLop(String maLop) { this.maLop = maLop; }

    public String getTenLop() { return tenLop; }
    public void setTenLop(String tenLop) { this.tenLop = tenLop; }

    public String getNienKhoa() { return nienKhoa; }
    public void setNienKhoa(String nienKhoa) { this.nienKhoa = nienKhoa; }

    public String getMaGVCN() { return maGVCN; }
    public void setMaGVCN(String maGVCN) { this.maGVCN = maGVCN; }


}