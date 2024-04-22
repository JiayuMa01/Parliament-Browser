package org.ppr.abschlussprojekt.data.interfaces;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jiayu Ma
 */
public interface Speaker {
    /**
     * @return id
     * @author Jiayu Ma(implemented)
     */
    String getSpeakerId();

    /**
     * @return vorname
     * @author Jiayu Ma(implemented)
     */
    String getVorname();

    /**
     * @return nachname
     * @author Jiayu Ma(implemented)
     */
    String getNachname();

    /**
     * @return titel
     * @author Jiayu Ma(implemented)
     */
    String getTitel();

    /**
     * @return birth date
     * @author Jiayu Ma(implemented)
     */
    long getBirthDate();

    /**
     * @return nirth place
     * @author Jiayu Ma(implemented)
     */
    String getBirthplace();

    /**
     * @return gender
     * @author Jiayu Ma(implemented)
     */
    String getGender();

    /**
     * @return religion
     * @author Jiayu Ma(implemented)
     */
    String getReligion();

    /**
     * @return profession
     * @author Jiayu Ma(implemented)
     */
    String getProfession();

    /**
     * @return titel
     * @author Jiayu Ma(implemented)
     */
    String getAcademicTitle();

    /**
     * @return family status
     * @author Jiayu Ma(implemented)
     */
    String getFamilyStatus();

    /**
     * @return death date
     * @author Jiayu Ma(implemented)
     */
    long getDeathDate();

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
     * @return party
     * @author Jiayu Ma(implemented)
     */
    Party getParty();

    /**
     * set party
     * @author Jiayu Ma(implemented)
     */
    void setParty(Party party);

    /**
     * @return speech map
     * @author Jiayu Ma(implemented)
     */
    Map<String, Speech> getSpeechMap();

    /**
     * @return comment list
     * @author Jiayu Ma(implemented)
     */
    List<Comment> getCommentList();

    /**
     * @return roll
     * @author Jiayu Ma(implemented)
     */
    String getRoll();

    /**
     * @return protocol list
     * @author Jiayu Ma(implemented)
     */
    List<Protocol> getProtocolList();

    /**
     * @return agenda list
     * @author Jiayu Ma(implemented)
     */
    List<Agenda> getAgendaList();

    /**
     * add speech
     * @author Jiayu Ma(implemented)
     */
    void addSpeech(Speech speech);

    /**
     * add agenda
     * @author Jiayu Ma(implemented)
     */
    void addAgenda(Agenda agenda);

    /**
     * add protocol
     * @author Jiayu Ma(implemented)
     */
    void addProtocol(Protocol protocol);

    /**
     * @return fraction name
     * @author Jiayu Ma(implemented)
     */
    String getFractionName();

    /**
     * @return party name
     * @author Jiayu Ma(implemented)
     */
    String getPartyName();

    /**
     * @return party name
     * @author Jiayu Ma(implemented)
     */
    void setPartyName(String partyName);

    /**
     * set fraction
     * @author Jiayu Ma(implemented)
     */
    void setFractionName(String fractionName);

    /**
     * @return speech list
     * @author Jiayu Ma(implemented)
     */
    List<Speech> getSpeechList();

    /**
     * @return picture info list
     * @author Jiayu Ma(implemented)
     */
    List<String> getPictureInfoList();
}
