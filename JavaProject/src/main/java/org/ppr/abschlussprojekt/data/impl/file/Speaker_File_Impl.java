package org.ppr.abschlussprojekt.data.impl.file;

import org.jsoup.Jsoup;
import org.ppr.abschlussprojekt.helper.XMLHelper.XMLReader;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.w3c.dom.Node;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class Speaker_File_Impl
 * @author Jiayu Ma(implemented)
 */
public class Speaker_File_Impl implements Speaker, Serializable {
    private String speakerId = "";
    private String vorname = "";
    private String nachname = "";
    private String titel = "";
    private String roll = "";
    private String fractionName = "fraktionslos";
    private transient Fraction fraction = null;
    private String partyName = "parteilos";
    private transient Party party = null;
    private long birthDate = -1;
    private long deathDate = -1;
    private String birthplace = "";
    private String gender = "";
    private String religion = "";
    private String profession = "";
    private String academicTitle = "";
    private String familyStatus = "";
    private List<String> pictureInfoList = new ArrayList<>();
    private transient Map<String, Speech> speechMap = new HashMap<>();
    private transient List<Comment> commentList = new ArrayList<Comment>();
    private transient List<Protocol> protocolList = new ArrayList<>();
    private transient List<Agenda> agendaList = new ArrayList<Agenda>();

    public Speaker_File_Impl() {
        super();
    }

    public Speaker_File_Impl(Node speechNode, Map<Integer, SpeakerStammdaten> speakerStammdatenMap) {
        super();
        setUpAttributes(speechNode, speakerStammdatenMap);
        this.pictureInfoList = setPictureInfoList();
    }

    /**
     * @param speechNode Node of Rede
     * @param speakerStammdatenMap Map of SpeakerStammendaten
     * @author Jiayu Ma(implemented)
     */
    private void setUpAttributes(Node speechNode, Map<Integer, SpeakerStammdaten> speakerStammdatenMap) {
        Node speakerNode = XMLReader.getSingleNodesFromXML(speechNode, "redner");
        this.speakerId = speakerNode.getAttributes().getNamedItem("id").getTextContent();
        Node rollNode = XMLReader.getSingleNodesFromXML(speakerNode, "rolle_lang");
        if (rollNode != null) { this.roll = rollNode.getTextContent(); }

        List<SpeakerStammdaten> checkSpeaker = speakerStammdatenMap.values().stream().filter(speaker -> String.valueOf(speaker.getId()).equalsIgnoreCase(this.speakerId)).collect(Collectors.toList());
        if (checkSpeaker.size()==1) {
            SpeakerStammdaten speakerStammdaten = speakerStammdatenMap.get(Integer.valueOf(this.speakerId));
            this.vorname = speakerStammdaten.getVorname();
            this.nachname = speakerStammdaten.getNachname();
            this.titel = speakerStammdaten.getTitel();
            this.fractionName = speakerStammdaten.getFractionName();
            this.partyName = speakerStammdaten.getPartyName();
            if(speakerStammdaten.getBirthDate()!=null) {
                this.birthDate = speakerStammdaten.getBirthDate().getTime();
            }
            if(speakerStammdaten.getDeathDate()!=null) {
                this.deathDate = speakerStammdaten.getDeathDate().getTime();
            }
            this.birthplace = speakerStammdaten.getBirthplace();
            this.gender = speakerStammdaten.getGender();
            this.religion = speakerStammdaten.getReligion();
            this.profession = speakerStammdaten.getProfession();
            this.academicTitle = speakerStammdaten.getAcademicTitle();
            this.familyStatus = speakerStammdaten.getFamilyStatus();
        }
        else {
            Node vornameNode = XMLReader.getSingleNodesFromXML(speakerNode, "vorname");
            Node nachnameNode = XMLReader.getSingleNodesFromXML(speakerNode, "nachname");
            Node titelNode = XMLReader.getSingleNodesFromXML(speakerNode, "titel");
            Node fractionNameNode = XMLReader.getSingleNodesFromXML(speakerNode, "fraktion");
            Node partyNameNode = XMLReader.getSingleNodesFromXML(speakerNode, "fraktion");

            if (vornameNode != null) { this.vorname = vornameNode.getTextContent(); }
            if (nachnameNode != null) { this.nachname = nachnameNode.getTextContent(); }
            if (titelNode != null) { this.titel = titelNode.getTextContent(); }
            if (fractionNameNode != null) { this.fractionName = fractionNameNode.getTextContent(); }
            if (partyNameNode != null) { this.partyName = partyNameNode.getTextContent(); }
        }


    }

