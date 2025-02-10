import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManagerImpl extends UnicastRemoteObject implements RoomManager {
    private final Map<Integer, Integer> roomAvailability;
    private final Map<Integer, Integer> roomPrices;
    private final List<String> guests;

    protected RoomManagerImpl() throws RemoteException {
        roomAvailability = new HashMap<>();
        roomPrices = new HashMap<>();
        guests = new ArrayList<>();

        roomAvailability.put(0, 10);
        roomAvailability.put(1, 20);
        roomAvailability.put(2, 5);
        roomAvailability.put(3, 3);
        roomAvailability.put(4, 2);

        roomPrices.put(0, 55);
        roomPrices.put(1, 75);
        roomPrices.put(2, 80);
        roomPrices.put(3, 150);
        roomPrices.put(4, 230);
    }

    @Override
    public synchronized String listRooms() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        for (int type : roomAvailability.keySet()) {
            sb.append(roomAvailability.get(type))
              .append(" quartos do tipo ")
              .append(type)
              .append(" estão disponíveis por ")
              .append(roomPrices.get(type))
              .append(" reais por noite\n");
        }
        return sb.toString();
    }

    @Override
    public synchronized String bookRoom(int roomType, String guestName) throws RemoteException {
        if (roomAvailability.get(roomType) > 0) {
            roomAvailability.put(roomType, roomAvailability.get(roomType) - 1);
            guests.add(guestName);
            return "Reserva confirmada para " + guestName + " no quarto do tipo " + roomType;
        } else {
            return "Desculpe, não há quartos disponíveis do tipo " + roomType;
        }
    }

    @Override
    public synchronized List<String> listGuests() throws RemoteException {
        return new ArrayList<>(guests);
    }
}