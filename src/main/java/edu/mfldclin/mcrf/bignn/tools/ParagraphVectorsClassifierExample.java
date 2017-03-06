package edu.mfldclin.mcrf.bignn.tools;

import edu.mfldclin.mcrf.bignn.setting.Setting;
import edu.mfldclin.mcrf.bignn.setting.TokenPreProcessType;
import edu.mfldclin.mcrf.bignn.tools.e.Evaluation;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import org.deeplearning4j.text.tokenization.tokenizer.TokenPreProcess;

public class ParagraphVectorsClassifierExample {

    protected static final Logger log = LoggerFactory.getLogger(ParagraphVectorsClassifierExample.class);
    protected static final boolean APPLY_WEIGHT = true;
    protected static final double WEIGHT = 1.4;
    protected ParagraphVectors paragraphVectors;
    protected LabelAwareIterator iterator;
    protected TokenizerFactory tokenizerFactory;

    protected final File trainingDir, dataDir;
    protected final Setting setting;

    public ParagraphVectorsClassifierExample(File trainingDir, File dataDir, Setting setting) {
        this.trainingDir = trainingDir;
        this.dataDir = dataDir;
        this.setting = setting;
    }

    public void listIterator2(JavaSparkContext sc) throws Exception {
        // build a iterator for our dataset
        iterator = listIterator(sc);
    }

    public void Tokenize(JavaSparkContext sc) throws Exception {
        //final EndingPreProcessor preProcessor = new EndingPreProcessor();
        TokenPreProcess tokenPreProcess = null;

        if (setting.getTokenPreProcessType() == TokenPreProcessType.COMMON) {
            tokenPreProcess = new ExtCommonPreProcessing();

        } else if (setting.getTokenPreProcessType() == TokenPreProcessType.STEAMING) {
            tokenPreProcess = new ExtStemmPreProcessing();
        }

        tokenizerFactory = new DefaultTokenizerFactory(); //UimaTokenizerFactory
        tokenizerFactory.setTokenPreProcessor(tokenPreProcess);

    }

    public void makeParagraphVectors(JavaSparkContext sc) throws Exception {
        paragraphVectors = new ParagraphVectors.Builder()
                .learningRate(setting.getDl4jLearningRate())
                .minLearningRate(0.001)
                .windowSize(setting.getDl4jWindowSize())
                .iterations(setting.getDl4jIterations())
                .batchSize(setting.getDl4jBatchSize())
                .workers(4)
                .stopWords(stopWords())
                .minWordFrequency(calcWordFreq())
                .layerSize(setting.getDl4jLayerSize())
                .epochs(setting.getDl4jEpochs())
                .iterate(iterator)
                .trainWordVectors(true)
                .tokenizerFactory(tokenizerFactory)
                .build();

        // Start model training
        paragraphVectors.fit();
    }

