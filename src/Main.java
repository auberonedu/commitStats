import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Creating a scanner object to read in the  user input
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the CSV filename: ");
        String f = s.nextLine();

        parseCSV(f);

        //  Filters through the original map and adds a new list to a second map
        // along with the id
        Map<String, List<Map<String, String>>> mp2 = new HashMap<>();
        for (Map<String, String> d : forkList) {
            String id = d.get("id");
            List<Map<String, String>> lst = mp2.get(id);
            if (lst == null) {
                lst = new ArrayList<>();
                mp2.put(id, lst);
            }
            lst.add(d);
        }
        // checks the size of the second map
        int cnt = mp2.size();

        // Lets the user know how many forks are available from the file
        System.out.println("There are " + cnt + " forks available (fork1 to fork" + cnt + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = s.nextLine();

        // initializes an empty list 
        // checks to see if the user entered all or a # 
        // routes them to to the right fork #
        List<Map<String, String>> sel;
        if (inp.equalsIgnoreCase("all")) {
            sel = forkList;
        } else {
            String id = "fork" + inp; 
            sel = mp2.get(id);
        }

        int sz = sel.size();

        // formats the date and time
        DateTimeFormatter f1 = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime lat = null;
        for (Map<String, String> d : sel) {
            LocalDateTime t = LocalDateTime.parse(d.get("tm"), f1); 
            if (lat == null || t.isAfter(lat)) {
                lat = t;
            }
        }
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String latT = lat.format(f2);

        // perform some basic math to grab stats
        double tot = 0.0;
        int tlc = 0;
        for (Map<String, String> d : sel) {
            int lc = Integer.parseInt(d.get("chg"));
            tot += lc;
            tlc += lc;
        }
        double avg = tot / sz;

        int mx = Integer.MIN_VALUE;
        int mn = Integer.MAX_VALUE;
        for (Map<String, String> d : sel) {
            int chg = Integer.parseInt(d.get("chg"));
            if (chg > mx) {
                mx = chg;
            }
            if (chg < mn) {
                mn = chg;
            }
        }

        // showcases those stats
        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + sz);
        System.out.println("Most recent commit timestamp: " + latT);
        System.out.printf("Average lines changed per commit: %.2f\n", avg);
        System.out.println("Total lines changed across all commits: " + tlc);
        System.out.println("Max lines changed in a commit: " + mx);
        System.out.println("Min lines changed in a commit: " + mn);

        s.close();
    }
    public static List<Map<String, String>> parseCSV(String filename){

        // Creates a scanner object that reads a file name
        // adds contents from file into a map
        List<Map<String, String>> forkList = new ArrayList<>();
        try (Scanner fs = new Scanner(new File(f))) {
            fs.nextLine();
            Map<String, String> indiForkMap = new HashMap<>();
            // 
            while (fs.hasNextLine()) {
                // Read through string and split at each comma, adding
                // to a string array
                String[] v = fs.nextLine().split(",");

                int chg = Integer.parseInt(v[2]);  

                // Adding all three elements to a map with the specific
                // key added for each type of variable
                
                indiForkMap.put("id", v[0]);  
                indiForkMap.put("tm", v[1]);  
                indiForkMap.put("chg", String.valueOf(chg));
                forkList.add(indiForkMap);
            }
            // Throws a file not found exception if the file does not exist
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            s.close();
            return null;
        }
        return forkList;
    }
}
