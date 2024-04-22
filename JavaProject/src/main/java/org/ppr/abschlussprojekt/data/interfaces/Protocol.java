package org.ppr.abschlussprojekt.data.interfaces;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Jiayu Ma(implemented)
 */
public interface Protocol {
    /**
     * @return id
     * @author Jiayu Ma(implemented)
     */
    String getProtocolId();

    /**
     * @return index
     * @author Jiayu Ma(implemented)
     */
    int getProtocolIndex();

    /**
     * @return title
     * @author Jiayu Ma(implemented)
     */
    String getProtocolTitle();

    /**
     * @return date
     * @author Jiayu Ma(implemented)
     */
    Date getDate();

    /**
     * @return protocol start time
     * @author Jiayu Ma(implemented)
     */
    Timestamp getProtocolStart();

    /**
     * @return protocol end time
     * @author Jiayu Ma(implemented)
     */
    Timestamp getProtocolEnd();

    /**
     * @return wahlperiode
     * @author Jiayu Ma(implemented)
     */
    int getWahlperiode();

    /**
     * @return place of protocol
     * @author Jiayu Ma(implemented)
     */
    String getPlace();

    /**
     * @return agenda list
     * @author Jiayu Ma(implemented)
     */
    List<Agenda> getAgendaList();

    /**
     * add agenda
     * @author Jiayu Ma(implemented)
     */
    void addAgenda(Agenda agenda);

    /**
     * @return speaker map
     * @author Jiayu Ma(implemented)
     */
    Map<String, Speaker> getSpeakerMap();

    /**
     * set speaker map
     * @author Jiayu Ma(implemented)
     */
    void setSpeakerMap();
}
