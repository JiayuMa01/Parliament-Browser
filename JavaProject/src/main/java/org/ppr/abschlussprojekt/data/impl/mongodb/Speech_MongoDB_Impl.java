package org.ppr.abschlussprojekt.data.impl.mongodb;

import org.bson.Document;
import org.ppr.abschlussprojekt.data.impl.file.Speech_File_Impl;
import org.ppr.abschlussprojekt.data.interfaces.Speech;
import java.util.*;
import java.util.stream.Collectors;


/**
 * class for upload a speech document
 * Or receive a document from DB then used for NLP_Impl
 * @author Jiayu Ma(implemented)
 */
public class Speech_MongoDB_Impl {

    private Speech speech;
    private String speechId = "";
    private String speakerId = "";
    private Document agendaDoc = new Document();
    private Document protocolDoc = new Document();
    private String text = "";
    private List<String> commentIdList = new ArrayList<>();  // ArrayList<KommentarId>

    // get from downloaded documents
    private Document document;
    // get from protocol document
    private Long protocolDate = null;
    private Long protocolStartTime = null;
    private Long protocolEndTime = null;
    private int protocolIndex = 0;
    // get from protocol document embedded in downloaded document
    private String protocolTitle = "";
    private String protocolPlace = "";
    private int protocolWp = 19;
    // get from agenda document embedded in downloaded document
    private String agendaIndex = "";
    private String agendaId = "";
    private String agendaTitle = "";

    /**
     * generate a Speech_Mongo_Impl object using a downloaded document
     * Then use this object to generate a NLP_Impl object
     * @param document downloaded document from DB
     * @authot Jiayu Ma(implemented)
     */
    public Speech_MongoDB_Impl(Document document) {
        this.document = document;
        this.speechId = document.getString("_id");
        this.speakerId = document.getString("speaker");
        this.text = document.getString("text");
        this.commentIdList.addAll(document.getList("comments", String.class));
        // get from protocol document that embedded in downloaded document
        this.protocolDoc = document.get("protocol", Document.class);
        this.protocolDate = this.protocolDoc.getLong("date");
        this.protocolStartTime = this.protocolDoc.getLong("starttime");
        this.protocolEndTime = this.protocolDoc.getLong("endtime");
        this.protocolIndex = this.protocolDoc.getInteger("index");
        this.protocolTitle = this.protocolDoc.getString("title");
        this.protocolPlace = this.protocolDoc.getString("place");
        this.protocolWp = this.protocolDoc.getInteger("wp");
        // get from agenda document that embedded in downloaded document
        this.agendaDoc = document.get("agenda", Document.class);
        this.agendaIndex = this.agendaDoc.getString("index");
        this.agendaId = this.agendaDoc.getString("id");
        this.agendaTitle = this.agendaDoc.getString("title");
    }

    /**
     * generate a Speech_Mongo_Impl object using a Speech object
     * Then use this object to generate a document and upload into DB
     * @param speech a Speech_File_Impl object
     * @authot Jiayu Ma(implemented)
     */
    public Speech_MongoDB_Impl(Speech speech) {
        this.speech = speech;
        this.speechId = speech.getSpeechId();
        this.speakerId = speech.getSpeaker().getSpeakerId();
        setAgendaDoc();
        setProtocolDoc();
        this.text = this.speech.getText();
        this.commentIdList = this.speech.getCommentList().stream().map(comment -> comment.getCommentSpeechId()).collect(Collectors.toList());
    }

    /**
     * make the protocol document
     * @author Jiayu Ma(implemented)
     */
    private void setProtocolDoc() {
        this.protocolDoc.put("date", this.speech.getProtocol().getDate().getTime());
        this.protocolDoc.put("starttime", this.speech.getProtocol().getProtocolStart().getTime());
        this.protocolDoc.put("endtime", this.speech.getProtocol().getProtocolEnd().getTime());
        this.protocolDoc.put("index", this.speech.getProtocol().getProtocolIndex());
        this.protocolDoc.put("title", this.speech.getProtocol().getProtocolTitle());
        this.protocolDoc.put("place", this.speech.getProtocol().getPlace());
        this.protocolDoc.put("wp", this.speech.getProtocol().getWahlperiode());
    }