    private ArrayList<String> stopWords() {
        String[] stopWordsArray = {"a", "at", "the", "an", "of", "from", "the", "and", "when",
            "abstract", "as", "to", "most", "year", "age", "common", "with", "have", "is", "off", "about",
            "above", "however", "after", "again", "against", "all", "further", "for", "any", "are", "be", "because",
            "been", "sometimes", "often", "pmc", "die", "types", "type", "back", "usually", "always", "sometime", "before", "being", "pattern", "patterns", "below", "study", "studies", "between", "both", "but", "by", "can", "could", "has", "have",
            "had", "better", "best", "doesnt", "dont", "didnt", "or", "he", "having", "her", "they", "background", "backgrounds", "method",
            "conclusion", "small", "large", "big", "factor", "much", "factors", "generally", "cancerous", "tissues", "clinic", "hospital", "center", "malignant", "outcome", "surgery", "early", "conclusions", "cancer", "in", "normally", "treat", "treatment", "methods", "results", "result", "approach", "also", "allow",
            "into", "x-ray", "xray", "organ", "organs", "is", "it", "its", "it's", "cells", "cell", "itself", "let's", "let", "me", "myslef", "no", "nor", "topic",
            "not", "off", "vs", "hr", "out", "create", "creates", "stage", "other", "", "or", "another", "once", "our", "ours", "ourselves", "area",
            "out", "over", "own", "explain", "same", "she", "should", "so", "week", "weeks", "some", "such", "than", "that", "their",
            "these", "they", "those", "review", "primary", "their", "through", "slowly", "to", "tx", "under", "up", "until", "very", "was", "we",
            "why", "with", "prior", "difficulty", "highlight", "trial", "accepting", "main", "major", "having", "be", "who", "grow", "whom", "place", "body", "ci", "%ci", "ratio", "cases", "case", "allows", "%", "anybody", "anyone", "patient", "patients", "among", "amongst",
            "accordingly", "size", "anyways", "year", "years", "day", "days", "month", "months", "january", "february", "march", "april", "may", "june", "july", "august", "october", "november", "december", "author", "authors", "jyauthor", "houston", "treated", "occur", "pc-", "level", "successfully", "protocol", "protocols", "option", "options", "information", "patient", "patients", "anyway", "appropriate", "aside", "consider", "considering", "com",
            "certain", "corresponding", "jama", "plos", "ki-", "tothe", "p=", "jpn", "patho", "bmj", "febs", "arch", "lett", "clin", "park", "methodology", "definitely", "different", "each", "every", "else", "et", "etc",
            "exactly", "if", "area", "total", "gets", "get", "go", "pathol", "identify", "hematol", "br", "goes", "greetings", "how", "what", "where", "when", "immediate",
            "immediately", "instead", "including", "include", "includes", "insert", "-year", "indicate", "later", "late", "test", "testing", "latest", "low", "high", "serious",
            "seen", "she", "you", "aknowledge", "rate", "rates", "so", "something", "still", "soon", "very", "large", "thus",
            "therefore", "hence", "big", "begin", "ci", "p =", "material", "materials", "supplementary", "conclusions", "cancers", "research", "researcher", "university",
            "universities", "hospital", "hospitals", "tumor", "disease", "drug", "unto", "used", "within", "wouldn't",
            "able", "across", "job", "almost", "although", "anyhow", "anywhere", "ask", "asked", "beyond", "beside",
            "certainly", "could", "-%", "described", "gene", "describe", "describes", "example", "moreover", "more", "main",
            "mainly", "objective", "objectives", "keep", "knows", "know", "latterly", "meanwhile", "look",
            "many", "same", "second", "data", "system", "acknowledge", "fund", "one", "two", "three", "four", "five", "five", "six", "seven", "significant",
            "eight", "role", "state", "nine", "us", "states", "ten", "thank", "thereafter", "then", "taken", "that", "while", "wherein",
            "whose", "wonder", "whole", "name", "ar", "hs", "zero", "whereafter", "viz", "usually", "truly", "tend", "tends",
            "someone", "presumably", "obviously", "overall", "auc", "not", "none", "namely", "we", "biomedical", "like",
            "hopefully", "hereby", "hardly", "following", "follow", "follows", "o-mar", "jan", "jan-", "especially", "either",
            "even", "every", "during", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "pmid",
            "pmid:", "c-jun", "ct", "chk", "jnk-c-jun", "di", "euro", "italy", "milan", "norway", "n-", "oslo", "japan", "china", "uk", "dept", "department", "kpnac-jun", "jul-", "jul", "sep-", "sep", "sep-octm-m", "oct", "nov", "nov-", "oct-",
            "mar-apr-", "mar-apr", "pcmid", "purpose", "purposes", "aims", "aim", "consequently", "already", "appreciate", "thanks", "according",
            "among", "unfortunately", "apr-", "zz", "ii", "ff", "aa", "bb", "cc", "dd", "ee", "ff", "jj", "hh", "ll", "kk", "gg",
            "rs", "mm", "nn", "oo", "pp", "qq", "rr", "ss", "tt", "uu", "ww", "vv", "zz", "yy", "apr", "may-", "may", "unlikely",
            "mar-comment", "positive", "negative", "mar", "mar-", "-gauge", "jan-the", "feb", "feb-", "jan-feb", "jan-feb-", "august", "mar-", "mar", "aug",
            "aug-", "-aug", "mar-apr-", "apr-", "useful", "via", "@", "million", ".com", ".org", "suggest", "suggestion", "similar", "significantly",
            ".", "?", "!", ",", "+", "=", "also", "s", "t", "u", "w", "x", "y", "z", "dec", "dec-", ">", ",", "<", "-", ";", ":", "who's",
            "whom", "why", "why's", "will", "with", "without",
            "won't", "wont", "wouldnt", "would", "theyd", "theyll", "theyre", "theyve", "wouldn't", "you", "feb-comment", "you'd", "countries", " mainly", "you'll", "youre", "youve", "youll", "you're", "you've", "your", "yours", "yourself", "yourselves",
            "we've", "were", "weren't", "what", "what's", "hu", "wu", "qc", "cz", "li", "percent", "when", "when's", "where", "where's", "which", "while",
            "who", "this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't",
            "we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's",
            "where", "where's", "something", "shouldn't", "so", "some", "such", "take", "than", "that", "that's",
            "the", "their", "theirs", "them", "themselves", "then", "there", "there's", "theres", "these", "they",
            "they'd", "they'll", "they're", "they've"};

        ArrayList<String> stopWords = new ArrayList<String>(Arrays.asList(stopWordsArray));
        return stopWords;
    }

