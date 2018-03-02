package Hack;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

public class PdfCreator {

    private int documentNumber;
    private File file;
    private PDDocument document;
    private PDFTextStripper pdfTextStripper;

    public PdfCreator(){

    }

    public void createPDF(String text, String path) throws Exception{
        File file = new File(path);
        //PDDocument doc = PDDocument.load(file);
        PrintWriter writer = new PrintWriter("C:/hack/ELA.txt", "ASCII");
        writer.print(text);
        writer.close();
        Scanner fileIn = new Scanner(new File("C:/hack/ELA.txt"));
        PDDocument document = new PDDocument();
        text = "";

        System.out.println(text);
        PDPage my_page = new PDPage();
        document.addPage(my_page);
        PDPage page = document.getPage(0);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();

        contentStream.setFont(PDType1Font.COURIER, 14);

        contentStream.setLeading(16.5f);

        contentStream.newLineAtOffset(25, 725);

        int counter = 0;
        while(fileIn.hasNext()){
            contentStream.showText(fileIn.next() + " ");
            if(counter%9 == 0){
                contentStream.newLine();
            }
            if(counter == 300){
                contentStream.endText();
                contentStream.close();
                PDPage new_page = new PDPage();
                document.addPage(new_page);
                page = document.getPage(1);
                contentStream = new PDPageContentStream(document, page);
                contentStream.beginText();
                contentStream.setFont(PDType1Font.COURIER, 14);

                contentStream.setLeading(16.5f);

                contentStream.newLineAtOffset(25, 725);
                counter = 0;
            }
            counter++;
        }

        contentStream.endText();

        contentStream.close();

        //Saving the document
        document.save(new File("C:/hack/new.pdf"));

        //Closing the document
        document.close();
    }
}
