package org.ppr.abschlussprojekt.helper.NLPHelper;

import com.google.gson.Gson;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS;
import de.tudarmstadt.ukp.dkpro.core.api.ner.type.NamedEntity;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.fit.pipeline.SimplePipeline;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.bson.Document;
import org.bson.json.JsonWriterSettings;
import org.hucompute.textimager.uima.type.Sentiment;
import org.hucompute.textimager.uima.type.category.CategoryCoveredTagged;
import org.json.JSONObject;
import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.impl.nlp.Comment_NLP_Impl;
import org.ppr.abschlussprojekt.data.impl.nlp.Speech_NLP_Impl;
import org.ppr.abschlussprojekt.data.impl.nlp.Protocol_NLP_Impl;
import org.texttechnologylab.uimadb.wrapper.mongo.MongoSerialization;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.CasSerializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.SerializerInitializationException;
import org.texttechnologylab.uimadb.wrapper.mongo.serilization.exceptions.UnknownFactoryException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

/**
 * This class enables the analysis of a text with the help of a nlp pipeline, furthermore extracting the tokens of the analysis
 *
 * @author Kevin Schuff
 */
public class Converter {
    private NLPEngine engine = null;
    private CategoriesMapper categoriesMapper;
    private Gson gson;
    private JsonWriterSettings settings;

    /**
     * This is the default constructor for the class
     * @author Kevin Schuff
     */
    public Converter(){
        // initilaize categoriesmapper for ddc categories
        this.categoriesMapper = new CategoriesMapper();

        // initialize nlp pipeline
        this.engine = new NLPEngine();
        engine.initPipeline();

        // initialize gson for serializing & deserializing
        gson = new Gson();

        //https://stackoverflow.com/questions/35209839/converting-document-objects-in-mongodb-3-to-pojos
        // workaround for numberlong in mongo documents like date, starttime and endtime
        settings = JsonWriterSettings.builder()
                .int64Converter((value, writer) -> writer.writeNumber(value.toString()))
                .build();


    }

    /**
     * This method takes not yet analyzed speeches or comments from one collection, analyzes them and uploads them into another collection
     * @param downloadFrom
     * @param uploadTo
     * @param mongoDBHandler
     * @author Kevin Schuff
     */
    public void convertAll(String downloadFrom, String uploadTo, MongoDBHandler mongoDBHandler){
        // takes speeches or comments from this collection
        MongoCollection fromCollection = mongoDBHandler.getCollection(downloadFrom);
        // analyzes them and uploads the speeches or comments with the results into this collection
        MongoCollection toCollection = mongoDBHandler.getCollection(uploadTo);

        // cursor to iterate through results
        MongoCursor<Document> mongoCursor = mongoDBHandler.getMongoCursor(fromCollection);
        // to see the progress
        int counter = 0;
        while(mongoCursor.hasNext()){
            // check if speech or comment already exists
            Document test = mongoCursor.next();
            String _id = test.getString("_id");
            if(mongoDBHandler.doesExist(toCollection, _id) == true){
                continue;
            }
            // analyze speech and add NLP information to doc
            Document doc = analyzeSpeechDoc(test);
            toCollection.insertOne(doc);
            // display progress in console
            counter = counter + 1;
            System.out.println("COUNTER " + counter);
        }
    }

    /**
     * This method adds NLP information to a speech/comment doc and uploads it to a collection
     * Method can be used for updating in frontend
     * @param toCollection
     * @param doc
     * @author Kevin Schuff
     */
    public void convertOne(MongoCollection toCollection, Document doc){
        String _id = doc.getString("_id");
        Document test = (Document) toCollection.find(eq("_id", _id)).first();
        if (test == null){
            // analyze speech and add NLP information to doc
            Document speechNLP = analyzeSpeechDoc(doc);
            toCollection.insertOne(speechNLP);
        }
    }

