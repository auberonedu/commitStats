import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create a scanner named "s"
        Scanner s = new Scanner(System.in);

        // Store user input in String "f" (this is the FILE NAME)
        System.out.print("Enter the CSV filename: ");
        String f = s.nextLine();

        // Create a List containing a Map (containing String key-value pairs) called "allUserCommits"
        List<Map<String, String>> allUserCommits = new ArrayList<>();
        // Create a new scanner ("fs" to read the FILE NAME stored in "f"
        try (Scanner fs = new Scanner(new File(f))) {
            // cycle through lines of file
            fs.nextLine();

            // For each line of content in FILE ...
            while (fs.hasNextLine()) {
                // ...declare an array ("v") and populate it with contents of line delimited by commas
                String[] v = fs.nextLine().split(",");

                // Convert the LinesChanged String into an integer
                int numLinesChanged = Integer.parseInt(v[2]);  

                // Create a new map ("eachCommitData") and populate with String data from array "v" and the integer at "numLinesChanged"
                Map<String, String> eachCommitData = new HashMap<>();
                eachCommitData.put("id", v[0]);  // first item in array "v", represents forkID
                eachCommitData.put("tm", v[1]);  // second item in array "v", represents push time
                eachCommitData.put("numLinesChanged", String.valueOf(numLinesChanged)); // represents number of lines in push
                allUserCommits.add(eachCommitData); // add String map "eachCommitData" to list "allUserCommits"
            }
            // try-catch error if file is not found
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            s.close();
            return;
        }

        // Creating a Map comprised of a Key String and a List Value comprised of a Map of Strings named "mp2"
        Map<String, List<Map<String, String>>> mp2 = new HashMap<>();

        // Iterating through the Map called "allUserCommits" that was created on line 17
        for (Map<String, String> d : allUserCommits) {
            // for each item in the map - we are getting the value of the Key: ID
            String id = d.get("id");

            //Creating a new list comprised of a Map named "lst" and we
            List<Map<String, String>> lst = mp2.get(id);
            if (lst == null) {
                lst = new ArrayList<>();
                mp2.put(id, lst);
            }
            lst.add(d);
        }
        int cnt = mp2.size();

        System.out.println("There are " + cnt + " forks available (fork1 to fork" + cnt + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = s.nextLine();

        List<Map<String, String>> sel;
        if (inp.equalsIgnoreCase("all")) {
            sel = allUserCommits;
        } else {
            String id = "fork" + inp; 
            sel = mp2.get(id);
        }

        int sz = sel.size();

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

        double tot = 0.0;
        int tlc = 0;
        for (Map<String, String> d : sel) {
            int lc = Integer.parseInt(d.get("numLinesChanged"));
            tot += lc;
            tlc += lc;
        }
        double avg = tot / sz;

        int mx = Integer.MIN_VALUE;
        int mn = Integer.MAX_VALUE;
        for (Map<String, String> d : sel) {
            int numLinesChanged = Integer.parseInt(d.get("numLinesChanged"));
            if (numLinesChanged > mx) {
                mx = numLinesChanged;
            }
            if (numLinesChanged < mn) {
                mn = numLinesChanged;
            }
        }

        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + sz);
        System.out.println("Most recent commit timestamp: " + latT);
        System.out.printf("Average lines changed per commit: %.2f\n", avg);
        System.out.println("Total lines changed across all commits: " + tlc);
        System.out.println("Max lines changed in a commit: " + mx);
        System.out.println("Min lines changed in a commit: " + mn);

        s.close();
    }
}
