/*
 * Created by JFormDesigner on Sat Jan 13 16:08:16 GMT 2018
 */

package Hack;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.GroupLayout;
import javax.swing.filechooser.FileNameExtensionFilter;

import io.netty.buffer.ByteBuf;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.knallgrau.utils.textcat.FingerPrint;


/**
 * @author Danilo Del Busso
 */
public class GUI extends JPanel {

    public String transcription = "";
    private String consoleOutput = "";
    private boolean done = false;
    public GUI() {

        initComponents();

        JFrame frame = new JFrame("Alpaca");
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

public void process() throws Exception {
    Reader reader = new Reader();

    String pdfPath = textField2.getText();
    reader.loadDocument(pdfPath);

    //String[] array = reader.keyWordArray("C:/hack/ProgrammingWithLogic3.pdf");
        /*reader.loadDocument("C:/hack/ProgrammingWithLogic3.pdf");
        reader.takeKeyWords("C:/hack/ProgrammingWithLogic3.pdf");
        String doc = reader.read();
        HashSet<String> keys = reader.getListOfKeyWords();
        reader.close();
        reader.loadDocument("C:/hack/ProgrammingWithLogic3+Sol.pdf");
        String speech = reader.read();
        reader.close();
        ClassComparison comp = new ClassComparison(doc, speech, keys);
        comp.checkText();*/
        /*String path="C:/hack/lecture.mp4";
        try { ;
            System.out.println("File is now being converted to .flac");
            VideoToAudio.convertToFLAC(path);
            System.out.print("Converted.");
        }catch (Exception e) {
            e.printStackTrace();
        }

        SpeechRecognition speechRecognition = new SpeechRecognition();
        String str = "";
        for(String line : speechRecognition.recognizeFromFile(pathFlac)) {

            str += line;
        }
        System.out.println(str);*/


    // String[] key = new String[reader.getReadSlides().size()];
    OpenNLP nlp = new OpenNLP();
    String text = reader.read();
    String[] doc = nlp.partOfSpeech(text);
    reader.close();


    text = transcription;
    /*Scanner fileIn = new Scanner("C:/hack/transcription.txt");

    while(fileIn.hasNext()){
        text += fileIn.nextLine();
    }*/

    String[] speech = nlp.partOfSpeech(text);
    reader.close();

    HashMap<Integer, String> newWords = new HashMap<>();
    ArrayList<Integer> position = new ArrayList<>();

    int j;
    boolean contains = true;
    for(int i = 0; i < speech.length;i++ ){
        for(j = 0; j < doc.length;j++){
            if(!speech[i].contains(doc[j]) && !newWords.containsValue(speech[i])){
                contains = false;
            }
            else{
                contains = true;
                break;
            }
        }
        if(contains == false) {
            newWords.put(i, speech[i]);
            position.add(i);
        }
    }

    HashMap<String, String> newText = new HashMap<>();

    Integer[] positionArray = new Integer[position.size()];
    positionArray = position.toArray(positionArray);
    int count = 0;
    for(int i = 0; i < positionArray.length-1; i++){
        if(positionArray[i+1] <= positionArray[i]+400 && i != positionArray.length-2){
            count++;
        }
        else{
            int g = 0;
            if(positionArray[i-count] != 0) {
                while (!(text.substring((positionArray[i - count]) - g, (positionArray[i - count]) - (g - 1))).equals("\n") && positionArray[i-count] - g > 0) {
                    g++;
                }
            }
            int t = 0;
                /*while(!(text.substring((positionArray[i-count]) + 500 + (t-1), (positionArray[i-count])+500+t).equals("."))){
                    t++;
                }*/
            newText.put(newWords.get(positionArray[i]), text.substring(positionArray[i-count] - g, (positionArray[i]+400 <= positionArray[positionArray.length-1]) ? positionArray[i]+400: positionArray[positionArray.length-1]));
        }
    }

    // System.out.println(speech.length + "    " + doc.length + "    "+ newWords.size());

    for(int words : newWords.keySet()){
        //System.out.println(words);
        //System.out.println(newWords.get(words));
    }

    PrintWriter writer = new PrintWriter("C:/hack/ELA.txt", "UTF-8");
    String paragraph = "";
    for(String words : newText.keySet()){
        //System.out.println(newText.get(words));
        //System.out.println("////////////////////////////////////////////////");
        paragraph += newText.get(words);
    }

    writer.print(paragraph);
    writer.close();



    SummaryTool summary = new SummaryTool();
    summary.init("C:/hack/ELA.txt");
    summary.extractSentenceFromContext();
    summary.groupSentencesIntoParagraphs();
    summary.printSentences();
    summary.createIntersectionMatrix();

    //System.out.println("INTERSECTION MATRIX");
    //summary.printIntersectionMatrix();

    summary.createDictionary();
    //summary.printDicationary();

    System.out.println("SUMMARY");
    summary.createSummary();
    PdfCreator pdf = new PdfCreator();
    pdf.createPDF(summary.printSummary(), "C:/hack/new.pdf");


    //summary.printStats();


        /*GoogleNLP google = new GoogleNLP();
        for(String keyWords : reader.takeKeyWords()) {
            google.test(keyWords);
        }*/

    /**
     * video to audio
     */



        /*PrintWriter writer = new PrintWriter("the-file-name.txt", "UTF-8");
        for(String lines : speechRecognition.recognizeFromFile(pathFlac)) {
            writer.println(lines);
        }
        writer.close();*/


        /*Classifier classifier = new Classifier();
        //reader.loadDocument("C:/hack/ELA.pdf");
        reader.split("C:/hack/ProgrammingWithLogic3.pdf");
        ArrayList<String> slides = reader.getReadSlides();
        int index = 0;
        for(String slide : slides){
            classifier.setFP(slide,index);
            index++;
        }
        classifier.createConfig();

        while (true){
            System.out.println("Type a word");
            TextCategorizer textCategorizer = new TextCategorizer();
            textCategorizer.setConfFile("config.conf");
            Scanner sc =new Scanner(System.in);
            String query = sc.nextLine();
            System.out.println(textCategorizer.getCategoryDistances(query.toLowerCase()));
            System.out.println(textCategorizer.categorize(query.toLowerCase()));
        }
*/

}


    private void button1ActionPerformed(ActionEvent e) {
        FileNameExtensionFilter mp4Filter = new FileNameExtensionFilter(
                "mp3 files (*.mp4)", "mp4");
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(mp4Filter);
        fc.setCurrentDirectory(new java.io.File("C:/hack"));
        fc.setDialogTitle("Choose the video");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(button2) == JFileChooser.APPROVE_OPTION) {
            System.out.println(fc.getSelectedFile().getAbsolutePath()+ " has been selected.\n");
            updateConsole(fc.getSelectedFile().getAbsolutePath()+ " has been selected.\n");
            textField1.setText(fc.getSelectedFile().getAbsolutePath());
            textField1.setForeground(Color.black);
        }
    }

