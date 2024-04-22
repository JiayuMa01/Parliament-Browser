package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.data.interfaces.*;
import java.io.Serializable;

/**
 * @author Jiayu Ma(implemented)
 */
public class Comment_File_Impl implements Comment, Serializable {
    private String commentSpeechId = "";
    private int commentIndex = 0;
    private String content = "";
    private Speech speech = null;
    private Fraction fraction = null;
    private Party party = null;
    private Speaker speaker = null;

    public Comment_File_Impl() {
        super();
    }

    public Comment_File_Impl(String content, int commentIndex, String speechId) {
        super();
        this.content = content;
        this.commentIndex = commentIndex;
        this.commentSpeechId = speechId + "-" + this.content.hashCode(); //ID19205800-hashcode
    }

    /**
     * @return information of all
     * @author Jiayu Ma(implemented)
     */
    public String toString(){
        String value = "commentRedeId:" + this.commentSpeechId + "  speechId:" + this.speech.getSpeechId()
                + "  speakerId:" + this.speaker.getSpeakerId() + "  speakerName:" + this.speaker.getVorname() + " " +this.speaker.getNachname()
                + "  fractionName:" + this.fraction.getFractionName() + "  partyName:" + this.party.getPartyName();
        return value;
    }

    /**
     * set attributes with speech
     * @param speech speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setUpAttributesWithSpeech(Speech speech) {
        this.speech = speech;
        this.speaker = speech.getSpeaker();
    }

    /**
     * @return Id of comment speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getCommentSpeechId() {
        return this.commentSpeechId;
    }

    /**
     * @param commentSpeechId Id of comment speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setCommentSpeechId(String commentSpeechId) {
        this.commentSpeechId = commentSpeechId;
    }

    /**
     * @return content of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getContent() {
        return this.content;
    }

    /**
     * @param content content of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return speech of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Speech getSpeech() {
        return this.speech;
    }

    /**
     * @param speech speech of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeech(Speech speech) {
        this.speech = speech;
    }

    /**
     * @return fraction of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Fraction getFraction() {
        return this.fraction;
    }

    /**
     * @param fraction fraction of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    /**
     * @return speaker of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    /**
     * @param speaker speaker of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    /**
     * @return index of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public int getCommentIndex() {
        return commentIndex;
    }

    /**
     * @param commentIndex index of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setCommentIndex(int commentIndex) {
        this.commentIndex = commentIndex;
    }

    /**
     * @return party of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Party getParty() {
        return party;
    }

    /**
     * @param party party of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setParty(Party party) {
        this.party = party;
    }
}
