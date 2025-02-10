import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class HotelServer {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1100);
            RoomManagerImpl roomManager = new RoomManagerImpl();
            Naming.rebind("//localhost:1100/RoomManager", roomManager);
            System.out.println("HotelServer está pronto.");
        } catch (Exception e) {
            System.err.println("Erro no HotelServer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}