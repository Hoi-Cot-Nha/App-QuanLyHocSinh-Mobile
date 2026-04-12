package com.example.quanlyhocsinhmobile.Controller_View;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.quanlyhocsinhmobile.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmobile.data.DAO.DiemDAO;
import com.example.quanlyhocsinhmobile.data.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.Model.Diem;
import com.example.quanlyhocsinhmobile.data.Model.Lop;
import com.example.quanlyhocsinhmobile.data.Model.MonHoc;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DiemController {
    private final DiemDAO diemDAO;
    private final MonHocDAO monHocDAO;
    private final LopDAO lopDAO;
    private final Context context;

    public DiemController(Context context) {
        this.context = context;
        AppDatabase db = AppDatabase.getDatabase(context);
        this.diemDAO = db.diemDAO();
        this.monHocDAO = db.monHocDAO();
        this.lopDAO = db.lopDAO();
    }

    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }

    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }

    public List<Diem.Display> getAllDiem() {
        return diemDAO.getAll();
    }

    public List<Diem.Display> searchDiem(String query) {
        return diemDAO.searchDiem("%" + query + "%");
    }

    public List<Diem.Display> filterDiem(String maMH, int hocKy, String maLop) {
        return diemDAO.filterDiem(maMH, hocKy, maLop);
    }

    public boolean updateDiem(Diem diem, String d15, String d1t, String dgk, String dck) {
        try {
            diem.setDiem15p(Double.parseDouble(d15));
            diem.setDiem1Tiet(Double.parseDouble(d1t));
            diem.setDiemGiuaKy(Double.parseDouble(dgk));
            diem.setDiemCuoiKy(Double.parseDouble(dck));
            diem.setDiemTongKet(diem.calculateDiemTongKet());
            
            diemDAO.update(diem);
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "Lỗi định dạng điểm!", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public void exportToExcel(List<Diem.Display> list) {
        if (list == null || list.isEmpty()) {
            Toast.makeText(context, "Danh sách trống, không thể xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("BangDiem");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã HS");
        headerRow.createCell(1).setCellValue("Họ Tên");
        headerRow.createCell(2).setCellValue("Môn");
        headerRow.createCell(3).setCellValue("HK");
        headerRow.createCell(4).setCellValue("15p");
        headerRow.createCell(5).setCellValue("1 Tiết");
        headerRow.createCell(6).setCellValue("Giữa Kỳ");
        headerRow.createCell(7).setCellValue("Cuối Kỳ");
        headerRow.createCell(8).setCellValue("Trung Bình");

        for (int i = 0; i < list.size(); i++) {
            Diem.Display display = list.get(i);
            Diem d = display.getDiem();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(d.getMaHS());
            row.createCell(1).setCellValue(display.getTenHS());
            row.createCell(2).setCellValue(display.getTenMH());
            row.createCell(3).setCellValue(d.getHocKy());
            row.createCell(4).setCellValue(d.getDiem15p() != null ? d.getDiem15p() : 0.0);
            row.createCell(5).setCellValue(d.getDiem1Tiet() != null ? d.getDiem1Tiet() : 0.0);
            row.createCell(6).setCellValue(d.getDiemGiuaKy() != null ? d.getDiemGiuaKy() : 0.0);
            row.createCell(7).setCellValue(d.getDiemCuoiKy() != null ? d.getDiemCuoiKy() : 0.0);
            row.createCell(8).setCellValue(d.getDiemTongKet() != null ? d.getDiemTongKet() : 0.0);
        }

        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDir.exists()) downloadDir.mkdirs();
            
            String fileName = "BangDiem_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);
            
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            Toast.makeText(context, "Đã lưu tại thư mục Download: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi xuất Excel: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