    public JTextField getTextField2() {
        return textField2;
    }

    private void button2ActionPerformed(ActionEvent e) {
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter(
                "pdf files (*.pdf)", "pdf");
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(pdfFilter);
        fc.setCurrentDirectory(new java.io.File("C:/hack"));
        fc.setDialogTitle("Choose the pdf");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if (fc.showOpenDialog(button2) == JFileChooser.APPROVE_OPTION) {
            System.out.println(fc.getSelectedFile().getAbsolutePath()+ " has been selected.\n");
            updateConsole(fc.getSelectedFile().getAbsolutePath()+ " has been selected.\n");
            textField2.setText(fc.getSelectedFile().getAbsolutePath());
            textField2.setForeground(Color.black);
        }
    }

    private void button3ActionPerformed(ActionEvent e) throws Exception {

        if(textField1.getText().equalsIgnoreCase("Choose a video file") || textField2.getText().equalsIgnoreCase("Choose a pdf file") ){
            JOptionPane.showMessageDialog(null, "Please choose a video and a pdf");
        }
        else{

            File directory1 = new File("c:/hack/audio");
            File directory2 = new File("c:/hack/video");
            FileUtils.cleanDirectory(directory1);
            FileUtils.cleanDirectory(directory2);



            consoleOutput = "";
            clearConsole();
            scrollPane1.getVerticalScrollBar().setValue(0);
            String path= textField1.getText();

            VideoTrim videoTrimmer = new VideoTrim(path, this);

            //CONVERT TO AUDIO
            try {
                clearConsole();
               System.out.println("File is now being converted to .flac");
                updateConsole("Files are now being converted to .flac\n");

                File dir1 = new File("c:/hack/video");
                File[] directoryListing1 = dir1.listFiles();
                if (directoryListing1 != null) {
                    int flacIndex = 0;
                    int index = 0;

                    for (File child : directoryListing1) {
                        System.out.println("File #"+flacIndex+" is now being converted to .flac");
                        updateConsole("File #"+flacIndex+" is now being converted to .flac");
                        File video= new File(child.getPath());
                        VideoToAudio.convertToFLAC(video, "c:/hack/audio/lectureAudio_"+index+ ".flac");
                        index+=1;
                        flacIndex++;
                    }
                }


                System.out.print("All files have been converted.\n");
                updateConsole("All files have been converted.\n");
                }catch (Exception ex) {
                // TODO Auto-generated catch block
                ex.printStackTrace();
            }

            //Speech to text
             File dir = new File("c:/hack/audio");
            File[] directoryListing = dir.listFiles();
            if (directoryListing != null) {
                int index = 0;
                System.out.print("Initialising Speech To Text Functionality.");
                updateConsole("Initialising Speech To Text Functionality.");



                int size = directoryListing.length;
                int i =0;
               for (File child : directoryListing) {
                    i++;
                    if (index < 100) {
                        String audio = "C:/hack/audio/lectureAudio_" + index + ".flac";

                        ArrayList<String> transcript = (new SpeechRecognition().recognizeFromFile(audio));


                        for (String srt : transcript) {
                            transcription += srt;
                            System.out.println(srt);
                            int percentage = ((i*100)/size);
                            System.out.println("STATUS: "+percentage+"%");
                            updateConsole("STATUS: "+percentage+"%");

                        }
                        if (index < 100) {
                            index += 1;
                        }

                    }
                }
                PrintWriter writer = new PrintWriter("c:/hack/transcription.txt");
                writer.print(transcription);
                writer.close();
                System.out.println("SUCCESS!");
                updateConsole("SUCCESS!");
            }

            textArea1.setText(consoleOutput);


            process();
        }


        try(  PrintWriter out = new PrintWriter( "c:/hack/consoleLog.txt" )  ){
            out.println( consoleOutput );
        }

        done = true;
    }

