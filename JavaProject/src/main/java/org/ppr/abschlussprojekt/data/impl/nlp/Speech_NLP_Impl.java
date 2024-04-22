package org.ppr.abschlussprojekt.data.impl.nlp;



import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.impl.mongodb.Speaker_MongoDB_Impl;
import org.ppr.abschlussprojekt.data.interfaces.NLPtemplate;
import org.ppr.abschlussprojekt.helper.LaTeXHelper.LatexHelper;
import org.ppr.abschlussprojekt.helper.NLPHelper.Converter;
import org.ppr.abschlussprojekt.helper.NLPHelper.Note;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;

/**
 * This class the final representation of a speech, to be uploaded into db with gson
 * could not implement speaker interface since attribute would look different like speaker beeing only a String
 * @author Kevin Schuff
 */
public class Speech_NLP_Impl implements NLPtemplate {
    private String _id;
    private String text;
    private String speaker;
    private Protocol_NLP_Impl protocol;
    private List<String> comments;
    private Agenda_NLP_Impl agenda;
    private List<Note> persons;
    private List<Note> locations;
    private List<Note> organisations;
    private List<Note> token;
    private List<Note> sentences;
    private double sentiment;
    private List<Note> lemma;
    private List<Note> ddc;
    private List<Note> pos;
    private transient String uima;

    private List<Integer> personIndices;
    private List<Integer> organisationIndices;
    private List<Integer> locationIndices;

    /**
     * This method sets the PersonIndicesList
     * @param PersonIndicesList
     * @author Fabian Hamid Fazli
     */
    public void setPersonIndicesList(){
        List<Integer> personIndices = new ArrayList<>();
        for(Note note : this.persons){
            for(int i = note.getBegin(); i < note.getEnd(); i++){
                personIndices.add(i);
            }
        }
        this.personIndices = personIndices;
    }

    /**
     * This method gets the PersonIndicesList
     * @param PersonIndicesList
     * @author Fabian Hamid Fazli
     */

    public List<Integer> getPersonIndices(){
        return this.personIndices;
    }


    /**
     * This method sets the OrganisationIndicesList
     * @param OrganisationIndicesList
     * @author Fabian Hamid Fazli
     */
    public void setOrganisationIndicesList(){
        List<Integer> organisationIndices = new ArrayList<>();
        for(Note note : this.organisations){
            for(int i = note.getBegin(); i < note.getEnd(); i++){
                organisationIndices.add(i);
            }
        }
        this.organisationIndices = organisationIndices;
    }

    /**
     * This method gets the OrganisationIndicesList
     * @param OrganisationIndicesList
     * @author Fabian Hamid Fazli
     */

    public List<Integer> getOrganisationIndices(){
        return this.organisationIndices;
    }


    /**
     * This method sets the LocationIndicesList
     * @param LocationIndicesList
     * @author Fabian Hamid Fazli
     */
    public void setLocationIndicesList(){
        List<Integer> locationIndices = new ArrayList<>();
        for(Note note : this.locations){
            for(int i = note.getBegin(); i < note.getEnd(); i++){
                locationIndices.add(i);
            }
        }
        this.locationIndices = locationIndices;
    }

    /**
     * This method gets the LocationIndicesList
     * @return  LocationIndicesList
     * @author Fabian Hamid Fazli
     */

    public List<Integer> getLocationIndices(){return this.locationIndices;}




    /**
     * This method sets the id
     * @param _id
     * @author Kevin Schuff
     */
    public void set_id(String _id){
        this._id = _id;
    }

    /**
     * This method gets the id
     * @return id
     * @author Kevin Schuff
     */
    public String get_id(){
        return this._id;
    }

    /**
     * This method sets the text
     * @param text
     * @author Kevin Schuff
     */
    public void setText(String text){
        this.text = text;
    }

    /**
     * This method gets the text
     * @return text
     * @author Kevin Schuff
     */
    public String getText(){
        return this.text;
    }

    /**
     * This method sets the speaker
     * @param speakerID
     * @author Kevin Schuff
     */
    public void setSpeaker(String speakerID){
        this.speaker = speakerID;
    }

    /**
     * This method gets the speaker
     * @return speaker
     * @author Kevin Schuff
     */
    public String getSpeaker(){
        return this.speaker;
    }

    /**
     * This method sets the protocol
     * @param protocol
     * @author Kevin Schuff
     */
    public void setProtocol(Protocol_NLP_Impl protocol){
        this.protocol = protocol;
    }

    /**
     * This method gets the protocol
     * @return protocol
     * @author Kevin Schuff
     */
    public Protocol_NLP_Impl getProtocol(){
        return this.protocol;
    }

    /**
     * This method sets the comments
     * @param comments
     * @author Kevin Schuff
     */
    public void setComments(List<String> comments){
        this.comments = comments;
    }

    /**
     * This method gets the comments
     * @return comments
     * @author Kevin Schuff
     */
    public List<String> getComments(){
        return this.comments;
    }

    /**
     * This method sets the agenda
     * @param agenda
     * @author Kevin Schuff
     */
    public void setAgenda(Agenda_NLP_Impl agenda){
        this.agenda = agenda;
    }

    /**
     * This method gets the agenda
     * @return
     * @author Kevin Schuff
     */
    public Agenda_NLP_Impl getAgenda(){
        return this.agenda;
    }
    @Override
    public void setPersons(List<Note> persons){
        this.persons = persons;
    }

