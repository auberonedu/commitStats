import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // instantiating new scanner object

        System.out.print("Enter the CSV filename: "); // prompting the user for file input
        String fileName = scanner.nextLine();

        List<Map<String, String>> structureMap = new ArrayList<>(); // creates a map to structure the CSV columns
        try (Scanner fileScanner = new Scanner(new File(fileName))) { 
            fileScanner.nextLine();

            while (fileScanner.hasNextLine()) {
                String[] values = fileScanner.nextLine().split(","); // seperating by commas

                int changes = Integer.parseInt(values[2]);  

                Map<String, String> fileDataMap = new HashMap<>(); // placing commit statistics into a new hashmap
                fileDataMap.put("id", values[0]);  
                fileDataMap.put("tm", values[1]);  
                fileDataMap.put("chg", String.valueOf(changes));
                structureMap.add(fileDataMap);
            }
        } catch (FileNotFoundException exception) {
            System.out.println("Error reading the file: " + exception.getMessage()); // if there is no file, throw exception
            scanner.close();
            return;
        }

        Map<String, List<Map<String, String>>> populatedMap = new HashMap<>(); // populating the mp2 hashmap with file content
        for (Map<String, String> data : structureMap) {
            String id = data.get("id");
            List<Map<String, String>> lst = populatedMap.get(id);
            if (lst == null) {
                lst = new ArrayList<>();
                populatedMap.put(id, lst);
            }
            lst.add(data);
        }
        int cnt = populatedMap.size();

        System.out.println("There are " + cnt + " forks available (fork1 to fork" + cnt + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = scanner.nextLine();

        List<Map<String, String>> sel;
        if (inp.equalsIgnoreCase("all")) {
            sel = structureMap;
        } else {
            String id = "fork" + inp; 
            sel = populatedMap.get(id);
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

        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + sz);
        System.out.println("Most recent commit timestamp: " + latT);
        System.out.printf("Average lines changed per commit: %.2f\n", avg);
        System.out.println("Total lines changed across all commits: " + tlc);
        System.out.println("Max lines changed in a commit: " + mx);
        System.out.println("Min lines changed in a commit: " + mn);

        scanner.close();
    }
}
