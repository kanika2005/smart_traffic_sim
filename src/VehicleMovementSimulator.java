import java.util.*;

public class VehicleMovementSimulator {
    private Queue<Vehicle> vehicleQueue;
    private Map<String, List<Vehicle>> roadMap;
    private TrafficGraphMatrix graph;

    public VehicleMovementSimulator(TrafficGraphMatrix graph) {
        this.graph = graph;
        this.vehicleQueue = new LinkedList<>();
        this.roadMap = new HashMap<>();
    }

public void addVehicle(Vehicle vehicle) {
    int[] pathArray = DijkstraPathFinder.findShortestPath(graph, vehicle.source, vehicle.destination);
    if (pathArray.length == 0) {
        System.out.println("No valid path found for vehicle from " + graph.getNodeName(vehicle.source) + " to " + graph.getNodeName(vehicle.destination));
        return;
    }
    List<Integer> path = new ArrayList<>();
    for (int node : pathArray) {
        path.add(node);
    }
    vehicle.setPath(path);
    vehicleQueue.add(vehicle);
    System.out.println("Vehicle added: From " + graph.getNodeName(vehicle.source) + " To " + graph.getNodeName(vehicle.destination));
    System.out.println("Path: " + path);
}
public void simulate(int steps) {
    for (int step = 0; step < steps; step++) {
        System.out.println("Step " + (step + 1) + ":");
        roadMap.clear(); // Clear the road map for the current step
        Queue<Vehicle> nextQueue = new LinkedList<>();

        while (!vehicleQueue.isEmpty()) {
            Vehicle vehicle = vehicleQueue.poll();
            if (vehicle.path == null || vehicle.path.isEmpty()) {
                System.out.println("No path found for vehicle.");
                continue;
            }

            int currentIndex = vehicle.path.indexOf(vehicle.currentPosition);
            if (currentIndex < vehicle.path.size() - 1) {
                // Move to the next position
                int nextPosition = vehicle.path.get(currentIndex + 1);
                String road = graph.getNodeName(vehicle.currentPosition) + " -> " + graph.getNodeName(nextPosition);

                roadMap.putIfAbsent(road, new ArrayList<>());
                roadMap.get(road).add(vehicle);

                System.out.println("Vehicle moved: " + graph.getNodeName(vehicle.currentPosition) + " -> " + graph.getNodeName(nextPosition));
                vehicle.currentPosition = nextPosition;

                if (vehicle.currentPosition != vehicle.destination) {
                    nextQueue.add(vehicle);
                } else {
                    System.out.println("Vehicle reached destination: " + graph.getNodeName(vehicle.destination));
                }
            }
        }

        vehicleQueue = nextQueue;
        printRoadMap();
    }
}

   private void printRoadMap() {
    System.out.println("Road Map:");
    for (Map.Entry<String, List<Vehicle>> entry : roadMap.entrySet()) {
        System.out.println(entry.getKey() + ": " + entry.getValue().size() + " vehicles");
    }
}
}
