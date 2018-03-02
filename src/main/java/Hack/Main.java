package Hack;

public class Main {

    public static void main(String[] args){

        GUI gui = new GUI();
        SummaryTool summary = new SummaryTool();
        summary.init("c:/hack/ELA.txt");
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
        summary.printSummary();

        summary.printStats();
    }
}
