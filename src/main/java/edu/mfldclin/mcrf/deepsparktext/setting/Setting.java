package edu.mfldclin.mcrf.deepsparktext.setting;

public class Setting {

    private int sparkExecutors;
    private int sparkSkkaThreads;
    private double dl4jLearningRate;
    private int dl4jBatchSize;
    private int dl4jMinWordFrequency;
    private int dl4jLayerSize;
    private int dl4jEpochs;
    private TokenPreProcessType tokenPreProcessType;
    private int dl4jWindowSize;

    public Setting() {
    }

    public Setting(int sparkExecutors, int sparkSkkaThreads, double dl4jLearningRate, int dl4jBatchSize, int dl4jMinWordFrequency,
            int dl4jLayerSize, int dl4jEpochs, TokenPreProcessType tokenPreProcessType,
            int dl4jWindowSize) {
        this.sparkExecutors = sparkExecutors;
        this.sparkSkkaThreads = sparkSkkaThreads;
        this.dl4jLearningRate = dl4jLearningRate;
        this.dl4jBatchSize = dl4jBatchSize;
        this.dl4jMinWordFrequency = dl4jMinWordFrequency;
        this.dl4jLayerSize = dl4jLayerSize;
        this.dl4jEpochs = dl4jEpochs;
        this.tokenPreProcessType = tokenPreProcessType;
        this.dl4jWindowSize = dl4jWindowSize;
    }

    public static Setting makeDefault() {
        Setting result = new Setting();

        result.setDl4jBatchSize(500);
        result.setDl4jLearningRate(0.2);
        result.setDl4jEpochs(1);
        result.setDl4jLayerSize(100);
        result.setDl4jMinWordFrequency(10);
        result.setDl4jWindowSize(1);

        int cores = 4;
        try {
            cores = Runtime.getRuntime().availableProcessors();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        result.setSparkExecutors(cores);
        result.setSparkSkkaThreads(cores);
        result.setTokenPreProcessType(TokenPreProcessType.COMMON);
        
        return result;
    }

    public void setDl4jWindowSize(int dl4jWindowSize) {
        this.dl4jWindowSize = dl4jWindowSize;
    }

    public int getDl4jWindowSize() {
        return dl4jWindowSize;
    }

    
    
    public TokenPreProcessType getTokenPreProcessType() {
        return tokenPreProcessType;
    }

    public void setTokenPreProcessType(TokenPreProcessType tokenPreProcessType) {
        this.tokenPreProcessType = tokenPreProcessType;
    }

    @Override
    public String toString() {
        return "Setting: " + "spark executors=" + sparkExecutors + ", spark akka threads=" + sparkSkkaThreads + ", learning rate=" + dl4jLearningRate + ", batch size=" + dl4jBatchSize + ", min word frequency=" + dl4jMinWordFrequency + "%, layer size=" + dl4jLayerSize + ", epochs=" + dl4jEpochs + ", token pre-process=" + tokenPreProcessType + ", window size=" + dl4jWindowSize  + '}';
    }
    
    public int getSparkExecutors() {
        return sparkExecutors;
    }

    public void setSparkExecutors(int sparkExecutors) {
        this.sparkExecutors = sparkExecutors;
    }

    public int getSparkSkkaThreads() {
        return sparkSkkaThreads;
    }

    public void setSparkSkkaThreads(int sparkSkkaThreads) {
        this.sparkSkkaThreads = sparkSkkaThreads;
    }

    public double getDl4jLearningRate() {
        return dl4jLearningRate;
    }

    public void setDl4jLearningRate(double dl4jLearningRate) {
        this.dl4jLearningRate = dl4jLearningRate;
    }

    public int getDl4jBatchSize() {
        return dl4jBatchSize;
    }

    public void setDl4jBatchSize(int dl4jBatchSize) {
        this.dl4jBatchSize = dl4jBatchSize;
    }

    public int getDl4jMinWordFrequency() {
        return dl4jMinWordFrequency;
    }

    public void setDl4jMinWordFrequency(int dl4jMinWordFrequency) {
        this.dl4jMinWordFrequency = dl4jMinWordFrequency;
    }

    public int getDl4jLayerSize() {
        return dl4jLayerSize;
    }

    public void setDl4jLayerSize(int dl4jLayerSize) {
        this.dl4jLayerSize = dl4jLayerSize;
    }

    public int getDl4jEpochs() {
        return dl4jEpochs;
    }

    public void setDl4jEpochs(int dl4jEpochs) {
        this.dl4jEpochs = dl4jEpochs;
    }

}
