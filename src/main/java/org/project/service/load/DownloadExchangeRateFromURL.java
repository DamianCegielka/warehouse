package org.project.service.load;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

public class DownloadExchangeRateFromURL {

    String[] SplitRate;
    String Date;
    Double Rate;
    public Map<String, Double> tabelaNBP = new TreeMap();

    public void rate() {
        try {
            String tableFromNBP2020="https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2020.csv";
            String tableFromNBP2021 = "https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2021.csv";
            String tableFromNBP2022 = "https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2022.csv";
            String tableFromNBP2023 = "https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2023.csv";

            this.downloadDataFromNBP(tableFromNBP2020);
            this.downloadDataFromNBP(tableFromNBP2021);
            this.downloadDataFromNBP(tableFromNBP2022);
            this.downloadDataFromNBP(tableFromNBP2023);

            System.out.println("Pobieranych danych serwerowych zakonczone powodzeniem");
        } catch (Exception e) {
            System.out.println("Pobieranych danych zakonczone bledem");
            e.printStackTrace();
        }
    }

    private void downloadDataFromNBP(String link) throws IOException {
        URL url = new URL(link);
        URLConnection urlCon = url.openConnection();
        BufferedReader read = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

        String tmp;

        read.readLine();
        read.readLine();

        while ((tmp = read.readLine()) != null) {
            SplitRate = tmp.split(";");
            Date = SplitRate[0];
            if (Date.trim().length() != 8) break;
            Rate = Double.parseDouble(SplitRate[2].replaceAll(",", "."));
            tabelaNBP.put(Date, Rate);
            System.out.println(Date + " " + Rate);
        }
        read.close();
    }
}

