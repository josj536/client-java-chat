import cliente.ClientInitializer;

/**
 * Clase principal del programa.
 */
public class Main {
    public static void main(String[] args) {
        // Inicializar Comunicaciones y comenzar el chat
        ClientInitializer clientInitializer = new ClientInitializer("localhost", 2000);
        clientInitializer.startChat();
    }
}
