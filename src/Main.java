import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        // Initializing scanner, taking input on the filename
        Scanner s = new Scanner(System.in);

        System.out.print("Enter the CSV filename: ");
        String f = s.nextLine();

        // call parseCSV method to get all data
        List<Map<String, String>> allData = parseCSV(f);

        // if allData empty then the file was not found or had problem
        if (allData.isEmpty()) {
            System.out.println("No data found or error reading the file.");
            s.close();
            return;
        }

        // grouping data by id
        Map<String, List<Map<String, String>>> mp2 = new HashMap<>();
        for (Map<String, String> d : allData) {
            String id = d.get("id");
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
            sel = allData;
        } else {
            String id = "fork" + inp;
            sel = mp2.get(id);
        }

        int sz = sel.size();

        // format timestamp
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

        // manipulating chg data
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

        // printing out report of requested forks
        System.out.println("\nStatistics:");
        System.out.println("Number of commits: " + sz);
        System.out.println("Most recent commit timestamp: " + latT);
        System.out.printf("Average lines changed per commit: %.2f\n", avg);
        System.out.println("Total lines changed across all commits: " + tlc);
        System.out.println("Max lines changed in a commit: " + mx);
        System.out.println("Min lines changed in a commit: " + mn);

        s.close();
    }

    public static List<Map<String, String>> parseCSV(String f) {
        List<Map<String, String>> allData = new ArrayList<>();
        try (Scanner fs = new Scanner(new File(f))) {
            // Opens file in scanner, iterates across, initializes ArrayList, coalescses
            // stats into a hashmap, then places in an array list
            fs.nextLine();

            while (fs.hasNextLine()) {
                String[] v = fs.nextLine().split(",");

                int chg = Integer.parseInt(v[2]);
                // one HashMap = one commit
                Map<String, String> valueMap = new HashMap<>();
                valueMap.put("id", v[0]);
                valueMap.put("tm", v[1]);
                valueMap.put("chg", String.valueOf(chg));
                allData.add(valueMap);
            }
        }

        catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
        return allData;
    }
}