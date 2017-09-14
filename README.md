## bigNN
# bigNN: an open-source big data toolkit focused on biomedical sentence classification

<p align="justify">Every single day, a large amount of text data is generated by different medical data sources, such as scientific literature, medical web pages, health related social media posts, clinical notes, and drug reviews. Processing this data in an efficient manner is a really daunting task without the help of clever computational strategies, and it makes text classification as an imperative and a major operation to big data text analytics. In this contribution, we developed an open-source software for big data text classification called <strong>bigNN</strong>. It implements a word2vec neural network model over Apache Spark to aim at big data sentence classification in a timely fashion. The software offers a graphical user interface, and it facilitates reproducible research in sentence analysis by allowing users to configure different sets of Apache Spark and word2vec neural network parameters. Furthermore, we introduce application of <strong>bigNN</strong> in medical informatics domain. bigNN is fully documented and it is publicly and freely available at https://github.com/bircatmcri/bigNN.
</p>

<p align="justify">
The <strong>bigNN</strong> includes the following packages: 

| Package Name        | Description |
| ------------- |-------------|
| edu.mfldclin.mcrf.bignn.gui  | Implementation of the graphical user interface |
| edu.mfldclin.mcrf.bignn.setting | Implementation of pre-defined and user-defined settings required to the system|
| edu.mfldclin.mcrf.bignn.learning | Implementation of text pre-processing and neural network learning model|
| edu.mfldclin.mcrf.bignn.evaluation | It evaluates the neural network predictive model|
</p>
<p align="justify">
The <strong>bigNN</strong> software architectural model is shown in includes the following figure.  

![alt text](https://github.com/bircatmcri/bigNN/blob/master/bigNN%20architecture.png =250x250 "bigNN software architectural model")
</p>

The workflow and architectural model of the <strong>bigNN</strong> is fully explained in the following work:
<p align="justify">
Ahmad P. Tafti, Ehsun Behravesh, Mehdi Assefi, Eric LaRose, Jonathan Badger, John Mayer, AnHai Doan, David Page, Peggy Peissig. 2017. <strong>bigNN: an open-source sentence classifier using big data neural network architecture. IEEE BIG DATA 2017.</strong> (Under Review)
</p>
