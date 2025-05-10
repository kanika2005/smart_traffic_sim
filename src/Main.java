import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load the graph
        TrafficGraphMatrix graph = MatrixGraphLoader.loadGraph("data/intersections.txt", "data/roads.txt");
        if (graph == null) {
            System.out.println("Failed to load graph.");
            return;
        }

        VehicleMovementSimulator simulator = new VehicleMovementSimulator(graph);
        System.out.println("Graph loaded successfully.");
        graph.printIntersections();
        graph.printMatrix();

        // Menu loop
        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Add Intersection");
            System.out.println("2. Add/Update Road");
            System.out.println("3. Print Graph");
            System.out.println("4. Add Vehicle");
            System.out.println("5. Simulate Vehicle Movement");
            System.out.println("6. Find Shortest Path");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear newline

            switch (choice) {
                case 1:
                    System.out.print("Enter intersection name: ");
                    String intersection = scanner.nextLine();
                    graph.addIntersection(intersection);
                    break;

                case 2:
                    System.out.print("Enter road (from,to,distance): ");
                    String[] road = scanner.nextLine().split(",");
                    if (road.length != 3) {
                        System.out.println("Invalid format. Use: from,to,distance");
                        break;
                    }
                    String from = road[0].trim();
                    String to = road[1].trim();
                    int distance = Integer.parseInt(road[2].trim());
                    graph.addOrUpdateRoad(from, to, distance);
                    break;

                case 3:
                    graph.printIntersections();
                    graph.printMatrix();
                    break;

                case 4:
                    System.out.print("Enter vehicle source and destination (source,destination): ");
                    String[] vehicleInput = scanner.nextLine().split(",");
                    if (vehicleInput.length != 2) {
                        System.out.println("Invalid format. Use: source,destination");
                        break;
                    }
                    int source = graph.getNodeIndex(vehicleInput[0].trim());
                    int destination = graph.getNodeIndex(vehicleInput[1].trim());
                    if (source == -1 || destination == -1) {
                        System.out.println("Invalid intersection name(s).");
                        break;
                    }
                    simulator.addVehicle(new Vehicle(source, destination));
                    break;

                case 5:
                    System.out.print("Enter number of simulation steps: ");
                    int steps = scanner.nextInt();
                    scanner.nextLine(); // clear newline
                    simulator.simulate(steps);
                    break;

                case 6:
                    System.out.print("Enter source and destination (source,destination): ");
                    String[] pathInput = scanner.nextLine().split(",");
                    if (pathInput.length != 2) {
                        System.out.println("Invalid format. Use: source,destination");
                        break;
                    }
                    int src = graph.getNodeIndex(pathInput[0].trim());
                    int dest = graph.getNodeIndex(pathInput[1].trim());
                    if (src == -1 || dest == -1) {
                        System.out.println("Invalid intersection name(s).");
                        break;
                    }
                    int[] path = DijkstraPathFinder.findShortestPath(graph, src, dest);
                    int cost = DijkstraPathFinder.getPathCost(graph, path);
                    System.out.println("Shortest Path: " + Arrays.toString(path));
                    System.out.println("Path Cost: " + cost);
                    break;

                case 7:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
