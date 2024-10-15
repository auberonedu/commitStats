import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        // Asking the user to enter the CSV filename and store their input
        System.out.print("Enter the CSV filename: ");
        String f = s.nextLine();
        // Creating an ArrayList and a HashMap 
        List<Map<String, String>> dataList = new ArrayList<>();
        try (Scanner fs = new Scanner(new File(f))) {
            fs.nextLine();

            while (fs.hasNextLine()) {
                String[] v = fs.nextLine().split(",");

                int chg = Integer.parseInt(v[2]);  

                Map<String, String> commitData = new HashMap<>();
                commitData.put("id", v[0]);  
                commitData.put("tm", v[1]);  
                commitData.put("chg", String.valueOf(chg));
                dataList.add(commitData);
            }
          // Catching the error if the file is not found and close the scanner 
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            s.close();
            return;
        }
        // Creating a Hashmap and iterating through the data to get the id of the fork
        Map<String, List<Map<String, String>>> mp2 = new HashMap<>();
        for (Map<String, String> d : dataList) {
            String id = d.get("id");
            List<Map<String, String>> lst = mp2.get(id);
            if (lst == null) {
                lst = new ArrayList<>();
                mp2.put(id, lst);
            }
            lst.add(d);
        }
        // Counting the total number of forks
        int cnt = mp2.size();
    
        System.out.println("There are " + cnt + " forks available (fork1 to fork" + cnt + ").");
        // Statement for the user to choose through the menu to analyze the/all forks
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = s.nextLine();
        // Selects commits based on the user input
        List<Map<String, String>> sel;
        if (inp.equalsIgnoreCase("all")) {
            sel = dataList;
        } else {
            String id = "fork" + inp; 
            sel = mp2.get(id);
        }

        int sz = sel.size();
        // Commit Time Stamps-Format
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
        // Calculating the total lines changed
        double tot = 0.0;
        int tlc = 0;
        for (Map<String, String> d : sel) {
            int lc = Integer.parseInt(d.get("chg"));
            tot += lc;
            tlc += lc;
        }
        // Calculating average lines per commit
        double avg = tot / sz;
        // Finding the max and the minimum lines updated in the commit 
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
        // Statistics from the program
        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + sz); 
        System.out.println("Most recent commit timestamp: " + latT);
        System.out.printf("Average lines changed per commit: %.2f\n", avg);
        System.out.println("Total lines changed across all commits: " + tlc);
        System.out.println("Max lines changed in a commit: " + mx);
        System.out.println("Min lines changed in a commit: " + mn);
        // Close the scanner 
        s.close();
    }
    // Helper method 
    public static List<Map<String, String>> parseCSV(String filename) {
        
    }
}
