package javaClienteSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaClienteSocket {
    //Declaran variables privadas que almacenan el puerto en el que el cliente se conecta al servidor
    private int port;
    // y el host donde esta el servidor.
    private String host;

    //el constructor
    public JavaClienteSocket(int port, String host) {
        this.port = port;
        this.host = host;
    }

    //el metodo es casi el mismo lo que cambia es que aqui no vamos a subir o montar el socket
    // Método para obtener un Socket conectado al servidor
    public Socket get() {
        try {
            return new Socket(this.host, this.port);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }
}
