package org.ppr.abschlussprojekt.data.database;


import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ppr.abschlussprojekt.data.impl.mongodb.Speaker_MongoDB_Impl;
import org.ppr.abschlussprojekt.data.impl.nlp.Speech_NLP_Impl;
import org.ppr.abschlussprojekt.helper.NLPHelper.Converter;

import java.sql.Timestamp;
import java.util.*;

/**
 * This class gets data from the database and brings it into a representable form for the frontend
 * @author Kevin Schuff
 */
public class DataService {
    MongoDBHandler dbHandler = null;

    /**
     * Default constructor for class
     * @param dbHandler
     * @author Kevin Schuff
     */
    public DataService(MongoDBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    /**
     * This method gets the top x of a subject of a time period from a collection
     * @param subject
     * @param resultLimit
     * @param startDate
     * @param endDate
     * @param collection
     * @param search
     * @return document list with top x of a subject
     * @author Kevin Schuff
     */
    public List<Document> getTop(String subject, int resultLimit, long startDate, long endDate, String collection, String search) {
        System.out.println("Loading Top "+ subject +" ...");
        List<Document> topSubjects = new ArrayList<>();
        List<Bson> pipeline = new ArrayList<>(Arrays.asList());

        // add search match if given
        if (!search.equals("")){
            pipeline.add(new Document().append("$match", new Document()
                    .append("text", new Document().append("$regex", search).append("$options","i"))));
        }
        // filter by date
        if (collection.startsWith("speech")){
            // filter documents that do not fit the timeframe
            pipeline.add(new Document().append("$match", new Document()
                    .append("protocol.date", new Document().append("$gte", startDate).append("$lte", endDate))));
        }
        // lookup with speech collection to get the date of the speech, where the comment was made, to filter by date
        if (collection.startsWith("comment")){
            pipeline.add(new Document().append("$lookup", new Document()
                    .append("from","speechComplete").append("localField","speech").append("foreignField","_id").append("as","speechData")));
            // filter documents that do not fit the timeframe
            pipeline.add(new Document().append("$match", new Document()
                    .append("speechData.protocol.date", new Document().append("$gte", startDate).append("$lte", endDate))));
        }
        // creating temporary document for every lemma in a speech
        pipeline.add(new Document().append("$unwind", "$"+subject));
        // grouping by subject and counting occurrences
        pipeline.add(new Document().append("$group", new Document().append("_id", new Document().append(subject, "$" + subject + ".value"))
                .append("count", new Document().append("$sum", 1.0))));
        pipeline.add(new Document().append("$sort", new Document().append("count", -1.0)));
        pipeline.add(new Document().append("$limit", resultLimit));
        List<Document> result = new ArrayList<>();
        // aggregate collection
        dbHandler.getCollection(collection).aggregate(pipeline).allowDiskUse(true).into(result);
        // get subject and occurrence from collection
        for (Document doc : result) {
            Document subDoc = (Document) doc.get("_id");
            Document subjectDoc = new Document();
            // add subject value
            subjectDoc.put("value", subDoc.get(subject).toString());
            // add subject count
            subjectDoc.put("count", doc.get("count"));
            topSubjects.add(subjectDoc);
        }

        System.out.println("top "+ subject.toUpperCase() +" LOADED !");
        return topSubjects;
    }

    /**
     * This method gets the top speakers of a time period from a collection
     * @param resultLimit
     * @param startDate
     * @param endDate
     * @param collection
     * @param search
     * @return
     * @author Kevin Schuff
     */
    public List<Document> getTopSpeaker(int resultLimit, long startDate, long endDate, String collection, String search) {
        System.out.println("Loading Top Speaker ...");
        List<Document> topSpeaker = new ArrayList<>();
        List<Bson> pipeline = new ArrayList<>(Arrays.asList());

        // add search match if given
        if (!search.equals("")){
            pipeline.add(new Document().append("$match", new Document()
                    .append("text", new Document().append("$regex", search).append("$options","i"))));
        }

        // filter documents that do not fit the timeframe
        pipeline.add(new Document().append("$match", new Document()
                .append("$and", Arrays.asList(
                        new Document().append("protocol.date", new Document().append("$gte", startDate)),
                        new Document().append("protocol.date", new Document().append("$lte", endDate))))));
        // grouping by lemma and counting occurrences
        pipeline.add(new Document().append("$group", new Document().append("_id", new Document().append("speakerID", "$speaker"))
                .append("count", new Document().append("$sum", 1.0))));
        pipeline.add(new Document().append("$sort", new Document().append("count", -1.0)));
        pipeline.add(new Document().append("$limit", resultLimit));
        // lookup with speaker collection to get name
        pipeline.add(new Document().append("$lookup", new Document()
                .append("from","speaker").append("localField","_id.speakerID").append("foreignField","_id").append("as","speakerData")));

        List<Document> result = new ArrayList<>();
        // aggregate collection
        dbHandler.getCollection(collection).aggregate(pipeline).allowDiskUse(true).into(result);
        // get token and occurrence from collection
        for (Document doc : result) {
            Document speakerDoc = new Document();
            // in case of no name found
            String speakerName = "";
            List<Document> documentList = (List<Document>) doc.get("speakerData");
            if(documentList.size() > 0){
                Document subDoc = documentList.get(0);
                speakerName = subDoc.getString("fullName");
            }
            // add speaker name
            speakerDoc.put("value", speakerName);
            // add speaker count
            speakerDoc.put("count", doc.get("count"));
            topSpeaker.add(speakerDoc);
        }

        System.out.println("top SPEAKER LOADED !");
        return topSpeaker;
    }

    /**
     * This method gets the #positive, negative and neutral speeches or comments of a time period from a collection
     * @param startDate
     * @param endDate
     * @param collection
     * @param search
     * @return  #positive, negative and neutral speeches
     * @author Kevin Schuff
     */
    public List<Document> getSentiment(long startDate, long endDate, String collection, String search) {
        System.out.println("Loading Sentiments ...");
        List<Document> sentiments = new ArrayList<>();
        List<Bson> pipeline = new ArrayList<>(Arrays.asList());

        // add search match if given
        if (!search.equals("")){
            pipeline.add(new Document().append("$match", new Document()
                    .append("text", new Document().append("$regex", search).append("$options","i"))));
        }

        if (collection.startsWith("speech")){
            // filter documents that do not fit the timeframe
            pipeline.add(new Document().append("$match", new Document()
                    .append("protocol.date", new Document().append("$gte", startDate).append("$lte", endDate))));
        }
        // lookup with speech collection to get the date of the speech, where the comment was made
        if (collection.startsWith("comment")){
            pipeline.add(new Document().append("$lookup", new Document()
                    .append("from","speechComplete").append("localField","speech").append("foreignField","_id").append("as","speechData")));
            // filter documents that do not fit the timeframe
            pipeline.add(new Document().append("$match", new Document()
                    .append("speechData.protocol.date", new Document().append("$gte", startDate).append("$lte", endDate))));
        }
        // projecting for efficiency
        pipeline.add(new Document().append("$project", new Document().append("sentiment", 1.0)));
        // group by sentiment values beeing greater, equal or smaller than 0
        pipeline.add(new Document().append("$group", new Document().append("_id", new Document()
                        .append("$cond", Arrays.asList(
                                new Document().append("$gt", Arrays.asList("$sentiment", 0.0)),
                                "Greater 0",
                                new Document()
                                        .append("$cond", Arrays.asList(new Document().append("$eq", Arrays.asList("$sentiment", 0.0)),
                                                "Equal 0",
                                                "Less 0")))))
                .append("count", new Document().append("$sum", 1.0))));
        List<Document> result = new ArrayList<>();
        // aggregate collection
        dbHandler.getCollection(collection).aggregate(pipeline).allowDiskUse(true).into(result);
        // get token and occurrence from collection
        Document sentimentDoc = new Document();
        double numberOfPositiveSpeeches = 0;
        double numberOfNeutralSpeeches = 0;
        double numberOfNegativeSpeeches = 0;
        for (Document doc : result) {
            String test = (String) doc.get("_id");
            switch(test) {
                case "Greater 0":
                    numberOfPositiveSpeeches = doc.getDouble("count");
                    break;
                case "Equal 0":
                    numberOfNeutralSpeeches = doc.getDouble("count");
                    break;
                case "Less 0":
                    numberOfNegativeSpeeches = doc.getDouble("count");
                    break;
            }
        }
        // scaling numbers
        double divider = Math.max(Math.max(numberOfPositiveSpeeches,numberOfNeutralSpeeches),numberOfNegativeSpeeches)/10;
        if (divider==0){
            divider = 1;
        }
        sentimentDoc.put("positive", numberOfPositiveSpeeches/divider);
        sentimentDoc.put("neutral", numberOfNeutralSpeeches/divider);
        sentimentDoc.put("negative", numberOfNegativeSpeeches/divider);
        sentiments.add(sentimentDoc);
        System.out.println(collection +" SENTIMENTS LOADED !");
        return sentiments;
    }

    /**
     * Graph which will be used later-on as dataset for the network visualization.
     *
     * @return "nodes" containing the speakers and "links" containing speakers who commented on other speakers
     * with the computed sentiment and wp values
     * @author Jiayu Ma
     */

    public JSONObject getCommentNetwork(int limit, long startDate, long endDate){

        JSONObject nodesLinks = new JSONObject();
        JSONArray jNodes = new JSONArray();
        JSONArray jLinks = new JSONArray();
        List<Document> resultOne = new ArrayList<>();
        List<Document> resultTwo = new ArrayList<>();
        BasicDBObject project = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$fullName', 'party':'$partyName'}}");
        List<BasicDBObject> aggregatation = Arrays.asList(project);
        dbHandler.getCollection("speakerWp1920").aggregate(aggregatation).into(resultOne);
        //docListOne.stream().map(Document::toJson).forEach(System.out::println);
        for (Document doc : resultOne){
            jNodes.put(doc);
        }
        nodesLinks.put("nodes",jNodes);

        BasicDBObject match1 = BasicDBObject.parse("{'$match':{'$expr':{'$ne':[{'$size':'$persons'},0]}}}");
        BasicDBObject lookup1 = BasicDBObject.parse("{'$lookup': {'from': 'speechComplete', 'localField': 'speech', 'foreignField': '_id', 'as': 'speechInfo'}}");
        BasicDBObject unwind1 = BasicDBObject.parse("{'$unwind':'$speechInfo'}");;
        BasicDBObject unwind2 = BasicDBObject.parse("{'$unwind':'$persons'}");
        BasicDBObject match2 = BasicDBObject.parse("{'$match':{'$and':[{'speechInfo.protocol.date':{'$gte':"+startDate+"}}, {'speechInfo.protocol.date':{'$lte':"+endDate+"}}]}}");
        BasicDBObject lookup2 = BasicDBObject.parse("{'$lookup': {'from': 'speakerWp1920_copy', 'localField': 'persons.value', 'foreignField': 'fullName', 'as': 'commenterInfo'}}");
        BasicDBObject match3 = BasicDBObject.parse("{'$match':{'$expr':{'$ne':[{'$size':'$commenterInfo'},0]}}}");
        BasicDBObject climit = BasicDBObject.parse("{'$limit':"+limit+"}");
        BasicDBObject project1 = BasicDBObject.parse("{'$project':{'commenterInfo':0}}");
        BasicDBObject lookup3 = BasicDBObject.parse("{'$lookup': {'from': 'speakerWp1920', 'localField': 'speaker', 'foreignField': '_id', 'as': 'speakerInfo'}}");
        BasicDBObject unwind3 = BasicDBObject.parse("{'$unwind':'$speakerInfo'}");
        BasicDBObject project2 = BasicDBObject.parse("{'$project':{'_id':0, 'source':'$persons.value', 'target':'$speakerInfo.fullName', 'sentiment':1, 'wp':'$speechInfo.protocol.wp'}}");

        List<BasicDBObject> aggregateOptionList = Arrays.asList(match1,lookup1,unwind1,unwind2,match2,lookup2,match3,climit,project1,lookup3,unwind3,project2);

        dbHandler.getCollection("commentComplete").aggregate(aggregateOptionList).into(resultTwo);
        for (Document document:resultTwo){
            jLinks.put(document);
        }
        nodesLinks.put("links",jLinks);

        return nodesLinks;

    }



    /**
     * Same functionality as getCommentNetwork but represents a specific protocol
     * @param protocol
     * @return JsonObject with "nodes" and "links" as keys
     * @author Rodiana Koukouzeli
     */
    public JSONObject commentNetByProtocol(String protocol){

        JSONObject nodesLinks = new JSONObject();
        JSONArray jNodes = new JSONArray();
        JSONArray jLinks = new JSONArray();
        List<Document> resultOne = new ArrayList<>();
        List<Document> resultTwo = new ArrayList<>();

        BasicDBObject project = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$fullName', 'party':'$partyName'}}");
        List<BasicDBObject> aggregatation = Arrays.asList(project);
        dbHandler.getCollection("speakerWp1920").aggregate(aggregatation).into(resultOne);
        //docListOne.stream().map(Document::toJson).forEach(System.out::println);
        for (Document doc : resultOne){
            jNodes.put(doc);
        }
        nodesLinks.put("nodes",jNodes);

        BasicDBObject match1 = BasicDBObject.parse("{'$match':{'$expr':{'$ne':[{'$size':'$persons'},0]}}}");
        BasicDBObject lookup1 = BasicDBObject.parse("{'$lookup': {'from': 'speechComplete', 'localField': 'speech', 'foreignField': '_id', 'as': 'speechInfo'}}");
        BasicDBObject unwind1 = BasicDBObject.parse("{'$unwind':'$speechInfo'}");;
        BasicDBObject unwind2 = BasicDBObject.parse("{'$unwind':'$persons'}");
        BasicDBObject match2 = BasicDBObject.parse("{'$match':{'speechInfo.protocol.title': '"+protocol+"'}}");
        BasicDBObject lookup2 = BasicDBObject.parse("{'$lookup': {'from': 'speakerWp1920_copy', 'localField': 'persons.value', 'foreignField': 'fullName', 'as': 'commenterInfo'}}");
        BasicDBObject match3 = BasicDBObject.parse("{'$match':{'$expr':{'$ne':[{'$size':'$commenterInfo'},0]}}}");
        BasicDBObject project1 = BasicDBObject.parse("{'$project':{'commenterInfo':0}}");
        BasicDBObject lookup3 = BasicDBObject.parse("{'$lookup': {'from': 'speakerWp1920', 'localField': 'speaker', 'foreignField': '_id', 'as': 'speakerInfo'}}");
        BasicDBObject unwind3 = BasicDBObject.parse("{'$unwind':'$speakerInfo'}");
        BasicDBObject project2 = BasicDBObject.parse("{'$project':{'_id':0, 'source':'$persons.value', 'target':'$speakerInfo.fullName', 'sentiment':1, 'wp':'$speechInfo.protocol.wp', 'protocol': '$speechInfo.protocol.title'}}");

        List<BasicDBObject> aggregateOptionList = Arrays.asList(match1,lookup1,unwind1,unwind2,match2,lookup2,match3,project1,lookup3,unwind3,project2);

        dbHandler.getCollection("commentComplete").aggregate(aggregateOptionList).into(resultTwo);
        for (Document document:resultTwo){
            jLinks.put(document);
        }
        nodesLinks.put("links",jLinks);

        return nodesLinks;

    }


    /**represents a graph with categories and speakers as nodes. The links represent
     *  which speaker commented on what category
     * @return JsonObject with "nodes" and "links" as keys
     * @author Rodiana Koukouzeli
     */
    public JSONObject getCategoriesNetwork(int limit, long startDate, long endDate){
        JSONObject nodeslinks = new JSONObject();
        JSONArray cNodes = new JSONArray();
        JSONArray cLinks = new JSONArray();
        List<Document> resultOne = new ArrayList<>();

        BasicDBObject unwind = BasicDBObject.parse("{'$unwind':'$ddc'}");
        BasicDBObject climit = BasicDBObject.parse("{'$limit':98}");
        BasicDBObject project = BasicDBObject.parse("{'$project':{'_id':0,'id': '$ddc.value', 'party':'ddc'}}");

        List<BasicDBObject> aggregate = Arrays.asList(unwind,climit,project);
        ArrayList<Document> ddcDocs = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregate).into(new ArrayList<>());
        for (Document doc:ddcDocs){
            cNodes.put(doc);
        }

        BasicDBObject project2 = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$fullName', 'party':'$partyName'}}");
        List<BasicDBObject> aggregatation = Arrays.asList(project2);
        dbHandler.getCollection("speakerWp1920").aggregate(aggregatation).into(resultOne);
        for (Document document:resultOne){
            cNodes.put(document);
        }
        nodeslinks.put("nodes", cNodes);


        BasicDBObject match2 = BasicDBObject.parse("{'$match':{'$and':[{'protocol.date':{'$gte':"+startDate+"}}, {'protocol.date':{'$lte':"+endDate+"}}]}}");
        BasicDBObject limit2 = BasicDBObject.parse("{'$limit':"+limit+ "}");
        BasicDBObject lookup = BasicDBObject.parse("{'$lookup':{'from':'speakerWp1920','localField':'speaker','foreignField':'_id', 'as':'speakerName'}}");
        BasicDBObject unwind2 = BasicDBObject.parse("{'$unwind':'$speakerName'}");
        BasicDBObject project3 = BasicDBObject.parse("{$project:{'_id':0, 'ddc':1, 'name':'$speakerName.fullName', 'sentiment':1}}");


        List<BasicDBObject> aggregateOptions = Arrays.asList(match2,limit2,lookup,unwind2,project3);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregateOptions).into(new ArrayList<>());

