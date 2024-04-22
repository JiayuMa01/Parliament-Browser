package org.ppr.abschlussprojekt.javaspark;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ppr.abschlussprojekt.data.database.DataService;
import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.database.MongoDBHelper;
import org.ppr.abschlussprojekt.data.impl.mongodb.Speaker_MongoDB_Impl;
import org.ppr.abschlussprojekt.data.impl.mongodb.Speech_MongoDB_Impl;
import org.ppr.abschlussprojekt.data.impl.nlp.Speech_NLP_Impl;
import org.ppr.abschlussprojekt.helper.LaTeXHelper.LatexHelper;
import org.ppr.abschlussprojekt.helper.NLPHelper.Converter;
import org.ppr.abschlussprojekt.helper.XMLHelper.XMLDownloader;
import org.xml.sax.SAXException;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.servlet.SparkApplication;
import spark.template.freemarker.FreeMarkerEngine;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

/**
 * Start the JavaSpark API
 * @author Jiayu Ma(implemented), Matthias Beck(modified)
 */
public class StartAPI implements SparkApplication {
    private static MongoDBHandler dbHandlerAPI = null;

    /**
     * main method, starting point
     * @param args
     * @throws IOException
     * @throws TemplateException
     * @author Kevin Schuff(modified)
     */
    public static void main(String[] args) throws IOException, TemplateException {
        dbHandlerAPI = new MongoDBHandler();
        StartAPI startapi = new StartAPI();
        startapi.init();


    }
    /**
     * This method sets the configuration for templates
     * took this from a tut
     * @author Kevin Bönisch
     */
    private static Configuration configuration = Configuration.getDefaultConfiguration();

