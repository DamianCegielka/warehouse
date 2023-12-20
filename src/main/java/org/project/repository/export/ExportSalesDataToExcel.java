package org.project.repository.export;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.project.repository.entity.DataFormat;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

    public class ExportSalesDataToExcel {


        private static String[] columns = {"data", "faktura", "indeks", "nazwa","ilosc na start","cena USD","pozostaje qty", "cena PLN", "powiazane faktury","wartosc powiazanych faktur"};
        //private static List<Employee> employees =  new ArrayList<>();
        private ArrayList<DataFormat> magazin;

        // Initializing employees data to insert into the excel file
	    /*static {
	        Calendar dateOfBirth = Calendar.getInstance();
	        dateOfBirth.set(1992, 7, 21);
	        employees.add(new Employee("Rajeev Singh", "rajeev@example.com",
	                dateOfBirth.getTime(), 1200000.0));

	        dateOfBirth.set(1965, 10, 15);
	        employees.add(new Employee("Thomas cook", "thomas@example.com",
	                dateOfBirth.getTime(), 1500000.0));

	        dateOfBirth.set(1987, 4, 18);
	        employees.add(new Employee("Steve Maiden", "steve@example.com",
	                dateOfBirth.getTime(), 1800000.0));
	    }
	    */

        public void export(ArrayList<DataFormat> magazin) throws FileNotFoundException {

            try {

                this.magazin=magazin;
                // Create a Workbook
                Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

	        /* CreationHelper helps us create instances of various things like DataFormat,
	           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
                CreationHelper createHelper = workbook.getCreationHelper();

                // Create a Sheet
                Sheet sheet = workbook.createSheet("Sprzedaz");

                // Create a Font for styling header cells
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 14);
                headerFont.setColor(IndexedColors.RED.getIndex());

                // Create a CellStyle with the font
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);

                // Create a Row
                Row headerRow = sheet.createRow(0);

                // Create cells
                for(int i = 0; i < columns.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columns[i]);
                    cell.setCellStyle(headerCellStyle);
                }

                // Create Cell Style for formatting Date
                CellStyle dateCellStyle = workbook.createCellStyle();
                dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));

                // Create Other rows and cells with employees data
                int rowNum = 1;
                String name="";
                Double summaryValueConnect=0.00;
                for(DataFormat mag: magazin) {

                    name=mag.getNameInvoice();

                    Row row = sheet.createRow(rowNum++);

                    Cell date=row.createCell(0);
                    date.setCellValue(mag.getDate());
                    date.setCellStyle(dateCellStyle);

                    row.createCell(1)
                            .setCellValue(mag.getNameInvoice());

                    row.createCell(2)
                            .setCellValue(mag.getMaterial());

                    row.createCell(3)
                            .setCellValue(mag.getDescription());

                    row.createCell(4)
                            .setCellValue(mag.getSize());

                    row.createCell(5)
                            .setCellValue(mag.getUnityPrice());

                    row.createCell(6)
                            .setCellValue(mag.getQtyShip());

                    row.createCell(7)
                            .setCellValue(mag.getExtendedPrice());

                    row.createCell(8)
                            .setCellValue(mag.getConnectDocuments());

                    row.createCell(9)
                            .setCellValue(mag.getValueConnectDocuments());

                    summaryValueConnect+=mag.getValueConnectDocuments();
                }


                // Resize all columns to fit the content size
                for(int i = 0; i < columns.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Write the output to a file
                FileOutputStream fileOut=new FileOutputStream("EKSPORTOWANA_SPRZEDAZ.xlsx");


                try {
                    workbook.write(fileOut);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    fileOut.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                // Closing the workbook
                try {
                    workbook.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            catch(Exception e5) {
                System.out.println("NIE UDALO SIE WYGENEROWAC PLIKU");
                e5.printStackTrace();
            }
        }
    }

