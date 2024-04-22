package org.ppr.abschlussprojekt.data.impl.nlp;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.interfaces.Agenda;
import org.ppr.abschlussprojekt.data.interfaces.Protocol;
import org.ppr.abschlussprojekt.data.interfaces.Speaker;
import org.ppr.abschlussprojekt.helper.LaTeXHelper.LatexHelper;
import org.ppr.abschlussprojekt.helper.NLPHelper.Converter;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class resembles a protocol after nlp analysis in a speech
 * @author Kevin Schuff(implemented), Matthias Beck(modified)
 */
public class Protocol_NLP_Impl implements Protocol {

    private Long date = null;
    private Long starttime = null;
    private Long endtime = null;
    private int index = 0;
    private String title = "";
    private String place = "";
    private int wp = 0;

    /**
     * This method gets the protocolid
     * @return protocolid
     * @author Kevin Schuff
     */
    @Override
    public String getProtocolId() {
        return null;
    }

    /**
     * This method gets the protocolindex
     * @return protocolindex
     * @author Kevin Schuff
     */
    @Override
    public int getProtocolIndex() {
        return this.index;
    }

    /**
     * This method sets the index
     * @param index
     * @author Kevin Schuff
     */
    public void setIndex(int index){
        this.index = index;
    }

    /**
     * This method gets the protocoltitle
     * @return title
     * @author Kevin Schuff
     */
    @Override
    public String getProtocolTitle() {
        return this.title;
    }

    /**
     * This method sets title
     * @author Kevin Schuff
     */
    public void setTitle(){
        this.title = title;
    }

    /**
     * This method gets the date
     * @return date
     * @author Kevin Schuff
     */
    @Override
    public Date getDate() {
        // saved date as timestamp, converting it back from long to TS to date
        Date dateFix = new Date(this.date);
        return dateFix;
    }

    /**
     * Set Date as Long type
     * @param date
     * @author Kevin Schuff
     */
    public void setDate(Long date){
        this.date = date;
    }

    /**
     * This method gets the protocolstart
     * @return start
     * @author Kevin Schuff
     */
    @Override
    public Timestamp getProtocolStart() {
        return new Timestamp(this.starttime);
    }

    /**
     * Set starttime as long type
     * @param starttime
     * @author Kevin Schuff
     */
    public void setStarttime(Long starttime){
        this.starttime = starttime;
    }

    /**
     * This method gets the protocolend
     * @return end
     * @author Kevin Schuff
     */
    @Override
    public Timestamp getProtocolEnd() {
        return new Timestamp(this.endtime);
    }

    /**
     * This method sets the endtime
     * @param endtime
     * @author Kevin Schuff
     */
    public void setEndtime(Long endtime){
        this.endtime = endtime;
    }

    /**
     * This method gets the wahlperiode
     * @return wahlperiode
     * @author Kevin Schuff
     */
    @Override
    public int getWahlperiode() {
        return this.wp;
    }

    /**
     * This method sets the wp
     * @param wp
     * @author Kevin Schuff
     */
    public void setWp(int wp){
        this.wp = wp;
    }

    /**
     * This method gets the place
     * @return place
     * @author Kevin Schuff
     */
    @Override
    public String getPlace() {
        return this.place;
    }

    /**
     * This method sets the place
     * @param place
     * @author Kevin Schuff
     */
    public void setPlace(String place){
        this.place = place;
    }

    /**
     * This method gets the agendalist
     * @return agendalist
     * @author Kevin Schuff
     */
    @Override
    public List<Agenda> getAgendaList() {
        return null;
    }

    /**
     * This method adds ana genda
     * @param agenda
     * @author Kevin Schuff
     */
    @Override
    public void addAgenda(Agenda agenda) {

    }

    /**
     * This method gets the speakermap
     * @return null
     * @author Kevin Schuff
     */
    @Override
    public Map<String, Speaker> getSpeakerMap() {
        return null;
    }
    /**
     * This method sets the speaker
     * @author Kevin Schuff
     */
    @Override
    public void setSpeakerMap() {

    }

    /**
     * Returns the latex code for a protocol, which already contains the speeches for this protocol
     *
     * @author Matthias Beck
     * @return String representation of the tex-code for the protocol.
     */
    public String toTex(String speeches) {
        LatexHelper latexHelper = new LatexHelper();
        MongoDBHandler dbHandler = new MongoDBHandler();
        Converter converter = new Converter();

        // creates variables needed for the task
        ArrayList<String> agenda = new ArrayList<>();
        int speechindex = 1;
        String protocolString = latexHelper.getTemplateAsString("protocol_template.txt");
        String speechString = "";

        // Replace the variables in the string defined in the template
        if (protocolString.contains("%%PROTOKOLLINDEX%%")) {
            protocolString = protocolString.replace("%%PROTOKOLLINDEX%%", index+"");
        }
        if (protocolString.contains("%%WAHLPERIODE%%")) {
            protocolString = protocolString.replace("%%WAHLPERIODE%%", wp+"");
        }

        // get all speeches of the protocol in sorted order from the database
        Document queryDocument = new Document();
        queryDocument.append("protocol.index", index);
        queryDocument.append("protocol.wp", wp);
        BasicDBObject sortObject = new BasicDBObject("_id", 1);
        MongoCursor<Document> speechCursor = dbHandler.doSortedQueryIterator("speechComplete", queryDocument, sortObject);

        // creates the latex code for the agenda and the speeches of the protocol
        // if given a set of speeches only they will be intergrated in the protocol
        if (protocolString.contains("%%REDEN%%")) {
            while (speechCursor.hasNext()) {
                if (!(speeches.equals("all") || speeches.contains(speechindex+""))) {
                    speechindex++;
                }
                else {
                    System.out.println("Index: " + speechindex);
                    try {
                        Speech_NLP_Impl speechNLP = converter.speechNLPConstructor(speechCursor.next());

                        // this part is used to create the agenda for a protocol
                        if (!agenda.contains(speechNLP.getAgenda().getAgendaIndex())) {
                            agenda.add(speechNLP.getAgenda().getAgendaIndex());
                            Agenda_NLP_Impl agendaNlp = speechNLP.getAgenda();
                            speechString += agendaNlp.toTex();
                        }
                        speechString += speechNLP.toTex();
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    speechindex++;
                }
            }

            protocolString = protocolString.replace("%%REDEN%%", speechString);
        }

        return protocolString;
    }
}
