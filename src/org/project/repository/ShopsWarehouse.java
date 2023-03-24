package org.project.repository;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.project.repository.entity.DataFormat;
import org.project.repository.load.DownloadExchangeRateFromURL;
import org.project.repository.load.ReadExcelSales;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class ShopsWarehouse {
    private BufferedReader fileReader = null;
    private String tempNameInvoice = "";
    private String[] data;
    private String tempDate = "";
    private Integer tempQtyShip = 0;
    private String tempMaterial = "";
    private String tempDescription = "";
    private String tempSize = "";
    private String tempUnitPrice = "";
    private Double tempDoubleUnitPrice = 0.00;
    private String tempExtendedPrice = "";
    private Boolean safetySwitchQtyShip = false;
    private Boolean safetySwitchMaterial = false;
    private Boolean safetySwitchDescription = false;
    private Boolean safetySwitchSize = false;
    private Boolean safetySwitchUnitPrice = false;
    private Boolean safetySwitchExtendedPrice = false;
    private int counterQtyShip = 0;
    private int generalCounter = 0;
    private Double tempTotalCost;
    private String tempInvoice;
    private Boolean safetySwitchCorrectRow = false;
    private Cell cell;
    private Boolean cellToCheckingCenaUSED = false;
    private Boolean cellToCheckingRoaming = false;
    private Boolean cellToCheckingNewFormatProgram = false;
    public ArrayList<DataFormat> warehouse = new ArrayList<DataFormat>();
    private FileInputStream fis;
    private XSSFWorkbook wb;
    private Double tempValuePLN;
    private Double tempUnitCost;
    private Integer tempQty;
    private int qtyMainMagazin;
    private int qtySalesMagazin;
    private int sizeMagazinZ;

    public void writingTextFromPDFtoMagazin() {

        try {
            fileReader = new BufferedReader(new FileReader("tempTextFile.txt"));
            System.out.println("\n\nOdczyt buforowany:\n");
            String readingTextLine = fileReader.readLine();
            readTextLine(readingTextLine);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void settingsExchangeRate(DownloadExchangeRateFromURL tabelaNBP) {
        DownloadExchangeRateFromURL tabela;
        tabela = tabelaNBP;

        int size = warehouse.size();

        String tempDate2;
        LocalDate localDate;
        String lastDate = "";
        Double lastExchangeRate = 0.00;

        for (int i = 0; i < size; i++) {

            System.out.println("I: " + i);

            if (warehouse.get(i).getExchangeRate() == 0) {

                localDate = LocalDate.parse(warehouse.get(i).getDate());
                localDate = localDate.minusDays(1);
                tempDate2 = localDate.toString().replace("-", "");


                System.out.println("I: " + i + " tempDate: " + tempDate2 + " lastDate: " + lastDate);

                if (tempDate2.contentEquals(lastDate)) {

                    warehouse.get(i).setExchangeRate(lastExchangeRate);
                    System.out.println("I: " + i + "bez obliczeñ zrobione");

                } else {
                    lastDate = tempDate2;

                    while (!tabela.tabelaNBP.containsKey(tempDate2)) {

                        localDate = localDate.minusDays(1);
                        tempDate2 = localDate.toString().replace("-", "");

                        System.out.println("I: " + i + "szukam daty " + tempDate2);

                    }
                    Double returnValue = tabela.tabelaNBP.get(tempDate2);
                    lastExchangeRate = returnValue;
                    warehouse.get(i).setExchangeRate(returnValue);

                    System.out.println("I: " + i + "Obliczy³em dla daty " + tempDate2 + " kurs " + returnValue);
                }
            }
        }

    }

    public void readMainExcel(String path) throws IOException {
        try {

            System.out.println("STEFAN 2");


            String pathString = path;

            System.out.println("sciezka "+pathString);

            File file = new File(pathString); // creating a new file instance
            FileInputStream fis = new FileInputStream(file); // obtaining bytes from the file
            // creating Workbook instance that refers to .xlsx file
            System.out.println("STEFAN 2");
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0); // creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator(); // iterating over excel file

            System.out.println("STEFAN 1");

            while (itr.hasNext()) {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator(); // iterating over each column

                while (cellIterator.hasNext()) {
                    cell = cellIterator.next();
                    checkThatColumnContainsTitle();

                    if (Boolean.TRUE.equals(safetySwitchCorrectRow)) {

                        String tempStringDate = cell.toString().trim();
                        String data_ = tempStringDate.replace(".", "-");
                        data = data_.split("-");
                        changeLiteralMonthToNumber();
                        checkWhichColumnContainsDataWithLengthFour();

                        cell = cellIterator.next();
                        tempInvoice = cell.toString().replace(".", "").replace("E9", "");

                        cell = cellIterator.next();
                        tempMaterial = cell.toString().trim();

                        cell = cellIterator.next();
                        tempDescription = cell.toString().trim();

                        cell = cellIterator.next();
                        tempSize = cell.toString().trim();

                        cell = cellIterator.next();
                        tempUnitCost = Double.parseDouble(cell.toString().trim());

                        cell = cellIterator.next();
                        Double tempQtyDouble = Double.parseDouble(cell.toString().trim());
                        tempQty = tempQtyDouble.intValue();

                        cell = cellIterator.next();
                        tempTotalCost = Double.parseDouble(cell.toString().trim());

                        if (cellToCheckingNewFormatProgram) {
                            cell = cellIterator.next();
                        }

                        cell = cellIterator.next();
                        tempValuePLN = Double.parseDouble(cell.toString().trim());

                        if (Boolean.TRUE.equals(cellToCheckingNewFormatProgram)) {
                            cell = cellIterator.next();
                        }

                        System.out.println("DATA: " + tempDate + " Material: " + tempMaterial + " QTY: " + tempQty
                                + " TempUnitCost: " + tempUnitCost + " TempTotalCost: " + tempTotalCost
                                + " TempDescription: " + tempDescription + " TempValuePLN " + tempValuePLN);

                        addWarehouseIfTempQtyIsMoreThenZero();
                    }
                }
                checkingCenaUsdAndRozmiar();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        System.out.println("Zakończyłem zapis danych");
    }

    public void calculate(ReadExcelSales tempSales) {
        try {
            tempMaterial = "";
            int sizeTempSales = tempSales.magazin.size();
            sizeMagazinZ = 0;

            for (int i = 0; i < sizeTempSales; i++) {
                tempMaterial = tempSales.magazin.get(i).getMaterial().substring(0, 11);
                //System.out.println("I---->:"+i+" "+tempMaterial+" "+tempSales.magazin.get(i).getQtyShip());
                sizeMagazinZ = this.warehouse.size();

                qtyMainMagazin = 0;
                qtySalesMagazin = 0;

                for (int z = 0; z < sizeMagazinZ; z++) {

                    qtyMainMagazin = this.warehouse.get(z).getQtyShip();
                    qtySalesMagazin = tempSales.magazin.get(i).getQtyShip();

                    if (this.warehouse.get(z).getMaterial().contains(tempMaterial) && qtyMainMagazin == qtySalesMagazin) {
                        tempSales.magazin.get(i).setConnectDocuments(this.warehouse.get(z).getNameInvoice() + " (" + this.warehouse.get(z).getQtyShip().toString() + ") ");
                        tempSales.magazin.get(i).setValueConnectDocuments(this.warehouse.get(z).getQtyShip() * this.warehouse.get(z).getUnityPrice() * this.warehouse.get(z).getExchangeRate());
                        this.warehouse.get(z).setConnectDocuments(tempSales.magazin.get(i).getNameInvoice() + " (" + tempSales.magazin.get(i).getQtyShip().toString() + ") ");
                        this.warehouse.get(z).setQtyShip(0);
                        tempSales.magazin.get(i).setQtyShip(0);

                        break;
                    }
                }

                analizeIfmagazinGetQtyShipisNotEquallZero(i, tempSales);

                if (tempSales.magazin.get(i).getQtyShip() > 0)
                    System.out.println("I------------------------------------->:" + i + " " + tempSales.magazin.get(i).getQtyShip());
            }


	/* usuwanie pozycji wersja testowa
	for(int i=sizeTempSales-1;i>=0;i--) {
		if(tempSales.magazin.get(i).getQtyShip()==0) {
			tempSales.magazin.remove(i);
		}
	}

	for(int i=sizeMagazinZ-1;i>=0;i--) {
		if(this.magazin.get(i).getQtyShip()==0) {
			this.magazin.remove(i);
		}
	}

	System.out.println("Zosta³o pozycji sprzedazy: "+sizeTempSales);
	System.out.println("Zosta³o pozycji na magazynie: "+sizeMagazinZ);
	*/
        } catch (Exception e) {
            System.out.println(e + " dzialanie funkcji read zakonczylo sie niepowodzeniem");
        }

        int size = this.warehouse.size();

        for (int i = 0; i < size; i++) {
            double priceUSD = ((this.warehouse.get(i).getQtyShip() * this.warehouse.get(i).getUnityPrice()) * 100);
            priceUSD = Math.round(priceUSD);
            priceUSD /= 100;
            this.warehouse.get(i).setExtendedPrice(priceUSD);


            double pricePLN = ((this.warehouse.get(i).getExtendedPrice() * this.warehouse.get(i).getExchangeRate()) * 100);
            pricePLN = Math.round(pricePLN);
            pricePLN /= 100;
            this.warehouse.get(i).setValuePLN(pricePLN);
        }
    }

    public String endResultToViewOnScreen() {
        int size = this.warehouse.size();
        Double priceUSD = 0.00;
        Double pricePLN = 0.00;
        for (int i = 0; i < size; i++) {
            priceUSD += this.warehouse.get(i).getExtendedPrice();
            pricePLN += this.warehouse.get(i).getValuePLN();
        }
        priceUSD *= 100;
        priceUSD = (double) Math.round(priceUSD);
        priceUSD /= 100;
        String USD = priceUSD.toString();
        pricePLN *= 100;
        pricePLN = (double) Math.round(pricePLN);
        pricePLN /= 100;
        String PLN = pricePLN.toString();
        String endScreen = "WARTOSC W SUMIE USD: " + USD + "\n WARTOSC W SUMIE PLN " + PLN + "\n";
        return endScreen;
    }

    public void changeLiteralMonthToNumber() {
        if (data[1].equals("sty"))
            data[1] = "1";
        if (data[1].equals("lut"))
            data[1] = "2";
        if (data[1].equals("mar"))
            data[1] = "3";
        if (data[1].equals("kwi"))
            data[1] = "4";
        if (data[1].equals("maj"))
            data[1] = "5";
        if (data[1].equals("cze"))
            data[1] = "6";
        if (data[1].equals("lip"))
            data[1] = "7";
        if (data[1].equals("sie"))
            data[1] = "8";
        if (data[1].equals("wrz"))
            data[1] = "9";
        if (data[1].equals("paz") || data[1].equals("paź"))
            data[1] = "10";
        if (data[1].equals("lis"))
            data[1] = "11";
        if (data[1].equals("gru"))
            data[1] = "12";
    }

    void checkThatColumnContainsTitle() {
        if (cell.toString().contains("faktury"))
            cellToCheckingNewFormatProgram = true;
        if (cell.toString().contains("cena USD"))
            cellToCheckingCenaUSED = true;
        if (cell.toString().contains("rozmiar"))
            cellToCheckingRoaming = true;
    }

    void checkWhichColumnContainsDataWithLengthFour() {
        if (data[2].length() == 4) {
            Integer tempYear = Integer.parseInt(data[2]);
            Integer tempMonth = Integer.parseInt(data[1]);

            if (data[0].indexOf("0") == 0) {
                data[0] = data[0].replace("0", "");
            }
            Integer tempDay = Integer.parseInt(data[0]);
            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);
        } else if (data[0].length() == 4) {
            Integer tempYear = Integer.parseInt(data[0]);
            Integer tempMonth = Integer.parseInt(data[1]);

            if (data[2].indexOf("0") == 0) {
                data[2] = data[2].replace("0", "");
            }
            Integer tempDay = Integer.parseInt(data[2]);
            tempDate = (tempYear + "-" + tempMonth + "-" + tempDay);
        }
    }

    void checkingCenaUsdAndRozmiar() {
        if (cellToCheckingCenaUSED && cellToCheckingRoaming) {
            safetySwitchCorrectRow = true;
            cellToCheckingCenaUSED = false;
            cellToCheckingRoaming = false;
        }
    }

    void addWarehouseIfTempQtyIsMoreThenZero() {
        if (tempQty > 0) {
            warehouse.add(new DataFormat(tempDate, tempInvoice, tempMaterial, tempDescription, tempSize,
                    tempUnitCost, tempQty, tempTotalCost, 0.0, tempValuePLN));
        }
    }


    void readTextLine(String readingTextLine) throws IOException {
        while (readingTextLine != null) {
            readingTextLine = fileReader.readLine();

            if (readingTextLine == null)
                break;

            // process the line
            readingTextLineContainsDifferentWords(readingTextLine);
        }
    }

    void readingTextLineContainsDifferentWords(String readingTextLine) {
        if (readingTextLine.contains("Invoice Number:")) {
            tempNameInvoice = readingTextLine.replace("Invoice Number:", "").trim();
        } else if (readingTextLine.contains("Date:")) {
            tempDate = readingTextLine.replace("Date:", "");
            data = tempDate.trim().split("/");
            tempDate = (data[2] + "-" + data[0] + "-" + data[1]);
        } else if (readingTextLine.contains("QTY SHIP")) {
            safetySwitchQtyShip = true;
            counterQtyShip = 0;
        } else if (readingTextLine.contains("MATERIAL #")) {
            safetySwitchQtyShip = false;
            safetySwitchMaterial = true;
            generalCounter = (warehouse.size() - counterQtyShip);
        } else if (readingTextLine.contains("DESCRIPTION")) {
            safetySwitchMaterial = false;
            safetySwitchDescription = true;
            generalCounter = (warehouse.size() - counterQtyShip);
        } else if (readingTextLine.contains("SIZE")) {
            safetySwitchDescription = false;
            safetySwitchSize = true;
            generalCounter = (warehouse.size() - counterQtyShip);
        } else if (readingTextLine.contains("UNIT PRICE")) {
            safetySwitchSize = false;
            safetySwitchUnitPrice = true;
            generalCounter = (warehouse.size() - counterQtyShip);
        } else if (readingTextLine.contains("EXTENDED PRICE")) {
            safetySwitchUnitPrice = false;
            safetySwitchExtendedPrice = true;
            generalCounter = (warehouse.size() - counterQtyShip);
        } else if (Boolean.TRUE.equals( safetySwitchQtyShip)) {
            tempQtyShip = Integer.parseInt(readingTextLine);
            counterQtyShip++;
            warehouse.add(
                    new DataFormat(tempDate, tempNameInvoice, "", "", "", 0.00, tempQtyShip, 0.00, 0.00, 0.00));
        } else if (Boolean.TRUE.equals( safetySwitchMaterial)) {
            tempMaterial = readingTextLine;
            warehouse.get(generalCounter).setMaterial(tempMaterial);
            generalCounter++;
        } else if (Boolean.TRUE.equals( safetySwitchDescription)) {
            tempDescription = readingTextLine;
            warehouse.get(generalCounter).setDescription(tempDescription);
            generalCounter++;
        } else if (Boolean.TRUE.equals(safetySwitchSize)) {
            tempSize = readingTextLine.trim();
            warehouse.get(generalCounter).setSize(readingTextLine);
            generalCounter++;
        } else if (Boolean.TRUE.equals(safetySwitchUnitPrice)) {
            tempUnitPrice = readingTextLine.trim();
            tempDoubleUnitPrice = Double.parseDouble(tempUnitPrice);
            warehouse.get(generalCounter).setUnityPrice(tempDoubleUnitPrice);
            generalCounter++;
        } else if (Boolean.TRUE.equals(safetySwitchExtendedPrice)) {
            tempExtendedPrice = readingTextLine.replace(",", "").trim();
            Double tempDoubleExtendedPrice = Double.parseDouble(tempExtendedPrice);
            warehouse.get(generalCounter).setExtendedPrice(tempDoubleExtendedPrice);
            generalCounter++;
            generalCounterIsEqualWarehouseSize();
        }
    }

    void generalCounterIsEqualWarehouseSize() {
        if (generalCounter == warehouse.size()) {
            safetySwitchExtendedPrice = false;
        }
    }

    void analizeIfmagazinGetQtyShipisNotEquallZero(int i, ReadExcelSales tempSales) {
        if (tempSales.magazin.get(i).getQtyShip() != 0) {

            for (int z = 0; z < sizeMagazinZ; z++) {

                if ((this.warehouse.get(z).getMaterial().contains(tempMaterial)) && (this.warehouse.get(z).getQtyShip() > 0)) {

                    //System.out.println("Z: "+z+" "+this.magazin.get(z).getMaterial()+" "+this.magazin.get(z).getQtyShip());

                    qtyMainMagazin = this.warehouse.get(z).getQtyShip();
                    qtySalesMagazin = tempSales.magazin.get(i).getQtyShip();

                    if (qtyMainMagazin < qtySalesMagazin) {

                        int tempQ = tempSales.magazin.get(i).getQtyShip();
                        int tempMinusQ = this.warehouse.get(z).getQtyShip();

                        tempSales.magazin.get(i).setConnectDocuments(this.warehouse.get(z).getNameInvoice() + " (" + this.warehouse.get(z).getQtyShip().toString() + ") ");
                        tempSales.magazin.get(i).setValueConnectDocuments(this.warehouse.get(z).getQtyShip() * this.warehouse.get(z).getUnityPrice() * this.warehouse.get(z).getExchangeRate());

                        this.warehouse.get(z).setConnectDocuments(tempSales.magazin.get(i).getNameInvoice() + " (" + this.warehouse.get(z).getQtyShip().toString() + ") ");
                        //System.out.println(z+" o1 "+this.magazin.get(z).getQtyShip()+" "+tempSales.magazin.get(i).getQtyShip());
                        tempSales.magazin.get(i).setQtyShip(tempQ - tempMinusQ);
                        this.warehouse.get(z).setQtyShip(0);

                        //System.out.println(z+" o1 "+this.magazin.get(z).getQtyShip()+" "+tempSales.magazin.get(i).getQtyShip());


                    } else if (qtyMainMagazin == qtySalesMagazin) {
                        //System.out.println(z+" o2 "+this.magazin.get(z).getQtyShip()+" "+tempSales.magazin.get(i).getQtyShip());
                        tempSales.magazin.get(i).setConnectDocuments(this.warehouse.get(z).getNameInvoice() + " (" + this.warehouse.get(z).getQtyShip().toString() + ") ");
                        tempSales.magazin.get(i).setValueConnectDocuments(this.warehouse.get(z).getQtyShip() * this.warehouse.get(z).getUnityPrice() * this.warehouse.get(z).getExchangeRate());

                        this.warehouse.get(z).setConnectDocuments(tempSales.magazin.get(i).getNameInvoice() + " (" + tempSales.magazin.get(i).getQtyShip().toString() + ") ");

                        this.warehouse.get(z).setQtyShip(0);
                        tempSales.magazin.get(i).setQtyShip(0);
                        //System.out.println(z+" o2 "+this.magazin.get(z).getQtyShip()+" "+tempSales.magazin.get(i).getQtyShip());

                        break;
                    } else if (qtyMainMagazin > qtySalesMagazin) {

                        int tempQ = this.warehouse.get(z).getQtyShip();
                        int tempMinusQ = tempSales.magazin.get(i).getQtyShip();
                        //System.out.println(z+" o3 "+this.magazin.get(z).getQtyShip()+" "+tempSales.magazin.get(i).getQtyShip());

                        tempSales.magazin.get(i).setConnectDocuments(this.warehouse.get(z).getNameInvoice() + " (" + tempSales.magazin.get(i).getQtyShip() + ") ");
                        tempSales.magazin.get(i).setValueConnectDocuments(tempSales.magazin.get(i).getQtyShip() * this.warehouse.get(z).getUnityPrice() * this.warehouse.get(z).getExchangeRate());

                        this.warehouse.get(z).setConnectDocuments(tempSales.magazin.get(i).getNameInvoice() + " (" + tempSales.magazin.get(i).getQtyShip().toString() + ") ");

                        this.warehouse.get(z).setQtyShip(tempQ - tempMinusQ);
                        this.warehouse.get(z).setExtendedPrice(this.warehouse.get(z).getQtyShip() * this.warehouse.get(z).getUnityPrice());
                        this.warehouse.get(z).setValuePLN(this.warehouse.get(z).getExtendedPrice() * warehouse.get(z).getExchangeRate());

                        tempSales.magazin.get(i).setQtyShip(0);

                        //System.out.println(z+" o3 "+this.magazin.get(z).getQtyShip()+" "+tempSales.magazin.get(i).getQtyShip());
                        break;
                    }
                }
            }
        }
    }

}
