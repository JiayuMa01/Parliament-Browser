package org.ppr.abschlussprojekt.helper.XMLHelper;

import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.impl.file.*;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.ppr.abschlussprojekt.helper.ProgressBar;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Helper to read and analyse XML files
 * @author Jiayu Ma(implemented)
 */
public class XMLReader {

    private Map<String, Map> dataMap = new HashMap<>();
    private Map<String, Protocol> protocolMap = new HashMap<>();
    private Map<String, Agenda> agendaMap = new HashMap<>();
    private Map<String, Fraction> fractionMap = new HashMap<>();
    private Map<String, Party> partyMap = new HashMap<>();
    private Map<String, Speaker> speakerMap = new HashMap<>();
    private Map<String, Speech> speechMap = new HashMap<>();
    private Map<String, Comment> commentMap = new HashMap<>();
    private Map<Integer, SpeakerStammdaten> speakerStammdatenMap = new HashMap<>();
    private List<File> xmlFiles = null;
    private File stammdatenFile = null;
    private MongoDBHandler dbHandler = null;


    /**
     * @param xmlFiles List of xml files
     * @param stammdatenFile a xml file with stammdaten
     * @author Jiayu Ma(implemented)
     */
    public XMLReader(List<File> xmlFiles, File stammdatenFile, MongoDBHandler dbHandler){
        this.dbHandler = dbHandler;
        this.xmlFiles = xmlFiles.stream().filter(f -> checkIfXMLNotAnalysedUsingDB(f) == true).collect(Collectors.toList());
        this.stammdatenFile = stammdatenFile;
        this.dataMap.put("protocol", this.protocolMap);
        this.dataMap.put("agenda", this.agendaMap);
        this.dataMap.put("fraction", this.fractionMap);
        this.dataMap.put("party", this.partyMap);
        this.dataMap.put("speaker", this.speakerMap);
        this.dataMap.put("speech", this.speechMap);
        this.dataMap.put("comment", this.commentMap);


    }


    /**
     * analyse a XML file, save the data into classes
     * @param file a protocol xml file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     * @author Jiayu Ma(implemented)
     */
    private void analyseOneXML(File file) throws ParserConfigurationException, IOException, SAXException, ParseException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // parse XML file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(file);

        Node rootNode = document.getElementsByTagName("dbtplenarprotokoll").item(0);
        // Init new protocol
        Protocol protocol = new Protocol_File_Impl(rootNode);
        //System.out.println(protocol);

