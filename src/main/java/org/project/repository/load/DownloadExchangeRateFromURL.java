package org.project.repository.load;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.TreeMap;

public class DownloadExchangeRateFromURL {

    String[] SplitRate;
    String Date;
    Double Rate;
    public Map<String,Double> tabelaNBP=new TreeMap();

    public void rate() {
        try {

            URL url = new URL("https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2021.csv");
            URLConnection urlCon = url.openConnection();
            BufferedReader read = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

            String tmp;

            read.readLine();
            read.readLine();

            while ((tmp=read.readLine()) != null) {
                SplitRate= tmp.split(";");
                Date=SplitRate[0];
                if(Date.trim().length()!=8) break;
                Rate=Double.parseDouble(SplitRate[2].replaceAll(",", "."));
                tabelaNBP.put(Date,Rate);
                System.out.println(Date+" "+Rate);

            }

            url = new URL("https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2022.csv");
            urlCon = url.openConnection();
            read = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

            read.readLine();
            read.readLine();

            while ((tmp=read.readLine()) != null) {
                SplitRate= tmp.split(";");
                Date=SplitRate[0];
                if(Date.trim().length()!=8) break;
                Rate=Double.parseDouble(SplitRate[2].replaceAll(",", "."));
                tabelaNBP.put(Date,Rate);
                System.out.println(Date+" "+Rate);

            }

            url = new URL("https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2023.csv");
            urlCon = url.openConnection();
            read = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

            read.readLine();
            read.readLine();

            while ((tmp=read.readLine()) != null) {
                SplitRate= tmp.split(";");
                Date=SplitRate[0];
                if(Date.trim().length()!=8) break;
                Rate=Double.parseDouble(SplitRate[2].replaceAll(",", "."));
                tabelaNBP.put(Date,Rate);
                System.out.println(Date+" "+Rate);

            }

            url = new URL("https://www.nbp.pl/kursy/Archiwum/archiwum_tab_a_2020.csv");
            urlCon = url.openConnection();
            read = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));

            read.readLine();
            read.readLine();

            while ((tmp=read.readLine()) != null) {
                SplitRate= tmp.split(";");
                Date=SplitRate[0];
                if(Date.trim().length()!=8) break;
                Rate=Double.parseDouble(SplitRate[2].replaceAll(",", "."));
                tabelaNBP.put(Date,Rate);
                System.out.println(Date+" "+Rate);

            }

            System.out.println("Pobieranych danych serwerowych zakoñczone powodzeniem");

            read.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