        for (Document doc : result){
            ArrayList<Object> ddcList = (ArrayList<Object>) doc.get("ddc");
            Document ddcFirst = (Document) ddcList.get(0);
            Map<String, Object> links = new HashMap<>();

            links.put("source",  ddcFirst.get("value").toString());
            links.put("target",  doc.getString("name"));
            links.put("wp", doc.getString("wp"));
            links.put("sentiment", doc.get("sentiment"));
            cLinks.put(links);
        }
        nodeslinks.put("links",cLinks);
        return nodeslinks;
    }

    /**
     * same as getCategoriesNetwork but focuses only on one specific protocol
     * @param protocol
     * @return JSonObject with "nodes" and "links" as keys
     * @author Rodiana Koukouzeli
     */
    public JSONObject categoriesByProtocol(String protocol){
        JSONObject nodeslinks = new JSONObject();
        JSONArray cNodes = new JSONArray();
        JSONArray cLinks = new JSONArray();
        List<Document> resultOne = new ArrayList<>();

        BasicDBObject unwind = BasicDBObject.parse("{'$unwind':'$ddc'}");
        BasicDBObject climit = BasicDBObject.parse("{'$limit':98}");
        BasicDBObject project = BasicDBObject.parse("{'$project':{'_id':0,'id': '$ddc.value', 'party':'ddc'}}");

        List<BasicDBObject> aggregate = Arrays.asList(unwind,climit,project);
        ArrayList<Document> ddcDocs = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregate).into(new ArrayList<>());
        for (Document doc:ddcDocs){
            cNodes.put(doc);
        }


        BasicDBObject project2 = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$fullName', 'party':'$partyName'}}");
        List<BasicDBObject> aggregatation = Arrays.asList(project2);
        dbHandler.getCollection("speakerWp1920").aggregate(aggregatation).into(resultOne);
        for (Document document:resultOne){
            cNodes.put(document);
        }
        nodeslinks.put("nodes", cNodes);


        BasicDBObject match2 = BasicDBObject.parse("{'$match':{'protocol.title': '"+protocol+"'}}");
        BasicDBObject lookup = BasicDBObject.parse("{'$lookup':{'from':'speakerWp1920','localField':'speaker','foreignField':'_id', 'as':'speakerName'}}");
        BasicDBObject unwind2 = BasicDBObject.parse("{'$unwind':'$speakerName'}");
        BasicDBObject project3 = BasicDBObject.parse("{$project:{'_id':0, 'ddc':1, 'name':'$speakerName.fullName', 'sentiment':1}}");


        List<BasicDBObject> aggregateOptions = Arrays.asList(match2,lookup,unwind2,project3);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregateOptions).into(new ArrayList<>());

        for (Document doc : result){
            ArrayList<Object> ddcList = (ArrayList<Object>) doc.get("ddc");
            Document ddcFirst = (Document) ddcList.get(0);
            Map<String, Object> links = new HashMap<>();

            links.put("source",  ddcFirst.get("value").toString());
            links.put("target",  doc.getString("name"));
            links.put("wp", doc.getString("wp"));
            links.put("sentiment", doc.get("sentiment"));
            cLinks.put(links);
        }
        nodeslinks.put("links",cLinks);
        return nodeslinks;
    }


    /**
     * Represents graph which shows the relationship between speeches and categories
     * @return JsonObject with "nodes" representing categories and speeches and
     * "links"  containing the links between the nodes
     * @author Rodiana Koukouzeli
     */
    public JSONObject getCategoryBySpeech(int limit, long startDate, long endDate){

        JSONObject nodeslinks = new JSONObject();
        JSONArray cNodes = new JSONArray();
        JSONArray cLinks = new JSONArray();


        BasicDBObject unwind = BasicDBObject.parse("{'$unwind':'$ddc'}");
        BasicDBObject project = BasicDBObject.parse("{'$project':{'_id':0,'id': '$ddc.value', 'party':'ddc'}}");
        BasicDBObject climit = BasicDBObject.parse("{'$limit':98}");

        List<BasicDBObject> aggregate = Arrays.asList(unwind,project,climit);
        ArrayList<Document> ddcDocs = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregate).into(new ArrayList<>());
        for (Document doc:ddcDocs){
            cNodes.put(doc);
        }


        BasicDBObject match1 = BasicDBObject.parse("{'$match':{'$and':[{'protocol.date':{'$gte':"+startDate+"}}, {'protocol.date':{'$lte':"+ endDate+"}}]}}");
        BasicDBObject sLimit = BasicDBObject.parse("{'$limit':"+ limit+"}");
        BasicDBObject project2 = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$_id', 'party': 'speech'}}");

        List<BasicDBObject> aggrSpeech = Arrays.asList(match1,sLimit,project2);
        ArrayList<Document> speechDocs = dbHandler.getCollectionDocument("speechComplete").aggregate(aggrSpeech).into(new ArrayList<>());
        for (Document doc:speechDocs){
            cNodes.put(doc);
        }

        nodeslinks.put("nodes", cNodes);

        BasicDBObject match2 = BasicDBObject.parse("{'$match':{'$and':[{'protocol.date':{'$gte':"+startDate+"}}, {'protocol.date':{'$lte':"+endDate+"}}]}}");
        BasicDBObject lastLimit = BasicDBObject.parse("{'$limit':"+ limit+"}");
        BasicDBObject project3 = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$_id' 'ddc':1, 'wp':'$protocol.wp', 'sentiment':1}}");

        List<BasicDBObject> aggregateProt = Arrays.asList(match2,lastLimit,project3);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregateProt).into(new ArrayList<>());


        for (Document doc : result){
            ArrayList<Object> ddcList = (ArrayList<Object>) doc.get("ddc");
            Document ddcFirst = (Document) ddcList.get(0);
            Map<Object, Object> links = new HashMap<>();

            links.put("source", doc.get("id"));
            links.put("target",  ddcFirst.get("value").toString());
            links.put("wp", doc.get("wp"));
            links.put("sentiment", doc.get("sentiment"));
            cLinks.put(links);
        }
        nodeslinks.put("links",cLinks);

        return nodeslinks;

    }

    /**
     * same as getCategoryBySpeech but returns graph representing as specific protocol
     * @param protocol
     * @return JSonObject with "nodes" and "links" as keys
     * @author Rodiana Koukouzeli
     */
    public JSONObject categorySpeechProtocol(String protocol){

        JSONObject nodeslinks = new JSONObject();
        JSONArray cNodes = new JSONArray();
        JSONArray cLinks = new JSONArray();


        BasicDBObject unwind = BasicDBObject.parse("{'$unwind':'$ddc'}");
        BasicDBObject project = BasicDBObject.parse("{'$project':{'_id':0,'id': '$ddc.value', 'party':'ddc'}}");
        BasicDBObject climit = BasicDBObject.parse("{'$limit':98}");

        List<BasicDBObject> aggregate = Arrays.asList(unwind,project,climit);
        ArrayList<Document> ddcDocs = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregate).into(new ArrayList<>());
        for (Document doc:ddcDocs){
            cNodes.put(doc);
        }
        BasicDBObject match1 = BasicDBObject.parse("{'$match':{'protocol.title': '"+protocol+"'}}");
        BasicDBObject project2 = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$_id' 'party': 'speech'}}");

        List<BasicDBObject> aggrSpeech = Arrays.asList(match1,project2);
        ArrayList<Document> speechDocs = dbHandler.getCollectionDocument("speechComplete").aggregate(aggrSpeech).into(new ArrayList<>());
        for (Document doc:speechDocs){
            cNodes.put(doc);
        }

        nodeslinks.put("nodes", cNodes);

        BasicDBObject match2 = BasicDBObject.parse("{'$match':{'protocol.title': '"+protocol+"'}}");
        BasicDBObject project3 = BasicDBObject.parse("{'$project':{'_id':0, 'id':'$_id' 'ddc':1, 'wp':'$protocol.wp', 'sentiment':1}}");

        List<BasicDBObject> aggregateProt = Arrays.asList(match2,project3);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregateProt).into(new ArrayList<>());


        for (Document doc : result){
            ArrayList<Object> ddcList = (ArrayList<Object>) doc.get("ddc");
            Document ddcFirst = (Document) ddcList.get(0);
            Map<Object, Object> links = new HashMap<>();

            links.put("source", doc.get("id"));
            links.put("target",  ddcFirst.get("value").toString());
            links.put("wp", doc.get("wp"));
            links.put("sentiment", doc.get("sentiment"));
            cLinks.put(links);
        }
        nodeslinks.put("links",cLinks);

        return nodeslinks;

    }

    /**
     * This method returns all speeches related to a search string, added hard limit for testing
     * @param collection
     * @param search
     * @param converter
     * @return
     * @author Kevin Schuff
     */
    public List<Speech_NLP_Impl> getRelatedSpeeches(String collection, String search, Converter converter){
        System.out.println("Loading Speeches ...");
        List<Speech_NLP_Impl> speeches = new ArrayList<>();
        List<Bson> pipeline = new ArrayList<>(Arrays.asList());

        pipeline.add(new Document().append("$match", new Document()
                .append("text", new Document().append("$regex", search).append("$options","i"))));
        // hard limit
        pipeline.add(new Document().append("$limit", 10));

        List<Document> result = new ArrayList<>();
        // aggregate collection
        dbHandler.getCollection(collection).aggregate(pipeline).allowDiskUse(true).into(result);
        for (Document doc : result) {
            Speech_NLP_Impl speech = converter.speechNLPConstructor(doc);
            speeches.add(speech);
        }
        System.out.println("SPEECHES LOADED!");
        return speeches;
    }

    /**
     * This method initiates a speaker_mongodb_impl for a speakerid
     * @param speakerID
     * @return speaker as speaker_mongodb_impl
     * @author Kevin Schuff
     */
    public Speaker_MongoDB_Impl getSpeakerData(MongoDBHandler db, String speakerID){
        // remove ID123 -> 123
        speakerID = speakerID.replace("ID","");
        MongoCollection mongoCollection = db.getCollection("speakerWp1920");
        Document speakerDoc = db.getOneDocument(mongoCollection, speakerID);
        return new Speaker_MongoDB_Impl(speakerDoc);
    }
    /**
     * creates new user in the "User" collection.
     * @param username
     * @param password
     * @param role
     * @return
     * @author Rodiana Koukouzeli
     */
    public String createUser(String username, String password, String role){
        Document doc = new Document().append("user", username)
                .append("password", password.hashCode()).append("role",role);
        BasicDBObject match = BasicDBObject.parse("{'$match':{'user':'"+username+"'}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("Users").aggregate(aggregate).into(new ArrayList<>());


        if (result.isEmpty()){
            dbHandler.insertOne(doc, "Users");
            return "User "+ username + " added!";
        }else {
            return "Username already exists, please choose another one.";
        }
    }

    /**
     * Authenticates an already existing user by checking if username and password exist in the database.
     * @param username
     * @param password
     * @return
     * @author Rodiana Koukouzeli
     */
    public boolean authenticate(String username, String password){

        BasicDBObject match = BasicDBObject
                .parse("{'$match':{'$and':[{'user':{'$eq':'"+ username+"'}}, {'password':{'$eq':"+ password.hashCode() +"}}]}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("Users").aggregate(aggregate).into(new ArrayList<>());

        if(result.isEmpty()){
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Returns role of a user.
     * @param username
     * @param password
     * @return String
     * @author Rodiana Koukouzeli
     */
    public String getRole(String username, String password){
        BasicDBObject match = BasicDBObject
                .parse("{'$match':{'$and':[{'user':{'$eq':'"+ username+"'}}, {'password':{'$eq':"+password.hashCode()+"}}]}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("Users").aggregate(aggregate).into(new ArrayList<>());
        String role = result.get(0).getString("role");
        return role;
    }
    /**
     * Adds a new speaker to the speaker collection, if parameter id does not exist.
     * @param id
     * @param firstname
     * @param lastname
     * @param party
     * @return
     * @author Rodiana Koukouzeli
     */
    public String addSpeaker(String id, String firstname, String lastname,String party){
        Document doc = new Document();
        doc.append("_id", id);
        doc.append("vorname", firstname);
        doc.append("nachname", lastname);
        doc.append("partyName", party);
        BasicDBObject match = BasicDBObject.parse("{'$match':{'_id':'"+id+"'}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speakerWp1920").aggregate(aggregate).into(new ArrayList<>());


        if (result.isEmpty()){
            dbHandler.insertOne(doc, "speakerWp1920");
            return "Speaker "+ firstname+ " " + lastname + " added!";
        }else {
            return "Username already exists, please choose another one.";
        }
    }
    /**
     * Adds new speech to the speech collection.
     * @param id
     * @param text
     * @param speakerID
     * @return
     * @author Rodiana Koukouzeli
     */
    public String addSpeech(String id, String text, String speakerID){
        Document doc = new Document();
        doc.append("_id", id);
        doc.append("text", text);
        doc.append("speaker", speakerID);
        BasicDBObject match = BasicDBObject.parse("{'$match':{'_id':'"+id+"'}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speechComplete").aggregate(aggregate).into(new ArrayList<>());


        if (result.isEmpty()){
            dbHandler.insertOne(doc, "speechComplete");
            return "Speech added!";
        }else {
            return "SpeechID already exists, please choose another one.";
        }
    }



    /**
     * Checks if speaker exists and deletes if he does.
     * @param id
     * @return
     * @author Rodiana Koukouzeli
     */
    public  String deleteSpeaker(String id){
        BasicDBObject match = BasicDBObject.parse("{'$match':{'_id':'"+id+"'}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("speakerWp1920").aggregate(aggregate).into(new ArrayList<>());
        BasicDBObject query = new BasicDBObject();
        query.put("_id", id);

        if (result.isEmpty()){
            return "Diesen Redner gibt es nicht.";
        }else {
            dbHandler.delete(query, dbHandler.getCollection("speakerWp1920"));
            return "Redner wurde entfernt";
        }
    }

    /**
     * Checks of user exists and deletes if he does.
     * @param user
     * @return
     * @author Rodiana Koukouzeli
     */
    public  String deleteUser(String user){
        BasicDBObject match = BasicDBObject.parse("{'$match':{'user':'"+user+"'}}");
        List<BasicDBObject> aggregate = Arrays.asList(match);
        ArrayList<Document> result = dbHandler.getCollectionDocument("Users").aggregate(aggregate).into(new ArrayList<>());
        BasicDBObject query = new BasicDBObject();
        query.put("user", user);

        if (result.isEmpty()){
            return "Diesen User gibt es nicht.";
        }else {
            dbHandler.delete(query, dbHandler.getCollection("Users"));
            return "User wurde entfernt";
        }
    }





}
