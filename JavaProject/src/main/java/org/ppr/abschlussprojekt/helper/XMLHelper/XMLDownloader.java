package org.ppr.abschlussprojekt.helper.XMLHelper;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import se.lth.cs.srl.languages.Language;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Class for download XMLs from the Bundestag website and save in a Folder ("parliament_browser_3_1/XMLResources")
 * check the collection protocol, only download the protocol xml files that not been analysed before.
 * @author Jiayu Ma(implemented)
 */

public class XMLDownloader {
    /**
     * Download XMLs
     * @author Jiayu Ma(implemented)
     */
    public static List<String> downloadAllXML(MongoDBHandler dbHandler){
        List<String> allDownloadedXmlNameList = new ArrayList<>();
        try {
            // Get HTML
            Thread.sleep(1000);
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent", "Mozilla/5.0");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            //headers.put("Accept-Language", "en-US,en;q=0.5");
            headers.put("Connection", "keep-alive");
            Document siteHTML = Jsoup.connect("https://www.bundestag.de/services/opendata")
                    .headers(headers)
                    .timeout(60000)
                    .get();

            // Download DTD and MDB-Stammdaten
            Elements elementList = siteHTML.getElementsByClass("bt-link-dokument");
            for(Element element: elementList){
                // Download DTD
                if(Pattern.matches("DTD für Plenarprotokolle des Deutschen Bundestags, gültig ab 19\\. Wahlperiode.*", element.attr("title"))){
                    try {
                        URL dtdURL = new URL(element.attr("abs:href"));
                        File dtdFile = new File("XMLResources/Protocols/dbtplenarprotokoll.dtd");
                        System.out.println("Downloading DTD from " + dtdURL + " at " + dtdFile);
                        FileUtils.copyURLToFile(dtdURL, dtdFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Download MDB-Stammdaten.zip then unzip it
                if(Pattern.matches("Stammdaten aller Abgeordneten seit 1949 im XML-Format.*", element.attr("title"))){
                    try {
                        URL mdbURL = new URL(element.attr("abs:href"));
                        File mdbFile = new File("XMLResources/MDB-Stammdaten.zip");
                        System.out.println("Downloading MDB-Stammdaten from " + mdbURL + " at " + mdbFile);
                        FileUtils.copyURLToFile(mdbURL, mdbFile);

                        // Unzip MDB-Stammdaten.zip
                        try (ZipFile zipFile = new ZipFile("XMLResources/MDB-Stammdaten.zip")) {
                            zipFile.extractAll("XMLResources/MDB-Stammdaten/");
                        } catch (ZipException e) {
                            e.printStackTrace();
                        }
                        // delete MDB-Stammdaten.zip after unzipping
                        mdbFile.delete();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            //Download all XMLs
            dbHandler.resetCountProgress("cntDownloadedXML");
            int cnt = 1;
            Elements sectionElementList = siteHTML.getElementsByTag("section");
            for (Element sectionElement: sectionElementList){
                String titleStr = sectionElement.getElementsByClass("bt-title").text();
                if(titleStr.matches("Plenarprotokolle der 19. Wahlperiode|Plenarprotokolle der [2-9][0-9]. Wahlperiode|Plenarprotokolle der [1-9][0-9]{2,}. Wahlperiode")){
                    String modId = (sectionElement.attr("id")).replace("mod", "");

                    String xmlUrl = "";
                    // check the number of all XMLs
                    int numAllXML = 0;
                    while(true){
                        xmlUrl = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/" + modId + "-" + modId + "?limit=10&noFilterSet=true&offset=" + numAllXML;
                        if(checkSiteError(xmlUrl)){
                            numAllXML += 10;
                        }
                        else { break; }
                    }
                    // This html only provides 10 XMLs each time, download 10 XMLs each loop
                    numAllXML = numAllXML - 10;
                    for(int offset = numAllXML; offset >= 0; offset = offset - 10){
                        xmlUrl = "https://www.bundestag.de/ajax/filterlist/de/services/opendata/" + modId + "-" + modId + "?limit=10&noFilterSet=true&offset=" + offset;
                        List<String> xmlNameList = downloadTenXMLs(xmlUrl, dbHandler);
                        cnt += xmlNameList.size();
                        allDownloadedXmlNameList.addAll(xmlNameList);
                        dbHandler.updateCountProgress("cntDownloadedXML", "count", cnt-1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return allDownloadedXmlNameList;
    }

    /**
     * Download 10 XMLs
     * @param url url to website
     * @author Jiayu Ma(implemented)
     */
    private static List<String> downloadTenXMLs(String url, MongoDBHandler dbHandler){
        List<String> xmlNameList = new ArrayList<>();
        int cnt =0;
        try {

            Document siteHTML = Jsoup.connect(url).get();
            Element tbodyElement = siteHTML.getElementsByTag("tbody").get(0);
            Elements aElementList = tbodyElement.getElementsByTag("a");
            for(Element element : aElementList) {
                try {
                    Thread.sleep(500);
                    URL xmlURL = new URL(element.attr("abs:href"));
                    String fileName = getFileName(element.attr("abs:href")) + ".xml";  // like 19123-data.xml
                    String fileIndex = fileName.split("-")[0];
                    if(!dbHandler.doesExist("protocol", fileIndex)) {
                        File xmlFile = new File("XMLResources/Protocols/" + fileName);
                        xmlNameList.add(fileName);
                        System.out.println("Downloading XML from " + xmlURL + " at " + xmlFile);
                        FileUtils.copyURLToFile(xmlURL, xmlFile);
                        cnt++;
                    }
                    else {
                        System.out.println("XML file " + fileName + " already exists!");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return xmlNameList;
    }

    /**
     * Method to get the Filename from URL
     * @param url
     * @return String
     * @author Jiayu Ma(implemented)
     */
    private static String getFileName(String url){
        Matcher regexMatcher = Pattern.compile(".*/(.*?)\\.xml").matcher(url);
        regexMatcher.find();
        String fileName = regexMatcher.group(1);
        return fileName;
    }

    /**
     * check the Website if there is no more files
     * @param url url to a website
     * @return true means error, false means no error
     * @author Jiayu Ma(implemented)
     */
    private static boolean checkSiteError(String url){
        try {
            Thread.sleep(500);
            Document siteHTML = Jsoup.connect(url).get();
            Elements elementList = siteHTML.getElementsByClass("col-xs-12 bt-slide-error bt-slide");
            return elementList.size()<1;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * delete XMLs
     * @author Jiayu Ma(implemented)
     */
    public static void deleteXMLResourcesFolder(File directory){
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteXMLResourcesFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        directory.delete();
    }

}