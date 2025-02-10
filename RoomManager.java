import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RoomManager extends Remote {
    String listRooms() throws RemoteException;
    String bookRoom(int roomType, String guestName) throws RemoteException;
    List<String> listGuests() throws RemoteException;
}