    @Override
    public void addPerson(Note person) {
        this.persons.add(person);
    }

    @Override
    public List<Note> getPersons(){
        return this.persons;
    }
    @Override
    public void setLocations(List<Note> locations){
        this.locations = locations;
    }

    @Override
    public void addLocation(Note location) {
        this.locations.add(location);
    }
    @Override
    public List<Note> getLocations(){
        return this.locations;
    }

    @Override
    public void setOrganisations(List<Note> organisations){
        this.organisations= organisations;
    }
    @Override
    public void addOrganisation(Note organisation) {
        this.organisations.add(organisation);
    }
    @Override
    public List<Note> getOrganisations(){
        return this.organisations;
    }

    @Override
    public void setTokens(List<Note> token){
        this.token = token;
    }
    @Override
    public void addToken(Note token) {
        this.token.add(token);
    }
    @Override
    public List<Note> getTokens(){
        return this.token;
    }

    @Override
    public void setSentences(List<Note> sentences){
        this.sentences = sentences;
    }
    @Override
    public void addSentence(Note sentence) {
        this.sentences.add(sentence);
    }
    @Override
    public List<Note> getSentences(){
        return this.sentences;
    }

    @Override
    public void setSentiment(double sentiment){
        this.sentiment = sentiment;
    }
    @Override
    public double getSentiment(){
        return this.sentiment;
    }

    @Override
    public void setLemmas(List<Note> lemmas) {
        this.lemma = lemmas;
    }
    @Override
    public void addLemma(Note lemma) {
        this.lemma.add(lemma);
    }
    @Override
    public List<Note> getLemmas() {
        return this.lemma;
    }

    @Override
    public void setDdc(List<Note> ddc) {
        this.ddc = ddc;
    }
    @Override
    public void addDdc(Note ddc) {
        this.ddc.add(ddc);
    }
    @Override
    public List<Note> getDdc() {
        return this.ddc;
    }

    @Override
    public void setPOS(List<Note> pos) {
        this.pos = pos;
    }
    @Override
    public void addPOS(Note pos) {
        this.pos.add(pos);
    }
    @Override
    public List<Note> getPOS() {
        return this.pos;
    }

    @Override
    public void setUima(String uima) {
        this.uima = uima;
    }
    @Override
    public String getUima() {
        return this.uima;
    }

    /**
     * Function returns the latex code for a speech
     *
     * @author Matthias Beck
     * @return String representation of the tex-code for a speech.
     */
    public String toTex(){
        LatexHelper latexHelper = new LatexHelper();
        MongoDBHandler dbHandler = new MongoDBHandler();
        Converter converter = new Converter();

        String speechString = latexHelper.getTemplateAsString("speech_template.txt");

        // Replace the variables in the string defined in the template
        if (speechString.contains("%%REDEID%%")) {
            speechString = speechString.replace("%%REDEID%%", _id);
        }
        if (speechString.contains("%%REDNER%%")) {
            Speaker_MongoDB_Impl speakerMongoDB = new Speaker_MongoDB_Impl(dbHandler.getOneDocument(dbHandler.getCollection("speakerWp1920"), speaker));
            speechString = speechString.replace("%%REDNER%%", speakerMongoDB.toTex());
        }
        if (speechString.contains("%%REDETEXT%%")) {
            String speechText = text;
            speechText = speechText.replaceAll("\u2003", " ");
            speechString = speechString.replace("%%REDETEXT%%", speechText);
        }

        // gets the comments of a speech in sorted order
        Document queryDocument = new Document();
        queryDocument.append("speech", _id);
        BasicDBObject sortObject = new BasicDBObject("_id", 1);
        MongoCursor<Document> commentCursor = dbHandler.doSortedQueryIterator("comment", queryDocument, sortObject);

        // replaces the placeholder for the comments with the data
        if (speechString.contains("%%KOMMENTARE%%")) {
            String commentString = "";
            while (commentCursor.hasNext()) {
                Comment_NLP_Impl commentNlp = converter.commentNLPConstructor(commentCursor.next());
                commentString += commentNlp.toTex();
            }

            speechString = speechString.replace("%%KOMMENTARE%%", commentString);
        }

        // used for the output oft the DDC category
        if (speechString.contains("DDC")) {
            speechString = speechString.replace("%%DDC%%", ddc.get(0).getValue());
        }

        // used for the named entities (personen, orte, organisationen)
        if (speechString.contains("PERSONEN")) {
            String personString = "";
            for (Note person : persons) {
                personString += person.getValue() + ", ";
            }
            speechString = speechString.replace("%%PERSONEN%%", personString);
        }

        if (speechString.contains("ORTE")) {
            String locationString = "";
            for (Note location : locations) {
                locationString += location.getValue() + ", ";
            }
            speechString = speechString.replace("%%ORTE%%", locationString);
        }

        if (speechString.contains("ORGANISATIONEN")) {
            String organisationString = "";
            for (Note organisation : organisations) {
                organisationString += organisation.getValue() + ", ";
            }
            speechString = speechString.replace("%%ORGANISATIONEN%%", organisationString);
        }

        // returns the sentiment for each sentence of a speech
        if (speechString.contains("SENTIMENT")) {
            String sentimentString = "";
            for (Note sentence : sentences) {
                sentimentString += sentence.getSentiment() + ", ";
            }
            speechString = speechString.replace("%%SENTIMENT%%", sentimentString);
        }

        return speechString;
    }

}
