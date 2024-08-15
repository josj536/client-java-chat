package interfaz;

import cliente.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ChatFrame extends JFrame {
    private JTextArea messageArea;
    private JTextArea userListArea;
    private JTextField messageField;
    private JButton sendButton;
    private Client client;

    public ChatFrame(Client client) {
        this.client = client;
        this.client.setChatFrame(this); // Registrar ChatFrame con Client

        setTitle("Chat");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel chatPanel = new JPanel();
        chatPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Chat");
        messageLabel.setOpaque(true);
        messageLabel.setBackground(new Color(200, 200, 255));
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        messagePanel.add(messageLabel, BorderLayout.NORTH);

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBackground(new Color(245, 245, 245));
        messageArea.setForeground(Color.BLACK);
        messagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        chatPanel.add(messagePanel);

        JPanel userListPanel = new JPanel();
        userListPanel.setLayout(new BorderLayout());

        JLabel userListLabel = new JLabel("Clientes Conectados");
        userListLabel.setOpaque(true);
        userListLabel.setBackground(new Color(200, 255, 200));
        userListLabel.setFont(new Font("Arial", Font.BOLD, 14));
        userListLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        userListPanel.add(userListLabel, BorderLayout.NORTH);

        userListArea = new JTextArea();
        userListArea.setEditable(false);
        userListArea.setLineWrap(true);
        userListArea.setWrapStyleWord(true);
        userListArea.setBackground(new Color(245, 255, 245));
        userListArea.setForeground(Color.BLACK);
        userListPanel.add(new JScrollPane(userListArea), BorderLayout.CENTER);

        chatPanel.add(userListPanel);

        add(chatPanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageField = new JTextField();
        inputPanel.add(messageField, BorderLayout.CENTER);

        sendButton = new JButton("enviar");
        inputPanel.add(sendButton, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    closeClient();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private void sendMessage() {
        String message = messageField.getText();
        if (message != null && !message.trim().isEmpty()) {
            try {
                client.send(message);
                messageField.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeClient() throws IOException {
        client.close();
        System.exit(0);
    }

    public void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append(message + "\n");
        });
    }

    public void onClientListUpdated(String clientList) {
        SwingUtilities.invokeLater(() -> {
            String[] users = clientList.split("\\r?\\n");
            StringBuilder userListText = new StringBuilder();
            for (String user : users) {
                if (!user.trim().isEmpty()) {
                    userListText.append(user.trim()).append("\n");
                }
            }
            userListArea.setText(userListText.toString());
        });
    }

    public void onConnectionClosed() {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "La conexión con el servidor se ha cerrado.");
        });
    }
}
