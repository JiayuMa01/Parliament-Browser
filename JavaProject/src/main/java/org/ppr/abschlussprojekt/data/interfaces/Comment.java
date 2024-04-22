package org.ppr.abschlussprojekt.data.interfaces;

/**
 * @author Jiayu Ma
 */
public interface Comment {

    /**
     * @return comment id
     * @author Jiayu Ma(implemented)
     */
    String getCommentSpeechId();

    /**
     * set id
     * @author Jiayu Ma(implemented)
     */
    void setCommentSpeechId(String commentSpeechId);

    /**
     * @return text
     * @author Jiayu Ma(implemented)
     */
    String getContent();

    /**
     * set text
     * @author Jiayu Ma(implemented)
     */
    void setContent(String content);

    /**
     * @return speech
     * @author Jiayu Ma(implemented)
     */
    Speech getSpeech();

    /**
     * set speech
     * @author Jiayu Ma(implemented)
     */
    void setSpeech(Speech speech);

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
     * @return speaker
     * @author Jiayu Ma(implemented)
     */
    Speaker getSpeaker();

    /**
     * set speaker
     * @author Jiayu Ma(implemented)
     */
    void setSpeaker(Speaker speaker);

    /**
     * set Up Attributes for comments with speech
     * @author Jiayu Ma(implemented)
     */
    void setUpAttributesWithSpeech(Speech speech);

    /**
     * @return index
     * @author Jiayu Ma(implemented)
     */
    int getCommentIndex();

    /**
     * set index
     * @author Jiayu Ma(implemented)
     */
    void setCommentIndex(int commentIndex);

    /**
     * @return party
     * @author Jiayu Ma(implemented)
     */
    Party getParty();

    /**
     * set party
     * @author Jiayu Ma(implemented)
     */
    void setParty(Party party);
}