        // For each agenda of this protocol.
        List<Node> agendaNodeList = XMLReader.getNodesFromXML(rootNode, "tagesordnungspunkt");
        for (int agendaIndex=0; agendaIndex<agendaNodeList.size(); agendaIndex++) {
            Agenda agenda = new Agenda_File_Impl(agendaNodeList.get(agendaIndex), protocol);
            protocol.addAgenda(agenda);

            // For each speech of this Tagesordnungspunkt.
            List<Node> speechNodeList = XMLReader.getNodesFromXML(agendaNodeList.get(agendaIndex), "rede");
            for (int speechIndex=0; speechIndex<speechNodeList.size(); speechIndex++) {
                Speech speech = new Speech_File_Impl(speechNodeList.get(speechIndex), this.speakerMap, this.commentMap, this.speakerStammdatenMap);
                speech.setAllCommentAttributes();
                speech.updateAllCommentIntoMap(this.commentMap);
                speech.setAgenda(agenda);
                speech.setProtocol(protocol);
                speech.addSpeakerProtocol();
                speech.addSpeakerAgenda();
                agenda.addSpeech(speech);
                agenda.addSpeaker(speech.getSpeaker(), this.speakerMap);
                agenda.addCommentMap(speech);
                this.speechMap.put(speech.getSpeechId(), speech);

                // set the fraktion for this abgeordnete.
                String fractionName = speech.getSpeaker().getFractionName();
                fractionName = normalizeFractionName(fractionName, this.fractionMap);
                if (Fraction_File_Impl.checkFractionNameExist(fractionName, this.fractionMap)) {
                    speech.getSpeaker().setFraction(this.fractionMap.get(fractionName));
                    speech.getSpeaker().setFractionName(fractionName);
                    this.fractionMap.get(fractionName).addProtocol(protocol);
                    this.fractionMap.get(fractionName).addSpeaker(speech.getSpeaker());
                }
                else {
                    Fraction fraction = new Fraction_File_Impl(fractionName);
                    speech.getSpeaker().setFraction(fraction);
                    speech.getSpeaker().setFractionName(fractionName);
                    fraction.addSpeaker(speech.getSpeaker());
                    fraction.addProtocol(protocol);
                    this.fractionMap.put(fractionName, fraction);
                }
                // set the partei for this abgeordnete.
                String partyName = speech.getSpeaker().getPartyName();
                partyName = normalizePartyName(partyName, this.partyMap);
                if (Party_File_Impl.checkPartyNameExist(partyName, this.partyMap)) {
                    speech.getSpeaker().setParty(this.partyMap.get(partyName));
                    speech.getSpeaker().setPartyName(partyName);
                    this.partyMap.get(partyName).addProtocol(protocol);
                    this.partyMap.get(partyName).addSpeaker(speech.getSpeaker());
                }
                else {
                    Party party = new Party_File_Impl(partyName);
                    speech.getSpeaker().setParty(party);
                    speech.getSpeaker().setPartyName(partyName);
                    party.addSpeaker(speech.getSpeaker());
                    party.addProtocol(protocol);
                    this.partyMap.put(partyName, party);
                }

                // set the fraction and the party for this speech.
                speech.setFraction(speech.getSpeaker().getFraction());
                speech.setParty(speech.getSpeaker().getParty());
                speech.setFractionName(speech.getSpeaker().getFractionName());
                speech.setPartyName(speech.getSpeaker().getPartyName());
                speech.setCommentFraction();
                speech.setCommentParty();
            }

            this.agendaMap.put(agenda.getAgendaId(), agenda);

        }
        protocol.setSpeakerMap();
        this.protocolMap.put(protocol.getProtocolTitle(), protocol);
    }


    /**
     * Read all XML files with a path, analyse them one by one
     * @return A map saving all Speakers, Fractions, Partys, Agendas, Protocols, Attachments, Speeches, Comments
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     * @throws ParseException
     * @author Jiayu Ma(implemented)
     */
    public Map<String, Map> readXML() throws ParserConfigurationException, IOException, SAXException, ParseException {
        analyseStammdaten();
        System.out.println("Only new protocol files that not exist in DB will be analysed");
        int numAllFiles = this.xmlFiles.size();

        dbHandler.resetCountProgress("cntAnalysedXML");
        dbHandler.updateCountProgress("cntAnalysedXML", "total", numAllFiles);
        int cnt = 1;
        ProgressBar progressBar = new ProgressBar("Reading "+numAllFiles+" XML", numAllFiles);
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        //System.out.println("Reading " + numAllFiles + " XML. Please wait...");
        for (int i=0;i<numAllFiles;i++){
            analyseOneXML(this.xmlFiles.get(i));
            progressBar.show(cnt);
            cnt ++;

            dbHandler.updateCountProgress("cntAnalysedXML", "count", cnt-1);
        }
        System.out.println(cnt-1 + " XML new Files are analysed completed.");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        return this.dataMap;
    }

    /**
     * check in DB protocol collection, whether a protocol already analysed.
     * If already analysed, then this file will not be analysed again.
     * fileName is like 19001-data.xml, 19239-data.xml, 20025-data.xml,
     * 19001-data.xml means wahlperiode 19, protocol index 1
     * @param file a xml file
     * @return
     */
    private boolean checkIfXMLNotAnalysedUsingDB(File file) {
        String fileName = file.getName();
        String protocolId = fileName.split("-")[0];
        return !dbHandler.doesExist("protocol", protocolId);
    }


    /**
     * Read MDB_STAMMDATEN.XML, initialize all Abgeordneten at first.
     * @author Jiayu Ma, edited with the methode from Giuseppe Abrami.
     */
    private void analyseStammdaten() throws IOException, SAXException, ParserConfigurationException, ParseException {
        //this.abgeordneteStammdatenMap;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        // parse XML file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document stammdatenDocument = db.parse(this.stammdatenFile);

        NodeList mdbNodeList = stammdatenDocument.getElementsByTagName("MDB");
        for(int i=0; i<mdbNodeList.getLength(); i++){
            SpeakerStammdaten speakerStammdaten = new SpeakerStammdaten(mdbNodeList.item(i));
            this.speakerStammdatenMap.put(speakerStammdaten.getId(), speakerStammdaten);
        }
    }


    /**
     * Methode um Knoten einer XML-Struktur zu erhalten.
     * @author Giuseppe Abrami
     * @param pNode a node
     * @param sNodeName the node name
     * @return a list of nodes
     */
    public static List<Node> getNodesFromXML(Node pNode, String sNodeName){
        List<Node> rSet = new ArrayList<>(0);
        if(pNode.getNodeName().equals(sNodeName)) {
            rSet.add(pNode);
        }
        else{
            if (pNode.hasChildNodes()) {
                for (int a = 0; a < pNode.getChildNodes().getLength(); a++) {
                    // recursive call!
                    rSet.addAll(getNodesFromXML(pNode.getChildNodes().item(a), sNodeName));
                }
            } else {
                if (pNode.getNodeName().equals(sNodeName)) {
                    rSet.add(pNode);
                }
            }
        }
        return rSet;
    }


    /**
     * Get one node on a tag-name as part of a given node
     * @author Giuseppe Abrami
     * @param pNode
     * @param sNodeName
     * @return
     */
    public static Node getSingleNodesFromXML(Node pNode, String sNodeName){
        List<Node> nList = getNodesFromXML(pNode, sNodeName);
        if(nList.size()>0){
            return nList.stream().findFirst().get();
        }
        return null;
    }

    /**
     * Some fraction names might be incorrect in the XML file. Here is to uniform fraction names.
     * @param name
     * @param fraktionMap
     * @return  Name String after correction
     */
    private static String normalizeFractionName(String name, Map<String, Fraction> fraktionMap) {
        if (name.length()==0 || name.equalsIgnoreCase("fraktionslos")) { name = "fraktionslos"; }
        else {
            for (String key : fraktionMap.keySet()) {  //Es gibt Problem bei BÜNDNIS 90/DIE GRÜNEN
                if (name.substring(0, 2).equalsIgnoreCase(key.substring(0, 2))) {  //vergleiche die erste drei Zeichen
                    name = key;
                    break;
                }
            }
        }
        return name;
    }

    /**
     * Some partys names might be incorrect in the XML file. Here is to uniform party names.
     * @param name
     * @param parteiMap
     * @return Name String after correction
     */
    private static String normalizePartyName(String name, Map<String, Party> parteiMap) {
        if (name.length()==0 || name.equalsIgnoreCase("fraktionslos")) { name = "parteilos"; }
        else {
            for (String key : parteiMap.keySet()) {
                if (name.substring(0, 2).equalsIgnoreCase(key.substring(0, 2))) {
                    name = key;
                    break;
                }
            }
        }
        return name;
    }

    /**
     * @return the list of xml files that just analysed
     */
    public List<File> getXmlFiles() {
        return this.xmlFiles;
    }

}
