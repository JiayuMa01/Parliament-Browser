package org.ppr.abschlussprojekt.data.impl.mongodb;

import com.google.gson.Gson;
import org.bson.Document;
import org.ppr.abschlussprojekt.data.impl.file.Speaker_File_Impl;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.ppr.abschlussprojekt.helper.LaTeXHelper.LatexHelper;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * class for uploading a speaker document
 * @author Jiayu Ma
 */
public class Speaker_MongoDB_Impl {

    private Speaker speaker;
    private String speakerId = "";
    private String vorname = "";
    private String nachname = "";
    private String title = "";
    private String fractionName = "";
    private String partyName = "";
    private long birthDate = -1;
    private String birthplace = "";
    private String gender = "";
    private String religion = "";
    private String profession = "";
    private String academicTitle = "";
    private String familyStatus = "";
    private long deathDate = -1;
    private List<String> pictureInfoList = new ArrayList<>();
    private List<String> speechIdList = new ArrayList<>();
    private List<String> agendaIdList = new ArrayList<>();
    private List<String> protocolIdList = new ArrayList<>();
    private Map<String, List<String>> commentIdMap = new HashMap<>();

    // get from downloaded documents
    private Document document;

    public Speaker_MongoDB_Impl(Document document) {
        this.document = document;
        this.speakerId = document.getString("_id");
        this.vorname = document.getString("vorname");
        this.nachname = document.getString("nachname");
        this.gender = document.getString("gender");
        this.title = document.getString("title");
        this.academicTitle = document.getString("academicTitle");
        this.birthplace = document.getString("birthplace");
        this.birthDate = document.getLong("birthDate");
        this.deathDate = document.getLong("deathDate");
        this.profession = document.getString("profession");
        this.religion = document.getString("religion");
        this.familyStatus = document.getString("familyStatus");
        this.fractionName = document.getString("fractionName");
        this.partyName = document.getString("partyName");
        this.speechIdList.addAll(document.getList("speechIdList", String.class));
        this.agendaIdList.addAll(document.getList("agendaIdList", String.class));
        this.protocolIdList.addAll(document.getList("protocolIdList", String.class));
        this.pictureInfoList.addAll(document.getList("pictureInfoList", String.class));
        this.commentIdMap.putAll((Map<? extends String, ? extends List<String>>) document.get((Object) "commentIdMap"));
    }



    public Speaker_MongoDB_Impl(Speaker speaker) {
        this.speaker = speaker;
        this.speakerId = this.speaker.getSpeakerId();
        this.vorname = this.speaker.getVorname();
        this.nachname = this.speaker.getNachname();
        this.title = this.speaker.getTitel();
        this.fractionName = this.speaker.getFractionName();
        this.partyName = this.speaker.getPartyName();
        this.birthDate = this.speaker.getBirthDate();
        this.deathDate = this.speaker.getDeathDate();
        this.birthplace = this.speaker.getBirthplace();
        this.gender = this.speaker.getGender();
        this.religion = this.speaker.getReligion();
        this.profession = this.speaker.getProfession();
        this.academicTitle = this.speaker.getAcademicTitle();
        this.familyStatus = this.speaker.getFamilyStatus();
        this.pictureInfoList = this.speaker.getPictureInfoList();
        this.speechIdList = this.speaker.getSpeechList().stream().map(Speech::getSpeechId).collect(Collectors.toList());
        this.agendaIdList = this.speaker.getAgendaList().stream().map(Agenda::getAgendaId).collect(Collectors.toList());
        this.protocolIdList = this.speaker.getProtocolList().stream().map(Protocol::getProtocolTitle).collect(Collectors.toList());
        this.commentIdMap = setKommentarMap();
    }


    /**
     * Set all Kommentare for this Abgeordnete as a Map. Map<RedeId, List<KommentarId>>
     * @author Jiayu Ma
     */
    private Map<String, List<String>> setKommentarMap() {
        Map<String, List<String>> kommentarMap = new HashMap<>();
        for (Speech speech : this.speaker.getSpeechList()) {
            List<String> kommentarIdList = speech.getCommentList().stream().map(Comment::getCommentSpeechId).collect(Collectors.toList());
            kommentarMap.put(speech.getSpeechId(), kommentarIdList);
        }
        return kommentarMap;
    }

    /**
     * Put the attributes in the document and return
     * This document can be uploaded to DB, it can also be used to build NLP_Impl objects
     * @author Jiayu Ma
     */
    public Document getMongoDocument() {
        Document mongoDocument = new Document();
        mongoDocument.put("_id", this.speakerId);
        mongoDocument.put("vorname", this.vorname);
        mongoDocument.put("nachname", this.nachname);
        mongoDocument.put("fullName", this.vorname + this.nachname);
        mongoDocument.put("title", this.title);
        mongoDocument.put("academicTitle", this.academicTitle);
        mongoDocument.put("fractionName", this.fractionName);
        mongoDocument.put("partyName", this.partyName);
        mongoDocument.put("birthDate", this.birthDate);
        mongoDocument.put("deathDate", this.deathDate);
        mongoDocument.put("birthplace", this.birthplace);
        mongoDocument.put("gender", this.gender);
        mongoDocument.put("religion", this.religion);
        mongoDocument.put("profession", this.profession);
        mongoDocument.put("familyStatus", this.familyStatus);
        mongoDocument.put("pictureInfoList", this.pictureInfoList);
        mongoDocument.put("speechIdList", this.speechIdList);
        mongoDocument.put("agendaIdList", this.agendaIdList);
        mongoDocument.put("protocolIdList", this.protocolIdList);
        mongoDocument.put("commentIdMap", this.commentIdMap);
        return mongoDocument;
    }

