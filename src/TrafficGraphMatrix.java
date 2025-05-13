import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TrafficGraphMatrix {
    private int[][] matrix;
    private Map<String, Integer> nodeIndex;
    private List<String> nodes;

    public TrafficGraphMatrix(String[] intersections) {
        nodes = new ArrayList<>(Arrays.asList(intersections));
        nodeIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndex.put(nodes.get(i), i);
        }
        matrix = new int[nodes.size()][nodes.size()];

        // Load roads from roads.txt and populate the adjacency matrix
        try (BufferedReader reader = new BufferedReader(new FileReader("data/roads.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3) {
                    String from = parts[0];
                    String to = parts[1];
                    int distance = Integer.parseInt(parts[2]);

                    if (nodeIndex.containsKey(from) && nodeIndex.containsKey(to)) {
                        int fromIndex = nodeIndex.get(from);
                        int toIndex = nodeIndex.get(to);
                        matrix[fromIndex][toIndex] = distance;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading roads: " + e.getMessage());
        }
    }

    public void addIntersection(String name) {
        if (nodeIndex.containsKey(name)) {
            System.out.println("Intersection already exists: " + name);
            return;
        }
        nodes.add(name);
        nodeIndex.put(name, nodes.size() - 1);

        // Resize the matrix
        int newSize = nodes.size();
        int[][] newMatrix = new int[newSize][newSize];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix[i].length);
        }
        matrix = newMatrix;

        // Save to intersections.txt
        try (FileWriter writer = new FileWriter("data/intersections.txt", true)) {
            writer.write(name + "\n");
        } catch (IOException e) {
            System.out.println("Error saving intersection: " + e.getMessage());
        }

        System.out.println("Added intersection: " + name);
    }

    public int getNodeIndex(String name) {
        if (!nodeIndex.containsKey(name)) {
            throw new IllegalArgumentException("Intersection does not exist: " + name);
        }
        return nodeIndex.get(name);
    }

    public int size() {
        return nodes.size();
    }

    public String getNodeName(int index) {
        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException("Invalid node index: " + index);
        }
        return nodes.get(index);
    }

    public int getDistance(int from, int to) {
        return matrix[from][to];
    }

    public void addOrUpdateRoad(String from, String to, int distance) {
        if (!nodeIndex.containsKey(from) || !nodeIndex.containsKey(to)) {
            System.out.println("One or both intersections do not exist: " + from + ", " + to);
            return;
        }
        int fromIndex = nodeIndex.get(from);
        int toIndex = nodeIndex.get(to);
        matrix[fromIndex][toIndex] = distance;

        // Save to roads.txt
        try {
            BufferedReader reader = new BufferedReader(new FileReader("data/roads.txt"));
            List<String> lines = reader.lines().collect(Collectors.toList());
            reader.close();

            boolean updated = false;
            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(" ");
                if (parts[0].equals(from) && parts[1].equals(to)) {
                    lines.set(i, from + " " + to + " " + distance);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                lines.add(from + " " + to + " " + distance);
            }

            FileWriter writer = new FileWriter("data/roads.txt");
            for (String line : lines) {
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving road: " + e.getMessage());
        }

        System.out.println("Added/Updated road from " + from + " to " + to + " with distance " + distance);
    }

    public void printIntersections() {
        System.out.println("Intersections: " + nodes);
    }
    public void deleteIntersection(String name) {
    if (!nodeIndex.containsKey(name)) {
        System.out.println("Intersection does not exist: " + name);
        return;
    }

    // Get the index of the node to be deleted
    int indexToRemove = nodeIndex.get(name);

    // Remove the node from the list and map
    nodes.remove(indexToRemove);
    nodeIndex.clear();
    for (int i = 0; i < nodes.size(); i++) {
        nodeIndex.put(nodes.get(i), i);
    }

    // Update the adjacency matrix
    int newSize = nodes.size();
    int[][] newMatrix = new int[newSize][newSize];
    for (int i = 0, newI = 0; i < matrix.length; i++) {
        if (i == indexToRemove) continue; // Skip the row to be deleted
        for (int j = 0, newJ = 0; j < matrix[i].length; j++) {
            if (j == indexToRemove) continue; // Skip the column to be deleted
            newMatrix[newI][newJ] = matrix[i][j];
            newJ++;
        }
        newI++;
    }
    matrix = newMatrix;

    // Update intersections.txt
    try (FileWriter writer = new FileWriter("data/intersections.txt")) {
        for (String node : nodes) {
            writer.write(node + "\n");
        }
    } catch (IOException e) {
        System.out.println("Error updating intersections file: " + e.getMessage());
    }

    System.out.println("Deleted intersection: " + name);
}
public void deleteRoad(String from, String to) {
    if (!nodeIndex.containsKey(from) || !nodeIndex.containsKey(to)) {
        System.out.println("One or both intersections do not exist: " + from + ", " + to);
        return;
    }

    int fromIndex = nodeIndex.get(from);
    int toIndex = nodeIndex.get(to);

    // Remove the road from the adjacency matrix
    matrix[fromIndex][toIndex] = 0;

    // Update roads.txt
    try {
        BufferedReader reader = new BufferedReader(new FileReader("data/roads.txt"));
        List<String> lines = reader.lines().collect(Collectors.toList());
        reader.close();

        // Remove the road from the file
        lines.removeIf(line -> {
            String[] parts = line.split(" ");
            return parts.length == 3 && parts[0].equals(from) && parts[1].equals(to);
        });

        FileWriter writer = new FileWriter("data/roads.txt");
        for (String line : lines) {
            writer.write(line + "\n");
        }
        writer.close();
    } catch (IOException e) {
        System.out.println("Error updating roads file: " + e.getMessage());
    }

    System.out.println("Deleted road from " + from + " to " + to);
}
    public void printMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}