    /**
     * This method takes speech document and analyzes it with a nlp pipeline and adds gathered information to the document
     * @param speechDoc
     * @return analyzed speech document
     * @author Kevin Schuff
     */
    public Document analyzeSpeechDoc(Document speechDoc){
        // get speech text
        String text = speechDoc.getString("text");
        JCas jCas = textToCAS(text);

        // serializing CAS to String
        String serializedSpeechCAS = serializeJCas(jCas);
        // setting a boundary so Documents won't be bigger than 16mb
        if(serializedSpeechCAS.length() <= 1500000){
            speechDoc.put("uima", new JSONObject(serializedSpeechCAS).toString());
        }

        // get named entities
        List<List<Document>> namedEntities = getNamedEntities(jCas);
        speechDoc.put("persons", namedEntities.get(0));
        speechDoc.put("locations", namedEntities.get(1));
        speechDoc.put("organisations", namedEntities.get(2));

        // get tokens
        speechDoc.put("token", getTokens(jCas));

        // get sentences
        speechDoc.put("sentences", getSentences(jCas));

        // get Sentiment
        speechDoc.put("sentiment", getSentiment(jCas));

        // get lemmas
        speechDoc.put("lemma", getLemma(jCas));

        // get ddc
        speechDoc.put("ddc", getDDCs(jCas));

        // get pos
        speechDoc.put("pos", getPOSs(jCas));

        return speechDoc;
    }

    /**
     * Extracts all named entities from a given jcas
     * @param jCas
     * @return named entities
     * @author Kevin Schuff
     */
    public List<List<Document>> getNamedEntities(JCas jCas){
        List<List<Document>> namedEntities = new ArrayList<>();
        List<Document> persons = new ArrayList<>();
        List<Document> locations = new ArrayList<>();
        List<Document> organisations = new ArrayList<>();
        for(NamedEntity namedEntity : JCasUtil.select(jCas, NamedEntity.class)){
            Document doc = new Document();
            doc.put("begin", namedEntity.getBegin());
            doc.put("end", namedEntity.getEnd());
            doc.put("value", namedEntity.getCoveredText());
            if(namedEntity.getValue().equals("PER")){
                persons.add(doc);
            }
            if(namedEntity.getValue().equals("LOC")){
                locations.add(doc);
            }
            if(namedEntity.getValue().equals("ORG")){
                organisations.add(doc);
            }
        }
        namedEntities.add(persons);
        namedEntities.add(locations);
        namedEntities.add(organisations);
        return namedEntities;
    }

    /**
     * Extracts all Tokens from a given Jcas
     * @param jCas
     * @return tokens
     * @author Kevin Schuff
     */
    public List<Document> getTokens(JCas jCas){
        List<Document> tokens = new ArrayList<>();
        for(Token token : JCasUtil.select(jCas, Token.class)){
            Document doc = new Document();
            doc.put("begin", token.getBegin());
            doc.put("end", token.getEnd());
            doc.put("value", token.getCoveredText());
            tokens.add(doc);
        }
        return tokens;
    }

    /**
     * Extracts all Sentences from a given Jcas
     * @param jCas
     * @return sentences
     * @author Kevin Schuff
     */
    public List<Document> getSentences(JCas jCas){
        List<Document> sentences = new ArrayList<>();
        for(Sentence sentence : JCasUtil.select(jCas, Sentence.class)){
            Document doc = new Document();
            doc.put("begin", sentence.getBegin());
            doc.put("end", sentence.getEnd());
            doc.put("value", sentence.getCoveredText());
            // subject to change
            for (Sentiment sentiment : JCasUtil.selectCovered(Sentiment.class, sentence)){
                doc.put("sentiment", sentiment.getSentiment());
            }
            sentences.add(doc);
        }
        return sentences;
    }

    /**
     * Extracts the average Sentiment over all Sentences in a given JCas
     * @param jCas
     * @return sentiment
     * @author Kevin Schuff
     */
    public double getSentiment(JCas jCas){
        double sentimentAverage = 0;
        Collection<Sentence> sentences= JCasUtil.select(jCas, Sentence.class);
        for(Sentence sentence : sentences){
            for(Sentiment sentiment :  JCasUtil.selectCovered(Sentiment.class, sentence)){
                sentimentAverage = sentimentAverage + sentiment.getSentiment();
            }
        }
        return sentimentAverage/sentences.size();
    }

    /**
     * Extracts all Lemmas from a given Jcas
     * @param jCas
     * @return lemmas
     * @author Kevin Schuff
     */
    public List<Document> getLemma(JCas jCas){
        List<Document> lemmas = new ArrayList<>();
        for(Lemma lemma :  JCasUtil.select(jCas, Lemma.class)){
            Document doc = new Document();
            doc.put("begin", lemma.getBegin());
            doc.put("end", lemma.getEnd());
            // check value
            doc.put("value", lemma.getValue());
            // from ML3
            POS p = JCasUtil.selectCovered(POS.class, lemma).get(0);
            if (p != null) {
                doc.put("pos", p.getType().getShortName());
            }
            lemmas.add(doc);
        }
        return lemmas;
    }

