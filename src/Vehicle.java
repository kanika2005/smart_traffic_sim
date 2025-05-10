import java.util.List;

public class Vehicle {
    public int source;
    public int destination;
    public int currentPosition;
    public List<Integer> path; // Stores the path for the vehicle

    public Vehicle(int source, int destination) {
        this.source = source;
        this.destination = destination;
        this.currentPosition = source;
    }

    public void setPath(List<Integer> path) {
        this.path = path;
    }
}