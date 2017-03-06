package edu.mfldclin.mcrf.bignn.tools.e;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Ehsun Behravesh <ehsun.behravesh@openet.com>
 */
public class Evaluation {

    private final List<String> classes;

    private Map<String, Double> tpCounter, tnCounter, fpCounter, fnCounter;

    public Evaluation(List<String> classes) {
        this.classes = classes;

        tpCounter = new HashMap<>();
        tnCounter = new HashMap<>();
        fpCounter = new HashMap<>();
        fnCounter = new HashMap<>();
    }

    private Map<String, Double> inc(Map<String, Double> counter, String className) {
        Double value = counter.get(className);

        if (value == null) {
            value = 0.0;
        }

        value++;

        counter.put(className, value);
        return counter;
    }

    public void incTruePositive(String className) {
        tpCounter = inc(tpCounter, className);
    }

    public void incFalsePositive(String className) {
        fpCounter = inc(fpCounter, className);
    }

    public void incTrueNegative(String className) {
        tnCounter = inc(tnCounter, className);
    }

    public void incFalseNegative(String className) {
        fnCounter = inc(fnCounter, className);
    }

    public Double recall(String className) {
        Double tp = tpCounter.get(className);
        Double fn = fnCounter.get(className);

        if (tp == null) {
            tp = 0.0;
        }

        if (fn == null) {
            fn = 0.0;
        }

        return tp / (tp + fn);
    }

    public Double precision(String className) {
        Double tp = tpCounter.get(className);
        Double fp = fpCounter.get(className);

        if (tp == null) {
            tp = 0.0;
        }

        if (fp == null) {
            fp = 0.0;
        }

        return tp / (tp + fp);
    }

    public Double accuracy(String ClassName) {
        Double tp = tpCounter.get(ClassName);
        Double tn = tnCounter.get(ClassName);
        Double fp = fpCounter.get(ClassName);
        Double fn = fnCounter.get(ClassName);

        if (tp == null) {
            tp = 0.0;
        }

        if (fn == null) {
            fn = 0.0;
        }

        if (tn == null) {
            tn = 0.0;
        }

        if (fp == null) {
            fp = 0.0;
        }

        return (tp + tn) / (tp + tn + fp + fn);
    }

    public Double recall() {
        Double sum = 0.0;

        for (String classe : classes) {
            sum += recall(classe);
        }

        return sum / classes.size();
    }

    public Double precision() {
        Double sum = 0.0;

        for (String classe : classes) {
            sum += precision(classe);
        }

        return sum / classes.size();
    }

    public Double accuracy() {
        Double sum = 0.0;

        for (String classe : classes) {
            sum += accuracy(classe);
        }

        return sum / classes.size();
    }

    /*
    public void printDebugData() {
        System.out.println("\n\nEvaluation debug");
        System.out.println("tn");
        printMap(tnCounter);
        System.out.println("tp");
        printMap(tpCounter);
        System.out.println("fn");
        printMap(fnCounter);
        System.out.println("fp");
        printMap(fpCounter);
    }

    private static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());            
        }
    }*/
}
