package org.ppr.abschlussprojekt.data.impl.nlp;

import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.impl.mongodb.Speaker_MongoDB_Impl;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.ppr.abschlussprojekt.helper.LaTeXHelper.LatexHelper;
import org.ppr.abschlussprojekt.helper.NLPHelper.Note;

import java.util.List;

/**
 * This class resembles a comment in the database after nlp analysis
 * @author Kevin Schuff
 */
public class Comment_NLP_Impl implements NLPtemplate {
    private String _id;
    private String speaker;
    private String speech;
    private String text;
    private List<Note> persons;
    private List<Note> locations;
    private List<Note> organisations;
    private List<Note> token;
    private List<Note> sentences;
    private double sentiment;
    private List<Note> lemma;
    private List<Note> ddc;
    private List<Note> pos;
    private String uima;

    /**
     * This method sets the id
     * @param _id
     * @author Kevin Schuff
     */
    public void set_id(String _id){this._id = _id;}

    /**
     * This method  gets the id
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
     * This method sets the speech
     * @param speech
     * @author Kevin Schuff
     */
    public void setSpeech(String speech) {
        this.speech = speech;
    }

    /**
     * This method gets a speech
     * @return speech
     * @author Kevin Schuff
     */
    public String getSpeech() {
        return this.speech;
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
     * Return the tex code for a comment
     *
     * @author Matthias Beck
     * @return
     */
    public String toTex() {
        LatexHelper latexHelper = new LatexHelper();
        MongoDBHandler dbHandler = new MongoDBHandler();

        String commentString = latexHelper.getTemplateAsString("comment_template.txt");

        // replaces the placeholders with data
        if (commentString.contains("%%REDNER%%")) {
            Speaker_MongoDB_Impl speakerMongoDB = new Speaker_MongoDB_Impl(dbHandler.getOneDocument(dbHandler.getCollection("speakerWp1920"), speaker));
            commentString = commentString.replace("%%REDNER%%", speakerMongoDB.toTex());
        }
        if (commentString.contains("%%KOMMENTARTEXT%%")) {
            commentString = commentString.replace("%%KOMMENTARTEXT%%", text);
            commentString = commentString.replaceAll("&nbsp;"," ");
        }

        return commentString;
    }
}
