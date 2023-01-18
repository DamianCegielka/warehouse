package org.project.view;

import org.project.repository.ShopsWarehouse;
import org.project.repository.export.ExportDataToExcel;
import org.project.repository.export.ExportSalesDataToExcel;
import org.project.repository.load.DownloadExchangeRateFromURL;
import org.project.repository.load.PDFBoxReadFromFile;
import org.project.repository.load.ReadExcelSales;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Pattern;

public class MainFrame extends JFrame implements ActionListener {


    private final JButton buttonLoadWarehouse;
    private final JButton buttonLoadSales;
    private final JButton buttonLoadDelivers;
    private final JButton buttonCalculate;
    private final JButton buttonCurrentRate;
    private final JButton buttonReloadTable;
    private final JButton buttonExport;
    private JPanel panelWest;
    private JPanel panelCenter;
    private JPanel panelSouth;
    private JTextArea textArea;
    private JLabel label;
    private JScrollPane scroll;
    private JScrollPane scrollTable;
    private JScrollPane scrollTable2;
    private JTable dataTableWarehouse;
    private JTable dataTableWarehouse2;
    private DefaultTableModel model;
    private DefaultTableModel model2;
    private ShopsWarehouse shops;
    private ReadExcelSales readExc;
    private DownloadExchangeRateFromURL download;
    private File[] openFiles;
    private String pathString;
    private boolean safetySwitchCleanShopsMagazin;
    private boolean safetySwitchCleanSalesMagazin;

    private ExportDataToExcel dataToExport;
    private ExportSalesDataToExcel salesToExport;

    int countingWarehouse;
    int countingSales;
    int sizeWarehouse;

    private static final String JEST_AKTUALNIE = " i jest aktualnie ";
    private static final String REKORDOW_ZAKUPOW = " rekordów zakupów";

    private static final String FORMAT_FOR_LOAD_WAREHOUSE = "Uwaga! Wymagany format:data, nr faktury,indeks, nazwa, rozmiar, cena jedno., sztuki, cenaUSD, cenaPLN" + '\n';

    private static final String FILL_EMPTY_FIELDS = "Uwaga! Wype³nij puste komórki w pliku" + '\n';

    private static final String PATH_FOR_ADMIN = "C:\\Users\\Damian\\Desktop";

