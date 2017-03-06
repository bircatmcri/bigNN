package edu.mfldclin.mcrf.bignn.tools;

import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.documentiterator.LabelsSource;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This is simple filesystem-based LabelAware iterator. It assumes that you have
 * one or more folders organized in the following way: 1st level subfolder:
 * label name 2nd level: bunch of documents for that label
 * <p>
 * You can have as many label folders as you want, as well.
 * <p>
 * Please note: as of DL4j 3.9 this iterator is available as part of DL4j
 * codebase, so there's no need to use this implementation.
 *
 * @author raver119@gmail.com
 */
public class HamidLabelAwareIterator implements LabelAwareIterator {

    protected List<Tuple2<String, String>> list;
    protected AtomicInteger position = new AtomicInteger(0);
    protected LabelsSource labelsSource;

    /*
        Please keep this method protected, it's used in tests
     */
    protected HamidLabelAwareIterator() {

    }

    public HamidLabelAwareIterator(List<Tuple2<String, String>> list, Set<String> lables) {
        this.list = list;
        this.labelsSource = new LabelsSource(new ArrayList<>(lables));

    }

    @Override
    public boolean hasNextDocument() {
        return position.intValue() < list.size();
    }

    @Override
    public LabelledDocument nextDocument() {
        Tuple2<String, String> t = list.get(position.intValue());
        LabelledDocument document = new LabelledDocument();
        document.setLabel(t._1);
        document.setContent(t._2);
        position.incrementAndGet();
        return document;
    }

    @Override
    public void reset() {
        position = new AtomicInteger(0);
    }

    @Override
    public LabelsSource getLabelsSource() {
        return labelsSource;
    }

}
