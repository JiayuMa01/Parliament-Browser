package org.ppr.abschlussprojekt.data.interfaces;

import java.util.List;
import java.util.Map;

/**
 * interface for Agenda
 * @author Jiayu Ma(implemented)
 */
public interface Agenda {

    /**
     * @return id
     * @author Jiayu Ma(implemented)
     */
    String getAgendaId();

    /**
     * set id
     * @author Jiayu Ma(implemented)
     */
    void setAgendaId(String agendaId);

    /**
     * @return index
     * @author Jiayu Ma(implemented)
     */
    String getAgendaIndex();

    /**
     * @return speech list
     * @author Jiayu Ma(implemented)
     */
    List<Speech> getSpeechList();

    /**
     * @return protocol
     * @author Jiayu Ma(implemented)
     */
    Protocol getProtocol();

    /**
     * @return speaker map
     * @author Jiayu Ma(implemented)
     */
    Map<String, Speaker> getSpeakerMap();

    /**
     * @return speech
     * @author Jiayu Ma(implemented)
     */
    void addSpeech(Speech speech);

    /**
     * @return comment map
     * @author Jiayu Ma(implemented)
     */
    Map<String, List<Comment>> getCommentMap();

    /**
     * add comment map to speech
     * @author Jiayu Ma(implemented)
     */
    void addCommentMap(Speech speech);

    /**
     * add speaker
     * @author Jiayu Ma(implemented)
     */
    void addSpeaker(Speaker speaker, Map<String, Speaker> allAbgeordneteMap);
}
