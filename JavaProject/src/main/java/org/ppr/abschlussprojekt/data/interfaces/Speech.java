package org.ppr.abschlussprojekt.data.interfaces;

import org.bson.Document;

import java.util.List;
import java.util.Map;

/**
 * @author Jiayu Ma
 */
public interface Speech {
    /**
     * @return id
     * @author Jiayu Ma(implemented)
     */
    String getSpeechId();

    /**
     * set id
     * @author Jiayu Ma(implemented)
     */
    void setSpeechId(String speechId);

    /**
     * @return text
     * @author Jiayu Ma(implemented)
     */
    String getText();

    /**
     * set text
     * @author Jiayu Ma(implemented)
     */
    void setText(String text);

    /**
     * get speaker
     * @author Jiayu Ma(implemented)
     */
    Speaker getSpeaker();

    /**
     * set speaker
     * @author Jiayu Ma(implemented)
     */
    void setSpeaker(Speaker speaker);

    /**
     * @return comment life
     * @author Jiayu Ma(implemented)
     */
    List<Comment> getCommentList();

    /**
     * @return protocol
     * @author Jiayu Ma(implemented)
     */
    Protocol getProtocol();

    /**
     * set protocol
     * @author Jiayu Ma(implemented)
     */
    void setProtocol(Protocol protocol);

    /**
     * @return agenda
     * @author Jiayu Ma(implemented)
     */
    Agenda getAgenda();

    /**
     * set agenda
     * @author Jiayu Ma(implemented)
     */
    void setAgenda(Agenda agenda);

    /**
     * set comment
     * @author Jiayu Ma(implemented)
     */
    void setAllCommentAttributes();

    /**
     * speaker to protocol
     * @author Jiayu Ma(implemented)
     */
    void addSpeakerProtocol();

    /**
     * add agend
     * @author Jiayu Ma(implemented)
     */
    void addSpeakerAgenda();

    /**
     * @return fraction
     * @author Jiayu Ma(implemented)
     */
    Fraction getFraction();

    /**
     * set fraction
     * @author Jiayu Ma(implemented)
     */
    void setFraction(Fraction fraction);

    /**
     * @return get party
     * @author Jiayu Ma(implemented)
     */
    Party getParty();

    /**
     * meiyou
     * @author Jiayu Ma(implemented)
     */
    void setParty(Party party);

    /**
     * update
     * @author Jiayu Ma(implemented)
     */
    void updateAllCommentIntoMap(Map<String, Comment> kommentarMap);

    /**
     * set comment
     * @author Jiayu Ma(implemented)
     */
    void setCommentFraction();

    /**
     * set party
     * @author Jiayu Ma(implemented)
     */
    void setCommentParty();

    /**
     * @return fraction name
     * @author Jiayu Ma(implemented)
     */
    String getFractionName();

    /**
     * set fraction
     * @author Jiayu Ma(implemented)
     */
    void setFractionName(String fractionName);

    /**
     * @return party name
     * @author Jiayu Ma(implemented)
     */
    String getPartyName();

    /**
     * set party name
     * @author Jiayu Ma(implemented)
     */
    void setPartyName(String partyName);



}
