package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.data.interfaces.*;
import org.w3c.dom.Node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Jiayu Ma
 */
public class Agenda_File_Impl implements Agenda, Serializable {
    private String agendaId = "";
    private String agendaIndex = "";
    private transient List<Speech> speechList = new ArrayList<Speech>();
    private transient Protocol protocol = null;
    private transient Map<String, List<Comment>> commentMap = new HashMap<>();
    private transient Map<String, Speaker> speakerMap = new HashMap<>();

    public Agenda_File_Impl() {
        super();
    }

    public Agenda_File_Impl(Node agendaNode, Protocol protocol) {
        super();
        setUpAttributes(agendaNode, protocol);
    }

    /**
     * @param agendaNode node of tagespunkt
     * @param protocol protokoll od tagespunkt
     */
    private void setUpAttributes(Node agendaNode, Protocol protocol) {
        this.protocol = protocol;
        this.agendaIndex = agendaNode.getAttributes().getNamedItem("top-id").getTextContent();
        this.agendaIndex = Pattern.compile("\\s*|\t|\r|\n").matcher(this.agendaIndex).replaceAll("");  //Tagesordnungspunkt1
        this.agendaId = this.agendaIndex + "-" + this.protocol.getProtocolTitle();  //Tagesordnungspunkt1-Plenarprotokoll 19/23
    }

    /**
     * @return information of all
     */
    public String toString(){
        String value = "agendaIndex:" + this.agendaIndex + "  protocolId:" + this.protocol.getProtocolTitle()
                + "  numSpeaker:" + this.speakerMap.size() + "  numSpeech:" + this.commentMap.size();
        return value;
    }

    /**
     * @return id of agenda
     */
    @Override
    public String getAgendaId() {
        return this.agendaId;
    }

    /**
     * @param agendaId id of agenda
     */
    @Override
    public void setAgendaId(String agendaId) {
        this.agendaId = agendaId;
    }

    /**
     * @return titel of agenda
     */
    @Override
    public String getAgendaIndex() {
        return this.agendaIndex;
    }

    /**
     * @return list of speech
     */
    @Override
    public List<Speech> getSpeechList() {
        return this.speechList;
    }

    /**
     * @return protokoll of agenda
     */
    @Override
    public Protocol getProtocol() {
        return this.protocol;
    }

    /**
     * @return map of comment
     */
    @Override
    public Map<String, List<Comment>> getCommentMap() {
        return commentMap;
    }

    /**
     * @return map of speaker
     */
    @Override
    public Map<String, Speaker> getSpeakerMap() {
        return this.speakerMap;
    }

    /**
     * @param speech speech of agenda
     */
    @Override
    public void addSpeech(Speech speech) {
        this.speechList.add(speech);
    }

    /**
     * @param speech map of comment
     */
    @Override
    public void addCommentMap(Speech speech){
        this.commentMap.put(speech.getSpeechId(), speech.getCommentList());
    }

    /**
     * @param speaker speaker in agenda
     * @param allSpeakerMap map of all speaker
     */
    @Override
    public void addSpeaker(Speaker speaker, Map<String, Speaker> allSpeakerMap) {
        if (allSpeakerMap.containsKey(speaker.getSpeakerId())) {
            this.speakerMap.put(speaker.getSpeakerId(), speaker);
        }
    }
}
