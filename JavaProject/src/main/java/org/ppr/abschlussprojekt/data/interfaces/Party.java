package org.ppr.abschlussprojekt.data.interfaces;

import java.util.List;

/**
 * @author Jiayu Ma
 */
public interface Party {

    /**
     * @return party name
     * @author Jiayu Ma(implemented)
     */
    String getPartyName();

    /**
     * @return speaker list
     * @author Jiayu Ma(implemented)
     */
    List<Speaker> getSpeakerList();

    /**
     * set party name
     * @author Jiayu Ma(implemented)
     */
    void setPartyName(String partyName);

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
     * @return speech list
     * @author Jiayu Ma(implemented)
     */
    List<Speech> getAllSpeechList();

    /**
     * @return comment list
     * @author Jiayu Ma(implemented)
     */
    List<Comment> getAllcommentList();

    /**
     * @return number of comments
     * @author Jiayu Ma(implemented)
     */
    int getNumComment();
}