    /**
     * make the agenda document
     * @author Jiayu Ma(implemented)
     */
    private void setAgendaDoc() {
        this.agendaDoc.put("index", this.speech.getAgenda().getAgendaIndex());
        this.agendaDoc.put("id", this.speech.getAgenda().getAgendaId());
        this.agendaDoc.put("title", this.speech.getAgenda().getAgendaIndex());
    }

    /**
     * Put the attributes in the document and return.
     * This document can be uploaded to DB, it can also be used to build NLP_Impl objects
     * @author Jiayu Ma(implemented)
     */
    public Document getMongoDocument() {
        Document mongoDocument = new Document();
        mongoDocument.put("_id", this.speechId);
        mongoDocument.put("text", this.text);
        mongoDocument.put("speaker", this.speakerId);
        mongoDocument.put("protocol", this.protocolDoc);
        mongoDocument.put("comments", this.commentIdList);
        mongoDocument.put("agenda", this.agendaDoc);
        return mongoDocument;
    }


    /**
     * get speech id
     * @return speech id
     * @author Jiayu Ma(implemented)
     */
    public String getSpeechId() {
        return speechId;
    }

    /**
     * get speaker id
     * @return speaker id
     * @author Jiayu Ma(implemented)
     */
    public String getSpeakerId() {
        return speakerId;
    }

    /**
     * get speech text
     * @return speech text
     * @author Jiayu Ma(implemented)
     */
    public String getText() {
        return text;
    }

    /**
     * get comment id list of this speech
     * @return comment id list
     * @author Jiayu Ma(implemented)
     */
    public List<String> getCommentIdList() {
        return commentIdList;
    }

    /**
     * get document downloaded from DB
     * @return the document downloaded from DB
     * @author Jiayu Ma(implemented)
     */
    public Document getDownloadedDocument() {
        return document;
    }

    /**
     * get protocol date
     * @return protocol date
     * @author Jiayu Ma(implemented)
     */
    public Long getProtocolDate() {
        return protocolDate;
    }

    /**
     * get protocol start time
     * @return protocol start time
     * @author Jiayu Ma(implemented)
     */
    public Long getProtocolStartTime() {
        return protocolStartTime;
    }

    /**
     * get protocol end time
     * @return protocol end time
     * @author Jiayu Ma(implemented)
     */
    public Long getProtocolEndTime() {
        return protocolEndTime;
    }

    /**
     * get protocol index
     * @return protocol index
     * @author Jiayu Ma(implemented)
     */
    public int getProtocolIndex() {
        return protocolIndex;
    }

    /**
     * get protocol title
     * @return protocol title
     * @author Jiayu Ma(implemented)
     */
    public String getProtocolTitle() {
        return protocolTitle;
    }

    /**
     * get protocol place
     * @return protocol place
     * @author Jiayu Ma(implemented)
     */
    public String getProtocolPlace() {
        return protocolPlace;
    }

    /**
     * get protocol wahlperiode
     * @return protocol wahlperiode
     * @author Jiayu Ma(implemented)
     */
    public int getProtocolWp() {
        return protocolWp;
    }

    /**
     * get agenda index
     * @return agenda index
     * @author Jiayu Ma(implemented)
     */
    public String getAgendaIndex() {
        return agendaIndex;
    }

    /**
     * get agenda id
     * @return agenda id
     * @author Jiayu Ma(implemented)
     */
    public String getAgendaId() {
        return agendaId;
    }

    /**
     * get agenda title
     * @return agenda title
     * @author Jiayu Ma(implemented)
     */
    public String getAgendaTitle() {
        return agendaTitle;
    }


}
