package com.vfasad.service;

import com.vfasad.entity.Order;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PrintService {
    @Autowired
    private OrderService orderService;


    public byte[] generateInProgressXlsx() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Mirrada\\Desktop\\Report.xlsx");//take a template
            XSSFWorkbook workbook = new XSSFWorkbook(file);//created the object of xlsx library

            Sheet sheet = workbook.getSheetAt(0);//retrieve the first sheet
            Row row = sheet.getRow(10);
            Cell cell_A11 = row.getCell(0);
            cell_A11.setCellValue(777);

            Row rowDate = sheet.getRow(1);
            Cell cellDate = rowDate.getCell(6);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            cellDate.setCellValue(format.format(new Date()));

            /// templateMapping();
            List<Order> list = templateMapping();
            int i = 11;
            for (Order order : list) {
                Cell cell = sheet.getRow(i).getCell(0);
                cell.setCellValue(order.getId());
                i++;
            }

            ByteArrayOutputStream report = new ByteArrayOutputStream();
            workbook.write(report);
            byte[] reportArray = report.toByteArray();

            workbook.close();//
            file.close();

            report.close();
            return reportArray;

        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException();
        }

    }

    private List<Order> templateMapping() throws IOException {
        Order order1 = new Order();
        order1.setId(11L);
        Order order2 = new Order();
        order2.setId(22L);
        List<Order> list = new ArrayList<>();
        list.add(order1);
        list.add(order2);

        return list;
    }
}

