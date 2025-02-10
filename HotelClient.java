import java.rmi.Naming;
import java.util.List;

public class HotelClient {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Uso: java HotelClient <comando> <endereço do servidor> [<tipo de quarto> <nome do convidado>]");
            return;
        }

        try {
            String command = args[0];
            String serverAddress = args[1];
            RoomManager roomManager = (RoomManager) Naming.lookup("//" + serverAddress + "/RoomManager");

            switch (command) {
                case "list":
                    System.out.println(roomManager.listRooms());
                    break;
                case "book":
                    if (args.length < 4) {
                        System.out.println("Uso: java HotelClient book <endereço do servidor> <tipo de quarto> <nome do convidado>");
                        return;
                    }
                    int roomType = Integer.parseInt(args[2]);
                    String guestName = args[3];
                    System.out.println(roomManager.bookRoom(roomType, guestName));
                    break;
                case "clientes":
                    List<String> guests = roomManager.listGuests();
                    for (String guest : guests) {
                        System.out.println(guest);
                    }
                    break;
                default:
                    System.out.println("Comando desconhecido: " + command);
                    break;
            }
        } catch (Exception e) {
            System.err.println("Erro no HotelClient: " + e.getMessage());
            e.printStackTrace();
        }
    }
}