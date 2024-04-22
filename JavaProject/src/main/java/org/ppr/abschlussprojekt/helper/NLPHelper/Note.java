package org.ppr.abschlussprojekt.helper.NLPHelper;

import org.bson.Document;

/**
 * This class resembles a token from an NLP analysis
 * @author Kevin Schuff
 */
public class Note {
    private int begin = 0;
    private int end = 0;
    private String value = "";
    private String type = null;
    private String pos = null;
    private double score = 0;
    private double sentiment = 0;

    /**
     * This method sets the begin of a token in a text
     * @param begin
     * @author Kevin Schuff
     */
    public void setBegin(int begin){
        this.begin = begin;
    }

    /**
     * This method returns the begin of a token in a text
     * @return begin
     * @author Kevin Schuff
     */
    public int getBegin(){
        return this.begin;
    }

    /**
     * This method sets the end of a token in a text
     * @param end
     * @author Kevin Schuff
     */
    public void setEnd(int end){
        this.end = end;
    }

    /**
     * This method returns the end of a token in a text
     * @return end
     * @author Kevin Schuff
     */
    public int getEnd(){
        return this.end;
    }

    /**
     * This method sets the value of a token
     * @param value
     * @author Kevin Schuff
     */
    public void setValue(String value){
        this.value = value;
    }

    /**
     * This method returns the value of a token
     * @return value
     * @author Kevin Schuff
     */
    public String getValue(){
        return this.value;
    }
    /**
     * This method sets the type of the part of speech
     * @param type
     * @author Kevin Schuff
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * This method returns the type of the part of speech
     * @return type
     * @author Kevin Schuff
     */
    public String getType(){
        return this.type;
    }
    /**
     * This method sets the pos of a lemma
     * @param pos
     * @author Kevin Schuff
     */
    public void setPos(String pos){
        this.pos = pos;
    }

    /**
     * This method gets the pos of a lemma
     * @return pos
     * @author Kevin Schuff
     */
    public String getPos(){
        return this.pos;
    }
    /**
     * This method sets the score of a ddc category
     * @param score
     * @author Kevin Schuff
     */
    public void setScore(double score){
        this.score = score;
    }

    /**
     * This method gets the score of a ddc category
     * @return score
     * @author Kevin Schuff
     */
    public double getScore(){
        return this.score;
    }

    /**
     * This method sets the sentiment of a sentence
     * @param sentiment
     * @author Kevin Schuff
     */
    public void setSentiment(double sentiment){
        this.sentiment = sentiment;
    }

    /**
     * This method returns the sentiment of a sentence
     * @return sentiment
     * @author Kevin Schuff
     */
    public double getSentiment(){
        return this.sentiment;
    }
}