    /**
     * speaker id
     * @return speaker id
     * @author Jiayu Ma(implemented)
     */
    public String getSpeakerId() {
        return speakerId;
    }

    /**
     * vorname
     * @return vorname
     * @author Jiayu Ma(implemented)
     */
    public String getVorname() {
        return vorname;
    }

    /**
     * nachname
     * @return nachname
     * @author Jiayu Ma(implemented)
     */
    public String getNachname() {
        return nachname;
    }

    /**
     * title
     * @return title
     * @author Jiayu Ma(implemented)
     */
    public String getTitle() {
        return title;
    }

    /**
     * fraction name
     * @return fraction name
     * @author Jiayu Ma(implemented)
     */
    public String getFractionName() {
        return fractionName;
    }

    /**
     * party name
     * @return party name
     * @author Jiayu Ma(implemented)
     */
    public String getPartyName() {
        return partyName;
    }

    /**
     * birthdate
     * @return birthdate
     * @author Jiayu Ma(implemented)
     */
    public long getBirthDate() {
        return birthDate;
    }

    /**
     * birthplace
     * @return birthplace
     * @author Jiayu Ma(implemented)
     */
    public String getBirthplace() {
        return birthplace;
    }

    /**
     * gender
     * @return gender
     * @author Jiayu Ma(implemented)
     */
    public String getGender() {
        return gender;
    }

    /**
     * religion
     * @return religion
     * @author Jiayu Ma(implemented)
     */
    public String getReligion() {
        return religion;
    }

    /**
     * profession
     * @return profession
     * @author Jiayu Ma(implemented)
     */
    public String getProfession() {
        return profession;
    }

    /**
     * academic title
     * @return academic title
     * @author Jiayu Ma(implemented)
     */
    public String getAcademicTitle() {
        return academicTitle;
    }

    /**
     * family status
     * @return family status
     * @author Jiayu Ma(implemented)
     */
    public String getFamilyStatus() {
        return familyStatus;
    }

    /**
     * death date
     * @return death date
     * @author Jiayu Ma(implemented)
     */
    public long getDeathDate() {
        return deathDate;
    }

    /**
     * picture information list
     * @return picture information list
     * @author Jiayu Ma(implemented)
     */
    public List<String> getPictureInfoList() {
        return pictureInfoList;
    }

    public List<String> getSpeechIdList() {
        return speechIdList;
    }

    /**
     * agenda id list
     * @return agenda id list
     * @author Jiayu Ma(implemented)
     */
    public List<String> getAgendaIdList() {
        return agendaIdList;
    }

    /**
     * protocol id list
     * @return protocol id list
     * @author Jiayu Ma(implemented)
     */
    public List<String> getProtocolIdList() {
        return protocolIdList;
    }

    /**
     * comment id map
     * @return comment id map {speechid:[commentId, commentId]}
     * @author Jiayu Ma(implemented)
     */
    public Map<String, List<String>> getCommentIdMap() {
        return commentIdMap;
    }

    /**
     * get document downloaded from DB
     * @return the document downloaded from DB
     * @author Jiayu Ma(implemented)
     */
    public Document getDownloadedDocument() {
        return document;
    }

    /**
     * Returns the tex code for a speaker given the template
     *
     * @author Matthias Beck
     */
    public String toTex() {
        LatexHelper latexHelper = new LatexHelper();

        // gets the template as string
        String speakerString = latexHelper.getTemplateAsString("speaker_template.txt");

        // replaces the placeholders in the template with data
        if (speakerString.contains("%%REDNERNAME%%")) {
            vorname = vorname.replace("+", " ");
            speakerString = speakerString.replace("%%REDNERNAME%%", vorname + " " + nachname);
        }
        if (speakerString.contains("%%PARTEI%%")) {
            speakerString = speakerString.replace("%%PARTEI%%", partyName);
        }
        if (speakerString.contains("%%FRAKTION%%")) {
            speakerString = speakerString.replace("%%FRAKTION%%", fractionName);
        }

        // adds the picture of the speaker
        if (speakerString.contains("%%BILD%%")) {
            if (!pictureInfoList.get(0).isEmpty()) {
                try {
                    // downloads the pictures from the given url and saves them locally
                    URL url = new URL(pictureInfoList.get(0));
                    InputStream in = url.openStream();
                    String savingPath = System.getProperty("user.dir") + "/JavaProject/latex/export/images/"+vorname+nachname+".jpg";
                    Files.copy(in, Paths.get(savingPath), StandardCopyOption.REPLACE_EXISTING);
                    in.close();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                speakerString = speakerString.replace("%%BILD%%", "\\includegraphics[height=50px]{images/"+vorname+nachname+".jpg}");
            }
            else {
                speakerString = speakerString.replace("%%BILD%%", "Kein Bild vorhanden.");
            }
        }

        return speakerString;
    }
}
