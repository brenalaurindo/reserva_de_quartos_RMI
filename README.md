# Sistema de Reserva de Quartos com RMI

## Descrição
Este projeto implementa um sistema de reserva de quartos usando Java RMI. O sistema permite listar quartos disponíveis, reservar quartos e listar convidados.

## Estrutura do Projeto
- `RoomManager`: Interface remota que define os métodos para listar quartos, reservar quartos e listar convidados.
- `RoomManagerImpl`: Implementação da interface `RoomManager`.
- `HotelServer`: Classe que inicia o servidor RMI e registra a implementação `RoomManagerImpl`.
- `HotelClient`: Classe que se conecta ao servidor RMI e invoca os métodos remotos.

## IDE de Escolha
A IDE escolhida para este projeto foi o Visual Studio Code.

## Como Executar

### Passo 1: Compile o código
Abra o terminal e navegue até o diretório onde os arquivos `.java` estão localizados. Compile todos os arquivos `.java` com o seguinte comando:
```sh
javac *.java
```

### Passo 2: Inicie o RMI Registry
No terminal, inicie o RMI Registry com o comando:
```sh
rmiregistry
```
Deixe essa janela do terminal aberta, pois o RMI Registry precisa estar em execução.

### Passo 3: Inicie o servidor
Em uma nova janela do terminal, navegue até o diretório onde os arquivos `.class` estão localizados e inicie o servidor com o comando:
```sh
java HotelServer
```
Você deve ver a mensagem "HotelServer está pronto." indicando que o servidor foi iniciado com sucesso.

### Passo 4: Execute o cliente
Em outra nova janela do terminal, navegue até o diretório onde os arquivos `.class` estão localizados e execute o cliente com os comandos apropriados:

#### Listar quartos disponíveis
```sh
java HotelClient list localhost
```

#### Reservar um quarto
```sh
java HotelClient book localhost <tipo de quarto> <nome do convidado>
```
Substitua `<tipo de quarto>` pelo número do tipo de quarto (0 a 4) e `<nome do convidado>` pelo nome do hóspede.

#### Listar convidados
```sh
java HotelClient clientes localhost
```

## Estrutura do Projeto

Certifique-se de que a estrutura do seu projeto esteja organizada da seguinte forma:

```
/seu-projeto
    ├── HotelClient.java
    ├── HotelServer.java
    ├── RoomManager.java
    ├── RoomManagerImpl.java
    ├── README.md
    ├── LICENSE
```

## Exemplos de Saída

### Listar quartos disponíveis
```sh
java HotelClient list localhost
```
Saída esperada:
```
10 quartos do tipo 0 estão disponíveis por 55 reais por noite
20 quartos do tipo 1 estão disponíveis por 75 reais por noite
5 quartos do tipo 2 estão disponíveis por 80 reais por noite
3 quartos do tipo 3 estão disponíveis por 150 reais por noite
2 quartos do tipo 4 estão disponíveis por 230 reais por noite
```

### Reservar um quarto
```sh
java HotelClient book localhost 1 João
```
Saída esperada:
```
Reserva confirmada para João no quarto do tipo 1
```

### Listar convidados
```sh
java HotelClient clientes localhost
```
Saída esperada:
```
João
```

## Comentários no Código

### HotelServer.java
```java
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class HotelServer {
    public static void main(String[] args) {
        try {
            // Cria o registro RMI na porta 1100
            LocateRegistry.createRegistry(1100);
            // Cria uma instância da implementação do RoomManager
            RoomManagerImpl roomManager = new RoomManagerImpl();
            // Registra a instância no registro RMI com o nome "RoomManager"
            Naming.rebind("//localhost:1100/RoomManager", roomManager);
            System.out.println("HotelServer está pronto.");
        } catch (Exception e) {
            System.err.println("Erro no HotelServer: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```

### HotelClient.java
```java
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
            // Conecta ao servidor RMI no endereço e porta especificados
            RoomManager roomManager = (RoomManager) Naming.lookup("//" + serverAddress + ":1100/RoomManager");

            switch (command) {
                case "list":
                    // Lista os quartos disponíveis
                    System.out.println(roomManager.listRooms());
                    break;
                case "book":
                    if (args.length < 4) {
                        System.out.println("Uso: java HotelClient book <endereço do servidor> <tipo de quarto> <nome do convidado>");
                        return;
                    }
                    int roomType = Integer.parseInt(args[2]);
                    String guestName = args[3];
                    // Reserva um quarto do tipo especificado para o convidado
                    System.out.println(roomManager.bookRoom(roomType, guestName));
                    break;
                case "clientes":
                    // Lista os convidados registrados
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
```

### RoomManager.java
```java
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RoomManager extends Remote {
    // Método remoto para listar os quartos disponíveis
    String listRooms() throws RemoteException;
    // Método remoto para reservar um quarto
    String bookRoom(int roomType, String guestName) throws RemoteException;
    // Método remoto para listar os convidados registrados
    List<String> listGuests() throws RemoteException;
}
```

### RoomManagerImpl.java
```java
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

        // Inicializa a disponibilidade e os preços dos quartos
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
```