    public MainFrame() {

        shops = new ShopsWarehouse();
        readExc = new ReadExcelSales();
        dataToExport = new ExportDataToExcel();
        salesToExport = new ExportSalesDataToExcel();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.pack();
        this.setSize(1500, 850);
        this.setResizable(true);
        this.setTitle("PROGRAM DO ANALIZY MAGAZYNU wersja 1.7");
        this.setBackground(Color.BLACK);
        this.setVisible(true);

        ImageIcon image = new ImageIcon("logo_compta.png");

        this.setIconImage(image.getImage());

        panelWest = new JPanel();
        panelWest.setBackground(Color.GRAY);
        panelWest.setPreferredSize(new Dimension(200, 500));
        this.add(panelWest, BorderLayout.WEST);

        panelCenter = new JPanel();
        panelCenter.setBackground(Color.lightGray);
        panelCenter.setPreferredSize(new Dimension(1600, 400));
        this.add(panelCenter, BorderLayout.CENTER);

        panelSouth = new JPanel();
        panelSouth.setBackground(Color.GRAY);
        panelSouth.setPreferredSize(new Dimension(200, 100));
        this.add(panelSouth, BorderLayout.SOUTH);

        buttonCurrentRate = new JButton("Pobierz kursy walut");
        buttonCurrentRate.setPreferredSize(new Dimension(180, 50));
        buttonCurrentRate.addActionListener(this);
        buttonCurrentRate.setFocusable(false);
        panelWest.add(buttonCurrentRate, BorderLayout.NORTH);

        buttonLoadWarehouse = new JButton("Wczytaj magazyn *.xlsx");
        buttonLoadWarehouse.setPreferredSize(new Dimension(180, 50));
        buttonLoadWarehouse.addActionListener(this);
        buttonLoadWarehouse.setFocusable(false);
        panelWest.add(buttonLoadWarehouse, BorderLayout.NORTH);

        buttonLoadDelivers = new JButton("Wczytaj zakupy *.pdf");
        buttonLoadDelivers.setPreferredSize(new Dimension(180, 50));
        buttonLoadDelivers.addActionListener(this);
        buttonLoadDelivers.setFocusable(false);
        panelWest.add(buttonLoadDelivers, BorderLayout.NORTH);

        buttonLoadSales = new JButton("Wczytaj sprzedaz *.xlsx");
        buttonLoadSales.setPreferredSize(new Dimension(180, 50));
        buttonLoadSales.addActionListener(this);
        buttonLoadSales.setFocusable(false);
        panelWest.add(buttonLoadSales, BorderLayout.NORTH);

        buttonCalculate = new JButton("OBLICZ");
        buttonCalculate.setPreferredSize(new Dimension(180, 50));
        buttonCalculate.setBackground(Color.green);
        buttonCalculate.addActionListener(this);
        buttonCalculate.setFocusable(false);
        panelWest.add(buttonCalculate, BorderLayout.SOUTH);

        buttonReloadTable = new JButton("Odswiez tabele");
        buttonReloadTable.setPreferredSize(new Dimension(180, 50));
        buttonReloadTable.addActionListener(this);
        buttonReloadTable.setFocusable(false);
        panelWest.add(buttonReloadTable, BorderLayout.NORTH);

        buttonExport = new JButton("Eksportuj");
        buttonExport.setPreferredSize(new Dimension(180, 50));
        buttonExport.addActionListener(this);
        buttonExport.setFocusable(false);
        panelWest.add(buttonExport, BorderLayout.NORTH);

        label = new JLabel("LOGS INFORMATION ");
        panelSouth.add(label, BorderLayout.NORTH);
        this.setVisible(true);

        textArea = new JTextArea(5, 40);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setVisible(true);

        panelSouth.add(textArea, BorderLayout.CENTER);

        scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panelSouth.add(scroll, BorderLayout.CENTER);
        this.setVisible(true);

        model = new DefaultTableModel();
        model.addColumn("DATA");
        model.addColumn("FAKTURA");
        model.addColumn("MATERIAL");
        model.addColumn("OPIS");
        model.addColumn("ROZMIAR");
        model.addColumn("CENA JEDN.");
        model.addColumn("ILOSC");
        model.addColumn("CENA CALA");
        model.addColumn("KURS WALUTY");
        model.addColumn("KWOTA PLN");

        model2 = new DefaultTableModel();
        model2.addColumn("DATA");
        model2.addColumn("FAKTURA");
        model2.addColumn("MATERIAL");
        model2.addColumn("OPIS");
        model2.addColumn("IL. POCZ.");
        model2.addColumn("CENA JEDN.");
        model2.addColumn("ILOSC");
        model2.addColumn("CENA CALA");
        model2.addColumn("KURS WALUTY");
        model2.addColumn("KWOTA PLN");

        dataTableWarehouse = new JTable(model);
        dataTableWarehouse.setPreferredScrollableViewportSize(new Dimension(1200, 300));

        dataTableWarehouse2 = new JTable(model2);
        dataTableWarehouse2.setPreferredScrollableViewportSize(new Dimension(1200, 300));

        scrollTable = new JScrollPane(dataTableWarehouse);
        scrollTable.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTable.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panelCenter.add(scrollTable, BorderLayout.CENTER);
        this.setVisible(true);

        scrollTable2 = new JScrollPane(dataTableWarehouse2);
        scrollTable2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollTable2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        panelCenter.add(scrollTable2, BorderLayout.CENTER);
        this.setVisible(true);

        this.model2 = (DefaultTableModel) dataTableWarehouse2.getModel();
        this.model = (DefaultTableModel) dataTableWarehouse.getModel();

    }

    public void addingNewRowToTable(int i) {

        this.model.addRow(new Object[]{shops.warehouse.get(i).getDate(),
                shops.warehouse.get(i).getNameInvoice(), shops.warehouse.get(i).getMaterial(),
                shops.warehouse.get(i).getDescription(), shops.warehouse.get(i).getSize(),
                shops.warehouse.get(i).getUnityPrice().toString(), shops.warehouse.get(i).getQtyShip().toString(),
                shops.warehouse.get(i).getExtendedPrice().toString(), shops.warehouse.get(i).getExchangeRate().toString(),
                shops.warehouse.get(i).getValuePLN().toString()});
        this.setVisible(true);

    }

    public void addingNewRowToTableSales(int i) {

        this.model2.addRow(new Object[]{readExc.magazin.get(i).getDate(),
                readExc.magazin.get(i).getNameInvoice(), readExc.magazin.get(i).getMaterial(),
                readExc.magazin.get(i).getDescription(), readExc.magazin.get(i).getSize(),
                readExc.magazin.get(i).getUnityPrice().toString(), readExc.magazin.get(i).getQtyShip().toString(),
                readExc.magazin.get(i).getExtendedPrice().toString(),
                readExc.magazin.get(i).getExchangeRate().toString(), readExc.magazin.get(i).getValuePLN().toString()});
        this.setVisible(true);

    }

    public void removeTable(int i) {
        this.model.removeRow(i);
    }

    public void removeTable2(int i) {
        this.model2.removeRow(i);
    }

