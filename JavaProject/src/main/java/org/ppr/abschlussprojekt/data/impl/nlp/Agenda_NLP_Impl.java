package org.ppr.abschlussprojekt.data.impl.nlp;

import org.bson.Document;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.ppr.abschlussprojekt.helper.LaTeXHelper.LatexHelper;

import java.util.List;
import java.util.Map;

/**
 * This class is only for the mongodb upload, to make it look similiar to ML Database
 * @author Kevin Schuff
 */
public class Agenda_NLP_Impl implements Agenda {
    private String index = "";
    private String id = "";
    private String title = "";
    public Agenda_NLP_Impl(Document doc){
        this.index = (String) doc.get("index");
        this.id = (String) doc.get("id");
        this.title = (String) doc.get("title");
    }

    /**
     * This method gets the agendaid
     * @return agendaid
     * @author Kevin Schuff
     */
    @Override
    public String getAgendaId() {
        return null;
    }

    /**
     * This method gets the title
     * @return title
     * @author Kevin Schuff
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * This method sets the title
     * @param title
     * @author Kevin Schuff
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This method sets the agendaid
     * @param agendaId
     * @author Kevin Schuff
     */
    @Override
    public void setAgendaId(String agendaId) {
        this.id = agendaId;
    }

    /**
     * This method gets the agendaindex
     * @return
     * @author Kevin Schuff
     */
    @Override
    public String getAgendaIndex() {
        return this.index;
    }

    /**
     * This method sets the index of an agendaitem, which is the title ?
     * @param index
     * @author Kevin Schuff
     */
    public void setIndex(String index){
        this.index = index;
    }

    /**
     * This method gets the speechlist
     * @return speechlist
     * @author Kevin Schuff
     */
    @Override
    public List<Speech> getSpeechList() {
        return null;
    }

    /**
     * This method gets the protocol
     * @return protocol
     * @author Kevin Schuff
     */
    @Override
    public Protocol getProtocol() {
        return null;
    }

    /**
     * This method gets the speakermap
     * @return speakermap
     * @author Kevin Schuff
     */
    @Override
    public Map<String, Speaker> getSpeakerMap() {
        return null;
    }

    /**
     * This method adds a speech
     * @param speech
     * @author Kevin Schuff
     */
    @Override
    public void addSpeech(Speech speech) {

    }

    /**
     * This method gets the commentmap
     * @return
     * @author Kevin Schuff
     */
    @Override
    public Map<String, List<Comment>> getCommentMap() {
        return null;
    }

    /**
     * This method adds a commentmap
     * @param speech
     * @author Kevin Schuff
     */
    @Override
    public void addCommentMap(Speech speech) {

    }

    /**
     * This method adds the speaker
     * @param speaker
     * @param allAbgeordneteMap
     * @author Kevin Schuff
     */
    @Override
    public void addSpeaker(Speaker speaker, Map<String, Speaker> allAbgeordneteMap) {

    }

    /**
     * Returns the String with the tex-code for the agenda given the structure of the template for an agenda
     *
     * @author Matthias Beck
     * @return
     */
    public String toTex() {
        LatexHelper latexHelper = new LatexHelper();

        String agendaString = latexHelper.getTemplateAsString("agenda_template.txt");

        agendaString = agendaString.replace("%%AGENDAITEM%%", index);

        return agendaString;
    }
}