    public Evaluation checkUnlabeledData() throws FileNotFoundException {
        /*
      At this point we assume that we have model built and we can check 
      which categories our unlabeled document falls into.
      So we'll start loading our unlabeled documents and checking them
         */

        FileLabelAwareIterator unClassifiedIterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(dataDir)
                .build();

        /*
      Now we'll iterate over unlabeled data, and check which label it could be assigned to
      Please note: for many domains it's normal to have 1 document fall into few labels at once,
      with different "weight" for each.
         */
        MeansBuilder meansBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
                tokenizerFactory);
        LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

        List<String> labels = unClassifiedIterator.getLabelsSource().getLabels();
        Evaluation eval = new Evaluation(labels);

        //StringBuilder result = new StringBuilder();
        
        while (unClassifiedIterator.hasNextDocument()) {
            LabelledDocument document = unClassifiedIterator.nextDocument();
            File currentFile = unClassifiedIterator.getCurrentFile();
            INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
            List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);

            /*
          please note, document.getLabel() is used just to show which document we're looking at now,
          as a substitute for printing out the whole document name.
          So, labels on these two documents are used like titles,
          just to visualize our classification done properly
             */
            if (APPLY_WEIGHT) {
                for (Pair<String, Double> score : scores) {
                    if (score.getFirst().equals(document.getLabel())) {
                        score.setSecond(score.getSecond() * WEIGHT);
                    }
                }
            }
            
            System.out.println("Document: '" + document.getLabel() + "' which is the file: " + currentFile.getAbsolutePath() + " falls into the following categories: ");
            Collections.sort(scores, (Pair<String, Double> o1, Pair<String, Double> o2) -> {
                if (o1.getSecond() > o2.getSecond()) {
                    return -1;
                } else if (o1.getSecond() < o2.getSecond()) {
                    return 1;
                } else {
                    return 0;
                }
            });

            int i = 0;
            String correctLabel = "";
            
            for (Pair<String, Double> score : scores) {
                System.out.println("        " + score.getFirst() + ": " + score.getSecond());

                if (i == 0) { // detected
                    correctLabel = score.getFirst();
                    if (document.getLabel().equalsIgnoreCase(score.getFirst())) {
                        // true positive                         
                        eval.incTruePositive(score.getFirst());
                    } else {
                        // false positive
                        eval.incFalsePositive(score.getFirst());

                    }
                } else { // not detected
                    if (document.getLabel().equalsIgnoreCase(score.getFirst())) {
                        // false negative 
                        eval.incFalseNegative(score.getFirst());

                    } else {
                        // true negative
                        eval.incTrueNegative(score.getFirst());

                    }
                }

                i++;
            }
            
            
            File outputDir = new File(new File(dataDir.getParent()).getAbsolutePath() + "/output/" + correctLabel + "/");
            File outputFile = new File (outputDir, currentFile.getName());
            outputDir.mkdirs();
            try {
                Files.copy(currentFile.toPath(), outputFile.toPath());
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ParagraphVectorsClassifierExample.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        StringBuilder result = new StringBuilder("\n");

        for (String label : labels) {
            result.append(label).append("\n")
                    .append("\t").append("Precision: ").append(eval.precision(label)).append("\n")
                    .append("\t").append("Recall: ").append(eval.recall(label)).append("\n")
                    .append("\t").append("Accuracy: ").append(eval.accuracy(label)).append("\n");
        }

        result.append("Precision: ").append(eval.precision() + "\n");
        result.append("Recall: ").append(eval.recall() + "\n");
        result.append("Accuracy: ").append(eval.accuracy());
        //eval.
        return eval;
    }

