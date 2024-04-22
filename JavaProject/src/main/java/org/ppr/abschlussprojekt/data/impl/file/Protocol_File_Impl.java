package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.helper.XMLHelper.XMLReader;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * class Protocol_File_Impl
 * @author Jiayu Ma(implemented)
 */
public class Protocol_File_Impl implements Protocol, Serializable {
    private String protocolId = "";
    private int protocolIndex = 0;
    private String protocolTitle = "Plenarprotokoll";
    private Date date = null;
    private Timestamp protocolStart = null;
    private Timestamp protocolEnd = null;
    private int wahlperiode = 19;
    private String place = "";
    private List<Agenda> agendaList = new ArrayList<Agenda>();
    private Map<String, Speaker> speakerMap = new HashMap<>();

    public Protocol_File_Impl() {
        super();
    }

    public Protocol_File_Impl(Node rootNode) throws ParseException {
        super();
        setUpAttributes(rootNode);
    }

    /**
     * Set attributes for a protocol.
     * @param rootNode node of root
     * @throws ParseException
     * @author Jiayu Ma(implemented)
     */
    private void setUpAttributes(Node rootNode) throws ParseException {
        // Set attributes for a protocol.
        Node vorspannNode = XMLReader.getSingleNodesFromXML(rootNode, "vorspann");
        Node kopfDatenNode = XMLReader.getSingleNodesFromXML(vorspannNode, "kopfdaten");
        this.protocolId = XMLReader.getSingleNodesFromXML(kopfDatenNode, "sitzungsnr").getTextContent();
        this.protocolIndex = Integer.valueOf(this.protocolId);
        this.wahlperiode = Integer.valueOf(XMLReader.getSingleNodesFromXML(kopfDatenNode, "wahlperiode").getTextContent());
        this.protocolTitle = "Plenarprotokoll " + this.wahlperiode + "/" + this.protocolId;
        this.place = XMLReader.getSingleNodesFromXML(kopfDatenNode, "ort").getTextContent();

        Node verlaufNode = XMLReader.getSingleNodesFromXML(rootNode, "sitzungsverlauf");

        // Define Date and Time Format
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

        // extract date
        this.date = new Date(sdfDate.parse(((Element) XMLReader.getSingleNodesFromXML(kopfDatenNode, "datum")).getAttribute("date")).getTime());
        // extract start time on this day
        String startTimeStr = ((Element) XMLReader.getSingleNodesFromXML(verlaufNode, "sitzungsbeginn")).getAttribute("sitzung-start-uhrzeit");
        startTimeStr = startTimeStr.replaceAll("\\.", ":");
        startTimeStr = startTimeStr.replace(" Uhr", "");
        Time startTime = null;
        try {
            startTime = new Time(sdfTime.parse(startTimeStr).getTime());
        }
        catch (ParseException pe){
            startTimeStr = ((Element) XMLReader.getSingleNodesFromXML(verlaufNode, "sitzungsbeginn")).getAttribute("sitzung-start-uhrzeit");
            startTime = new Time(sdfTime.parse(startTimeStr.replaceAll("\\.", ":")).getTime());
        }
        this.protocolStart = new Timestamp(startTime.getTime());
        // extract end time on this day
        String endTimeStr = ((Element) XMLReader.getSingleNodesFromXML(verlaufNode, "sitzungsende")).getAttribute("sitzung-ende-uhrzeit");
        endTimeStr = endTimeStr.replaceAll("\\.", ":");
        endTimeStr = endTimeStr.replace(" Uhr", "");
        Time endTime = null;
        try {
            endTime = new Time(sdfTime.parse(endTimeStr).getTime());
        }
        catch (ParseException pe){
            try {
                endTimeStr = ((Element) XMLReader.getSingleNodesFromXML(verlaufNode, "sitzungsende")).getAttribute("sitzung-ende-uhrzeit");
                endTime = new Time(sdfTime.parse(endTimeStr.replaceAll("\\.", ":")).getTime());
            }
            catch (ParseException e){
                System.err.println(e.getMessage());
            }
        }
        if(endTime!=null) {
            this.protocolEnd = new Timestamp(endTime.getTime());
        }
        else{
            this.protocolEnd = protocolStart;
        }
        // extract start tim and end time
        startTime = new Time(this.getDate().getTime()+this.getProtocolStart().getTime());
        if(this.protocolEnd.before(this.protocolStart)){
            Calendar calender = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            calender.setTime(this.getDate());
            calender.add(Calendar.DAY_OF_YEAR, 1);
            endTime = new Time(calender.getTime().getTime()+this.protocolEnd.getTime());
        }
        else{
            endTime = new Time(this.getDate().getTime()+this.protocolEnd.getTime());
        }
        this.protocolStart = new Timestamp(startTime.getTime());
        this.protocolEnd = new Timestamp(endTime.getTime());



    }


    /**
     * @return information of all
     * @author Jiayu Ma(implemented)
     */
    public String toString(){
        String value = "protocolTitel:" + this.protocolTitle + "  wahlperiode:" + this.wahlperiode + " numSpeaker:" + this.speakerMap.size();
        return value;
    }

    /**
     * @return Id of this protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getProtocolId() {
        return this.protocolId;
    }

    /**
     * @return index of this protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public int getProtocolIndex() {
        return this.protocolIndex;
    }

    /**
     * @return titel of this protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getProtocolTitle() {
        return this.protocolTitle;
    }

    /**
     * @return date this protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Date getDate() {
        return this.date;
    }

    /**
     * @return place
     * @author Jiayu Ma(implemented)
     */
    @Override
    public String getPlace() {
        return this.place;
    }

    /**
     * @return start time
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Timestamp getProtocolStart() {
        return this.protocolStart;
    }

    /**
     * @return end time
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Timestamp getProtocolEnd() {
        return this.protocolEnd;
    }

    /**
     * @return wahl periode
     * @author Jiayu Ma(implemented)
     */
    @Override
    public int getWahlperiode() {
        return this.wahlperiode;
    }

    /**
     * @return list of agenda
     * @author Jiayu Ma(implemented)
     */
    @Override
    public List<Agenda> getAgendaList() {
        return this.agendaList;
    }

    /**
     * @param agenda agenda
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void addAgenda(Agenda agenda){
        this.agendaList.add(agenda);
    }

    /**
     * @return map of all speaker
     * @author Jiayu Ma(implemented)
     */
    @Override
    public Map<String, Speaker> getSpeakerMap() {
        return speakerMap;
    }

    /**
     * add all speaker of this protocol
     * @author Jiayu Ma(implemented)
     */
    @Override
    public void setSpeakerMap() {
        for (Agenda agenda : this.agendaList) {
            for (String speakerId : agenda.getSpeakerMap().keySet()) {
                if (!this.speakerMap.containsKey(speakerId)) {
                    this.speakerMap.put(speakerId, agenda.getSpeakerMap().get(speakerId));
                }
            }
        }
    }

}