    /**
     * starting dataservice and implementing some routes
     * @author Kevin Schuff(implemented), Matthias Beck(modified), Rodiana Koukouzeli(modified), Fabian Hamid Fazli(modified), Jiayu Ma(modified)
     */
    @Override
    public void init() {
        DataService dataService = new DataService(dbHandlerAPI);
        Gson gson = new Gson();

        // Set the folder for our template files
        try {
            configuration.setDirectoryForTemplateLoading(new File("JavaProject/Web/"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //CORS Problem Handling
        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
        });


        // Volltextvisualisierungsroute
        get("/Volltextvisualisierung",  (request, response) -> {
            // eine bestimmte speech oder comment ziehen
            MongoCollection speechCollection = dbHandlerAPI.getCollection("speechComplete");
            Document doc =  dbHandlerAPI.getOneDocument(speechCollection, "ID1910603100");
            Converter converter = new Converter();
            Speech_NLP_Impl speechNlP = converter.speechNLPConstructor(doc);
            // get speaker data
            Speaker_MongoDB_Impl speaker = dataService.getSpeakerData(dbHandlerAPI, speechNlP.getSpeaker());
            String speakerName = speaker.getVorname() + " " + speaker.getNachname();
            String speakerFraction = speaker.getFractionName();
            String speakerParty = speaker.getPartyName();
            String speakerPictureURL = speaker.getPictureInfoList().get(0);
            System.out.println(speakerName +  speakerFraction + speakerParty + speakerPictureURL);

            // get Person index
            speechNlP.setPersonIndicesList();
            List<Integer> personIndices = speechNlP.getPersonIndices();
            System.out.println("Person" + personIndices);

            // get Organisation index
            speechNlP.setOrganisationIndicesList();
            List<Integer> organisationIndices = speechNlP.getOrganisationIndices();
            System.out.println("Organisation" + organisationIndices);

            // get Location index
            speechNlP.setLocationIndicesList();
            List<Integer> locationIndices = speechNlP.getLocationIndices();
            System.out.println("Location" + locationIndices);

            // List<Speech_NLP_Impl> speeches = dataService.getRelatedSpeeches("speechComplete", "Russland", converter);
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("speechText", speechNlP.getText());
            attributes.put("speech", speechNlP);
            attributes.put("personIndices", personIndices.toString());
            attributes.put("organisationIndices", organisationIndices.toString());
            attributes.put("locationIndices", locationIndices.toString());
            attributes.put("speakerFraction", speakerFraction);
            attributes.put("speakerName", speakerName);
            attributes.put("speakerParty", speakerParty);
            attributes.put("speakerPictureURL", speakerPictureURL);

            return new ModelAndView(attributes, "ktemplates/Volltextvisualisierung/testIndex.ftl");
        }, new FreeMarkerEngine(configuration));

        // path to get data for pos in speeches
        get("/speech/pos", (request, response) -> {
            //List<Document> topPos = dataService.getPosUse();
            List<Document> topPos = getTopDocumentList(request, "pos", dataService, "speechComplete");
            return gson.toJson(topPos);
        });
        // path to get data for tokens in speeches
        get("/speech/token", (request, response) -> {
            List<Document> topToken = getTopDocumentList(request, "token", dataService, "speechComplete");
            return gson.toJson(topToken);
        });
        // path to get data for sentiment in speeches
        get("/speech/sentiment", (request, response) -> {
            List<Document> sentiments = getSentiments(request, dataService, "speechComplete");
            return gson.toJson(sentiments);
        });
        // path to get data for sentiment in comments
        get("/comment/sentiment", (request, response) -> {
            List<Document> sentiments = getSentiments(request, dataService, "commentComplete");
            return gson.toJson(sentiments);
        });
        // path to get data for named entities in speeches
        get("/speech/namedEntities", (request, response) -> {
            List<Document> namedEntities = getTopNamedEntityList(request, dataService, "speechComplete");
            return gson.toJson(namedEntities);
        });
        // path to get data for speakers in speeches
        get("/speech/speaker", (request, response) -> {
            List<Document> topSpeaker = getTopSpeakerList(request, dataService, "speechComplete");
            return gson.toJson(topSpeaker);
        });
        // path to get data for lemmas in speeches
        get("/speech/lemma", (request, response) -> {
            List<Document> topLemma = getTopDocumentList(request, "lemma", dataService, "speechComplete");
            return gson.toJson(topLemma);
        });
        // path to get data for persons in speeches
        get("/speech/persons", (request, response) -> {
            List<Document> topPersons = getTopDocumentList(request, "persons", dataService, "speechComplete");
            return gson.toJson(topPersons);
        });
        // path to data for persons in comments
        get("/comment/persons", (request, response) -> {
            List<Document> topPersons = getTopDocumentList(request, "persons", dataService, "commentComplete");
            return gson.toJson(topPersons);
        });
        // path to get data for organisations in speeches
        get("/speech/organisations", (request, response) -> {
            List<Document> topOrganisations = getTopDocumentList(request, "organisations", dataService, "speechComplete");
            return gson.toJson(topOrganisations);
        });
        // path to get data for organisations in comments
        get("/comment/organisations", (request, response) -> {
            List<Document> topOrganisations = getTopDocumentList(request, "organisations", dataService, "commentComplete");
            return gson.toJson(topOrganisations);
        });
        // path to get data for locations in speeches
        get("/speech/locations", (request, response) -> {
            List<Document> topLocations = getTopDocumentList(request, "locations", dataService, "speechComplete");
            return gson.toJson(topLocations);
        });
        // path to get data for locations in comments
        get("/comment/locations", (request, response) -> {
            List<Document> topLocations = getTopDocumentList(request, "locations", dataService, "commentComplete");
            return gson.toJson(topLocations);
        });
        // d3 graphs
        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("title", "Coole Charts :)");
            return new ModelAndView(attributes, "ktemplates/kIndex.ftl");
        }, new FreeMarkerEngine(configuration));
        // path to get data for locations in comments
        get("/loginFeature/update/speaker", (request, response) -> {
            // update speaker from speech
            Document query = new Document ("_id", request.queryParams("speechID"));
            Document updateDocument = new Document("$set", new Document("speaker", request.queryParams("speakerID")));
            // returns true if successful else false
            return dbHandlerAPI.updateWithResult("speech", query, updateDocument);
        });
        // path to get data for locations in comments
        get("/loginFeature/update/comment", (request, response) -> {
            // update speaker from speech
            Document query = new Document ("_id", request.queryParams("commentID"));
            Document updateDocument = new Document("$set", new Document()
                    .append("IDOfCommentator", request.queryParams("commentatorID"))
                    .append("NameOfFraction", request.queryParams("fraction")));
            // returns true if successful else false
            return dbHandlerAPI.updateWithResult("comment", query, updateDocument);
        });

