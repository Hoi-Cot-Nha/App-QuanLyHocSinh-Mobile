package com.example.quanlyhocsinhmobile.data.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Lop",
        foreignKeys = {@ForeignKey(entity = GiaoVien.class,
                parentColumns = "maGV",
                childColumns = "maGVCN",
                onDelete = ForeignKey.SET_NULL,
                onUpdate = ForeignKey.CASCADE)},
        indices = {@Index("maGVCN")})
public class Lop {
    @PrimaryKey
    @NonNull
    @androidx.room.ColumnInfo(name = "MaLop")
    private String maLop;
    
    @NonNull
    @androidx.room.ColumnInfo(name = "TenLop")
    private String tenLop;
    private String nienKhoa;
    private String maGVCN;

    public Lop() {}

    @NonNull
    public String getMaLop() { return maLop; }
    public void setMaLop(@NonNull String maLop) { this.maLop = maLop; }

    @NonNull
    public String getTenLop() { return tenLop; }
    public void setTenLop(@NonNull String tenLop) { this.tenLop = tenLop; }

    public String getNienKhoa() { return nienKhoa; }
    public void setNienKhoa(String nienKhoa) { this.nienKhoa = nienKhoa; }

    public String getMaGVCN() { return maGVCN; }
    public void setMaGVCN(String maGVCN) { this.maGVCN = maGVCN; }
}
