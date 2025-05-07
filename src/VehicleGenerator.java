import java.util.Random;

public class VehicleGenerator {
    public static Vehicle[] generateVehicles(TrafficGraphMatrix graph, int count) {
        Vehicle[] vehicles = new Vehicle[count];
        Random rand = new Random();
        int n = graph.size();
        for (int i = 0; i < count; i++) {
            int source, destination;
            do {
                source = rand.nextInt(n);
                destination = rand.nextInt(n);
            } while (source == destination);
            vehicles[i] = new Vehicle(source, destination);
        }
        return vehicles;
    }
}