    public Evaluation checkUnlabeledData(String stringData) throws IOException {
        String dir = stringToDocument(stringData);

        FileLabelAwareIterator unClassifiedIterator = new FileLabelAwareIterator.Builder()
                .addSourceFolder(new File(dir))
                .build();

        MeansBuilder meansBuilder = new MeansBuilder(
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable(),
                tokenizerFactory);
        LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
                (InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

        List<String> labels = unClassifiedIterator.getLabelsSource().getLabels();
        Evaluation eval = new Evaluation(labels);

        //StringBuilder result = new StringBuilder();
        while (unClassifiedIterator.hasNextDocument()) {
            LabelledDocument document = unClassifiedIterator.nextDocument();
            INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
            List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);

            /*
          please note, document.getLabel() is used just to show which document we're looking at now,
          as a substitute for printing out the whole document name.
          So, labels on these two documents are used like titles,
          just to visualize our classification done properly
             */
            if (APPLY_WEIGHT) {
                for (Pair<String, Double> score : scores) {
                    if (score.getFirst().equals(document.getLabel())) {
                        score.setSecond(score.getSecond() * WEIGHT);
                    }
                }
            }

            System.out.println("Document '" + document.getLabel() + "' falls into the following categories: ");
            Collections.sort(scores, (Pair<String, Double> o1, Pair<String, Double> o2) -> {
                if (o1.getSecond() > o2.getSecond()) {
                    return -1;
                } else if (o1.getSecond() < o2.getSecond()) {
                    return 1;
                } else {
                    return 0;
                }
            });

            int i = 0;
            for (Pair<String, Double> score : scores) {
                System.out.println("        " + score.getFirst() + ": " + score.getSecond());

                if (i == 0) { // detected
                    if (document.getLabel().equalsIgnoreCase(score.getFirst())) {
                        // true positive 
                        eval.incTruePositive(score.getFirst());
                    } else {
                        // false positive
                        eval.incFalsePositive(score.getFirst());

                    }
                } else { // not detected
                    if (document.getLabel().equalsIgnoreCase(score.getFirst())) {
                        // false negative 
                        eval.incFalseNegative(score.getFirst());

                    } else {
                        // true negative
                        eval.incTrueNegative(score.getFirst());

                    }
                }

                i++;
            }
        }

        StringBuilder result = new StringBuilder("\n");

        for (String label : labels) {
            result.append(label).append("\n")
                    .append("\t").append("Precision: ").append(eval.precision(label)).append("\n")
                    .append("\t").append("Recall: ").append(eval.recall(label)).append("\n")
                    .append("\t").append("Accuracy: ").append(eval.accuracy(label)).append("\n");
        }

        result.append("Precision: ").append(eval.precision() + "\n");
        result.append("Recall: ").append(eval.recall() + "\n");
        result.append("Accuracy: ").append(eval.accuracy());
        //eval.
        return eval;
    }

    /*
    private static HamidLabelAwareIterator listIterator(final JavaSparkContext sc) throws Exception {

        final Set<String> set = new HashSet<>();

        String path = PREFIX + "/labeled/breast_cancer";
        List<Tuple2<String, String>> collect = getTuple2s(sc, set, path);
        path = PREFIX + "/labeled/prostate_cancer";
        List<Tuple2<String, String>> collect2 = getTuple2s(sc, set, path);               
        collect.addAll(collect2);
        path = PREFIX + "/labeled/lung_cancer";
        List<Tuple2<String, String>> collect3 = getTuple2s(sc, set, path);
        collect.addAll(collect3);
        return new HamidLabelAwareIterator(collect, set);

    }*/
    private HamidLabelAwareIterator listIterator(final JavaSparkContext sc) throws Exception {
        final Set<String> set = new HashSet<>();

        File[] listFiles = trainingDir.listFiles((File f) -> {
            return f.isDirectory();
        });

        List<Tuple2<String, String>> collect = new LinkedList<>();

        for (File f : listFiles) {
            String path = f.getAbsolutePath();
            List<Tuple2<String, String>> c = getTuple2s(sc, set, path);
            collect.addAll(c);
        }

        return new HamidLabelAwareIterator(collect, set);

    }

    private static List<Tuple2<String, String>> getTuple2s(JavaSparkContext sc, final Set<String> set, String path) {
        JavaPairRDD<String, String> a = sc.wholeTextFiles(path);

        a = a.mapToPair(new PairFunction<Tuple2<String, String>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<String, String> stringStringTuple2) throws Exception {
                String[] s = stringStringTuple2._1.split("/");
                return new Tuple2<String, String>(s[s.length - 2], stringStringTuple2._2);
            }
        });
        List<Tuple2<String, String>> collect = a.collect();
        for (Tuple2<String, String> c : collect) {
            set.add(c._1);
        }
        return collect;
    }

    private int calcWordFreq() {
        //return setting.getDl4jMinWordFrequency();
        int count = trainingDir.listFiles((File pathname) -> pathname.isFile()).length;
        int value = setting.getDl4jMinWordFrequency();
        return (count * value) / 100;
    }

    private String stringToDocument(String stringData) throws UnsupportedEncodingException, IOException {
        String tmpDir = System.getProperty("java.io.tmpdir");
        String dir = tmpDir + "/" + System.currentTimeMillis();
        File file = new File(dir + "/doc");
        file.mkdirs();
        Files.write(Paths.get(dir + "/doc"), stringData.getBytes("UTF-8"));
        return dir;
    }

}
