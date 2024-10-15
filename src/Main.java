import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Create a scanner named "s"
        Scanner scan = new Scanner(System.in);

        // Store user input in String "f" (this is the FILE NAME)
        System.out.print("Enter the CSV filename: ");
        String fileName = scan.nextLine();

        // Create a List containing a Map (containing String key-value pairs) called "allUserCommits"
        List<Map<String, String>> allUserCommits = new ArrayList<>();
        // Create a new scanner ("fs" to read the FILE NAME stored in "f"
        try (Scanner fileScan = new Scanner(new File(fileName))) {
            // cycle through lines of file
            fileScan.nextLine();

            // For each line of content in FILE ...
            while (fileScan.hasNextLine()) {
                // ...declare an array ("v") and populate it with contents of line delimited by commas
                String[] dataItems = fileScan.nextLine().split(",");

                // Convert the LinesChanged String into an integer
                int numLinesChanged = Integer.parseInt(dataItems[2]);  

                // Create a new map ("eachCommitData") and populate with String data from array "v" and the integer at "numLinesChanged"
                Map<String, String> eachCommitData = new HashMap<>();
                eachCommitData.put("id", dataItems[0]);  // forkID
                eachCommitData.put("timeStamp", dataItems[1]);  // push time
                eachCommitData.put("numLinesChanged", String.valueOf(numLinesChanged)); // number of lines in push
                allUserCommits.add(eachCommitData); // add String map "eachCommitData" to list "allUserCommits"
            }
            // try-catch error if file is not found
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            scan.close();
            return;
        }

        // Creating a Map comprised of a Key String and a List Value comprised of a Map of Strings named "mp2"
        Map<String, List<Map<String, String>>> mp2 = new HashMap<>();

        // Iterating through the Map called "allUserCommits" that was created on line 17
        for (Map<String, String> eachCommit : allUserCommits) {
            // for each item in the map - we are getting the value of the Key: ID
            String id = eachCommit.get("id");

            //Creating a new list comprised of a Map named "lst" and we
            List<Map<String, String>> lst = mp2.get(id);
            if (lst == null) {
                lst = new ArrayList<>();
                mp2.put(id, lst);
            }
            lst.add(eachCommit);
        }

        // store size of map in "numberOfForks"
        int numberOfForks = mp2.size();

        // print output
        System.out.println("There are " + numberOfForks + " forks available (fork1 to fork" + numberOfForks + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = scan.nextLine();

        // if user enters "all", display whole list, else get list from map
        List<Map<String, String>> dataFromFork;
        if (inp.equalsIgnoreCase("all")) {
            dataFromFork = allUserCommits;
        } else {
            String id = "fork" + inp; 
            dataFromFork = mp2.get(id);
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
