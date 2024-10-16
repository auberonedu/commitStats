import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create a scanner
        Scanner scan = new Scanner(System.in);

        // Collect filename from user
        System.out.print("Enter the CSV filename: ");
        String fileName = scan.nextLine();

        // Create a List of Maps (containing String key-value pairs) called "allCommits"
        List<Map<String, String>> allCommits = new ArrayList<>();
        // Cycle through lines of the file
        try (Scanner fileScan = new Scanner(new File(fileName))) {
            fileScan.nextLine();
            // For each line of content in FILE ...
            while (fileScan.hasNextLine()) {
                // ...create an array ("dataItems") and populate it with contents of line delimited by commas (id, timeStamp, numLinesChanged)
                String[] dataItems = fileScan.nextLine().split(",");

                // Convert the numLinesChanged from a String into an integer
                int numLinesChanged = Integer.parseInt(dataItems[2]);  

                // Create a new map ("eachCommitData") and populate with String data from array and the integer at "numLinesChanged"
                Map<String, String> eachCommitData = new HashMap<>();
                eachCommitData.put("id", dataItems[0]);  // forkID
                eachCommitData.put("timeStamp", dataItems[1]);  // push time
                eachCommitData.put("numLinesChanged", String.valueOf(numLinesChanged)); // number of lines in push

                // add this map ("eachCommitData") to the List of allCommits
                allCommits.add(eachCommitData); 
            }
        // try-catch throw error if file is not found
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            scan.close();
            return;
        }

        // Create a Map  where the key is a string id and the value is a List of Maps containing all data about commits made by that id
        Map<String, List<Map<String, String>>> commitsByID = new HashMap<>();

        // Iterating through List "allCommits," which contains Maps of eachCommitData
        // For each Map stored in the List of all commits ...
        for (Map<String, String> eachCommit : allCommits) {
            // ... retrieve the String id value stored at the key "id" (forkID)
            String id = eachCommit.get("id");

            // ... create a new List comprised of Maps and populate it with 
            // the List in commitsByID Map stored at the key matching the id
            List<Map<String, String>> listOfMapsByID = commitsByID.get(id);
            // ... if the id has not been added to the Map, add it with this list
            if (listOfMapsByID == null) {
                listOfMapsByID = new ArrayList<>();
                commitsByID.put(id, listOfMapsByID);
            }
            // ...add the Map to the List of maps by ID
            listOfMapsByID.add(eachCommit);
        }

        // store size of map in "numberOfForks"
        int numberOfForks = commitsByID.size();

        // print output
        System.out.println("There are " + numberOfForks + " forks available (fork1 to fork" + numberOfForks + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = scan.nextLine();

        // if user enters "all", display whole list, else get list from map
        List<Map<String, String>> dataFromFork;
        if (inp.equalsIgnoreCase("all")) {
            dataFromFork = allCommits;
        } else {
            String id = "fork" + inp; 
            dataFromFork = commitsByID.get(id);
        }

        int numOfCommits = dataFromFork.size();

        // Create a date formatter and use it to format date
        DateTimeFormatter f1 = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime lat = null;
        for (Map<String, String> d : dataFromFork) {
            LocalDateTime t = LocalDateTime.parse(d.get("timeStamp"), f1); 
            if (lat == null || t.isAfter(lat)) {
                lat = t;
            }
        }
        DateTimeFormatter f2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String latestTimeStamp = lat.format(f2);


        double tot = 0.0;
        int totalLinesChanged = 0;
        for (Map<String, String> d : dataFromFork) {
            int intLinesChanged = Integer.parseInt(d.get("numLinesChanged"));
            tot += intLinesChanged;
            totalLinesChanged += intLinesChanged;
        }
        double avgLinesPerCommit = tot / numOfCommits;

        int maxChangedInACommit = Integer.MIN_VALUE;
        int minChangedInACommit = Integer.MAX_VALUE;
        for (Map<String, String> d : dataFromFork) {
            int numLinesChanged = Integer.parseInt(d.get("numLinesChanged"));
            if (numLinesChanged > maxChangedInACommit) {
                maxChangedInACommit = numLinesChanged;
            }
            if (numLinesChanged < minChangedInACommit) {
                minChangedInACommit = numLinesChanged;
            }
        }

        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + numOfCommits);
        System.out.println("Most recent commit timestamp: " + latestTimeStamp);
        System.out.printf("Average lines changed per commit: %.2f\n", avgLinesPerCommit);
        System.out.println("Total lines changed across all commits: " + totalLinesChanged);
        System.out.println("Max lines changed in a commit: " + maxChangedInACommit);
        System.out.println("Min lines changed in a commit: " + minChangedInACommit);

        scan.close();
    }
}
