package org.project.repository.load;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.project.repository.entity.DataFormat;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class ReadExcelSales {

    public ArrayList<DataFormat> magazin = new ArrayList<DataFormat>();
    Boolean safetySwitchDescription = false;
    String tempDate;
    String tempInvoice;
    Double TempTotalCost;
    Double TempUnitCost;
    Long TempUnitlong;

    public boolean read(String path) {
        try {
            String pathString = path;

            File file = new File(pathString); // creating a new file instance
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator(); // iterating over excel file
            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    System.out.println(cell.toString());

                    if (cell.toString() != "") {

                        if (cell.toString().contains("Invoice Date:")) {

                            cell = cellIterator.next();

                            System.out.println(cell.toString());

                            String data[] = cell.toString().trim().split("-");

                            if (data[1].equals("sty")) data[1] = "1";
                            if (data[1].equals("lut")) data[1] = "2";
                            if (data[1].equals("mar")) data[1] = "3";
                            if (data[1].equals("wrz")) data[1] = "4";
                            if (data[1].equals("maj")) data[1] = "5";
                            if (data[1].equals("cze")) data[1] = "6";
                            if (data[1].equals("lip")) data[1] = "7";
                            if (data[1].equals("sie")) data[1] = "8";
                            if (data[1].equals("wrz")) data[1] = "9";
                            if (data[1].equals("paz") || data[1].equals("paź")) data[1] = "10";
                            if (data[1].equals("lis")) data[1] = "11";
                            if (data[1].equals("gru")) data[1] = "12";

                            Integer tempYear = Integer.parseInt(data[2]);

                            Integer tempMonth = Integer.parseInt(data[1]);

                            if (data[0].indexOf("0") == 0) {
                                data[0].replace("0", "");
                            }

                            Integer tempDay = Integer.parseInt(data[0]);

                            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);
                            System.out.println("DATA STEFAN: " + tempDate);

                        }

                        if (cell.toString().contains("Invoice Number:")) {
                            cell = cellIterator.next();
                            System.out.print(cell.toString());
                            tempInvoice = cell.toString();
                        }

                        if (cell.toString().contains("Item") && cell.toString().contains("invoice")) {
                            safetySwitchDescription = false;
                        }

                        if (safetySwitchDescription == true && !(cell.toString().contains("ustom"))) {

                            String tempMaterial = cell.toString().trim();
                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            double tempQtyDouble = Double.parseDouble(cell.toString().trim());
                            Integer tempQty = (int) tempQtyDouble;
                            cell = cellIterator.next();
                            try {
                                TempUnitCost = Double.parseDouble(cell.toString().trim());
                                TempUnitCost *= 100;
                                TempUnitlong = Math.round(TempUnitCost);
                                TempUnitCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {

                                System.out.println("Nie uda³o siê odczytac wartosci jednostkowej");
                                TempUnitCost = 0.00;

                            }

                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            try {
                                TempTotalCost = Double.parseDouble(cell.toString().trim());
                                TempTotalCost *= 100;
                                TempUnitlong = Math.round(TempTotalCost);
                                TempTotalCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {
                                TempTotalCost = tempQty * TempUnitCost;
                            }

                            if (TempUnitCost == 0.00) {

                                TempUnitCost = TempTotalCost / tempQty;
                            }

                            cell = cellIterator.next();
                            cell = cellIterator.next();
                            System.out.print(cell.toString());
                            String tempDescription = cell.toString().trim();

                            System.out.println("DATA: " + tempDate + " Material: " + tempMaterial + " QTY: " + tempQty + " TempUnitCost: "
                                    + TempUnitCost + " TempTotalCost: " + TempTotalCost + " TempDescription: "
                                    + tempDescription);

                            magazin.add(new DataFormat(tempDate, tempInvoice, tempMaterial, tempDescription, tempQty.toString(),
                                    TempUnitCost, tempQty, TempTotalCost, 0.0, 0.0));

                            try {
                                cell = cellIterator.next();
                            } catch (Exception e) {

                            }
                        }

                        if (cell.toString().contains("HTS Descripton")) {
                            safetySwitchDescription = true;
                        }

                    }
                }
            }
            fis.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean readDifferentExcellFormat(String path) {
        try {

            System.out.println("PRZElaczam na drugi format odczytu");
            safetySwitchDescription = false;
            String pathString = path;

            File file = new File(pathString); // creating a new file instance
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator(); // iterating over excel file

            System.out.println("WCHODZE DO WHILE");
            String SafetyStringToCheckCorrectReadFile = "";

            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column
                System.out.println("cell iterator wchodze do druigego while");

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    System.out.println("--->:" + cell.toString());

                    if (cell.toString() != "") {
                        if (cell.toString().contains("Invoice Date:")) {

                            cell = cellIterator.next();

                            while (cell.toString() == "") {
                                cell = cellIterator.next();
                            }

                            System.out.println("CELL DATA:" + cell.toString());

                            String data[] = cell.toString().trim().split("-");

                            if (data[1].equals("sty")) data[1] = "1";
                            if (data[1].equals("lut")) data[1] = "2";
                            if (data[1].equals("mar")) data[1] = "3";
                            if (data[1].equals("wrz")) data[1] = "4";
                            if (data[1].equals("maj")) data[1] = "5";
                            if (data[1].equals("cze")) data[1] = "6";
                            if (data[1].equals("lip")) data[1] = "7";
                            if (data[1].equals("sie")) data[1] = "8";
                            if (data[1].equals("wrz")) data[1] = "9";
                            if (data[1].equals("paz")|| data[1].equals("paź")) data[1] = "10";
                            if (data[1].equals("lis")) data[1] = "11";
                            if (data[1].equals("gru")) data[1] = "12";

                            Integer tempYear = Integer.parseInt(data[2]);

                            Integer tempMonth = Integer.parseInt(data[1]);

                            if (data[0].indexOf("0") == 0) {
                                data[0].replace("0", "");
                            }

                            Integer tempDay = Integer.parseInt(data[0]);

                            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);

                        }

                        if (cell.toString().contains("Invoice Number:")) {
                            cell = cellIterator.next();
                            while (cell.toString() == "") {
                                cell = cellIterator.next();
                            }
                            tempInvoice = cell.toString();

                        }

                        if (cell.toString().contains("Item") && cell.toString().contains("invoice")) {
                            safetySwitchDescription = false;
                        }

                        if (safetySwitchDescription == true) {

                            String tempMaterial = cell.toString().trim();
                            SafetyStringToCheckCorrectReadFile = tempMaterial;
                            cell = cellIterator.next();
                            cell = cellIterator.next();
                            double tempQtyDouble = Double.parseDouble(cell.toString().trim());
                            Integer tempQty = (int) tempQtyDouble;
                            cell = cellIterator.next();
                            Double TempUnitCost = Double.parseDouble(cell.toString().trim());

                            TempUnitCost *= 100;
                            Long TempUnitlong = Math.round(TempUnitCost);
                            TempUnitCost = (double) TempUnitlong / 100;

                            cell = cellIterator.next();


                            try {
                                TempTotalCost = Double.parseDouble(cell.toString().trim());
                                TempTotalCost *= 100;
                                TempUnitlong = Math.round(TempTotalCost);
                                TempTotalCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {
                                TempTotalCost = tempQty * TempUnitCost;
                            }


                            cell = cellIterator.next();
                            String tempDescription = cell.toString().trim();
                            cell = cellIterator.next();
                            cell = cellIterator.next();

                            System.out.println("DATA: " + tempDate + " Material: " + tempMaterial + " QTY: " + tempQty + " TempUnitCost: "
                                    + TempUnitCost + " TempTotalCost: " + TempTotalCost + " TempDescription: "
                                    + tempDescription);

                            magazin.add(new DataFormat(tempDate, tempInvoice, tempMaterial, tempDescription, tempQty.toString(),
                                    TempUnitCost, tempQty, TempTotalCost, 0.0, 0.0));
                        }

                        if (cell.toString().contains("Trade mark")) {
                            safetySwitchDescription = true;
                        }

                    }
                }
            }
            fis.close();
            if (SafetyStringToCheckCorrectReadFile == "") {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean readDifferentExcellFormatNr3(String path) {
        try {

            System.out.println("PRZE³aczam na trzeci format odczytu");
            safetySwitchDescription = false;
            String pathString = path;

            File file = new File(pathString); // creating a new file instance
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator(); // iterating over excel file

            System.out.println("WCHODZE DO WHILE");

            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column
                System.out.println("cell iterator wchodze do drugiego while");

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    System.out.println(">:" + cell.toString());

                    if (cell.toString() != "") {
                        if (cell.toString().contains("Invoice Date:")) {

                            cell = cellIterator.next();

                            while (cell.toString() == "") {
                                cell = cellIterator.next();
                            }

                            System.out.println("CELL DATA:" + cell.toString());

                            String data[] = cell.toString().trim().split("-");

                            if (data[1].equals("sty")) data[1] = "1";
                            if (data[1].equals("lut")) data[1] = "2";
                            if (data[1].equals("mar")) data[1] = "3";
                            if (data[1].equals("wrz")) data[1] = "4";
                            if (data[1].equals("maj")) data[1] = "5";
                            if (data[1].equals("cze")) data[1] = "6";
                            if (data[1].equals("lip")) data[1] = "7";
                            if (data[1].equals("sie")) data[1] = "8";
                            if (data[1].equals("wrz")) data[1] = "9";
                            if (data[1].equals("paz")|| data[1].equals("paź")) data[1] = "10";
                            if (data[1].equals("lis")) data[1] = "11";
                            if (data[1].equals("gru")) data[1] = "12";

                            Integer tempYear = Integer.parseInt(data[2]);

                            Integer tempMonth = Integer.parseInt(data[1]);

                            if (data[0].indexOf("0") == 0) {
                                data[0].replace("0", "");
                            }

                            Integer tempDay = Integer.parseInt(data[0]);

                            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);

                        }

                        if (cell.toString().contains("Invoice Number:")) {
                            cell = cellIterator.next();
                            while (cell.toString() == "") {
                                cell = cellIterator.next();
                            }
                            tempInvoice = cell.toString();


                        }

                        if (cell.toString().contains("tem") && cell.toString().contains("nvoice")) {
                            safetySwitchDescription = false;
                        }

                        if (safetySwitchDescription == true) {

                            String tempMaterial = cell.toString().trim();
                            cell = cellIterator.next();
                            System.out.println(">:" + cell.toString());
                            cell = cellIterator.next();
                            System.out.println(">:" + cell.toString());
                            cell = cellIterator.next();
                            System.out.println(">:" + cell.toString());
                            double tempQtyDouble = Double.parseDouble(cell.toString().trim());
                            Integer tempQty = (int) tempQtyDouble;
                            cell = cellIterator.next();
                            System.out.println(">:" + cell.toString());
                            Double TempUnitCost = Double.parseDouble(cell.toString().trim());

                            TempUnitCost *= 100;
                            Long TempUnitlong = Math.round(TempUnitCost);
                            TempUnitCost = (double) TempUnitlong / 100;

                            cell = cellIterator.next();
                            System.out.println(">:" + cell.toString());


                            try {
                                TempTotalCost = Double.parseDouble(cell.toString().trim());
                                TempTotalCost *= 100;
                                TempUnitlong = Math.round(TempTotalCost);
                                TempTotalCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {
                                TempTotalCost = tempQty * TempUnitCost;
                            }


                            cell = cellIterator.next();
                            System.out.println(">:" + cell.toString());
                            cell = cellIterator.next();
                            System.out.println("temp descr>:" + cell.toString());
                            String tempDescription = cell.toString().trim();


                            System.out.println("DATA: " + tempDate + " Material: " + tempMaterial + " QTY: " + tempQty + " TempUnitCost: "
                                    + TempUnitCost + " TempTotalCost: " + TempTotalCost + " TempDescription: "
                                    + tempDescription);

                            magazin.add(new DataFormat(tempDate, tempInvoice, tempMaterial, tempDescription, tempQty.toString(),
                                    TempUnitCost, tempQty, TempTotalCost, 0.0, 0.0));
                        }

                        if (cell.toString().contains("HTS Descripton")) {
                            safetySwitchDescription = true;
                        }

                    }
                }
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean readDifferentExcellFormatNr4(String path) {
        try {
            System.out.println("PRZE³aczam na trzeci format odczytu");
            safetySwitchDescription = false;
            String pathString = path;

            File file = new File(pathString); // creating a new file instance
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator(); // iterating over excel file
            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column

                System.out.println("pierwszy while STEFAN");

                while (cellIterator.hasNext()) {

                    System.out.println("drugi while STEFAN");

                    Cell cell = cellIterator.next();

                    System.out.println(cell.toString());

                    if (cell.toString() != "") {

                        if (cell.toString().contains("Invoice Date:")) {

                            cell = cellIterator.next();

                            System.out.println(cell.toString());

                            String data[] = cell.toString().trim().split("-");

                            if (data[1].equals("sty")) data[1] = "1";
                            if (data[1].equals("lut")) data[1] = "2";
                            if (data[1].equals("mar")) data[1] = "3";
                            if (data[1].equals("wrz")) data[1] = "4";
                            if (data[1].equals("maj")) data[1] = "5";
                            if (data[1].equals("cze")) data[1] = "6";
                            if (data[1].equals("lip")) data[1] = "7";
                            if (data[1].equals("sie")) data[1] = "8";
                            if (data[1].equals("wrz")) data[1] = "9";
                            if (data[1].equals("paz") || data[1].equals("paź")) data[1] = "10";
                            if (data[1].equals("lis")) data[1] = "11";
                            if (data[1].equals("gru")) data[1] = "12";

                            Integer tempYear = Integer.parseInt(data[2]);

                            Integer tempMonth = Integer.parseInt(data[1]);

                            if (data[0].indexOf("0") == 0) {
                                data[0].replace("0", "");
                            }

                            Integer tempDay = Integer.parseInt(data[0]);

                            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);
                            System.out.println("DATA STEFAN: " + tempDate);

                        }

                        if (cell.toString().contains("Invoice Number:")) {
                            cell = cellIterator.next();
                            System.out.print(cell.toString());
                            tempInvoice = cell.toString();
                        }

                        if (cell.toString().contains("Item") && cell.toString().contains("invoice")) {
                            safetySwitchDescription = false;
                        }

                        if (safetySwitchDescription == true && !(cell.toString().contains("HTS Code"))) {

                            System.out.println("JESTEM W KONCU W ANALIZIE DANYCH :)");
                            String tempMaterial = cell.toString().trim();
                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            double tempQtyDouble = Double.parseDouble(cell.toString().trim());
                            Integer tempQty = (int) tempQtyDouble;
                            cell = cellIterator.next();
                            try {
                                TempUnitCost = Double.parseDouble(cell.toString().trim());
                                TempUnitCost *= 100;
                                TempUnitlong = Math.round(TempUnitCost);
                                TempUnitCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {

                                System.out.println("Nie uda³o siê odczytac wartosci jednostkowej");
                                TempUnitCost = 0.00;

                            }

                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            try {
                                TempTotalCost = Double.parseDouble(cell.toString().trim());
                                TempTotalCost *= 100;
                                TempUnitlong = Math.round(TempTotalCost);
                                TempTotalCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {
                                TempTotalCost = tempQty * TempUnitCost;
                            }

                            if (TempUnitCost == 0.00) {

                                TempUnitCost = TempTotalCost / tempQty;
                            }

                            cell = cellIterator.next();
                            cell = cellIterator.next();
                            System.out.print(cell.toString());
                            String tempDescription = cell.toString().trim();

                            System.out.println("DATA: " + tempDate + " Material: " + tempMaterial + " QTY: " + tempQty + " TempUnitCost: "
                                    + TempUnitCost + " TempTotalCost: " + TempTotalCost + " TempDescription: "
                                    + tempDescription);

                            magazin.add(new DataFormat(tempDate, tempInvoice, tempMaterial, tempDescription, tempQty.toString(),
                                    TempUnitCost, tempQty, TempTotalCost, 0.0, 0.0));

                            try {
                                cell = cellIterator.next();
                            } catch (Exception e) {

                            }
                        }

                        if (cell.toString().contains("HTS Descripton")) {
                            safetySwitchDescription = true;
                        }

                    }
                }
            }
            fis.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public void readDifferentExcellFormatNr5(String path) {
        try {
            System.out.println("PRZE³aczam na czwarty format odczytu");
            safetySwitchDescription = false;
            String pathString = path;

            File file = new File(pathString); // creating a new file instance
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator(); // iterating over excel file
            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column

                System.out.println("pierwszy while STEFAN");

                while (cellIterator.hasNext()) {

                    System.out.println("drugi while STEFAN");

                    Cell cell = cellIterator.next();

                    System.out.println(cell.toString());

                    if (cell.toString() != "") {

                        if (cell.toString().contains("Invoice Date:")) {

                            cell = cellIterator.next();

                            System.out.println(cell.toString());

                            String data[] = cell.toString().trim().split("-");

                            if (data[1].equals("sty")) data[1] = "1";
                            if (data[1].equals("lut")) data[1] = "2";
                            if (data[1].equals("mar")) data[1] = "3";
                            if (data[1].equals("wrz")) data[1] = "4";
                            if (data[1].equals("maj")) data[1] = "5";
                            if (data[1].equals("cze")) data[1] = "6";
                            if (data[1].equals("lip")) data[1] = "7";
                            if (data[1].equals("sie")) data[1] = "8";
                            if (data[1].equals("wrz")) data[1] = "9";
                            if (data[1].equals("paz") || data[1].equals("paź")) data[1] = "10";
                            if (data[1].equals("lis")) data[1] = "11";
                            if (data[1].equals("gru")) data[1] = "12";

                            Integer tempYear = Integer.parseInt(data[2]);

                            Integer tempMonth = Integer.parseInt(data[1]);

                            if (data[0].indexOf("0") == 0) {
                                data[0].replace("0", "");
                            }

                            Integer tempDay = Integer.parseInt(data[0]);

                            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);
                            System.out.println("DATA STEFAN: " + tempDate);

                        }

                        if (cell.toString().contains("Invoice Number:")) {
                            cell = cellIterator.next();
                            System.out.print(cell.toString());
                            tempInvoice = cell.toString();
                        }

                        if (cell.toString().contains("Item") && cell.toString().contains("invoice")) {
                            safetySwitchDescription = false;
                        }

                        if (safetySwitchDescription == true && (!(cell.toString().contains("Trailer ID")) ||!(cell.toString().contains("upc")))) {

                            System.out.println("JESTEM W KONCU W ANALIZIE DANYCH :)");
                            String tempMaterial = cell.toString().trim();
                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            double tempQtyDouble = Double.parseDouble(cell.toString().trim());
                            Integer tempQty = (int) tempQtyDouble;
                            cell = cellIterator.next();
                            try {
                                TempUnitCost = Double.parseDouble(cell.toString().trim());
                                TempUnitCost *= 100;
                                TempUnitlong = Math.round(TempUnitCost);
                                TempUnitCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {

                                System.out.println("Nie uda³o siê odczytac wartosci jednostkowej");
                                TempUnitCost = 0.00;

                            }

                            cell = cellIterator.next();
                            System.out.print(cell.toString());

                            try {
                                TempTotalCost = Double.parseDouble(cell.toString().trim());
                                TempTotalCost *= 100;
                                TempUnitlong = Math.round(TempTotalCost);
                                TempTotalCost = (double) TempUnitlong / 100;
                            } catch (Exception e) {
                                TempTotalCost = tempQty * TempUnitCost;
                            }

                            if (TempUnitCost == 0.00) {

                                TempUnitCost = TempTotalCost / tempQty;
                            }

                            cell = cellIterator.next();
                            cell = cellIterator.next();
                            System.out.print(cell.toString());
                            String tempDescription = cell.toString().trim();

                            System.out.println("DATA: " + tempDate + " Material: " + tempMaterial + " QTY: " + tempQty + " TempUnitCost: "
                                    + TempUnitCost + " TempTotalCost: " + TempTotalCost + " TempDescription: "
                                    + tempDescription);

                            magazin.add(new DataFormat(tempDate, tempInvoice, tempMaterial, tempDescription, tempQty.toString(),
                                    TempUnitCost, tempQty, TempTotalCost, 0.0, 0.0));

                            try {
                                cell = cellIterator.next();
                            } catch (Exception e) {

                            }
                        }

                        if (cell.toString().contains("HTS Descripton")) {
                            safetySwitchDescription = true;
                        }

                    }
                }
            }
            fis.close();

        } catch (Exception e) {
        }
    }
}
