package org.ppr.abschlussprojekt.helper.XMLHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jiayu Ma
 * Helper to read file
 */
public class XMLFileReader {

    /**
     * Read and analyse the XML files. It accepts both the folder path and the file path
     * @param folder A path, which has .xml.
     * @return result.
     */
    public static List<File> findXMLFiles(File folder) {
        List<File> result = new ArrayList<File>();
        if (folder.isFile() && checkExtension(folder, ".xml")){  //folder ist eine einzelne xml datei
            result.add(folder);
            return result;
        }
        if (folder.listFiles() == null) {  //folder ist leer
            return result;
        }
        //folder ist eine Directory
        LinkedList<File> queue = new LinkedList<>();  //erstellen eine warteschlange dann breitensuche alle xml datei unter diese folder
        queue.addAll(Arrays.asList(folder.listFiles()));  //return alle datei unter folder als list
        while (!queue.isEmpty()) {
            File item = queue.removeFirst();
            if (item.isDirectory())
                queue.addAll(Arrays.asList(item.listFiles()));
            boolean check = checkExtension(item, ".xml");
            if (item.isFile() && check) {
                result.add(item);
            }
        }
        return result;
    }

    /**
     * Check if the file has the extension .xml.
     * @param file The file to check.
     * @param extension The correct extension of the file.
     * @return check.
     */
    public static boolean checkExtension(File file, String extension) {
        boolean check = false;
        try {
            String extensionToCheck = "";
            String name = file.getName();
            extensionToCheck = name.substring(name.lastIndexOf("."));
            if (extensionToCheck.equals(extension)) {  //vergleiche datei. mit extension(xml)
                check = true;
            }
        }
        catch (Exception e) {
            extension = "";
        }
        return check;
    }

    public static void main(String[] args) {
        String path = "/Users/majiayu/MyDesktop/WS2223_3_Semester/2_Programmierpraktikum/2_Übungsblatt/blatt1/XMLResources";
        File folder = new File(path);
        List<File> files = findXMLFiles(folder);
        int a=0;
        for(File i:files) {
            System.out.println(i);
            a++;
        }
        System.out.println(a);
        System.out.println(files.isEmpty());
    }
}
