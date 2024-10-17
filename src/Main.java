import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {

        //Instantiating new scanner
        Scanner s = new Scanner(System.in);

        //Ask user for the file name
        System.out.print("Enter the CSV filename: ");
        //variable f stores the input from the user
        String f = s.nextLine();

        
        //List of maps created to store commit info
        List<Map<String, String>> data = new ArrayList<>();
        try (Scanner fs = new Scanner(new File(f))) {
            fs.nextLine();

            while (fs.hasNextLine()) {
                String[] v = fs.nextLine().split(",");

                int chg = Integer.parseInt(v[2]);  

                Map<String, String> mp2 = new HashMap<>();
                mp2.put("id", v[0]);  
                mp2.put("tm", v[1]);  
                mp2.put("chg", String.valueOf(chg));
                data.add(mp2);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error reading the file: " + e.getMessage());
            s.close();
            return;
        }

        Map<String, List<Map<String, String>>> forkCommits = new HashMap<>();
        for (Map<String, String> d : data) {
            String id = d.get("id");
            List<Map<String, String>> lst = forkCommits.get(id);
            if (lst == null) {
                lst = new ArrayList<>();
                forkCommits.put(id, lst);
            }
            lst.add(d);
        }
        int cnt = forkCommits.size();

        System.out.println("There are " + cnt + " forks available (fork1 to fork" + cnt + ").");
        System.out.print("Enter the fork number to analyze (or 'all' for all forks): ");
        String inp = s.nextLine();

        List<Map<String, String>> sel;
        if (inp.equalsIgnoreCase("all")) {
            sel = data;
        } else {
            String id = "fork" + inp; 
            sel = forkCommits.get(id);
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

        //Print Results
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
