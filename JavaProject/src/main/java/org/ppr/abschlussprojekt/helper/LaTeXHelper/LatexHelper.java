package org.ppr.abschlussprojekt.helper.LaTeXHelper;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.impl.nlp.Protocol_NLP_Impl;
import org.ppr.abschlussprojekt.data.impl.nlp.Speech_NLP_Impl;
import org.ppr.abschlussprojekt.helper.NLPHelper.Converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * This class defines methods which helping to create latex and pdf files for the protocol export
 * @author Matthias Beck(implemented)
 */
public class LatexHelper {

    private static MongoDBHandler dbHandler = new MongoDBHandler();
    private static Converter converter = new Converter();

    /**
     * Constructor for the class
     * @author Matthias Beck
     */
    public LatexHelper() {

    }

    /**
     * Method takes the name of a template, reads it and returns the content as a string
     * @author Matthias Beck
     * @param templateName
     * @return
     */
    public String getTemplateAsString(String templateName) {
        // creates the path of the template given the templateName
        String templatePath = System.getProperty("user.dir") + "/JavaProject/latex/templates/" + templateName;
        Path filePath = Paths.get(templatePath);
        String templateAsString = "";

        // this code reads the file from the path and saves it to a string
        try {
            List<String> listOfLines = Files.readAllLines(filePath);

            for (String line : listOfLines) {
                templateAsString += line + "\n";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return templateAsString;
    }

    /**
     * Creates the pdf document for all protocols
     *
     * @author Matthais Beck
     */
    public void createLatexForAllProtocols() {
        String documentString = getTemplateAsString("document_template.txt");
        String protocolString = "";

        // creates the latex code for a protocol
        if (documentString.contains("%%PROTOKOLLE%%")) {
            MongoCursor<Document> protocolCursor = dbHandler.getMongoCursor("protocol");
            while (protocolCursor.hasNext()) {
                Protocol_NLP_Impl protocolNLP = converter.protocolNLPConstructor(protocolCursor.next());
                protocolString += protocolNLP.toTex("all");
            }
        }

        documentString = documentString.replace("%%PROTOKOLLE%%", protocolString);

        createPDF(documentString);
    }

    /**
     * function which creates the latex code for a set of protocols
     *
     * @author Matthias Beck
     * @param wp
     * @param index
     * @param speech
     */
    public void createLatexForSomeProtocols(String wp, String index, String speech) {
        String documentString = getTemplateAsString("document_template.txt");
        String protocolString = "";

        String[] wps = wp.split("-");
        String[] indexes = index.split("-");
        String[] speeches = speech.split("-");

        // creates the latex code for a protocol
        if (documentString.contains("%%PROTOKOLLE%%")) {
            for (int i = 0; i < wps.length; i++) {
                int queryWp = Integer.parseInt(wps[i]);
                int queryIndex = Integer.parseInt(indexes[i]);

                Document query = new Document();
                query.append("wp", queryWp);
                query.append("index", queryIndex);
                BasicDBObject sortObject = new BasicDBObject("_id", 1);
                MongoCursor<Document> protocolCursor = dbHandler.doSortedQueryIterator( "protocol", query, sortObject);

                while (protocolCursor.hasNext()) {
                    Protocol_NLP_Impl protocolNLP = converter.protocolNLPConstructor(protocolCursor.next());
                    protocolString += protocolNLP.toTex(speeches[i]);
                }
            }
        }

        documentString = documentString.replace("%%PROTOKOLLE%%", protocolString);

        createPDF(documentString);
    }

    /**
     * Creates a pdf file from a given latexcode
     * @author Matthias Beck
     * @param latexCode
     */
    public void createPDF(String latexCode) {
        String fromPath = System.getProperty("user.dir") + "/JavaProject/latex/export/export.tex";
        String toPath = System.getProperty("user.dir") + "/JavaProject/latex/export";
        Path latexPath = Paths.get(fromPath);

        // Converting string to byte array using getBytes() method
        byte[] arr = latexCode.getBytes();

        try {
            // Calling Files.write() method using path and byte array
            Files.write(latexPath, arr);
        }
        catch (IOException ex) {
            // Print message as exception occurred when invalid path of local machine is passed
            System.out.print("Invalid Path");
        }

        try {
            Runtime.getRuntime().exec("pdflatex --output-directory=" + toPath + " " + fromPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
