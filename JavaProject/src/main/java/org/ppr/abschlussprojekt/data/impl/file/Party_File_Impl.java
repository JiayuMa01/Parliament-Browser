package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.data.interfaces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class Party_File_Impl
 * @author Jiayu Ma(implemented)
 */
public class Party_File_Impl implements Party {

    private String partyName = "";
    private List<Speaker> speakerList = new ArrayList<Speaker>();
    private List<Protocol> protocolList = new ArrayList<>();

    public Party_File_Impl() {
        super();
    }

    public Party_File_Impl(String partyName) {
        super();
        this.partyName = partyName;
    }

    /**
     * @return information of all
     * @author Jiayu Ma(implemented)
     */
    public String toString(){
        String value = "partyName:" + this.partyName + "  numSpeaker:" + this.speakerList.size();
        return value;
    }

    /**
     * @return name of party
     */
    @Override
    public String getPartyName() {
        return this.partyName;
    }

    /**
     * @param partyName
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    /**
     * @return list of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Speaker> getSpeakerList() {
        return this.speakerList;
    }

    /**
     * @param speakerList list of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeakerList(List<Speaker> speakerList) {
        this.speakerList = speakerList;
    }

    /**
     * @return list of protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Protocol> getProtocolList() {
        return this.protocolList;
    }

    /**
     * @param protocolList list of protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setProtocolList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }

    /**
     * @param speaker speaker this party
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addSpeaker(Speaker speaker) {
        if (!this.speakerList.contains(speaker)) {
            this.speakerList.add(speaker);
        }
    }

    /**
     * @param protocol protocol this party
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addProtocol(Protocol protocol) {
        if (!this.protocolList.contains(protocol)) {
            this.protocolList.add(protocol);
        }
    }

    /**
     * @return list of all speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Speech> getAllSpeechList() {
        List<Speech> speechList = new ArrayList<>();
        this.getSpeakerList().stream().map(Speaker::getSpeechList).forEach(speechList::addAll);
        return speechList;
    }

    /**
     * @return List of all comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Comment> getAllcommentList() {
        List<Speech> speechList = getAllSpeechList();
        List<Comment> commentList = new ArrayList<>();
        speechList.stream().map(Speech::getCommentList).forEach(commentList::addAll);
        return commentList;
    }

    /**
     * @return number of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public int getNumComment() {
        return this.getAllcommentList().size();
    }

    /**
     * @param partyName name of partei
     * @param partyMap map of partei
     * @return boolean, check result 
     */
    public static boolean checkPartyNameExist(String partyName, Map<String, Party> partyMap) {
        boolean check = false;
        for (String key : partyMap.keySet()) {
            if (partyName.substring(0, 2).equalsIgnoreCase(key.substring(0, 2))) {
                check = true;
                break;
            }
        }
        return check;
    }
}
