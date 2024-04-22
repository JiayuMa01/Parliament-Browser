package org.ppr.abschlussprojekt;

import org.ppr.abschlussprojekt.data.database.MongoDBHandler;
import org.ppr.abschlussprojekt.data.database.MongoDBHelper;
import org.ppr.abschlussprojekt.helper.InputException;
import org.ppr.abschlussprojekt.helper.XMLHelper.XMLDownloader;
import org.ppr.abschlussprojekt.helper.XMLHelper.XMLReader;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;


/**
 * A console helps to analyse and upload data from XMLs
 * @author Jiayu Ma(implemented)
 */
public class UserInterfaceConsole {

    public static void main(String[] args) throws InterruptedException, IOException {
        MongoDBHandler dbHandler = new MongoDBHandler();

        Scanner scan = new Scanner(System.in);
        String optInput = "";

        while (!optInput.equals("3")){

            Thread.sleep(1000);
            try {
                System.out.println("======================================================================");
                System.out.println("(1) Download all XMLs");
                System.out.println("(2) Upload to MongoDB");
                System.out.println("(3) Exit");
                System.out.print("-> Input: ");
                optInput = scan.nextLine();

                switch (optInput) {
                    case "1": {
                        List<String> allDownloadedXmlNameList = XMLDownloader.downloadAllXML(dbHandler);
                        System.out.println("All new XMLs have been downloaded.");
                        break;
                    }
                    case "2": {
                        // load downloaded xml files, analyse xml files, insert to db and nlp
                        MongoDBHelper.insertAllDocument(dbHandler);
                        System.out.println("All documents with NLP results have been uploaded.");
                        //System.out.println("All data without NLP results have been uploaded.");
                        break;
                    }
                    case "3": {
                        System.out.println("Exit. Bye!");
                        System.out.println("======================================================================");
                        break;
                    }
                    default:
                        throw new InputException("False input: " + "\"" + optInput + "\"");
                }

            } catch (InputException e) {
                System.err.println(e.getMessage());
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }

        }

        // the progress in collection countProgress should be reset
        dbHandler.resetCountProgress("cntUploadedSpeaker");
        dbHandler.resetCountProgress("cntUploadedSpeech");
        dbHandler.resetCountProgress("cntUploadedComment");
        dbHandler.resetCountProgress("cntDownloadedXML");
        dbHandler.resetCountProgress("cntAnalysedXML");
    }


}