    public void actionPerformedMethod(String formatToFilterArgument) {

        JFileChooser fileChoser = new JFileChooser();

        String formatWithDot = "*." + formatToFilterArgument;

        fileChoser.removeChoosableFileFilter(fileChoser.getAcceptAllFileFilter());
        fileChoser.addChoosableFileFilter(new FileNameExtensionFilter(formatWithDot, formatToFilterArgument));
        fileChoser.setCurrentDirectory(new File(PATH_FOR_ADMIN));
        fileChoser.setMultiSelectionEnabled(true);
        int respone = fileChoser.showOpenDialog(null);

        if (respone == JFileChooser.APPROVE_OPTION) {
            openFiles = fileChoser.getSelectedFiles();

            textArea.append(" Pobrano: " + '\n');
            for (File path : openFiles) {
                pathString = path.toString();

                String separator = "\\";
                String[] arrayWordsPath = pathString.replaceAll(Pattern.quote(separator), "\\\\").split("\\\\");
                int numbersArrayWordsPath = (arrayWordsPath.length - 1);

                textArea.append(arrayWordsPath[numbersArrayWordsPath] + ";  ");
                textArea.append("SCIEZKA: " + pathString + "\n");
            }
            textArea.append("\n");

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == buttonLoadWarehouse) {
            try {
                buttonLoadWarehouse();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if (e.getSource() == buttonLoadDelivers) {
            buttonLoadDelivers();
        }

        if (e.getSource() == buttonLoadSales) {
            buttonLoadSales();
        }

        if (e.getSource() == buttonCurrentRate) {
            buttonCurrentRate();
        }

        if (e.getSource() == buttonCalculate) {
            buttonCalculate();
        }

        if (e.getSource() == buttonReloadTable) {
            buttonReloadTable();
        }

        if (e.getSource() == buttonExport) {
            buttonExport();
        }
    }

    void buttonLoadWarehouse() throws IOException {
        actionPerformedMethod("xlsx");
        textArea.append(FORMAT_FOR_LOAD_WAREHOUSE);
        textArea.append(FILL_EMPTY_FIELDS);
        shops.readMainExcel(pathString);
        shops.settingsExchangeRate(download);
        sizeWarehouse = 0;
        sizeWarehouse = shops.warehouse.size();
        addNewRowsToTableBasedOnSizeWarehouse(sizeWarehouse);
        textArea.append(JEST_AKTUALNIE + sizeWarehouse + REKORDOW_ZAKUPOW + '\n');
    }

    void buttonLoadDelivers() {
        actionPerformedMethod("pdf");
        sizeWarehouse = 0;
        openFilesInLoopsVersion1();
        textArea.append(JEST_AKTUALNIE + sizeWarehouse + REKORDOW_ZAKUPOW + '\n');
    }

    void buttonLoadSales() {
        actionPerformedMethod("xlsx");
        sizeWarehouse = 0;
        openFilesInLoopsVersion2();
        textArea.append(JEST_AKTUALNIE + sizeWarehouse + " rekordów sprzedazy" + '\n');
    }

    void buttonCurrentRate() {

        try {
            download = new DownloadExchangeRateFromURL();
            download.rate();
            int sizeMagazin = download.tabelaNBP.size();
            textArea.append(" Pobrano poprawnie " + sizeMagazin + " rekordów kursu walut " + '\n');

        } catch (Exception z) {
            textArea.append(" Pobieranie kursu walut nie powiodlo sie \n");
        }
    }

    void buttonCalculate() {

        try {
            shops.calculate(readExc);
            textArea.append(" Obliczenia zosta³y zakoñczone powodzeniem\n");
            textArea.append(shops.endResultToViewOnScreen());
        } catch (Exception b) {
            textArea.append(" Obliczenie nie powiod³o siê \n");
        }
    }

    void buttonReloadTable() {

        try {
            sizeWarehouse = shops.warehouse.size();
            safetySwitchCleanShopsMagazin = false;
            safetySwitchCleanSalesMagazin = false;
            removeRowsFromWarehouse(sizeWarehouse);
            int sizeWarehouse2 = readExc.magazin.size();
            removeRowsFromWarehouse2(sizeWarehouse2);
            countingWarehouse = 0;
            switchToSecondFormatReading(safetySwitchCleanShopsMagazin, sizeWarehouse);
            countingSales = 0;
            switchToSecondFormatReading2(safetySwitchCleanSalesMagazin, sizeWarehouse2);
            textArea.append(" Tabela rekordów zostala zaktualizowana" + '\n');
            textArea.append(" Jest teraz: " + countingWarehouse + " rekordow zakupu" + '\n');
            textArea.append(" oraz " + countingSales + " rekordow sprzedazy" + '\n');

        } catch (Exception c) {
            System.out.println("Nie udalo sie usunac tabeli i wstawic poprawionych danych");

        }
    }

    void buttonExport() {
        dataToExport = new ExportDataToExcel();
        salesToExport = new ExportSalesDataToExcel();
        try {
            System.out.println("wykonuje eksport");
            dataToExport.export(shops.warehouse);
            salesToExport.export(readExc.magazin);
            System.out.println("wykonoalem eksport");
            textArea.append("Eksport do plikow zakonczony powodzeniem!" + '\n');
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            System.out.println("Eksport nieudany  " + '\n');
        }
    }

    void addNewRowsToTableBasedOnSizeWarehouse(int sizeWarehouse) {
        for (int i = 0; i < sizeWarehouse; i++) {
            addingNewRowToTable(i);
        }
    }

    void addNewRowsToTableBasedOnSizeWarehouse(int startWarehouse, int sizeWarehouse) {
        for (int i = startWarehouse; i < sizeWarehouse; i++) {
            addingNewRowToTable(i);
        }
    }

    void addNewRowsToTableSalesBasedOnStartAndSizeWarehouse(int startWarehouse, int sizeWareHouse) {
        for (int i = startWarehouse; i < sizeWareHouse; i++) {
            addingNewRowToTableSales(i);
        }
    }

    void removeRowsFromWarehouse(int sizeWarehouse) {
        for (int a = sizeWarehouse - 1; a >= 0; a--) {
            //System.out.println("Zakup odjety a: " + a);
            removeTable(a);
            if (a == 0) {
                safetySwitchCleanShopsMagazin = true;
            }
        }
    }

    void removeRowsFromWarehouse2(int sizeWarehouse2) {
        for (int b = sizeWarehouse2 - 1; b >= 0; b--) {
            removeTable2(b);
            if (b == 0) {
                safetySwitchCleanSalesMagazin = true;
            }
        }
    }

    void switchToSecondFormatReading(boolean safetySwitchCleanShopsMagazin, int sizeWarehouse) {
        if (safetySwitchCleanShopsMagazin) {
            for (int c = 0; c < sizeWarehouse; c++) {
                if (shops.warehouse.get(c).getQtyShip() > 0) {
                    //System.out.println("Zakup dodany c: " + c);
                    addingNewRowToTable(c);
                    countingWarehouse++;
                }
            }
        }
    }

    void switchToSecondFormatReading2(boolean safetySwitchCleanSalesMagazin, int sizeWarehouse2) {
        if (safetySwitchCleanSalesMagazin) {
            for (int d = 0; d < sizeWarehouse2; d++) {
                if (readExc.magazin.get(d).getQtyShip() > 0) {
                    //System.out.println("Sprzedaz odjeta d: " + d);
                    addingNewRowToTableSales(d);
                    countingSales++;
                }
            }
        }
    }

    void openFilesInLoopsVersion1() {
        for (File path : openFiles) {
            pathString = path.toString();
            int startWarehouse = shops.warehouse.size();
            PDFBoxReadFromFile pdfReadFromFile = new PDFBoxReadFromFile();
            pdfReadFromFile.pdfBoxReadFile(pathString);
            shops.writingTextFromPDFtoMagazin();
            shops.settingsExchangeRate(download);
            sizeWarehouse = shops.warehouse.size();
            addNewRowsToTableBasedOnSizeWarehouse(startWarehouse, sizeWarehouse);
        }
    }

    void openFilesInLoopsVersion2() {
        for (File path : openFiles) {
            pathString = path.toString();
            int startWarehouse = readExc.magazin.size();
            boolean switchingToAnotherExcelReader = readExc.read(pathString);
            boolean switchingToAnotherExcelReaderNr3 = true;
            boolean switchingToAnotherExcelReaderNr4=true;
            boolean switchingToAnotherExcelReaderNr5=true;

            if (!switchingToAnotherExcelReader) {
                switchingToAnotherExcelReaderNr3 = readExc.readDifferentExcellFormat(pathString);
            }

            if (!switchingToAnotherExcelReaderNr3) {
                switchingToAnotherExcelReaderNr4= readExc.readDifferentExcellFormatNr3(pathString);
            }

            if (!switchingToAnotherExcelReaderNr4) {
                switchingToAnotherExcelReaderNr5= readExc.readDifferentExcellFormatNr4(pathString);
            }

            if (!switchingToAnotherExcelReaderNr5) {
                readExc.readDifferentExcellFormatNr4(pathString);
            }


            sizeWarehouse = readExc.magazin.size();
            addNewRowsToTableSalesBasedOnStartAndSizeWarehouse(startWarehouse, sizeWarehouse);
        }
    }


}
