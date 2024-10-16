import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class MainTest {
    @Test
    void testParseCSV() {
      // You will finish implementing this method in Wave 3
      // TODO: Call Main.parseCSV("data/small_commit_data.csv") here and set it to an actual variable

      List<Map<String, String>> test = Main.parseCSV("data/small_commit_data.csv");


      // Sets up the expected value for you. You do not need to edit this part
      List<Map<String, String>> expectedCommits = new ArrayList<>();

      Map<String, String> commit1 = new HashMap<>();
      commit1.put("numLinesChanged", "179");
      commit1.put("timeStamp", "2024-10-15T08:02:04Z");
      commit1.put("id", "fork1");
      expectedCommits.add(commit1);

      Map<String, String> commit2 = new HashMap<>();
      commit2.put("numLinesChanged", "1");
      commit2.put("timeStamp", "2024-10-12T19:48:59Z");
      commit2.put("id", "fork2");
      expectedCommits.add(commit2);

      Map<String, String> commit3 = new HashMap<>();
      commit3.put("numLinesChanged", "13");
      commit3.put("timeStamp", "2024-10-10T21:56:18Z");
      commit3.put("id", "fork2");
      expectedCommits.add(commit3);
      // Finish expected value setup

      // TODO: Assert that the expected equals the actual
      assertEquals(expectedCommits, test);
    }
}
