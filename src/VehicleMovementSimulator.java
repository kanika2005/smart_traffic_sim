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

    // Add a vehicle to the simulation
    public void addVehicle(Vehicle vehicle) {
        vehicleQueue.add(vehicle);
        System.out.println("Vehicle added: From " + vehicle.source + " To " + vehicle.destination);
    }

    // Simulate vehicle movement
    public void simulate(int steps) {
        for (int step = 0; step < steps; step++) {
            System.out.println("Step " + (step + 1) + ":");
            Queue<Vehicle> nextQueue = new LinkedList<>();

            while (!vehicleQueue.isEmpty()) {
                Vehicle vehicle = vehicleQueue.poll();
                int[] path = DijkstraPathFinder.findShortestPath(graph, vehicle.source, vehicle.destination);

                if (path.length > 1) {
                    int nextIntersection = path[1];
                    String road = vehicle.source + " -> " + nextIntersection;

                    roadMap.putIfAbsent(road, new ArrayList<>());
                    roadMap.get(road).add(vehicle);

                    vehicle.source = nextIntersection;
                    if (vehicle.source != vehicle.destination) {
                        nextQueue.add(vehicle);
                    } else {
                        System.out.println("Vehicle reached destination: " + vehicle.destination);
                    }
                }
            }

            vehicleQueue = nextQueue;
            printRoadMap();
        }
    }

    // Print the current state of the roads
    private void printRoadMap() {
        System.out.println("Road Map:");
        for (Map.Entry<String, List<Vehicle>> entry : roadMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().size() + " vehicles");
        }
    }
}