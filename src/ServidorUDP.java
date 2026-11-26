import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class ServidorUDP {
    public static void main(String[] args) throws IOException {
        try (DatagramSocket socket = new DatagramSocket(5555)) { // buzón en el 5555
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket recibido = new DatagramPacket(buffer, buffer.length);
                socket.receive(recibido); // BLOQUEA
                String texto = new String(recibido.getData(), 0,
                        recibido.getLength(), StandardCharsets.UTF_8); // ¡solo los bytes válidos!
                System.out.println(recibido.getAddress() + ":" + recibido.getPort() + " -> " + texto);

                String[] palabras = texto.split(";");
                String masLarga = "";
                for (String p : palabras) {
                    if (p.trim().length() > masLarga.length()) {
                        masLarga = p.trim();
                    }
                }
                String respuesta = "OK " + masLarga + " " + masLarga.length();
                byte[] resp = respuesta.getBytes(StandardCharsets.UTF_8);
                socket.send(new DatagramPacket(resp, resp.length,
                        recibido.getAddress(), recibido.getPort())); // respondo al remitente
            }
        }
    }
}