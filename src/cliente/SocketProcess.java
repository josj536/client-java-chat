package cliente;

import java.io.IOException;

public interface SocketProcess {
  boolean connect() throws IOException;
  boolean send(Object data) throws IOException;
  Object receive() throws IOException;  // Modificado para lanzar IOException
  boolean close() throws IOException;   // Asegúrate de que también pueda lanzar IOException
}
