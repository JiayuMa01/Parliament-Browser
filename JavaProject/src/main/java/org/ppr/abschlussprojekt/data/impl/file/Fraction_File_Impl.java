package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.data.interfaces.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * class Fraction_File_Impl
 * @author Jiayu Ma(implemented)
 */
public class Fraction_File_Impl implements Fraction {

    private String fractionName = "";
    private List<Speaker> speakerList = new ArrayList<Speaker>();
    private List<Protocol> protocolList = new ArrayList<>();

    public Fraction_File_Impl() {
        super();
    }

    public Fraction_File_Impl(String fractionName) {
        super();
        this.fractionName = fractionName;
    }

    /**
     * @return information of all
     * @author Jiayu Ma(implemented)
     */
    public String toString(){
        String value = "fractionName:" + this.fractionName + "  numSpeaker:" + this.speakerList.size();
        return value;
    }

    /**
     * @return name of fraction
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getFractionName() {
        return this.fractionName;
    }

    /**
     * @param fractionName name of fraction
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setFractionName(String fractionName) {
        this.fractionName = fractionName;
    }

    /**
     * @return List of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Speaker> getSpeakerList() {
        return this.speakerList;
    }

    /**
     * @param speakerList List of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeakerList(List<Speaker> speakerList) {
        this.speakerList = speakerList;
    }

    /**
     * @return List of protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Protocol> getProtocolList() {
        return this.protocolList;
    }

    /**
     * @param protocolList List of Protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setProtocolList(List<Protocol> protocolList) {
        this.protocolList = protocolList;
    }

    /**
     * @param speaker speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addSpeaker(Speaker speaker) {
        if (!this.speakerList.contains(speaker)) {
            this.speakerList.add(speaker);
        }
    }

    /**
     * @param protocol protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addProtocol(Protocol protocol) {
        if (!this.protocolList.contains(protocol)) {
            this.protocolList.add(protocol);
        }
    }

    /**
     * @return List of all speech
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
    public List<Comment> getAllCommentList() {
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
        return this.getAllCommentList().size();
    }

    /**
     * check if fraction name exist
     * @param fractionName name of fraction
     * @param fractionMap map of fraction
     * @return true or false
     * @author Jiayu Ma(implemented)
     */
    public static boolean checkFractionNameExist(String fractionName, Map<String, Fraction> fractionMap) {
        boolean check = false;
        for (String key : fractionMap.keySet()) {
            if (fractionName.substring(0, 2).equalsIgnoreCase(key.substring(0, 2))) {
                check = true;
                break;
            }
        }
        return check;
    }
}
