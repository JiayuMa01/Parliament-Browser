package org.ppr.abschlussprojekt.helper.NLPHelper;

import org.apache.uima.fit.factory.AggregateBuilder;
import org.hucompute.textimager.fasttext.labelannotator.LabelAnnotatorDocker;
import org.hucompute.textimager.uima.gervader.GerVaderSentiment;
import org.hucompute.textimager.uima.spacy.SpaCyMultiTagger3;

import java.net.URL;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * This class is the nlp pipeline
 * @author Giuseppe Abrami
 */

public class NLPEngine {
    private AggregateBuilder builder;

    /**
     * This method returns the Aggregatebuilder, where you can initialize a pipeline
     * @return builder
     * @author Kevin Schuff
     */
    public AggregateBuilder getBuilder(){
        return this.builder;
    }

    /**
     * This method starts the pipeline
     * @return boolean value
     */
    public boolean initPipeline() {
        builder = new AggregateBuilder();
        URL posmap = NLPEngine.class.getClassLoader().getResource("am_posmap.txt");
        try {
            builder.add(createEngineDescription(SpaCyMultiTagger3.class,
                    SpaCyMultiTagger3.PARAM_REST_ENDPOINT, "http://spacy.lehre.texttechnologylab.org"
            ));

            builder.add(createEngineDescription(GerVaderSentiment.class,
                    GerVaderSentiment.PARAM_REST_ENDPOINT, "http://gervader.lehre.texttechnologylab.org",
                    GerVaderSentiment.PARAM_SELECTION, "text,de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence"
            ));

            builder.add(createEngineDescription(LabelAnnotatorDocker.class,
                    LabelAnnotatorDocker.PARAM_FASTTEXT_K, 100,
                    LabelAnnotatorDocker.PARAM_CUTOFF, false,
                    LabelAnnotatorDocker.PARAM_SELECTION, "text",
                    LabelAnnotatorDocker.PARAM_TAGS, "ddc3",
                    LabelAnnotatorDocker.PARAM_USE_LEMMA, true,
                    LabelAnnotatorDocker.PARAM_ADD_POS, true,
                    LabelAnnotatorDocker.PARAM_POSMAP_LOCATION, posmap.getPath(),
                    LabelAnnotatorDocker.PARAM_REMOVE_FUNCTIONWORDS, true,
                    LabelAnnotatorDocker.PARAM_REMOVE_PUNCT, true,
                    LabelAnnotatorDocker.PARAM_REST_ENDPOINT, "http://ddc.lehre.texttechnologylab.org"
            ));
            // successful pipeline
            return true;
        } catch (Exception e) {
        }
        // failure pipeline
        return false;
    }
}

