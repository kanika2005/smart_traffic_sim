import java.util.*;

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
    }

    // Add an intersection to the graph
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
        System.out.println("Added intersection: " + name);
    }

    // Get the index of a node by its name
    public int getNodeIndex(String name) {
        if (!nodeIndex.containsKey(name)) {
            throw new IllegalArgumentException("Intersection does not exist: " + name);
        }
        return nodeIndex.get(name);
    }

    // Get the number of nodes in the graph
    public int size() {
        return nodes.size();
    }

    // Get the distance between two nodes
    public int getDistance(int from, int to) {
        return matrix[from][to];
    }

    // Add or update a road in the graph
    public void addOrUpdateRoad(String from, String to, int distance) {
        if (!nodeIndex.containsKey(from) || !nodeIndex.containsKey(to)) {
            System.out.println("One or both intersections do not exist: " + from + ", " + to);
            return;
        }
        int fromIndex = nodeIndex.get(from);
        int toIndex = nodeIndex.get(to);
        matrix[fromIndex][toIndex] = distance;
        System.out.println("Added/Updated road from " + from + " to " + to + " with distance " + distance);
    }

    // Print all intersections
    public void printIntersections() {
        System.out.println("Intersections: " + nodes);
    }

    // Print the adjacency matrix
    public void printMatrix() {
        System.out.println("Adjacency Matrix:");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }
}