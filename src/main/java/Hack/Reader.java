package Hack;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * Reader class reads a pdf document and provides a text.
 * @author Tomas Andriuskevicius, Danilo Del Busso, Alvaro Rausell
 * @version 1.1
 */
public class Reader {
    private int documentNumber;
    private File file;
    private PDDocument document;
    private PDFTextStripper pdfTextStripper;
    private String text;
    private HashSet<String> listOfKeyWords;
    private ArrayList<String> readSlides;


    public Reader(){
        documentNumber = 0;
        listOfKeyWords = new HashSet<String>();
        readSlides = new ArrayList<String>();
        try{
            pdfTextStripper = new PDFTextStripper();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Loads the pdf document.
     * @param path path to pdf document.
     */
    public void loadDocument(String path){
        try {
            file = new File(path);
            document = PDDocument.load(file);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String read(){
        try{
            text = pdfTextStripper.getText(document);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return text;
    }

    public String read(PDDocument doc){
        try{
            text = pdfTextStripper.getText(doc);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return text;
    }

    public void split(String path){
        loadDocument(path);
        Splitter splitter = new Splitter();
        try{
            List<PDDocument> pages = splitter.split(document);
            Iterator<PDDocument> iterator = pages.listIterator();

            while(iterator.hasNext()){
                String slide = read(iterator.next());
                readSlides.add(slide);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> getReadSlides() {
        return readSlides;
    }

    public HashSet<String> getListOfKeyWords() {
        return listOfKeyWords;
    }

    public String[] keyWordArray(String path) {
        takeKeyWords(path);
        String[] keyWordString = new String[listOfKeyWords.size()];
        keyWordString = listOfKeyWords.toArray(keyWordString);
        return keyWordString;
    }

    public void takeKeyWords(String path){
        split(path);
        for(String text : readSlides){
            int index = text.indexOf("\n");
            listOfKeyWords.add(text.substring(0, index));
        }
        //return listOfKeyWords;
    }

    public void close(){
        try{
            document.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


}
