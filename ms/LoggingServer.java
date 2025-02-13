import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoggingServer {
    public static void main(String[] args) {
        try {            
            Registry registry = LocateRegistry.createRegistry(1099);
            LoggingServices loggingService = new LoggingServices();

            registry.rebind("LoggingService", loggingService);

            System.out.println("Logger service started on port 1099, waiting for clients...");
        } catch (Exception e) {
            System.err.println("Logger server failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
