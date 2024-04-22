package org.ppr.abschlussprojekt.data.database;

import org.bson.Document;
import org.ppr.abschlussprojekt.helper.ProgressBar;
import org.ppr.abschlussprojekt.data.impl.mongodb.*;
import org.ppr.abschlussprojekt.data.interfaces.*;
import org.ppr.abschlussprojekt.helper.XMLHelper.XMLFileReader;
import org.ppr.abschlussprojekt.helper.XMLHelper.XMLReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Insert the Mongo_Impl objects to Mongo database.
 * Fetch from database
 * @author Jiayu Ma(implemented)
 */
public class MongoDBHelper {

    /**
     * Insert all Abgeordnete documents to Abgeordnete Collection
     * @param speakerMap A map with all Abgeordnete_File_Impl objects
     * @param dbHandler MongoDBConnectionHandler, which can connect to the MongoDB
     * @author Jiayu Ma(implemented)
     */
    public static void insertAllSpeakerDocument(Map<String, Speaker> speakerMap, MongoDBHandler dbHandler, String collectionName) {
        dbHandler.updateCountProgress("cntUploadedSpeaker", "total", speakerMap.size());

        ProgressBar progressBar = new ProgressBar("Uploading all speakers", speakerMap.size());
        int cnt = 1;
        for (Speaker speakerFile : speakerMap.values()) {
            Speaker_MongoDB_Impl speakerMongoDB = new Speaker_MongoDB_Impl(speakerFile);
            Document document = speakerMongoDB.getMongoDocument();
            dbHandler.insertOneNotExists(document, collectionName);
            progressBar.show(cnt);
            cnt++;
            if (cnt%10==0 || cnt==speakerMap.size()+1) {
                dbHandler.updateCountProgress("cntUploadedSpeaker", "count", cnt-1);
            }
        }
    }

    /**
     * insert all Rede documents to Rede Collection
     * @param speechMap A map with all Rede_File_Impl objects
     * @param dbHandler MongoDBConnectionHandler, which can connect to the MongoDB
     * @author Jiayu Ma(implemented)
     */
    public static void insertAllSpeechDocument(Map<String, Speech> speechMap, MongoDBHandler dbHandler, String collectionName) {
        dbHandler.updateCountProgress("cntUploadedSpeech", "total", speechMap.size());

        ProgressBar progressBar = new ProgressBar("Uploading all speeches", speechMap.size());
        int cnt = 1;
        for (Speech speechFile : speechMap.values()) {
            Speech_MongoDB_Impl speechMongoDB = new Speech_MongoDB_Impl(speechFile);
            Document document = speechMongoDB.getMongoDocument();
            dbHandler.insertOneNotExists(document, collectionName);
            progressBar.show(cnt);
            cnt++;
            if (cnt%10==0 || cnt==speechMap.size()+1) {
                dbHandler.updateCountProgress("cntUploadedSpeech", "count", cnt-1);
            }
        }
    }

    /**
     * insert all Kommentar documents to Kommentar Collection
     * @param commentMap A map with all Kommentar_File_Impl objects
     * @param dbHandler MongoDBConnectionHandler, which can connect to the MongoDB
     * @author Jiayu Ma(implemented)
     */
    public static void insertAllCommentDocument(Map<String, Comment> commentMap, MongoDBHandler dbHandler, String collectionName) {
        dbHandler.updateCountProgress("cntUploadedComment", "total", commentMap.size());

        ProgressBar progressBar = new ProgressBar("Uploading all comments", commentMap.size());
        int cnt = 1;
        for (Comment commentFile : commentMap.values()) {
            Comment_MongoDB_Impl commentMongoDB = new Comment_MongoDB_Impl(commentFile);
            Document document = commentMongoDB.getMongoDocument();
            dbHandler.insertOneNotExists(document, collectionName);
            progressBar.show(cnt);
            cnt++;
            if (cnt%10==0 || cnt==commentMap.size()+1) {
                dbHandler.updateCountProgress("cntUploadedComment", "count", cnt-1);
            }
        }
    }

    /**
     * insert all documents to each Collection
     * @param dbHandler MongoDBConnectionHandler, which can connect to the MongoDB
     * @author Jiayu Ma(implemented)
     */
    public static void insertAllDocument(MongoDBHandler dbHandler) throws ParserConfigurationException, IOException, ParseException, SAXException {
        // load all xml files
        File folder = new File("XMLResources/Protocols");
        List<File> files = XMLFileReader.findXMLFiles(folder);
        File stammdatenFile = new File("XMLResources/MDB-Stammdaten/MDB_STAMMDATEN.XML");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("All XMLs have been loaded.");

        // analyse all loaded xml files
        XMLReader xmlReader = new XMLReader(files, stammdatenFile, dbHandler);
        Map<String, Map> dataMap = xmlReader.readXML();

        // for each speech or comment: insert document without NLP, then run NLP, then insert document with NLP into another collection
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        dbHandler.resetCountProgress("cntUploadedSpeaker");
        dbHandler.resetCountProgress("cntUploadedSpeech");
        dbHandler.resetCountProgress("cntUploadedComment");

        insertAllSpeakerDocument(dataMap.get("speaker"), dbHandler, "speakerWp1920");
        insertAllSpeechDocument(dataMap.get("speech"), dbHandler, "speechComplete");
        insertAllCommentDocument(dataMap.get("comment"), dbHandler, "commentComplete");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        // if all data have been analysed and insert successfully, then insert the protocol index which just analysed.
        // each file is a new protocol. Upload those protocol's ID to DB.
        // If we want to start XML analyse again later, we can only analyse the new files after those.
        for(File file : xmlReader.getXmlFiles()) {
            String protocolId = file.getName().split("-")[0]; //19001-data.xml  =>  19001
            int wp = Integer.parseInt(protocolId.substring(0,2));  // 19123 => 19
            int index = Integer.parseInt(protocolId.substring(2, protocolId.length()));  //19123 => 123
            org.bson.Document doc = new org.bson.Document("_id", protocolId);
            doc.put("_id", protocolId);
            doc.put("index", index);
            doc.put("wp", wp);
            dbHandler.insertOneNotExists(doc, "protocol");
        }
    }




}

