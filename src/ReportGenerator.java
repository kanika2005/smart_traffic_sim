import java.io.FileWriter;

public class ReportGenerator {
    public static void generateReport(Vehicle[] vehicles, String filename) {
        try {
            FileWriter writer = new FileWriter("data/" + filename);
            for (int i = 0; i < vehicles.length; i++) {
                writer.write("Vehicle " + (i+1) + ": From " + vehicles[i].source + " To " + vehicles[i].destination + "\n");
            }
            writer.close();
        } catch (Exception e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }
}