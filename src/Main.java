import java.util.Scanner;
import java.util.Arrays; // Import Arrays for Arrays.toString()

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load initial graph
        TrafficGraphMatrix graph = MatrixGraphLoader.loadGraph("data/intersections.txt", "data/roads.txt");
        if (graph == null) {
            System.out.println("Failed to load graph.");
            return;
        }

        VehicleMovementSimulator simulator = new VehicleMovementSimulator(graph);

        System.out.println("Graph loaded successfully.");
        graph.printIntersections();
        graph.printMatrix();

        // Interactive menu
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
            scanner.nextLine(); // Consume newline

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
                        System.out.println("Invalid input. Format: from,to,distance");
                        break;
                    }
                    String from = road[0];
                    String to = road[1];
                    int distance = Integer.parseInt(road[2]);
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
                        System.out.println("Invalid input. Format: source,destination");
                        break;
                    }
                    int source = graph.getNodeIndex(vehicleInput[0]);
                    int destination = graph.getNodeIndex(vehicleInput[1]);
                    simulator.addVehicle(new Vehicle(source, destination));
                    break;
                case 5:
                    System.out.print("Enter number of simulation steps: ");
                    int steps = scanner.nextInt();
                    simulator.simulate(steps);
                    break;
                case 6:
                    System.out.print("Enter source and destination (source,destination): ");
                    String[] pathInput = scanner.nextLine().split(",");
                    if (pathInput.length != 2) {
                        System.out.println("Invalid input. Format: source,destination");
                        break;
                    }
                    int src = graph.getNodeIndex(pathInput[0]);
                    int dest = graph.getNodeIndex(pathInput[1]);
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