package org.ppr.abschlussprojekt.data.impl.file;

import org.ppr.abschlussprojekt.helper.XMLHelper.XMLReader;
import org.w3c.dom.Node;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SpeakerStammdaten {

    private int speakerId = 0;
    private String vorname = "";
    private String nachname = "";
    private String titel = "";
    private String fractionName = "fraktionslos";
    private String partyName = "parteilos";
    private Date birthDate = null;
    private String birthplace = "";
    private String gender = "";
    private String religion = "";
    private String profession = "";
    private String academicTitle = "";
    private String familyStatus = "";
    private Date deathDate = null;

    public SpeakerStammdaten(Node mdbNode) throws ParseException {
        Node speakerIdNode = XMLReader.getSingleNodesFromXML(mdbNode, "ID");
        Node nachnameNode = XMLReader.getSingleNodesFromXML(mdbNode, "NACHNAME");
        Node vornameNode = XMLReader.getSingleNodesFromXML(mdbNode, "VORNAME");
        Node titelNode = XMLReader.getSingleNodesFromXML(mdbNode, "ANREDE_TITEL");
        Node partyNameNode = XMLReader.getSingleNodesFromXML(mdbNode, "PARTEI_KURZ");
        Node fractionNameNode = XMLReader.getSingleNodesFromXML(mdbNode, "PARTEI_KURZ");
        Node birthdayNode = XMLReader.getSingleNodesFromXML(mdbNode, "GEBURTSDATUM");
        Node birthplaceNode = XMLReader.getSingleNodesFromXML(mdbNode, "GEBURTSORT");
        Node genderNode = XMLReader.getSingleNodesFromXML(mdbNode, "GESCHLECHT");
        Node religionNode = XMLReader.getSingleNodesFromXML(mdbNode, "RELIGION");
        Node professionNode = XMLReader.getSingleNodesFromXML(mdbNode, "BERUF");
        Node familyStatusNode = XMLReader.getSingleNodesFromXML(mdbNode, "FAMILIENSTAND");
        Node deathDateNode = XMLReader.getSingleNodesFromXML(mdbNode, "STERBEDATUM");
        Node academicTitleNode = XMLReader.getSingleNodesFromXML(mdbNode, "AKAD_TITEL");

        if (speakerIdNode != null) { this.speakerId = Integer.valueOf(speakerIdNode.getTextContent()); }
        if (vornameNode != null) { this.vorname = vornameNode.getTextContent(); }
        if (nachnameNode != null) { this.nachname = nachnameNode.getTextContent(); }
        if (titelNode != null) { this.titel = titelNode.getTextContent(); }
        if (fractionNameNode != null) { this.fractionName = fractionNameNode.getTextContent(); }
        if (partyNameNode != null) { this.partyName = partyNameNode.getTextContent(); }
        if (birthplaceNode != null) { this.birthplace = birthplaceNode.getTextContent(); }
        if (genderNode != null) { this.gender = genderNode.getTextContent(); }
        if (religionNode != null) { this.religion = religionNode.getTextContent(); }
        if (professionNode != null) { this.profession = professionNode.getTextContent(); }
        if (familyStatusNode != null) { this.familyStatus = familyStatusNode.getTextContent(); }
        if (academicTitleNode != null) { this.academicTitle = academicTitleNode.getTextContent(); }

        SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.YYYY");
        if (birthdayNode != null) {
            try {
                this.birthDate = new Date(sdfDate.parse(birthdayNode.getTextContent()).getTime());
            } catch (java.text.ParseException e) {
                this.birthDate = null;
            }
        }
        if (deathDateNode != null) {
            try {
                this.deathDate = new Date(sdfDate.parse(deathDateNode.getTextContent()).getTime());
            } catch (java.text.ParseException e) {
                this.deathDate = null;
            }
        }
    }

    /**
     * @return Id of speaker
     * @author Jiayu Ma(implemented)
     */
    public int getId(){
        return this.speakerId;
    }

    /**
     * @return vorname of speaker
     * @author Jiayu Ma(implemented)
     */
    public String getVorname() {
        return this.vorname;
    }

    /**
     * @return nachname of speaker
     * @author Jiayu Ma(implemented)
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * @return titel of speaker
     * @author Jiayu Ma(implemented)
     */
    public String getTitel() {
        return titel;
    }

    /**
     * @return name of fraction
     * @author Jiayu Ma(implemented)
     */
    public String getFractionName() {
        return fractionName;
    }

    /**
     * @return name of party
     * @author Jiayu Ma(implemented)
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * @return birthday date
     * @author Jiayu Ma(implemented)
     */
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * @return birthplace
     * @author Jiayu Ma(implemented)
     */
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * @return gender
     * @author Jiayu Ma(implemented)
     */
    public String getGender() {
        return gender;
    }

    /**
     * @return religion
     * @author Jiayu Ma(implemented)
     */
    public String getReligion() {
        return religion;
    }

    /**
     * @return profession
     * @author Jiayu Ma(implemented)
     */
    public String getProfession() {
        return profession;
    }

    /**
     * @return academic title
     * @author Jiayu Ma(implemented)
     */
    public String getAcademicTitle() {
        return academicTitle;
    }

    /**
     * @return family status
     * @author Jiayu Ma(implemented)
     */
    public String getFamilyStatus() {
        return familyStatus;
    }

    /**
     * @return death date
     * @author Jiayu Ma(implemented)
     */
    public Date getDeathDate() {
        return deathDate;
    }
}
