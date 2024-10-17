import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    void testParseCSV() {
      // You will finish implementing this method in Wave 3
      // TODO: Call Main.parseCSV("data/small_commit_data.csv") here and set it to an actual variable

      // Call Main.parseCSV 
      List<Map<String, String>> actualCommits = Main.parseCSV("data/small_commit_data.csv");

      // Sets up the expected value for you. You do not need to edit this part
      List<Map<String, String>> expectedCommits = new ArrayList<>();

      Map<String, String> commit1 = new HashMap<>();
      commit1.put("chg", "179");
      commit1.put("tm", "2024-10-15T08:02:04Z");
      commit1.put("id", "fork1");
      expectedCommits.add(commit1);

      Map<String, String> commit2 = new HashMap<>();
      commit2.put("chg", "1");
      commit2.put("tm", "2024-10-12T19:48:59Z");
      commit2.put("id", "fork2");
      expectedCommits.add(commit2);

      Map<String, String> commit3 = new HashMap<>();
      commit3.put("chg", "13");
      commit3.put("tm", "2024-10-10T21:56:18Z");
      commit3.put("id", "fork2");
      expectedCommits.add(commit3);
      // Finish expected value setup

      // TODO: Assert that the expected equals the actual
      assertEquals(expectedCommits, actualCommits);
    }
}
