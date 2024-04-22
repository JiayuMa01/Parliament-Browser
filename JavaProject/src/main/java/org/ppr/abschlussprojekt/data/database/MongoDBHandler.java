package org.ppr.abschlussprojekt.data.database;

import static com.mongodb.client.model.Filters.eq;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.ppr.abschlussprojekt.data.impl.nlp.Protocol_NLP_Impl;
import org.ppr.abschlussprojekt.helper.NLPHelper.Converter;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * This Class enables the communication with the database
 * @author Kevin Schuff(implemented), Jiayu Ma(modified), Matthias Beck(modified)
 */
public class MongoDBHandler {
    private MongoClient mongoClient = null;

    private MongoDatabase mongoDatabase = null;

    private Converter converter = null;

    /**
     * This method enables the connection to a database
     * Took the logic for this method from example 3
     * @author Giuseppe Abrami(implemented), Kevin Schuff(modified)
     */
    public MongoDBHandler(){
        URL url = MongoDBHandler.class.getClassLoader().getResource("PRG_WiSe22_Group_3_1.cfg");
        File config = new File(url.getFile());
        // https://stackoverflow.com/questions/16273174/how-to-read-a-configuration-file-in-java
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(config)) {
            prop.load(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String hostName = prop.getProperty("remote_host");
        String database = prop.getProperty("remote_database");
        String userName =prop.getProperty("remote_user");
        String password = prop.getProperty("remote_password");
        int port = Integer.parseInt(prop.getProperty("remote_port"));

        // defined credentials (Username, database, password)
        MongoCredential credential = MongoCredential.createScramSha1Credential(userName, database, password.toCharArray());
        // defining Hostname and Port
        ServerAddress seed = new ServerAddress(hostName, port);
        List<ServerAddress> seeds = new ArrayList(0);
        seeds.add(seed);
        // defining some Options, increased socketTimeout
        MongoClientOptions options = MongoClientOptions.builder()
                .connectionsPerHost(20)
                .socketTimeout(300000)
                .maxWaitTime(300000)
                .socketKeepAlive(true)
                .serverSelectionTimeout(300000)
                .connectTimeout(300000)
                .sslEnabled(false)
                .build();

        // connect to MongoDB
        mongoClient = new MongoClient(seeds, credential, options);

        this.mongoDatabase = mongoClient.getDatabase(database);

        this.converter = new Converter();

    }

    /**
     * This method returns a collection from the database.
     * Took the method from example 3.
     * @author Giuseppe Abrami
     * @param collection
     * @return collection
     * @author Kevin Schuff
     */
    public MongoCollection getCollection(String collection){
        return this.mongoDatabase.getCollection(collection);
    }

    /**
     * Get a collection<Document> with collection name.
     *
     * @param collectionName collection name
     * @return MongoCollection<Document> collectionDocument
     * @author Jiayu Ma(implemented)
     */
    public MongoCollection<Document> getCollectionDocument(String collectionName) {
        MongoCollection<Document> collectionDocument = this.mongoDatabase.getCollection(collectionName);
        if (collectionDocument==null) {
            System.out.println("Error: MongoDB collection " + collectionName + " not found");
            return null;
        }
        return collectionDocument;
    }

    /**
     * Insert a document to a collection. If duplicate document then report exception.
     *
     * @param document document to insert
     * @param collectionName a collection in database
     * @author Jiayu Ma(implemented)
     */
    public void insertOne(Document document, String collectionName) {
        try {
            getCollectionDocument(collectionName).insertOne(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert a document to a collection. Check the _id first, If duplicate document then do nothing. If not exists then insert.
     * The value of _id must be String
     *
     * @param document a document to insert
     * @param collectionName collection name
     * @author Jiayu Ma(implemented) Kevin Schuff(modified)
     */
    public void insertOneNotExists(Document document, String collectionName) {
        try {
            // Use findOneAndUpdate() to insert the document if it does not already exist
            Document searchQuery = new Document("_id", document.getString("_id"));
            Document update = new Document("$setOnInsert", document);
            Document result = getCollectionDocument(collectionName).findOneAndUpdate(searchQuery, update, new FindOneAndUpdateOptions().upsert(true).returnDocument(ReturnDocument.BEFORE));

            if (result != null) {
                System.out.println("Document with _id:" + document.getString("_id") + " already exists in the collection" + collectionName + "!");
            }
            // modification
            else{
                if(collectionName.startsWith("speech")){
                    // adds nlp analyze
                    this.converter.convertOne(getCollection("speechComplete"), document);
                }
                if(collectionName.startsWith("comment")){
                    // adds nlp analyze
                    this.converter.convertOne(getCollection("commentComplete"), document);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method collects all filtered Documents in a List.
     * @param collection
     * @param query
     * @return documents matching query
     * @author Kevin Schuff
     */
    public List<Document> readAndFilter(MongoCollection collection, BasicDBObject query) {
        FindIterable<Document> document = collection.find(query);
        List<Document> filteredDocuments = new ArrayList<>();
        MongoCursor<Document> cursor = document.iterator();

        while (cursor.hasNext()) {
            filteredDocuments.add(cursor.next());
        }
        return filteredDocuments;
    }

    /**
     * This Method returns the number of documents in a collection
     *
     * @param collection
     * @return count of documents
     * @author Kevin Schuff
     */
    public Long count(MongoCollection collection) {
        return collection.countDocuments();
    }

    /**
     * This method checks wheter a Document already exists or not, through their _id
     * @param collection
     * @param _id
     * @return a boolean saying if the document exists or not
     * @author Kevin Schuff
     */
    public boolean doesExist(MongoCollection collection, String _id){
        Document doc = (Document) collection.find(eq("_id", _id)).first();
        if (doc == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * same as doesExist above. change the parameter collectionName data type
     * @param collectionName
     * @param _id
     * @return
     */
    public boolean doesExist(String collectionName, String _id){
        Document doc = (Document) getCollection(collectionName).find(eq("_id", _id)).first();
        if (doc == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * This method takes a query and updates the first match.
     * @param collection
     * @param query
     * @param updateDocument
     * @return if the update was successful or not
     * @author Kevin Schuff
     */
    public String updateWithResult(String collection, Document query, Document updateDocument) {
        UpdateResult result = getCollection(collection).updateOne(query, updateDocument);
        boolean success = (result.getModifiedCount() > 0);
        if (success){
            return "true";
        }
        else{
            return "false";
        }
    }

    /**
     * Takes a document query and the name of a collection, updates the first match.
     *
     * @param collectionName collection name
     * @param query query
     * @param updateDocument the document to update
     * @author Jiayu Ma(implemented)
     */
    public void update(String collectionName, Document query, Document updateDocument) {
        getCollection(collectionName).updateOne(query, updateDocument);
    }

    /**
     * This method deletes the first document that matches the query from the collection
     *
     * @param query
     * @param collection
     * @author Kevin Schuff
     */
    public void delete(BasicDBObject query, MongoCollection collection) {
        collection.deleteOne(query);
    }

    /**
     * Method to initialize mongocursor that entails all documents matching a query
     * @param query
     * @param sCollection
     * @return mongocursor to iterate documents
     * @author Giuseppe Abrami
     */
    public MongoCursor doQueryIterator(BasicDBObject query, String sCollection){
        FindIterable result = this.getCollection(sCollection).find(query);
        return result.iterator();
    }

    /**
     * Method to initialize mongocursor that entails all documents matching a query sorted ascending
     * Based on the method doQueryIterator but modified to be able to return in sorted order
     * @author Matthias Beck
     * @param collection
     * @param queryDocument
     * @param sortObject
     * @return
     */
    public MongoCursor<Document> doSortedQueryIterator(String collection, Document queryDocument, BasicDBObject sortObject) {
        FindIterable<Document> result = this.getCollection(collection).find(queryDocument).sort(sortObject);
        return result.iterator();
    }

    /**
     * Gets mongocursor to iterate through all documents in a collection with a string
     * @param collection
     * @return mongocursor to iterate documents
     * @author Kevin Schuff
     */
    public MongoCursor getMongoCursor(String collection){
        FindIterable result = this.getCollection(collection).find().batchSize(10);
        return result.iterator();
    }

    /**
     * Gets mongocursor to iterate through all documents in a collection with a string
     * @param collection
     * @return mongocursor to iterate documents
     * @author Kevin Schuff
     */
    public MongoCursor getMongoCursor(MongoCollection collection){
        // limited batchsize. to prevent outofmemory errors
        FindIterable result = collection.find().batchSize(10);
        return result.iterator();
    }

    /**
     * This method looks for a document that matches the given id in a collection
     * @param collection
     * @param id
     * @return first matching document from a collection
     * @author Kevin Schuff
     */
    public Document getOneDocument(MongoCollection collection, String id){
        Document search = new Document();
        search.put("_id", id);
        MongoCursor<Document> mongoCursor = collection.find(search).iterator();
        if(mongoCursor.hasNext()){
            return mongoCursor.next();
        }
        else{
            return null;
        }

    }

    /**
     * aggregate the documents with options
     * @param optionList aggregate options
     * @param collectionName collection name
     * @return documents list after aggregation
     * @author Jiayu Ma(implemented)
     */
    public ArrayList<Document> aggregateDocument(ArrayList<BasicDBObject> optionList, String collectionName) {
        return getCollectionDocument(collectionName).aggregate(optionList).into(new ArrayList<>());
    }

    /**
     * update the current count or total number of documents in collection countProgress.
     * In order to show with progress bar in HTML
     * @param docIdName which document to update. cntAnalysedXML, cntUploadedSpeaker, cntUploadedSpeech, cntUploadedComment, cntNLPAnalysedSpeech, cntNLPAnalysedComment
     * @param keyName which field to update. count, total
     * @param num the number to update
     * @author Jiayu Ma(implemented)
     */
    public void updateCountProgress(String docIdName, String keyName, int num) {
        Document query = new Document("_id", docIdName);
        Document updateDoc = new Document("$set", new Document(keyName, num));
        update("countProgress", query, updateDoc);
    }

    /**
     * reset the count and total of one document in collection countProgress,
     * @param progressName which progress(document id) to reset
     * @author Jiayu Ma(implemented)
     */
    public void resetCountProgress(String progressName) {
        updateCountProgress(progressName, "count", 0);
        updateCountProgress(progressName, "total", 0);
    }

    /**
     * set upload work status in collection countProgress,
     * @param status 1 or 0
     * @author Jiayu Ma(implemented)
     */
    public void setWorkStatus(int status) {
        Document query = new Document("_id", "workstatus");
        Document updateDoc = new Document("$set", new Document("status", status));
        update("countProgress", query, updateDoc);
    }

}
