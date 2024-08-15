package cliente;

import interfaz.ChatFrame;
import interfaz.NameInputFrame;

import javax.swing.*;

/**
 * Clase responsable de inicializar y gestionar la conexión del cliente con el servidor,
 * así como de mostrar las ventanas necesarias para la interacción del usuario.
 */
public class ClientInitializer {

    private Client client; // Instancia del cliente que maneja la conexión con el servidor

    /**
     * Constructor de ClientInitializer.
     * @param host La dirección del servidor al que el cliente se conectará.
     * @param port El puerto en el que el servidor está escuchando.
     */
    public ClientInitializer(String host, int port) {
        this.client = new Client(host, port); // Inicializa el cliente con la dirección y puerto del servidor
    }

    /**
     * Método para iniciar el chat.
     * Muestra la ventana de entrada de nombre, espera a que el nombre sea ingresado y luego intenta conectar al servidor.
     */
    public void startChat() {
        try {
            // Mostrar la ventana para ingresar el nombre
            SwingUtilities.invokeLater(() -> {
                NameInputFrame nameInputFrame = new NameInputFrame(client);
                nameInputFrame.setVisible(true); // Hacer visible la ventana de ingreso de nombre
            });

            // Esperar a que se ingrese el nombre antes de continuar con la conexión
            while (client.getClientName() == null) {
                Thread.sleep(100); // Pausa breve para evitar un bucle intenso
            }

            // Intentar conectar al servidor
            if (client.connect()) {
                System.out.println("Conectado al servidor");

                // Mostrar la ventana de chat
                SwingUtilities.invokeLater(() -> {
                    ChatFrame chatFrame = new ChatFrame(client);
                    client.setChatFrame(chatFrame); // Establecer la referencia de ChatFrame en Client
                    chatFrame.setVisible(true); // Hacer visible la ventana de chat

                    // Asegurarse de cerrar la conexión cuando la ventana de chat se cierre
                    chatFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                            closeConnection(); // Cerrar la conexión del cliente
                        }
                    });
                });

            } else {
                System.out.println("No se pudo conectar al servidor");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el stack trace en caso de error
            closeConnection(); // Cerrar la conexión en caso de error
        }
    }


    /**
     * Método para cerrar la conexión del cliente.
     */
    private void closeConnection() {
        try {
            if (client != null) {
                client.close(); // Cerrar la conexión del cliente
            }
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir el stack trace en caso de error al cerrar la conexión
        }
    }
}