    public void showPDF(){

        File file = new File("c:/hack/new1.pdf");
        try {
            String content = "<!doctype html>\n" +
                    "<html lang='en'>\n" +
                    "<head>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<embed style = 'width: 100%; height: 1000px;' src='c:/hack/new.pdf' type='application/pdf'>\n" +
                    "</body>\n" +
                    "</html>";
            Files.write(file.toPath(), content.getBytes());

            Desktop.getDesktop().browse(URI.create(("c:/hack/new.pdf")));
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }

    }

    public void clearConsole() {
        textArea1.setText(null);
        textArea1.update(textArea1.getGraphics());
    }

    public void updateConsole(String s){

        getTextArea1().append("\n" + s);
        getTextArea1().update(textArea1.getGraphics());
        consoleOutput += ("\n" + s);
    }

    public JTextArea getTextArea1() {
        return textArea1;
    }

    private void button4ActionPerformed(ActionEvent e) {
        if(done){
        showPDF();
    }
        else{
            JOptionPane.showMessageDialog(null, "You have to elaborate a video and a pdf first.");
        }
    }

    private void button5ActionPerformed(ActionEvent e) throws IOException {
        Desktop.getDesktop().browse(URI.create(("c:/hack/lecture.pdf")));

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - Danilo Del Busso
        label1 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();
        textField1 = new JTextField();
        textField2 = new JTextField();
        button3 = new JButton();
        scrollPane1 = new JScrollPane();
        textArea1 = new JTextArea();
        button4 = new JButton();
        button5 = new JButton();

        //======== this ========
        setBackground(Color.white);

        // JFormDesigner evaluation mark
        setBorder(new javax.swing.border.CompoundBorder(
            new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){public void propertyChange(java.beans.PropertyChangeEvent e){if("border".equals(e.getPropertyName()))throw new RuntimeException();}});


        //---- label1 ----
        label1.setText(null);
        label1.setIcon(new ImageIcon("C:\\Users\\delbu\\IdeaProjects\\hack\\src\\images\\logo.png"));

        //---- button1 ----
        button1.setText("Browse");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button1ActionPerformed(e);
            }
        });

        //---- button2 ----
        button2.setText("Browse");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button2ActionPerformed(e);
            }
        });

        //---- textField1 ----
        textField1.setText("Choose a video file");
        textField1.setForeground(Color.lightGray);
        textField1.setEditable(false);

        //---- textField2 ----
        textField2.setText("Choose a pdf file");
        textField2.setForeground(Color.lightGray);
        textField2.setEditable(false);

        //---- button3 ----
        button3.setText(null);
        button3.setIcon(new ImageIcon("C:\\Users\\delbu\\IdeaProjects\\hack\\src\\images\\alpaca.png"));
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    button3ActionPerformed(e);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });

        //======== scrollPane1 ========
        {
            scrollPane1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scrollPane1.setAutoscrolls(true);

            //---- textArea1 ----
            textArea1.setFont(new Font("Lucida Console", Font.PLAIN, 16));
            textArea1.setEditable(false);
            scrollPane1.setViewportView(textArea1);
        }

        //---- button4 ----
        button4.setIcon(new ImageIcon("C:\\Users\\delbu\\IdeaProjects\\hack\\src\\images\\projector.png"));
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                button4ActionPerformed(e);
            }
        });

        //---- button5 ----
        button5.setText(null);
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    button5ActionPerformed(e);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup()
                        .addGroup(layout.createSequentialGroup()
                            .addGap(38, 38, 38)
                            .addComponent(label1))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(textField1, GroupLayout.PREFERRED_SIZE, 170, GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(50, 50, 50)
                                    .addComponent(button1))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(21, 21, 21)
                                    .addComponent(button3)
                                    .addGap(41, 41, 41)
                                    .addComponent(button5, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)))
                            .addGap(20, 20, 20)
                            .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(36, 36, 36)
                                    .addGroup(layout.createParallelGroup()
                                        .addComponent(button4)
                                        .addComponent(button2)))
                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 506, Short.MAX_VALUE)
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 202, GroupLayout.PREFERRED_SIZE)
                            .addGap(190, 190, 190)
                            .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(textField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup()
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(button2)
                                        .addComponent(button1))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 189, Short.MAX_VALUE)
                                    .addGroup(layout.createParallelGroup()
                                        .addComponent(button3)
                                        .addComponent(button4)))
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 268, Short.MAX_VALUE)
                                    .addComponent(button5, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)))
                            .addGap(11, 11, 11))
                        .addComponent(scrollPane1, GroupLayout.DEFAULT_SIZE, 738, Short.MAX_VALUE))
                    .addContainerGap())
        );
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Danilo Del Busso
    private JLabel label1;
    private JButton button1;
    private JButton button2;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button3;
    private JScrollPane scrollPane1;
    private JTextArea textArea1;
    private JButton button4;
    private JButton button5;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