    /**
     * Extracts all ddc categories from a given Jcas
     * @param jCas
     * @return ddcs
     * @author Kevin Schuff
     */
    public List<Document> getDDCs(JCas jCas){
        List<Document> ddcs = new ArrayList<>();
        for(CategoryCoveredTagged cct : JCasUtil.select(jCas, CategoryCoveredTagged.class)){
            Document doc = new Document();
            doc.put("begin", cct.getBegin());
            doc.put("end", cct.getEnd());
            doc.put("value", this.categoriesMapper.getCategory(cct.getValue()));
            doc.put("score", cct.getScore());
            ddcs.add(doc);
        }
        return ddcs;
    }

    /**
     * Extracts all part of Speeches(pos) from a given Jcas
     * @param jCas
     * @return pos
     * @author Kevin Schuff
     */
    public List<Document> getPOSs(JCas jCas){
        List<Document> poss = new ArrayList<>();
        for(POS pos : JCasUtil.select(jCas, POS.class)){
            Document doc = new Document();
            doc.put("begin", pos.getBegin());
            doc.put("end", pos.getEnd());
            doc.put("value", pos.getPosValue());
            doc.put("type", pos.getType().getShortName());
            poss.add(doc);
        }
        return poss;
    }

    /**
     * This method analyses a text with and nlp pipeline and returns the cas
     * @return Jcas from text
     * @author Kevin Schuff
     */
    public JCas textToCAS(String text) {
        JCas jCas = null;
        try {
            jCas = JCasFactory.createText(text, "de");
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        }
        try {
            SimplePipeline.runPipeline(jCas, this.engine.getBuilder().createAggregate());
        } catch (AnalysisEngineProcessException e) {
            throw new RuntimeException(e);
        } catch (ResourceInitializationException e) {
            throw new RuntimeException(e);
        }
        return jCas;
    }

    /**
     * This method serializes jcas to cas to a string, inspired by ML3.
     * @param jCas
     * @return serialized jcas
     * @author Guiseppe Abrami
     */
    public String serializeJCas(JCas jCas){
        String speechCAS = null;
        try {
            speechCAS = MongoSerialization.serializeJCas(jCas);
        } catch (UnknownFactoryException e) {
            throw new RuntimeException(e);
        } catch (SerializerInitializationException e) {
            throw new RuntimeException(e);
        } catch (CasSerializationException e) {
            throw new RuntimeException(e);
        }
        return speechCAS;

    }

    /**
     * This method deserializes a string to JCas, inspired by ML3.
     * @param serializedSpeechCAS
     * @return Jcas
     * @author Guiseppe Abrami
     */
    public JCas deserializeJCas(String serializedSpeechCAS){
        try {
            JCas jCas = JCasFactory.createJCas();
            MongoSerialization.deserializeJCas(jCas, serializedSpeechCAS);
            return jCas;
        } catch (UIMAException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructs a Speech_NLP_impl through a MongoDocument
     * @param doc
     * @return speechnlp instance
     * @author Kevin Schuff
     */
    public Speech_NLP_Impl speechNLPConstructor(Document doc){
        String json = doc.toJson(this.settings);
        Speech_NLP_Impl speechNLP = this.gson.fromJson(json, Speech_NLP_Impl.class);
        return speechNLP;
    }

    /**
     * Construcs a Comment_MLP_impl through a MongoDocument
     * @param doc
     * @return commentnlp instance
     * @author Kevin Schuff
     */
    public Comment_NLP_Impl commentNLPConstructor(Document doc){
        String json = doc.toJson(this.settings);
        Comment_NLP_Impl commentNLP = this.gson.fromJson(json, Comment_NLP_Impl.class);
        return commentNLP;
    }

    /**
     * Construcs a Protocol_MLP_impl through a MongoDocument
     * @author Matthias Beck
     * @param doc
     * @return protocolnlp instance
     */
    public Protocol_NLP_Impl protocolNLPConstructor(Document doc){
        String json = doc.toJson(this.settings);
        Protocol_NLP_Impl protocolNLP = this.gson.fromJson(json, Protocol_NLP_Impl.class);
        return protocolNLP;
    }




}
