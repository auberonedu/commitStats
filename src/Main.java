import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        // create scanner and get the csv file from the user
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the CSV filename: ");
        //String f = s.nextLine();

        List<Map<String, String>> data = parseCSV(s.nextLine());

        
        // creates new map containing the lists of maps with the same id
        Map<String, List<Map<String, String>>> mp2 = new HashMap<>();
        // loops through list of maps
        for (Map<String, String> d : data) {
            //Saves id into a string from d 
            String id = d.get("id");
            //Creates a new list map called lst and gets the key value
            List<Map<String, String>> lst = mp2.get(id);
            // if there isn't a lst, it create the list
            if (lst == null) {
                lst = new ArrayList<>();
                // adds the list into the map
                mp2.put(id, lst);
            }
            // adds the data
            lst.add(d);
        }

        // gets count of forks and displays to user
        int count = mp2.size();

        System.out.println("There are " + count + " forks available (fork1 to fork" + count + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String input = s.nextLine();

        // creates a list of maps for the selection (all of the forks)
        List<Map<String, String>> selection;

        // if user picks "all" get all the data
        if (input.equalsIgnoreCase("all")) {
            selection = data;
        } 
        // otherwise select the fork from the id
        else {
            String id = "fork" + input; 
            selection = mp2.get(id);
        }

        int size = selection.size();

        DateTimeFormatter f1 = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime lat = null;
        for (Map<String, String> d : selection) {
            LocalDateTime t = LocalDateTime.parse(d.get("tm"), f1); 
            if (lat == null || t.isAfter(lat)) {
                lat = t;
            }
        }
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String latT = lat.format(f2);

        double tot = 0.0;
        int tlc = 0;
        for (Map<String, String> d : selection) {
            int lc = Integer.parseInt(d.get("chg"));
            tot += lc;
            tlc += lc;
        }
        double avg = tot / size;

        int mx = Integer.MIN_VALUE;
        int mn = Integer.MAX_VALUE;
        for (Map<String, String> d : selection) {
            int chg = Integer.parseInt(d.get("chg"));
            if (chg > mx) {
                mx = chg;
            }
            if (chg < mn) {
                mn = chg;
            }
        }

        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + size);
        System.out.println("Most recent commit timestamp: " + latT);
        System.out.printf("Average lines changed per commit: %.2f\n", avg);
        System.out.println("Total lines changed across all commits: " + tlc);
        System.out.println("Max lines changed in a commit: " + mx);
        System.out.println("Min lines changed in a commit: " + mn);

        s.close();
    }

    public static List<Map<String, String>> parseCSV(String filename){
         // create a list of maps called dta
         List<Map<String, String>> data = new ArrayList<>();

         // iterate through the csv file
         try (Scanner fs = new Scanner(new File(filename))) {
             fs.nextLine();
 
             while (fs.hasNextLine()) {
                 //split each line by comma and put in string array
                 String[] v = fs.nextLine().split(",");
 
                 // get the integer from the 3rd position in the line
                 int chg = Integer.parseInt(v[2]);  
 
                 // each map is a line in the file
                 // add map line to the list
                 Map<String, String> lineMap = new HashMap<>();
                 lineMap.put("id", v[0]);  
                 lineMap.put("tm", v[1]);  
                 lineMap.put("chg", String.valueOf(chg));
                 data.add(lineMap);
             }
         } catch (FileNotFoundException e) {
             System.out.println("Error reading the file: " + e.getMessage());
             return null;
         }
         return data; 

}
