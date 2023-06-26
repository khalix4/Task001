import UI.FileWriterMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TaskManagerGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public TaskManagerGUI() {
        frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                char[] password = passwordField.getPassword();

                // Perform authentication logic here
                if (authenticateUser(username, password)) {
                    openTaskManager(username);
                    FileWriterMain fileWriterMain = new FileWriterMain();
                    new FileWriterMain();

                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid username or password");
                }

                // Clear the input fields
                usernameField.setText("");
                passwordField.setText("");
            }
        });

        frame.add(usernameLabel);
        frame.add(usernameField);
        frame.add(passwordLabel);
        frame.add(passwordField);
        frame.add(loginButton);

        frame.setVisible(true);
    }

    private void Manage_Gui() {
    }

    private boolean authenticateUser(String username, char[] password) {
        // Implement your authentication logic here
        // You can check against a user database or authentication service
        // For demonstration purposes, let's assume a hard-coded username and password
        String validUsername = "admin";
        char[] validPassword = "password".toCharArray();

        return username.equals(validUsername) && comparePasswords(password, validPassword);
    }

    private boolean comparePasswords(char[] password1, char[] password2) {
        if (password1.length != password2.length) {
            return false;
        }
        for (int i = 0; i < password1.length; i++) {
            if (password1[i] != password2[i]) {
                return false;
            }
        }
        return true;
    }

    private void openTaskManager(String username) {
        // Implement the task manager GUI here
        // You can create a new JFrame or transition to a different panel
        // based on your design and requirements
        JOptionPane.showMessageDialog(frame, "Welcome, " + username + "! Task manager will open.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TaskManagerGUI();
            }
        });
    }
}
