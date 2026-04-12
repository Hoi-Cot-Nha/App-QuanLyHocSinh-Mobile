package com.example.quanlyhocsinhmobile.Controller_View;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.quanlyhocsinhmobile.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmobile.data.DAO.HocPhiDAO;
import com.example.quanlyhocsinhmobile.data.DAO.LopDAO;
import com.example.quanlyhocsinhmobile.data.Model.HocPhi;
import com.example.quanlyhocsinhmobile.data.Model.Lop;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class HocPhiController {
    private final HocPhiDAO hocPhiDAO;
    private final LopDAO lopDAO;
    private final Context context;

    public HocPhiController(Context context) {
        this.context = context;
        AppDatabase db = AppDatabase.getDatabase(context);
        this.hocPhiDAO = db.hocPhiDAO();
        this.lopDAO = db.lopDAO();
    }

    public List<Lop> getAllLop() {
        return lopDAO.getAll();
    }

    public List<HocPhi.Display> filterHocPhi(String maLop, int hocKy, String namHoc, String trangThai) {
        return hocPhiDAO.filterHocPhi(maLop, hocKy, namHoc, trangThai);
    }

    public List<HocPhi.Display> searchHocPhi(String query) {
        return hocPhiDAO.searchHocPhi("%" + query + "%");
    }

    public void updateHocPhi(HocPhi hp, String trangThai) {
        hp.setTrangThai(trangThai);
        hocPhiDAO.update(hp);
        Toast.makeText(context, "Cập nhật trạng thái học phí thành công", Toast.LENGTH_SHORT).show();
    }

    public void exportToExcel(List<HocPhi.Display> list) {
        if (list == null || list.isEmpty()) {
            Toast.makeText(context, "Danh sách trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("HocPhi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã HS");
        headerRow.createCell(1).setCellValue("Họ Tên");
        headerRow.createCell(2).setCellValue("Lớp");
        headerRow.createCell(3).setCellValue("Học Kỳ");
        headerRow.createCell(4).setCellValue("Năm Học");
        headerRow.createCell(5).setCellValue("Phải Đóng");
        headerRow.createCell(6).setCellValue("Trạng Thái");

        for (int i = 0; i < list.size(); i++) {
            HocPhi.Display display = list.get(i);
            HocPhi hp = display.getHocPhi();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(hp.getMaHS());
            row.createCell(1).setCellValue(display.getTenHS());
            row.createCell(2).setCellValue(display.getTenLop());
            row.createCell(3).setCellValue(hp.getHocKy());
            row.createCell(4).setCellValue(hp.getNamHoc());
            row.createCell(5).setCellValue(hp.getPhaiDong());
            row.createCell(6).setCellValue(hp.getTrangThai());
        }

        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "HocPhi_" + System.currentTimeMillis() + ".xlsx";
            File file = new File(downloadDir, fileName);
            FileOutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            Toast.makeText(context, "Đã lưu tại Download: " + fileName, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Lỗi khi xuất Excel", Toast.LENGTH_SHORT).show();
        }
    }
}
