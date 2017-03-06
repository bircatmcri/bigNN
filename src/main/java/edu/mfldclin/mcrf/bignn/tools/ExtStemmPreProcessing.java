package edu.mfldclin.mcrf.bignn.tools;

import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.StemmingPreprocessor;

public class ExtStemmPreProcessing extends StemmingPreprocessor {

    public ExtStemmPreProcessing() {
        super();
    }

    @Override
    public String preProcess(String token) {
        token = token.toLowerCase();
        String base = super.preProcess(token);

        base = base.replaceAll("\\d+.*", "");

        return base;
    }

}
