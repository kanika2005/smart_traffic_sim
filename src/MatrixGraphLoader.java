import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class MatrixGraphLoader {
    public static TrafficGraphMatrix loadGraph(String intersectionFile, String roadsFile) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(intersectionFile));
            ArrayList<String> names = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) names.add(line.trim());
            reader.close();

            TrafficGraphMatrix graph = new TrafficGraphMatrix(names.toArray(new String[0]));

            reader = new BufferedReader(new FileReader(roadsFile));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(" "); // Updated to split by spaces
                if (parts.length != 3) {
                    System.out.println("Invalid road entry: " + line);
                    continue;
                }

                String from = parts[0];
                String to = parts[1];
                int distance = Integer.parseInt(parts[2]);

                if (!names.contains(from) || !names.contains(to)) {
                    System.out.println("Invalid intersection in road entry: " + line);
                    continue;
                }

                graph.addOrUpdateRoad(from, to, distance);
            }
            reader.close();
            return graph;
        } catch (Exception e) {
            System.out.println("Error loading graph: " + e.getMessage());
            return null;
        }
    }
}