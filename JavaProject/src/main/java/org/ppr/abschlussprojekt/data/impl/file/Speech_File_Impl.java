package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.helper.XMLHelper.XMLReader;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Jiayu Ma
 */
public class Speech_File_Impl implements Speech, Serializable {
    private String speechId = "";
    private transient Protocol protocol = null;
    private transient Agenda agenda = null;
    private String text = "";
    private int speechLength = 0;
    private transient Fraction fraction = null;
    private transient Party party = null;
    private String fractionName = "";
    private String partyName = "";
    private transient Speaker speaker = null;
    private transient List<Comment> commentList = new ArrayList<Comment>();


    public Speech_File_Impl(Node speechNode, Map<String, Speaker> speakerMap, Map<String, Comment> commentMap, Map<Integer, SpeakerStammdaten> speakerStammdatenMap){
        super();
        setUpAttributes(speechNode, speakerMap, commentMap, speakerStammdatenMap);
    }


    /**
     * @param speechNode node of speech
     * @param speakerMap map of speaker
     * @param commentMap map of comment
     * @param speakerStammdatenMap map of speaker stammendaten
     */
    private void setUpAttributes(Node speechNode, Map<String, Speaker> speakerMap, Map<String, Comment> commentMap, Map<Integer, SpeakerStammdaten> speakerStammdatenMap) {
        this.speechId = speechNode.getAttributes().getNamedItem("id").getTextContent();

        NodeList speechChildrenNodeList = speechNode.getChildNodes();
        // set the redner
        for (int i=0; i<speechChildrenNodeList.getLength(); i++) {
            Node speechChildNode = speechChildrenNodeList.item(i);
            if (speechChildNode.getNodeName().equals("p")) {
                String nodeKlasseName = "";
                if (speechChildNode.hasAttributes()) {
                    nodeKlasseName = speechChildNode.getAttributes().getNamedItem("klasse").getTextContent();
                }
                if (nodeKlasseName.equals("redner")) {
                    String speakerId = XMLReader.getSingleNodesFromXML(speechNode, "redner").getAttributes().getNamedItem("id").getTextContent();
                    if (speakerMap.containsKey(speakerId)) {
                        speakerMap.get(speakerId).addSpeech(this);
                        this.speaker = speakerMap.get(speakerId);
                        //Node speakerNode = XMLReader.getSingleNodesFromXML(speechNode, "speaker");
                    }
                    else {
                        Speaker speaker = new Speaker_File_Impl(speechNode, speakerStammdatenMap);
                        this.speaker = speaker;
                        this.speaker.addSpeech(this);
                        speakerMap.put(speaker.getSpeakerId(), this.speaker);
                    }
                    //this.fraktion = this.abgeordnete.getFranktion();
                    //this.partei = this.abgeordnete.getPartei();
                    break;
                }
            }
        }

        // Collect the text("kommentar":comment; "name":protocolLeiter;"p": speech content)
        int commentIndex = 1;
        for (int i=0; i<speechChildrenNodeList.getLength(); i++) {
            Node speechChildNode = speechChildrenNodeList.item(i);
            switch (speechChildNode.getNodeName()) {
                case "kommentar":
                    String content = speechChildNode.getTextContent();
                    Comment comment = new Comment_File_Impl(content, commentIndex, this.speechId);
                    this.commentList.add(comment);
                    commentMap.put(comment.getCommentSpeechId(), comment);
                    commentIndex++;
                    break;
                case "p":
                    String nodeKlasseName = "";
                    if (speechChildNode.hasAttributes()) {
                        nodeKlasseName = speechChildNode.getAttributes().getNamedItem("klasse").getTextContent();
                    }
                    switch (nodeKlasseName) {
                        case "redner":
                            break;
                        default:
                            String text = speechChildNode.getTextContent();  //add content
                            this.text += text + " ";
                    }
                    break;

            }
        }
        this.speechLength = this.text.length();
    }

    /**
     * @return information of all
     * @author Jiayu Ma(implemented)
     */
    public String toString(){
        String value = "speechId:" + this.speechId + "  speechLength:" + this.speechLength + "  speakerId:" + this.speaker.getSpeakerId()
                + "  speakerName:" + this.speaker.getVorname() + " " +this.speaker.getNachname()
                + "  fractionName:" + this.fraction.getFractionName() + "  partyName:" + this.party.getPartyName()
                + "  numComment:" + this.commentList.size();
        return value;
    }

    /**
     * @return name of fraction
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getFractionName() {
        return fractionName;
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
     * @return name of party
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getPartyName() {
        return partyName;
    }

    /**
     * @param partyName name of party
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    /**
     * @return id of speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getSpeechId() {
        return this.speechId;
    }

    /**
     * @param speechId id of speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeechId(String speechId) {
        this.speechId = speechId;
    }

    /**
     * @return text
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getText() {
        return this.text;
    }

    /**
     * @param text text
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return speaker this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Speaker getSpeaker() {
        return this.speaker;
    }

    /**
     * @param speaker speaker this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

    /**
     * @return fraction this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Fraction getFraction() {
        return fraction;
    }

    /**
     * @param fraction fraction this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setFraction(Fraction fraction) {
        this.fraction = fraction;
    }

    /**
     * @return party this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Party getParty() {
        return party;
    }

    /**
     * @param party party this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setParty(Party party) {
        this.party = party;
    }

    /**
     * @return list of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Comment> getCommentList() {
        return this.commentList;
    }

    /**
     * @return protocol this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Protocol getProtocol() {
        return this.protocol;
    }

    /**
     * @param protocol protocol this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    /**
     * @return agenda this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Agenda getAgenda() {
        return this.agenda;
    }

    /**
     * @param agenda agenda this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    /**
     * set speech und speaker for all comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setAllCommentAttributes() {
        for (Comment k : this.commentList) {
            k.setUpAttributesWithSpeech(this);
        }
    }

    /**
     * add the protocol for the speaker of this speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addSpeakerProtocol() {
        // add the protocol for the speaker of this speech.
        if (!this.speaker.getProtocolList().contains(this.protocol)) {
            this.speaker.addProtocol(this.protocol);
        }
    }

    /**
     * add the agenda for the speaker of this speech.
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addSpeakerAgenda() {
        // add the agenda for the speaker of this speech.
        if (!this.speaker.getAgendaList().contains(this.agenda)) {
            this.speaker.addAgenda(this.agenda);
        }
    }

    /**
     * @param commentMap map of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void updateAllCommentIntoMap(Map<String, Comment> commentMap) {
        for (Comment comment : this.commentList) {
            commentMap.put(comment.getCommentSpeechId(), comment);
        }
    }

    /**
     * set fraction for comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setCommentFraction() {
        for (Comment comment : this.commentList) {
            comment.setFraction(this.fraction);
        }
    }

    /**
     * set party for comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setCommentParty() {
        for (Comment comment : this.commentList) {
            comment.setParty(this.party);
        }
    }

}
