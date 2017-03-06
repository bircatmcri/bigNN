package edu.mfldclin.mcrf.bignn.learning;

import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;

public class ExtCommonPreProcessing extends CommonPreprocessor {

    public ExtCommonPreProcessing() {
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
