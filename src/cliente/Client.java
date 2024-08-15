package cliente;

import interfaz.ChatFrame;
import javaClienteSocket.JavaClienteSocket;

import java.io.IOException;
import java.net.Socket;

public class Client implements SocketProcess {
    private Socket socket;
    private Session session;
    private JavaClienteSocket javaClienteSocket;
    private boolean running;
    private String clientName;
    private ChatFrame chatFrame;

    public Client(String serverAddress, int serverPort) {
        this.javaClienteSocket = new JavaClienteSocket(serverPort, serverAddress);
        this.session = null;
        this.running = false;
        this.clientName = null;
    }

    @Override
    public boolean connect() throws IOException {
        try {
            // Intenta obtener el socket desde la instancia de JavaClienteSocket
            this.socket = javaClienteSocket.get();
            // Verifica si el socket es nulo (indica que la conexión falló)
            if (this.socket == null) {
                return false;
            }
            // Inicializa la sesión de comunicación con el socket
            this.session = new Session(this.socket);
            // Marca el cliente como en ejecución
            this.running = true;

            // Inicia un nuevo hilo para procesar mensajes en segundo plano
            startMessageProcessingThread();

            // Espera a que el nombre del cliente sea establecido
            while (clientName == null) {
                try {
                    // Pausa el hilo principal brevemente para no sobrecargar el proceso
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // Restaura el estado de interrupción del hilo y lanza una excepción
                    Thread.currentThread().interrupt(); // Restaurar estado de interrupción
                    throw new IOException("Error al esperar por el nombre del cliente", e);
                }
            }

            // Una vez que el nombre del cliente está disponible, lo envía al servidor
            return send(clientName); // Enviar el nombre del cliente
        } catch (IOException e) {
            // Imprime la traza del error y vuelve a lanzar la excepción
            e.printStackTrace();
            throw e; // Propagar IOException
        }
    }


    @Override
    public boolean send(Object data) throws IOException {
        if (session != null) {
            return session.write(data.toString()); // Enviar el mensaje a través de la sesión
        }
        return false;
    }

    @Override
    public Object receive() throws IOException {
        if (session != null) {
            return session.read(); // Leer el mensaje a través de la sesión
        }
        return null;
    }

    @Override
    public boolean close() throws IOException {
        try {
            // Detener el procesamiento de mensajes
            running = false;

            // Cerrar la sesión de comunicación
            if (this.session != null) {
                this.session.close();
            }

            // Cerrar el socket si no está cerrado
            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
            }

            // Notificar al chatFrame sobre el cierre de la conexión
            if (chatFrame != null) {
                chatFrame.onConnectionClosed();
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Propagar IOException
        }
    }


    private void startMessageProcessingThread() {
        new Thread(this::processMessages).start();
    }

    public void setChatFrame(ChatFrame chatFrame) {
        this.chatFrame = chatFrame;
    }

    public void setClientName(String name) {
        this.clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    private void processMessages() {
        try {
            while (running) {
                if (socket.isClosed()) {
                    System.out.println("Socket cerrado. Terminando procesamiento de mensajes.");
                    break;
                }
                String message = (String) receive(); // Usar receive para obtener el mensaje
                if (message != null) {
                    if (message.startsWith("Lista_clientes:")) {
                        String clientList = message.substring("Lista_clientes:".length()).trim();
                        if (chatFrame != null) {
                            chatFrame.onClientListUpdated(clientList);
                        }
                    } else {
                        if (chatFrame != null) {
                            chatFrame.onMessageReceived(message);
                        }
                    }
                }
            }
        } catch (IOException e) {
            if (e.getMessage().contains("Socket cerrado")) {
                System.out.println("Socket cerrado.");
            } else {
                System.out.println("Socket cerrado inesperadamente.");
                e.printStackTrace(); // Puedes imprimir la traza completa si es necesario para depuración
            }
        }
    }

}
