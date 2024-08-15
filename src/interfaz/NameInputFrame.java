package interfaz;

import cliente.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NameInputFrame extends JFrame {
    private JTextField nameField;
    private JButton submitButton;
    private Client client;

    public NameInputFrame(Client client) {
        this.client = client;

        // Configuración de la ventana
        setTitle("Ingrese su Nombre");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        // Campo de texto para ingresar el nombre
        nameField = new JTextField(20);
        add(new JLabel("Ingrese su nombre:"));
        add(nameField);

        // Botón para enviar el nombre
        submitButton = new JButton("Enviar");
        add(submitButton);

        // Acción del botón
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String clientName = nameField.getText();
                if (clientName != null && !clientName.trim().isEmpty()) {
                    client.setClientName(clientName);
                    dispose(); // Cierra la ventana después de enviar el nombre
                } else {
                    JOptionPane.showMessageDialog(NameInputFrame.this, "Name cannot be empty", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
