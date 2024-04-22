package org.ppr.abschlussprojekt.helper.NLPHelper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads out a csv file and maps the ddc category number to their actual category
 * @author Kevin Schuff
 */
public class CategoriesMapper {
    private List<String> categories;

    /**
     * This method creates a list with all ddc categories, with their index in the list beeing their corresponding category number
     * @author Kevin Schuff
     */
    public CategoriesMapper(){
        // reading in csv file with ddc categories
        List<String> categories = new ArrayList<>();
        String line = "";
        try {
            // get CSV File from Resource folder
            URL csvPath = CategoriesMapper.class.getClassLoader().getResource("ddc3-names-de.csv");
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvPath.getFile()));
            // https://www.youtube.com/watch?v=f1fQwd06zRs
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split("\t");
                // some lines in csv are empty
                if (line.length() > 0) {
                    categories.add(values[1]);
                }
            }
            this.categories = categories;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("CSV File was not found");
        }
    }

    /**
     * This method returns the List of all categories
     * @return categories
     * @author Kevin Schuff
     */
    public List<String> getCategories(){
        return this.categories;
    }

    /**
     * This method returns the corresponding category of a category number
     * @param search
     * @return category
     * @author Kevin Schuff
     */
    public String getCategory(String search){
        // __label_ddc__N , gets N in categories List
        String labelNumber = search.substring(search.length() - 3);
        return this.categories.get(Integer.parseInt(labelNumber));
    }

}
