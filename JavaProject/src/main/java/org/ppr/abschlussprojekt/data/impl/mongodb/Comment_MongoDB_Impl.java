package org.ppr.abschlussprojekt.data.impl.mongodb;

import org.bson.Document;
import org.ppr.abschlussprojekt.data.impl.file.Comment_File_Impl;
import org.ppr.abschlussprojekt.data.interfaces.Comment;


/**
 * class for uploading a comment document
 * @author Jiayu Ma(implemented)
 */
public class Comment_MongoDB_Impl {

    private Comment comment;
    private String commentId = "";
    private String text = "";
    private String speechId = "";
    private String speakerId = "";
    private Document document;

    public Comment_MongoDB_Impl(Document document) {
        this.document = document;
        this.commentId = document.getString("_id");
        this.text = document.getString("text");
        this.speakerId = document.getString("speaker");
        this.speechId = document.getString("speech");
    }

    public Comment_MongoDB_Impl(Comment comment) {
        this.comment = comment;
        this.commentId = this.comment.getCommentSpeechId();
        this.text = this.comment.getContent();
        this.speechId = this.comment.getSpeech().getSpeechId();
        this.speakerId = this.comment.getSpeaker().getSpeakerId();
    }

    /**
     * Put the attributes in the document and return
     * This document can be uploaded to DB, it can also be used to build NLP_Impl objects
     * @author Jiayu Ma(implemented)
     */
    public Document getMongoDocument() {
        Document mongoDocument = new Document();
        mongoDocument.put("_id", this.commentId);
        mongoDocument.put("text", this.text);
        mongoDocument.put("speaker", this.speakerId);
        mongoDocument.put("speech", this.speechId);
        return mongoDocument;
    }

    /**
     * comment id
     * @return comment id
     * @author Jiayu Ma(implemented)
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * text
     * @return text
     * @author Jiayu Ma(implemented)
     */
    public String getText() {
        return text;
    }

    /**
     * speech id
     * @return speech id
     * @author Jiayu Ma(implemented)
     */
    public String getSpeechId() {
        return speechId;
    }

    /**
     * speaker id
     * @return speaker id
     * @author Jiayu Ma(implemented)
     */
    public String getSpeakerId() {
        return speakerId;
    }

    /**
     * get document downloaded from DB
     * @return the document downloaded from DB
     * @author Jiayu Ma(implemented)
     */
    public Document getDownloadedDocument() {
        return document;
    }

}
