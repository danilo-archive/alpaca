package Hack;


import opennlp.tools.doccat.*;
import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.postag.*;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import opennlp.tools.util.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class OpenNLP {

    private Reader reader;

    public OpenNLP(){

        reader = new Reader();
    }

    public String readFirstSentence(String text) {
        String firstSentence = "";
        try {
            InputStream modelIn = new FileInputStream("C:/hack/en-sent.bin");
            SentenceModel model = new SentenceModel(modelIn);

            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            String sentences[] = sentenceDetector.sentDetect(text);
            firstSentence = sentences[0];
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return firstSentence;
    }

    public String[] readSentences(String text) throws Exception{
        String[] sentences;
        InputStream modelIn = new FileInputStream("C:/hack/en-sent.bin");
        SentenceModel model = new SentenceModel(modelIn);

        SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
        sentences = sentenceDetector.sentDetect(text);
        return sentences;
    }

    public void training(){
        try{

            InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File("C:/hack"+File.separator+"en-movie-category.train"));
            ObjectStream lineStream = new PlainTextByLineStream(dataIn, StandardCharsets.UTF_8);
            ObjectStream sampleStream = new DocumentSampleStream(lineStream);

            TrainingParameters params = new TrainingParameters();
            params.put(TrainingParameters.ITERATIONS_PARAM, 10+"");
            params.put(TrainingParameters.CUTOFF_PARAM, 0+"");
            params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

            DoccatModel model = DocumentCategorizerME.train("en", sampleStream, params, new DoccatFactory());

            BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream("C:/hack"+File.separator+"en-movie-classifier-naive-bayes.bin"));
            model.serialize(modelOut);
            reader.loadDocument("C:/hack/ProgrammingWithLogic3.pdf");
            DocumentCategorizer doccat = new DocumentCategorizerME(model);

            double[] aProbs = doccat.categorize(reader.read().replaceAll("[^A-Za-z]", " ").split(" "));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public String[] partOfSpeech(String text){
        //training();
        String[] allWords = {};
        try{
            InputStream modelIn = new FileInputStream("C:/hack/en-pos-maxent.bin");
            POSModel model = new POSModel(modelIn);
            POSTaggerME posTagger = new POSTaggerME(model);
           //String sentence = "Hi welcome to Tutorialspoint";

            //Tokenizing the sentence using WhitespaceTokenizer class
           WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;
           String[] tokens = whitespaceTokenizer.tokenize(text);


            String[] tags = posTagger.tag(tokens);
            //String[] tags = posTagger.tag(tokens);

            double probs[] = posTagger.probs();

            for (int i = 0; i < tokens.length; i++){
                //System.out.println(tokens[i]);
                //System.out.println(probs[i]);
            }


            POSSample sample = new POSSample(tokens, tags);
            String[] words = whitespaceTokenizer.tokenize(sample.toString());
            for(int i = 0; i < words.length; i++){
                if(!words[i].contains("NN")){
                    words[i] = "";
                }
            }
            int counter = 0;
            for (int i = 0; i < words.length; i++){
                if(!words[i].equals("")) {
                    words[counter] = words[i];
                    counter++;
                }
            }
            allWords = new String[counter];
            for(int i = 0; i < allWords.length;i++){
                allWords[i] = words[i];
            }
            //System.out.println(sample.toString());


        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return allWords;
    }


}
