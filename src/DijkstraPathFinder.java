import java.util.Arrays;

public class DijkstraPathFinder {
    public static int[] findShortestPath(TrafficGraphMatrix graph, int src, int dest) {
        int n = graph.size();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n];

        // Initialize distances and previous nodes
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[src] = 0;

        // Dijkstra's algorithm
        for (int i = 0; i < n; i++) {
            int u = -1;

            // Find the unvisited node with the smallest distance
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || dist[j] < dist[u])) {
                    u = j;
                }
            }

            // If no reachable node is found, break
            if (u == -1 || dist[u] == Integer.MAX_VALUE) break;

            visited[u] = true;

            // Update distances to neighboring nodes
            for (int v = 0; v < n; v++) {
                int weight = graph.getDistance(u, v);
                if (weight > 0 && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                }
            }
        }

        // Check if the destination is unreachable
        if (dist[dest] == Integer.MAX_VALUE) {
            System.out.println("No path exists from " + graph.getNodeName(src) + " to " + graph.getNodeName(dest));
            return new int[0];
        }

        // Reconstruct the path from src to dest
        int[] path = new int[n];
        int idx = 0;
        for (int at = dest; at != -1; at = prev[at]) {
            path[idx++] = at;
        }

        // Reverse the path to get the correct order
        int[] result = new int[idx];
        for (int i = 0; i < idx; i++) {
            result[i] = path[idx - i - 1];
        }

        // Print and return the result
        System.out.println("Path found from " + graph.getNodeName(src) + " to " + graph.getNodeName(dest) + ": " + Arrays.toString(result));
        return result;
    }

    public static int getPathCost(TrafficGraphMatrix graph, int[] path) {
        if (path.length == 0) {
            System.out.println("No valid path to calculate cost.");
            return -1;
        }

        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += graph.getDistance(path[i], path[i + 1]);
        }
        return cost;
    }
}