public class DijkstraPathFinder {
    public static int[] findShortestPath(TrafficGraphMatrix graph, int src, int dest) {
        int n = graph.size();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
        }
        dist[src] = 0;

        for (int i = 0; i < n; i++) {
            int u = -1;
            for (int j = 0; j < n; j++) {
                if (!visited[j] && (u == -1 || dist[j] < dist[u])) u = j;
            }

            if (dist[u] == Integer.MAX_VALUE) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                int weight = graph.getDistance(u, v);
                if (weight > 0 && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                }
            }
        }

        int[] path = new int[n];
        int idx = 0;
        for (int at = dest; at != -1; at = prev[at]) path[idx++] = at;

        int[] result = new int[idx];
        for (int i = 0; i < idx; i++) result[i] = path[idx - i - 1];
        return (dist[dest] == Integer.MAX_VALUE) ? new int[0] : result;
    }

    public static int getPathCost(TrafficGraphMatrix graph, int[] path) {
        int cost = 0;
        for (int i = 0; i < path.length - 1; i++) {
            cost += graph.getDistance(path[i], path[i + 1]);
        }
        return cost;
    }
}