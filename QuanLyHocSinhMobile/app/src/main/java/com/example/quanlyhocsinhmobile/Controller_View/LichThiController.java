package com.example.quanlyhocsinhmobile.Controller_View;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.quanlyhocsinhmobile.data.Connection.AppDatabase;
import com.example.quanlyhocsinhmobile.data.DAO.LichThiDAO;
import com.example.quanlyhocsinhmobile.data.DAO.MonHocDAO;
import com.example.quanlyhocsinhmobile.data.DAO.PhongHocDAO;
import com.example.quanlyhocsinhmobile.data.Model.LichThi;
import com.example.quanlyhocsinhmobile.data.Model.MonHoc;
import com.example.quanlyhocsinhmobile.data.Model.PhongHoc;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class LichThiController {
    private final LichThiDAO lichThiDAO;
    private final MonHocDAO monHocDAO;
    private final PhongHocDAO phongHocDAO;
    private final Context context;

    public LichThiController(Context context) {
        this.context = context;
        AppDatabase db = AppDatabase.getDatabase(context);
        this.lichThiDAO = db.lichThiDAO();
        this.monHocDAO = db.monHocDAO();
        this.phongHocDAO = db.phongHocDAO();
    }

    public List<LichThi.Display> getAllLichThi() {
        return lichThiDAO.getAll();
    }

    public List<LichThi.Display> searchLichThi(String query) {
        return lichThiDAO.searchLichThi("%" + query + "%");
    }

    public List<MonHoc> getAllMonHoc() {
        return monHocDAO.getAll();
    }

    public List<PhongHoc> getAllPhongHoc() {
        return phongHocDAO.getAll();
    }

    public void insertLichThi(String ten, String ngay, String bd, String kt, int monPos, List<MonHoc> monHocs, int phongPos, List<PhongHoc> phongHocs) {
        if (ten.isEmpty()) {
            Toast.makeText(context, "Vui lòng nhập tên kỳ thi", Toast.LENGTH_SHORT).show();
            return;
        }
        LichThi newLich = new LichThi();
        newLich.setTenKyThi(ten);
        newLich.setNgayThi(ngay);
        newLich.setGioBatDau(bd);
        newLich.setGioKetThuc(kt);

        if (monPos >= 0 && !monHocs.isEmpty()) {
            newLich.setMaMH(monHocs.get(monPos).getMaMH());
        }
        if (phongPos >= 0 && !phongHocs.isEmpty()) {
            newLich.setMaPhong(phongHocs.get(phongPos).getMaPhong());
        }

        lichThiDAO.insert(newLich);
        Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
    }

    public void updateLichThi(LichThi selected, String ten, String ngay, String bd, String kt, int monPos, List<MonHoc> monHocs, int phongPos, List<PhongHoc> phongHocs) {
        if (selected == null) {
            Toast.makeText(context, "Chưa chọn lịch thi", Toast.LENGTH_SHORT).show();
            return;
        }
        selected.setTenKyThi(ten);
        selected.setNgayThi(ngay);
        selected.setGioBatDau(bd);
        selected.setGioKetThuc(kt);

        if (monPos >= 0 && !monHocs.isEmpty()) {
            selected.setMaMH(monHocs.get(monPos).getMaMH());
        }
        if (phongPos >= 0 && !phongHocs.isEmpty()) {
            selected.setMaPhong(phongHocs.get(phongPos).getMaPhong());
        }

        lichThiDAO.update(selected);
        Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }

    public void deleteLichThi(LichThi lichThi) {
        lichThiDAO.delete(lichThi);
    }

    public void exportToExcel(List<LichThi.Display> list) {
        if (list == null || list.isEmpty()) {
            Toast.makeText(context, "Danh sách trống, không thể xuất!", Toast.LENGTH_SHORT).show();
            return;
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("LichThi");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Mã LT");
        headerRow.createCell(1).setCellValue("Tên Kỳ Thi");
        headerRow.createCell(2).setCellValue("Môn Học");
        headerRow.createCell(3).setCellValue("Phòng");
        headerRow.createCell(4).setCellValue("Ngày Thi");
        headerRow.createCell(5).setCellValue("Giờ Bắt Đầu");
        headerRow.createCell(6).setCellValue("Giờ Kết Thúc");

        for (int i = 0; i < list.size(); i++) {
            LichThi.Display display = list.get(i);
            LichThi lt = display.getLichThi();
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue(lt.getMaLT());
            row.createCell(1).setCellValue(lt.getTenKyThi());
            row.createCell(2).setCellValue(display.getTenMH() != null ? display.getTenMH() : lt.getMaMH());
            row.createCell(3).setCellValue(display.getTenPhong() != null ? display.getTenPhong() : lt.getMaPhong());
            row.createCell(4).setCellValue(lt.getNgayThi());
            row.createCell(5).setCellValue(lt.getGioBatDau());
            row.createCell(6).setCellValue(lt.getGioKetThuc());
        }

        try {
            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadDir.exists()) downloadDir.mkdirs();

            String fileName = "LichThi_" + System.currentTimeMillis() + ".xlsx";
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