    /**
     * @return picture url as well as details as a list for this speaker
     * @author JiayuMa(implemented)
     */
    private ArrayList<String> setPictureInfoList() {
        // pictureInfoList saves the url and details
        ArrayList<String> pictureInfoList = new ArrayList<>();

        // Sleep for 250ms, so that we won't get baned from the bundestag server
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Parser Bilddatenbank website
        try {
            //get Bilddatenbank-URL
            this.vorname = this.vorname.replaceAll(" ", "+");
            this.nachname = this.nachname.replaceAll(" ", "+");
            String queryName = this.nachname + "%2C+" + this.vorname;
            String pictureHTMLUrl = "https://bilddatenbank.bundestag.de/search/picture-result?filterQuery%5Bname%5D%5B%5D=" + queryName + "&filterQuery%5Bereignis%5D%5B%5D=Portr%C3%A4t%2FPortrait&sortVal=3";
            //System.out.println(pictureHTMLUrl);
            org.jsoup.nodes.Document pictureHTML = Jsoup.connect(pictureHTMLUrl).get();

            // find the first picture in database website and go into its HTML
            org.jsoup.nodes.Element firstPicture = pictureHTML.getElementsByAttributeValue("data-fancybox", "group").first();
            org.jsoup.nodes.Document firstPictureHTML = Jsoup.connect(firstPicture.attr("abs:href")).get();
            org.jsoup.nodes.Element picture = firstPictureHTML.getElementsByTag("figure").first();

            //Get Picture URL
            String pictureURLStr = picture.child(0).attr("abs:src");
            pictureInfoList.add(pictureURLStr);

//            URL pictureURL = new URL(pictureURLStr);
//            InputStream in = pictureURL.openStream();
//            byte[] bytes = in.readAllBytes();
//            String encodedImage = Base64.getEncoder().encodeToString(bytes);
//            pictureInfoList.add(pictureURL);

            //Get details of the Picture
            String[] pictureDetail = picture.child(1).child(0).child(0).html().replaceAll("\\s*<h6>.*</h6>\\s*","").replaceAll("\\s*<b>.*</b>\\s*","").split("\\s*\n*\\s*<br>\\s*");
            //System.out.println(Arrays.toString(pictureDetail));

            // add details about this picture
            pictureInfoList.addAll(Arrays.asList(pictureDetail));

        } catch (Exception e) {

        }
        return pictureInfoList;
    }

    /**
     * @return value of all information
     * @author Jiayu Ma(implemented)
     */
    public String toString(){
        String value = "speakerId:" + this.speakerId + "  name:" + this.vorname + " " +this.nachname + "  titel:" + this.titel
                + "  fractionName:" + this.fraction.getFractionName() + "  partyName:" + this.party.getPartyName()
                + "  numSpeech:" + this.speechMap.size() + "  numProtocol:" + this.protocolList.size() + "  numAgenda:" + this.agendaList.size();
        return value;
    }

    /**
     * @return speakerID
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getSpeakerId() {
        return this.speakerId;
    }

    /**
     * @return vorname
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getVorname() {
        return this.vorname;
    }

    /**
     * @return nachname
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getNachname() {
        return this.nachname;
    }

    /**
     * @return titel
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getTitel() {
        return this.titel;
    }

    /**
     * @return roll
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getRoll() {
        return roll;
    }

    /**
     * @return birthdate
     * @author Jiayu Ma(implemented)
     */
    @Override
    public long getBirthDate() {
        return birthDate;
    }

    /**
     * @return birthplace
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * @return gender
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getGender() {
        return gender;
    }

    /**
     * @return religion
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getReligion() {
        return religion;
    }

    /**
     * @return profession
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getProfession() {
        return profession;
    }

    /**
     * @return academic title
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getAcademicTitle() {
        return academicTitle;
    }

    /**
     * @return family status
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getFamilyStatus() {
        return familyStatus;
    }

    /**
     * @return death date
     * @author Jiayu Ma(implemented)
     */
    @Override
    public long getDeathDate() {
        return deathDate;
    }

    /**
     * @return fraction of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Fraction getFraction() {
        return this.fraction;
    }

    /**
     * @param fraction fraction of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setFraction(Fraction fraction) {
        if (this.fraction != null) {
            if (!this.fraction.equals(fraction)) {
                this.fraction = fraction;
            }
        }
        else {
            this.fraction = fraction;
        }
    }

    /**
     * @return fractionName
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getFractionName() {
        return fractionName;
    }

    /**
     * @param fractionName set the fraktionName
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setFractionName(String fractionName) {
        this.fractionName = fractionName;
    }

    /**
     * @return partyName
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getPartyName() {
        return partyName;
    }

    /**
     * @param partyName set the partyName
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    /**
     * @return party of speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Party getParty() {
        return this.party;
    }

    /**
     * @param party partyName
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setParty(Party party) {
        if (this.party != null) {
            if (!this.party.equals(party)) {
                this.party = party;
            }
        }
        else {
            this.party = party;
        }
    }

    /**
     * @return Map of speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Map<String, Speech> getSpeechMap() {
        return this.speechMap;
    }

    /**
     * @return List of comment
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Comment> getCommentList() {
        return this.commentList;
    }

    /**
     * @return List of Protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Protocol> getProtocolList() {
        return this.protocolList;
    }

    /**
     * @return List of agenda
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Agenda> getAgendaList() {
        return this.agendaList;
    }

    /**
     * @param speech speech
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addSpeech(Speech speech) {
        this.speechMap.put(speech.getSpeechId(), speech);
    }

    /**
     * @param protocol protocol
     * @author Jiayu Ma(implement)
     */
    @Override
    public void addProtocol(Protocol protocol) {
        this.protocolList.add(protocol);
    }

    /**
     * @param agenda agenda
     * @author Jiayu Ma(implement)
     */
    @Override
    public void addAgenda(Agenda agenda) {
        this.agendaList.add(agenda);
    }

    /**
     * @return List of Speech
     * @author Jiayu Ma(implement)
     */
    @Override
    public List<Speech> getSpeechList() {
        return new ArrayList<>(this.speechMap.values());
    }

    /**
     * @return List of Speech
     * @author Jiayu Ma(implement)
     */
    @Override
    public List<String> getPictureInfoList() {
        return pictureInfoList;
    }
}
