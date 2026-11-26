import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce varias palabras separadas por espacios:");
        String linea = sc.nextLine().trim();
        String mensaje = String.join(";", linea.split("\\s+"));

        try (DatagramSocket socket = new DatagramSocket()) { // puerto efímero
            socket.setSoTimeout(2000); // 2 segundos de espera máxima
            InetAddress servidor = InetAddress.getByName("localhost");
            byte[] datos = mensaje.getBytes(StandardCharsets.UTF_8);
            boolean recibido = false;
            for (int intento = 1; intento <= 3; intento++) {
                socket.send(new DatagramPacket(datos, datos.length, servidor, 5555));
                byte[] buffer = new byte[1024];
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(respuesta);
                    System.out.println(new String(buffer, 0, respuesta.getLength(), StandardCharsets.UTF_8));
                    recibido = true;
                    break;
                } catch (SocketTimeoutException e) {
                    System.out.println("Sin respuesta (intento " + intento + " de 3)...");
                }
            }
            if (!recibido) {
                System.out.println("Servidor no disponible");
            }
        }
        sc.close();
    }
}