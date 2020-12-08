package com.vfasad.service;

import com.vfasad.entity.Order;
import com.vfasad.entity.OrderConsume;
import com.vfasad.entity.Product;
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
import java.util.*;

@Service
public class PrintService {
    @Autowired
    private OrderService orderService;


    public byte[] generateInProgressXlsx() {
        try {
            FileInputStream file = new FileInputStream("C:\\Users\\Mirrada\\Desktop\\Report.xlsx");//take a template
            XSSFWorkbook workbook = new XSSFWorkbook(file);//created the object of xlsx library

            Sheet sheet = workbook.getSheetAt(0);//retrieve the first sheet
            /*Row row = sheet.getRow(5);
            Cell cell_A11 = row.getCell(0);
            cell_A11.setCellValue(777);*/

            Row rowDate = sheet.getRow(1);
            Cell cellDate = rowDate.getCell(6);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            cellDate.setCellValue(format.format(new Date()));

            /// templateMapping();
            List<Order> list = templateMapping();
            int i = 5;
            int a = 0;
            for (Order order : list) {
                Cell cell = sheet.getRow(i).getCell(0);
                cell.setCellValue(order.getId());
                Cell cell1 = sheet.getRow(i).getCell(1);
                cell1.setCellValue(order.getDocument());
                Cell cell2 = sheet.getRow(i).getCell(2);
                cell2.setCellValue(order.getArea());
                for (OrderConsume orderConsume : order.getConsumes()) {
                    Cell cell3 = sheet.getRow(i).getCell(3);
                    cell3.setCellValue(orderConsume.getProduct().getName());
                    Cell cell4 = sheet.getRow(i).getCell(4);
                    cell4.setCellValue(orderConsume.getProduct().getProducer());
                    Cell cell5 = sheet.getRow(i).getCell(5);
                    cell5.setCellValue(orderConsume.getCalculatedQuantity());
                    i++;
                }
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
        order1.setId(1L);
        order1.setDocument("Gate");
        order1.setArea(115);

        OrderConsume orderConsume_Gate_green = new OrderConsume();//The First OrderConsume for order1
        Product product_green = new Product();
        product_green.setName("Colour_green");
        product_green.setProducer("USA");
        orderConsume_Gate_green.setProduct(product_green);
        orderConsume_Gate_green.setCalculatedQuantity(45);

        OrderConsume orderConsume_Gate_grey = new OrderConsume();//The Second OrderConsume for order1
        Product product_grey = new Product();
        product_grey.setName("Colour_grey");
        product_grey.setProducer("Ukraine");
        orderConsume_Gate_grey.setProduct(product_grey);
        orderConsume_Gate_grey.setCalculatedQuantity(91);

        Set<OrderConsume> set_orde1 = new HashSet<>();
        set_orde1.add(orderConsume_Gate_green);
        set_orde1.add(orderConsume_Gate_grey);
        order1.setConsumes(set_orde1);


        Order order2 = new Order();
        order2.setId(2L);
        order2.setDocument("House");
        order2.setArea(1225);

        OrderConsume orderConsume_house_green = new OrderConsume();
        orderConsume_house_green.setProduct(product_green);
        orderConsume_house_green.setCalculatedQuantity(3000);
        Set<OrderConsume>set_order2 = new HashSet<>();
        set_order2.add(orderConsume_house_green);
        order2.setConsumes(set_order2);

        List<Order> list = new ArrayList<>();
        list.add(order1);
        list.add(order2);

        return list;
    }
}

