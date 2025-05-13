import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<String> intersections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("data/intersections.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                intersections.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error loading intersections: " + e.getMessage());
        }

        TrafficGraphMatrix graph = new TrafficGraphMatrix(intersections.toArray(String[]::new));
        Scanner scanner = new Scanner(System.in);

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
            System.out.println("8. Delete Intersection");
            System.out.println("9. Delete Road");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Clear newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter intersection name: ");
                    String intersection = scanner.nextLine().trim();
                    if (intersection.isEmpty()) {
                        System.out.println("Intersection name cannot be empty.");
                        break;
                    }
                    graph.addIntersection(intersection);
                }
                case 2 -> {
                    System.out.print("Enter road (from,to,distance): ");
                    String[] road = scanner.nextLine().split(",");
                    if (road.length != 3) {
                        System.out.println("Invalid format. Use: from,to,distance");
                        break;
                    }
                    try {
                        String from = road[0].trim();
                        String to = road[1].trim();
                        int distance = Integer.parseInt(road[2].trim());
                        graph.addOrUpdateRoad(from, to, distance);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid distance. Please enter a valid number.");
                    }
                }
                case 3 -> {
                    graph.printIntersections();
                    graph.printMatrix();
                }
                case 4 -> {
                    System.out.print("Enter vehicle source and destination (source,destination): ");
                    String[] vehicleInput = scanner.nextLine().split(",");
                    if (vehicleInput.length != 2) {
                        System.out.println("Invalid format. Use: source,destination");
                        break;
                    }
                    try {
                        int source = graph.getNodeIndex(vehicleInput[0].trim());
                        int destination = graph.getNodeIndex(vehicleInput[1].trim());
                        simulator.addVehicle(new Vehicle(source, destination));
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 5 -> {
                    System.out.print("Enter number of simulation steps: ");
                    try {
                        int steps = scanner.nextInt();
                        scanner.nextLine(); // Clear newline
                        simulator.simulate(steps);
                    } catch (Exception e) {
                        System.out.println("Invalid input. Please enter a valid number.");
                        scanner.nextLine(); // Clear invalid input
                    }
                }
                case 6 -> {
                    System.out.print("Enter source and destination (source,destination): ");
                    String[] pathInput = scanner.nextLine().split(",");
                    if (pathInput.length != 2) {
                        System.out.println("Invalid format. Use: source,destination");
                        break;
                    }
                    try {
                        int src = graph.getNodeIndex(pathInput[0].trim());
                        int dest = graph.getNodeIndex(pathInput[1].trim());
                        int[] path = DijkstraPathFinder.findShortestPath(graph, src, dest);
                        int cost = DijkstraPathFinder.getPathCost(graph, path);
                        System.out.println("Shortest Path: " + Arrays.toString(path));
                        System.out.println("Path Cost: " + cost);
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 7 -> {
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                }
                case 8 -> {
                    System.out.print("Enter intersection name to delete: ");
                    String intersectionToDelete = scanner.nextLine().trim();
                    if (intersectionToDelete.isEmpty()) {
                        System.out.println("Intersection name cannot be empty.");
                        break;
                    }
                    graph.deleteIntersection(intersectionToDelete);
                }
                case 9 -> {
                    System.out.print("Enter road to delete (from,to): ");
                    String[] roadInput = scanner.nextLine().split(",");
                    if (roadInput.length != 2) {
                        System.out.println("Invalid format. Use: from,to");
                        break;
                    }
                    String from = roadInput[0].trim();
                    String to = roadInput[1].trim();
                    graph.deleteRoad(from, to);
                }
                default -> System.out.println("Invalid choice. Please enter a number between 1 and 9.");
            }
        }
    }
}