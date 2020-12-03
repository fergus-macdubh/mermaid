package com.vfasad.controller;

import com.vfasad.service.OrderService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class PrintService {
    @Autowired
    private OrderService orderService;


    public void generateInProgressXlsx() {

    }

    public static void main(String[] args) throws IOException {
        FileInputStream file = new FileInputStream("C:\\Users\\Mirrada\\Desktop\\Report.xlsx");//indicate the path from where to read
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);//retrieve the first sheet

        Row row = sheet.getRow(10);
        Cell cell_A11 = row.getCell(0);
        cell_A11.setCellValue(777);

        Row rowDate = sheet.getRow(1);
        Cell cellDate = rowDate.getCell(6);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        cellDate.setCellValue(format.format(new Date()));


        FileOutputStream outputStream = new FileOutputStream("C:\\Users\\Mirrada\\Desktop\\temp.xlsx");//
        workbook.write(outputStream);//
        workbook.close();//
        file.close();
        outputStream.close();


    }
}
