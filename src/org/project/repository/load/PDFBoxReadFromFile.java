package org.project.repository.load;


import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PDFBoxReadFromFile {

    public void pdfBoxReadFile(String pathFile){

        PDFManager pdfManager = new PDFManager();
        pdfManager.setFilePath(pathFile);
        try {
            pdfManager.toText();
        } catch (IOException ex) {
            Logger.getLogger(PDFBoxReadFromFile.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
