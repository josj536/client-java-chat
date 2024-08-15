package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class Session {
  private DataOutputStream dataOutputStream;
  private DataInputStream dataInputStream;
  private Socket socket;

  public Session(Socket socket) {
    this.socket = socket;
    try {
      this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
      this.dataInputStream = new DataInputStream(this.socket.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
      close(); // Asegúrate de cerrar la sesión si ocurre un error
    }
  }

  public String read() throws IOException {
    if (socket.isClosed()) {
      throw new IOException("Socket is closed");
    }
    try {
      return this.dataInputStream.readUTF(); // Leer cadena en formato UTF
    } catch (SocketException e) {
      // Captura el SocketException y lanza IOException con un mensaje personalizado
      throw new IOException("Socket cerrado", e);
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public boolean write(String data) throws IOException {
    if (socket.isClosed()) {
      throw new IOException("Socket is closed");
    }
    try {
      this.dataOutputStream.writeUTF(data); // Escribir cadena en formato UTF
      this.dataOutputStream.flush();
      return true;
    } catch (IOException e) {
      e.printStackTrace();
      throw e;
    }
  }

  public boolean close() {
    boolean success = true;
    try {
      if (this.dataOutputStream != null) {
        this.dataOutputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    try {
      if (this.dataInputStream != null) {
        this.dataInputStream.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    try {
      if (this.socket != null && !this.socket.isClosed()) {
        this.socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
      success = false;
    }
    return success;
  }
}