        // path contains data i.e. JsonObject with nodes and links to be displayed on /network/comments/vis
        get("/network/comments", (request, response) -> {
            JSONObject graph = applyCommentFilter(request,dataService);
            response.raw().setContentType("application/json");
            response.type("application/json");
            response.header("Content-Type", "application/json");
            return graph;
        });
        // path contains data of a specific protocol which will be displayd as a graph on /network/comments/vis
        get("/network/comments/protocol", (request, response) -> {
            JSONObject graph = protocolFilterComment(request,dataService);
            response.raw().setContentType("application/json");
            response.type("application/json");
            response.header("Content-Type", "application/json");
            return graph;
        });
       // path of the actual visualization of the data
        get("/network/comments/vis", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("title", "Kommentarnetzwerk");
            return new ModelAndView(attributes, "templates/networks/commentNetwork.ftl");
        }, new FreeMarkerEngine(configuration));


        // path contains data i.e. JsonObject with nodes and links to be displayed on /network/categories/vis
        get("/network/categories", (request, response) -> {
            //Map<String, JSONArray> result = new HashMap<>();
            JSONObject categoriesMPs = applyCategoryFilter(request,dataService);
            response.raw().setContentType("application/json");
            response.type("application/json");
            response.header("Content-Type", "application/json");

            return categoriesMPs;
        });
        // path contains data of a specific protocol which will be displayed as a graph on /network/categories/vis
        get("network/categories/protocol", (request, response) -> {
            JSONObject graph = protocolFilterCateg(request,dataService);
            return  graph;

        });
        // path of the actual visualization of the category network
        get("/network/categories/vis", (request, response) -> {
            Map<String,Object> attributes = new HashMap<>();
            attributes.put("title","Kategorienetzwerk");
            return new ModelAndView(attributes,"templates/networks/categoryNet.ftl");
        }, new FreeMarkerEngine(configuration));

        // path contains data of a specific protocol which will be displayed as a network on /network/speeches/vis
        get("/network/speeches/protocol", (request, response) -> {
            JSONObject graph = protocolFilterSpeech(request, dataService);
            return  graph;
        });

        // path contains data i.e. JsonObject with nodes and links to be displayed on /network/speeches/vis
        get("/network/speeches", (request, response) -> {
            JSONObject spGraph = applySpeechFilter(request,dataService);
            response.raw().setContentType("application/json");
            response.type("application/json");
            response.header("Content-Type", "application/json");
            return  spGraph;

        });

        // path of the actual visualization of the speech network
        get("network/speeches/vis", (request, response) -> {
            Map<String, String> attributes = new HashMap<>();
            attributes.put("title", "Sentiment-Rede-Netzerk");
            return new ModelAndView(attributes, "templates/networks/speechNetwork.ftl");
        }, new FreeMarkerEngine(configuration));


        // route contains data to be used in the authentication query
        get("/login/users", (request, response) -> {
            String result  = authenticated(request, dataService);
            return result ;
        });


        // route contains data to be used in the query
        get("/login/create", (request, response) -> {
            String user = request.queryParams("user");
            String pw = request.queryParams("password");
            String role = request.queryParams("role");
            String createdUser = dataService.createUser(user, pw, role);
            return createdUser;
        });

        // route contains data to be used in the add speaker query
        get("/login/add", (request, response) -> {
            String id = request.queryParams("id");
            String fname = request.queryParams("firstname");
            String lname = request.queryParams("lastname");
            String party = request.queryParams("party");
            return dataService.addSpeaker(id,fname,lname,party);
        });

        // route contains data to be used in the add speech query
        get("/login/addspeech", (request, response) -> {
            String id = request.queryParams("id");
            String text = request.queryParams("text");
            String speakerID = request.queryParams("speakerID");
            return dataService.addSpeech(id,text,speakerID);
        });
        // route contains data to be used in the delete speaker query
        get("/login/delete", (request, response) -> {
            String id = request.queryParams("id");
            return  dataService.deleteSpeaker(id);
        });

        // route contains data to be used in the delete user query
        get("/login/deleteuser", (request, response) -> {
            String user = request.queryParams("user");
            return  dataService.deleteUser(user);
        });
        // contains data to be used as parameters of the delete password query
        get("/login/deletepw", (request, response) -> {
            String user = request.queryParams("user");
            String pw = request.queryParams("oldPassword");
            boolean userInfo =  dataService.authenticate(user,pw);
            Document query = new Document ("user", request.queryParams("user"));
            if (userInfo){
                Document updateDocument = new Document("$set", new Document("password", request.queryParams("newPassword").hashCode()));
                return dbHandlerAPI.updateWithResult("Users", query, updateDocument);

            }else {
                return "false";
            }
        });
        // contains data to be used as parameters of the upgrade user query
        get("/login/upgradeuser", (request, response) -> {
            String user = request.queryParams("user");
            String pw = request.queryParams("userpassword");
            String role = request.queryParams("role");
            boolean userInfo =  dataService.authenticate(user,pw);
            Document query = new Document ("user", user);
            if (userInfo){
                Document updateDocument = new Document("$set", new Document("role", role));
                return dbHandlerAPI.updateWithResult("Users", query, updateDocument);

            }else {
                return "false";
            }
        });

        // path to the login page
        get("/login", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("title", "Login");
            return new ModelAndView(attributes, "templates/loginfiles/login.ftl");
        }, new FreeMarkerEngine(configuration));


        // Routes for the export of latex/pdf documents and editing the templates, author: Matthias Beck
        // This route displays the site where you can choose which protocols to export/display
        // also displayed on this route is the functionality to edit templates
        get("/export", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("title", "Export");
            return new ModelAndView(attributes, "Export.ftl");
        }, new FreeMarkerEngine(configuration));

        // Returns the pdf file with all protocols in it, author: Matthias Beck
        get("/export/all", (request, response) -> {
            LatexHelper latexHelper = new LatexHelper();
            latexHelper.createLatexForAllProtocols();

            // defines the response
            response.header("Content-disposition=inline", "attachment; filename=export.pdf;");

            // gets the pdf file from path
            String filePath = System.getProperty("user.dir") + "/JavaProject/latex/export/export.pdf";
            File returnFile = new File(filePath);

            // streams the pdf to the frontend
            OutputStream outputStream = response.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(returnFile.toPath()));
            outputStream.flush();

            return response;
        });

        // creates a pdf given the protocols which should be included, author: Matthias Beck
        post("/export/exportSomeProtocols", "application/json", (request, response) -> {
            LatexHelper latexHelper = new LatexHelper();

            // reads the given request in json
            JSONObject protocols = new JSONObject(request.body());

            // saves the given data and creates latex/pdf
            String wahlperioden = protocols.getString("wp");
            String indexes = protocols.getString("index");
            String speeches = protocols.getString("speeches");

            latexHelper.createLatexForSomeProtocols(wahlperioden, indexes, speeches);

            return "Erfolgreich erstellt!";
        });

        // returns the pdf created from a set of protocols, author: Matthias Beck
        get("/export/displaySomeProtocols", (request, response) -> {
            // defines the header
            response.header("Content-disposition=inline", "attachment; filename=export.pdf;");

            // gets the pdf file and streams it to the frontend
            String filePath = System.getProperty("user.dir") + "/JavaProject/latex/export/export.pdf";
            File returnFile = new File(filePath);

            OutputStream outputStream = response.raw().getOutputStream();
            outputStream.write(Files.readAllBytes(returnFile.toPath()));
            outputStream.flush();

            return response;
        });

        // Returns the template names , author: Matthias Beck
        get("/export/getTemplateNames", (request, response) -> {
            String templateNames = "";

            // gets the list of the templates
            String filePath = System.getProperty("user.dir") + "/JavaProject/latex/templates";
            File directory = new File(filePath);
            File[] files = directory.listFiles();

            // creates a list of the templates without the ending "_template.txt"
            for (File file : files) {
                String[] partsOfTemplateName = file.getName().split("_");
                templateNames += partsOfTemplateName[0]+",";
            }

            return templateNames;
        });

        // returns the text of a given template, author: Matthias Beck
        get("/export/getTemplateTextByName/:templatename",  (request, response) -> {
            LatexHelper latexHelper = new LatexHelper();

            // gets the name of a template und returns the templatetext
            String name = request.params(":templatename");
            String templateName = name + "_template.txt";

            return latexHelper.getTemplateAsString(templateName);
        });

        // creates a new template from the given data, if the template already exits it will be overwritten
        // author: Matthias Beck
        post("/export/saveTemplate", "application/json", (request, response) -> {
            JSONObject template = new JSONObject(request.body());

            // reads the data from the request
            String templatename = template.getString("name") + "_template.txt";
            String templatetext = template.getString("text");

            // saves the created template - if exiting it will be overwritten
            String templatePath = System.getProperty("user.dir") + "/JavaProject/latex/templates/" + templatename;
            FileWriter myWriter = new FileWriter(templatePath);
            myWriter.write(templatetext);
            myWriter.close();

            return "Erzeugen erfolgreich!";
        });


        // localhost:4567/uploadStatus
        // Get current upload status, NLP process status
        // author: Jiayu Ma
        get("/uploadStatus", "application/json;charset=utf-8", (request, response) -> getUploadStatus(request, response, dbHandlerAPI));

        // localhost:4567/addNewData
        // download XMLs, analyze XMLs, insert to MongoDB and NLP
        // only new XMLs will be downloaded
        // author: Jiayu Ma
        get("/addNewData", (request, response) -> addNewData(request, response, dbHandlerAPI));

        // localhost:4567/pageAddData
        // start the freemarker page to show progress bar, and is able to add new data from Bundestag.
        // author: Jiayu Ma
        get("/pageAddData", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();
            return new ModelAndView(attributes, "ProgressBar.ftl");
        }, new FreeMarkerEngine(configuration));


    }

    /**
     * This method gets the document list with the top x of a subject
     * @param request
     * @param subject
     * @param dataService
     * @param collection
     * @return list with top x
     * @author Kevin Schuff
     */
    public List<Document> getTopDocumentList(Request request, String subject, DataService dataService, String collection){
        String searchInput = request.queryParams("searchInput");
        // getting these from default setting or frontend calender
        int resultLimit = Integer.parseInt(request.queryParams("resultLimit"));
        // subtracting 7200000 mil sec(2hrs) to account for time zones
        long startDateAsLong = Long.parseLong(request.queryParams("startDate"))-7200000;
        long endDateAsLong = Long.parseLong(request.queryParams("endDate"));
        List<Document> topSubjects = dataService.getTop(subject,resultLimit, startDateAsLong, endDateAsLong, collection, searchInput);
        return topSubjects;
    }
    /**
     * This method gets a document list with the top named entitities
     * @param request
     * @param dataService
     * @param collection
     * @return list with top named entity
     * @author Kevin Schuff
     */
    public List<Document> getTopNamedEntityList(Request request, DataService dataService, String collection){
        List<Document> allNamedEntities = new ArrayList<>();
        // get top x of each type
        List<Document> topPersons = getTopDocumentList(request, "persons", dataService, collection);
        List<Document> topOrganisations = getTopDocumentList(request, "organisations", dataService, collection);
        List<Document> topLocations = getTopDocumentList(request, "locations", dataService, collection);
        for(int i=0;i< topPersons.size();i++){
            // add name, so they can be grouped by that value
            topPersons.get(i).put("name", "persons");
            // add x value
            topPersons.get(i).put("x", i+1);
            allNamedEntities.add(topPersons.get(i));
            topOrganisations.get(i).put("name", "organisations");
            topOrganisations.get(i).put("x", i+1);
            allNamedEntities.add(topOrganisations.get(i));
            topLocations.get(i).put("name", "locations");
            topLocations.get(i).put("x", i+1);
            allNamedEntities.add(topLocations.get(i));
        }
        System.out.println(allNamedEntities);
        return allNamedEntities;
    }
    /**
     * This method gets a document list with the top speakers
     * @param request
     * @param dataService
     * @param collection
     * @return list with top speaker
     * @author Kevin Schuff
     */
    public List<Document> getTopSpeakerList(Request request, DataService dataService, String collection){
        String searchInput = request.queryParams("searchInput");
        // getting these from default setting or frontend calender
        int resultLimit = Integer.parseInt(request.queryParams("resultLimit"));
        // subtracting 7200000 mil sec(2hrs) to account for time zones
        long startDateAsLong = Long.parseLong(request.queryParams("startDate"))-7200000;
        long endDateAsLong = Long.parseLong(request.queryParams("endDate"));
        List<Document> topSpeaker = dataService.getTopSpeaker(resultLimit, startDateAsLong, endDateAsLong, collection, searchInput);
        return topSpeaker;
    }
    /**
     * This method gets a document list with occurrences of positive, negative or neutral speech and comments.
     * @param request
     * @param dataService
     * @param collection
     * @return list with sentiment occurrences
     * @author Kevin Schuff
     */
    public List<Document> getSentiments(Request request, DataService dataService, String collection){
        String searchInput = request.queryParams("searchInput");
        // getting these from default setting or frontend calender
        // subtracting 7200000 mil sec(2hrs) to account for time zones
        long startDateAsLong = Long.parseLong(request.queryParams("startDate"))-7200000;
        long endDateAsLong = Long.parseLong(request.queryParams("endDate"));
        List<Document> sentiments = dataService.getSentiment(startDateAsLong, endDateAsLong, collection, searchInput);
        return sentiments;
    }


    /**
     * Get current upload status, NLP process status
     * @param request request
     * @param response response
     * @return current upload status
     * @author Jiayu Ma(implemented)
     */
    private JSONArray getUploadStatus(Request request, Response response, MongoDBHandler dbHandlerAPI) {
        JSONArray jsonArray = new JSONArray();

        //Get all documents
        MongoCursor<Document> documentList = dbHandlerAPI.doQueryIterator(new BasicDBObject(), "countProgress");
        while(documentList.hasNext()) {
            jsonArray.put(documentList.next());
        }

        response.raw().setContentType("application/json");
        response.type("application/json");
        return jsonArray;
    }

    /**
     * download XMLs, analyze XMLs, insert to MongoDB and NLP
     * only new XMLs will be downloaded
     * @param request
     * @param response
     * @param dbHandlerAPI dbHandler
     * @return name list of downloaded XMLs
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws ParseException
     * @throws SAXException
     */
    private JSONArray addNewData(Request request, Response response, MongoDBHandler dbHandlerAPI) throws ParserConfigurationException, IOException, ParseException, SAXException {
        // reset count progress
        dbHandlerAPI.resetCountProgress("cntUploadedSpeaker");
        dbHandlerAPI.resetCountProgress("cntUploadedSpeech");
        dbHandlerAPI.resetCountProgress("cntUploadedComment");
        dbHandlerAPI.resetCountProgress("cntAnalysedXML");
        dbHandlerAPI.resetCountProgress("cntDownloadedXML");
        dbHandlerAPI.setWorkStatus(1);  // set work status to 1 means start working


        // download new XMLs
        List<String> allDownloadedXmlNameList = XMLDownloader.downloadAllXML(dbHandlerAPI);
        System.out.println("All new XMLs have been downloaded.");
        // load downloaded xml files, analyse xml files, insert to db and nlp
        MongoDBHelper.insertAllDocument(dbHandlerAPI);
        System.out.println("All documents with NLP results have been uploaded.");
        // delete all XMLs that downloaded before at the end
        XMLDownloader.deleteXMLResourcesFolder(new File("XMLResources"));
        System.out.println("All downloaded XMLs have been deleted.");
        dbHandlerAPI.setWorkStatus(0);  // set work status to 0 means stop working

        JSONArray jsonArray = new JSONArray();
        jsonArray.put(new Document("downloadedXMLsName", allDownloadedXmlNameList));

        response.raw().setContentType("application/json");
        response.type("application/json");
        return jsonArray;
    }

    /**
     * stores data from the request url to be further used as mongoDB query, as seen above in the "get" methods
     * @param request
     * @param dataService
     * @return JsonObject with "nodes" and "links"
     * @author Rodiana Koukouzeli
     */
    public JSONObject applyCommentFilter(Request request, DataService dataService){
        String limit = request.queryParams("limit");
        int intLimit = Integer.parseInt(limit);
        String startDate = request.queryParams("startDate");
        long longStart = Long.parseLong(startDate);
        // subtracting 7200000 mil sec(2hrs) to account for time zones
        longStart -= 7200000;
        String endDate = request.queryParams("endDate");
        long longEnd = Long.parseLong(endDate);
        JSONObject graph = dataService.getCommentNetwork(intLimit, longStart, longEnd);
        return graph;
    }


    /**
     * stores data from the request url to be further used as mongoDB query, as seen above in the "get" methods
     * @param request
     * @param dataService
     * @return JsonObject with "nodes" and "links"
     * @author Rodiana Koukouzeli
     */
    public JSONObject applyCategoryFilter(Request request, DataService dataService){
        String limitCat = request.queryParams("limit");
        int intLimit = Integer.parseInt(limitCat);
        String startDate = request.queryParams("startDate");
        long longStart = Long.parseLong(startDate);
        // subtracting 7200000 mil sec(2hrs) to account for time zones
        longStart -= 7200000;
        String endDate = request.queryParams("endDate");
        long longEnd = Long.parseLong(endDate);
        JSONObject graph = dataService.getCategoriesNetwork(intLimit,longStart,longEnd);
        return graph;

    }

    /**
     * stores data from the request url to be further used as mongoDB query, as seen above in the "get" methods
     * @param request
     * @param dataService
     * @return JsonObject with "nodes" and "links"
     * @author Rodiana Koukouzeli
     */
    public JSONObject protocolFilterComment(Request request, DataService dataService){
        String protocol = request.queryParams("protocol");
        JSONObject graphComment = dataService.commentNetByProtocol(protocol);
        return graphComment;
    }

    /**
     * gets name of protocol from the url to be used in the mongoDB query
     * @param request
     * @param dataService
     * @return JsonObject
     * @author Rodiana Koukouzeli
     */
    public JSONObject protocolFilterCateg(Request request, DataService dataService){
        String protocolSp = request.queryParams("protocol");
        JSONObject spGraph = dataService.categoriesByProtocol(protocolSp);
        return spGraph;
    }

    /**
     * gets name of protocol from the url to be used in the mongoDB query
     * @param request
     * @param dataService
     * @return JsonObject
     * @auhtor Rodiana Koukouzeli
     */
    public JSONObject protocolFilterSpeech(Request request, DataService dataService){
        String speechProtocol = request.queryParams("protocol");
        JSONObject speechGraph = dataService.categorySpeechProtocol(speechProtocol);
        return  speechGraph;
    }

    /**
     * stores data from the request url to be further used as mongoDB query, as seen above in the "get" methods
     * @param request
     * @param dataService
     * @return JsonObject with "nodes" and "links"
     * @author Rodiana Koukouzeli
     */
    public JSONObject applySpeechFilter(Request request, DataService dataService){
        String spLimit = request.queryParams("limit");
        int intLimit = Integer.parseInt(spLimit);
        String startDate = request.queryParams("startDate");
        long longStart = Long.parseLong(startDate);
        String endDate = request.queryParams("endDate");
        long longEnd = Long.parseLong(endDate);
        JSONObject graphSpeech = dataService.getCategoryBySpeech(intLimit,longStart,longEnd);
        return graphSpeech;
    }

    /**
     * If user is authenticated, users role is beeing returned. If not simply return false.
     * @param request
     * @param dataService
     * @return String
     * @author Rodiana Koukouzeli
     */
    public String authenticated(Request request, DataService dataService){
        String user = request.queryParams("user");
        String pw = request.queryParams("password");
        boolean userInfo =  dataService.authenticate(user,pw);
        if (userInfo){
            return dataService.getRole(user,pw);
        }else {
            return "false";
        }

    }



}
