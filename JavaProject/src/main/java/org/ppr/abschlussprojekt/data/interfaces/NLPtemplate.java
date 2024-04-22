package org.ppr.abschlussprojekt.data.interfaces;

import org.ppr.abschlussprojekt.helper.NLPHelper.Note;

import java.util.List;

/**
 * This interface can be used for analyzed speeches and comments
 * @author Kevin Schuff
 */
public interface NLPtemplate {
    /**
     * This method sets the persons
     * @param persons
     */
    public void setPersons(List<Note> persons);

    /**
     * This method adds a person to persons
     * @param person
     */
    public void addPerson(Note person);

    /**
     * This method gets the persons
     * @return
     */
    public List<Note> getPersons();

    /**
     * This emthod sets the locations
     * @param locations
     */
    public void setLocations(List<Note> locations);

    /**
     * This method adds a location to locations
     * @param location
     */
    public void addLocation(Note location);

    /**
     * This method gets the locations
     * @return
     */
    public List<Note> getLocations();

    /**
     * This method sets the organisations
     * @param organisations
     */
    public void setOrganisations(List<Note> organisations);

    /**
     * This method adds an organisation to organisations
     * @param organisation
     */
    public void addOrganisation(Note organisation);

    /**
     * This method gets the organisations
     * @return
     */
    public List<Note> getOrganisations();

    /**
     * This method sets the tokens
     * @param tokens
     */
    public void setTokens(List<Note> tokens);

    /**
     * This method adds a token to tokens
     * @param token
     */
    public void addToken(Note token);

    /**
     * This method gets the tokens
     * @return
     */
    public List<Note> getTokens();

    /**
     * This method sets the sentences
     * @param sentences
     */
    public void setSentences(List<Note> sentences);

    /**
     * This method adds a sentence to sentences
     * @param sentence
     */
    public void addSentence(Note sentence);

    /**
     * This method gets the sentences
     * @return
     */
    public List<Note> getSentences();

    /**
     * This method sets the sentiment
     * @param sentiment
     */
    public void setSentiment(double sentiment);

    /**
     * This method gets the sentiment
     * @return
     */
    public double getSentiment();

    /**
     * This method sets the lemmas
     * @param lemmas
     */
    public void setLemmas(List<Note> lemmas);

    /**
     * This method adds a lemma to lemmas
     * @param lemma
     */
    public void addLemma(Note lemma);

    /**
     * This method gets the lemmas
     * @return
     */
    public List<Note> getLemmas();

    /**
     * This methods sets the ddcs
     * @param ddc
     */
    public void setDdc(List<Note> ddc);

    /**
     * This method adds a ddc to ddcs
     * @param ddc
     */
    public void addDdc(Note ddc);

    /**
     * This method gets the ddcs
     * @return
     */
    public List<Note> getDdc();

    /**
     * This method sets the pos
     * @param pos
     */
    public void setPOS(List<Note> pos);

    /**
     * This method adds a pos to pos's
     * @param pos
     */
    public void addPOS(Note pos);

    /**
     * This method gets the pos
     * @return
     */
    public List<Note> getPOS();

    /**
     * This method sets the uima
     * @param uima
     */
    public void setUima(String uima);

    /**
     * This method gets the uima
     * @return
     */
    public String getUima();

}
