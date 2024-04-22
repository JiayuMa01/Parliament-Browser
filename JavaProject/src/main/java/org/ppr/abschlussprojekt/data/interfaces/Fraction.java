package org.ppr.abschlussprojekt.data.interfaces;

import java.util.List;

/**
 * @author Jiayu Ma
 */
public interface Fraction {

    /**
     * @return fraction name
     * @author Jiayu Ma(implemented)
     */
    String getFractionName();

    /**
     * set fraction name
     * @author Jiayu Ma(implemented)
     */
    void setFractionName(String fractionName);

    /**
     * @return speaker list
     * @author Jiayu Ma(implemented)
     */
    List<Speaker> getSpeakerList();

    /**
     * set speaker list
     * @author Jiayu Ma(implemented)
     */
    void setSpeakerList(List<Speaker> speakerList);

    /**
     * @return protocol list
     * @author Jiayu Ma(implemented)
     */
    List<Protocol> getProtocolList();

    /**
     * set protocol list
     * @author Jiayu Ma(implemented)
     */
    void setProtocolList(List<Protocol> protocolList);

    /**
     * add speaker
     * @author Jiayu Ma(implemented)
     */
    void addSpeaker(Speaker speaker);

    /**
     * add protocol
     * @author Jiayu Ma(implemented)
     */
    void addProtocol(Protocol protocol);

    /**
     * @return all speeches of this fraction
     * @author Jiayu Ma(implemented)
     */
    List<Speech> getAllSpeechList();

    /**
     * @return comment list
     * @author Jiayu Ma(implemented)
     */
    List<Comment> getAllCommentList();


    /**
     * @return the number of comments
     * @author Jiayu Ma(implemented)
     */
    int getNumComment();
}
