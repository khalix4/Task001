package UI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileWriterMain {
    private JFrame frame;
    private List<Task> taskList;
    private JTable taskTable;
    private JTextField titleField;
    private JTextArea descriptionArea;
    private JComboBox<String> priorityComboBox;
    private String filePath = "tasks.txt"; // Default file path

    public FileWriterMain() {
        frame = new JFrame("Task Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Initialize task list
        taskList = new ArrayList<>();

        // Create task table model and table
        TaskTableModel taskTableModel = new TaskTableModel(taskList);
        taskTable = new JTable(taskTableModel);

        // Create components for adding/editing tasks
        JLabel titleLabel = new JLabel("Title:");
        titleField = new JTextField(20);

        JLabel descriptionLabel = new JLabel("Description:");
        descriptionArea = new JTextArea(5, 20);
        JScrollPane descriptionScrollPane = new JScrollPane(descriptionArea);

        JLabel priorityLabel = new JLabel("Priority:");
        String[] priorityOptions = {"Low", "Medium", "High"};
        priorityComboBox = new JComboBox<>(priorityOptions);

        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String description = descriptionArea.getText();
                String priority = (String) priorityComboBox.getSelectedItem();

                Task task = new Task(title, description, priority);
                taskList.add(task);
                taskTableModel.fireTableDataChanged();

                // Save todo - list to a file
                saveTasksToFile();

                // Clear input fields
                titleField.setText("");
                descriptionArea.setText("");
                priorityComboBox.setSelectedIndex(0);
            }
        });

        JButton editButton = new JButton("Edit Task");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    String title = titleField.getText();
                    String description = descriptionArea.getText();
                    String priority = (String) priorityComboBox.getSelectedItem();

                    Task task = taskList.get(selectedRow);
                    task.title = title;
                    task.description = description;
                    task.priority = priority;
                    taskTableModel.fireTableDataChanged();

                    // Save tasks to file
                    saveTasksToFile();

                    // Clear input fields
                    titleField.setText("");
                    descriptionArea.setText("");
                    priorityComboBox.setSelectedIndex(0);
                }
            }
        });

        JButton deleteButton = new JButton("Delete Task");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = taskTable.getSelectedRow();
                if (selectedRow != -1) {
                    taskList.remove(selectedRow);
                    taskTableModel.fireTableDataChanged();

                    // Save tasks to file
                    saveTasksToFile();

                    // Clear input fields
                    titleField.setText("");
                    descriptionArea.setText("");
                    priorityComboBox.setSelectedIndex(0);
                }
            }
        });

        // Create a panel for adding/editing tasks
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        inputPanel.add(titleLabel, constraints);
        constraints.gridy++;
        inputPanel.add(titleField, constraints);
        constraints.gridy++;
        inputPanel.add(descriptionLabel, constraints);
        constraints.gridy++;
        inputPanel.add(descriptionScrollPane, constraints);
        constraints.gridy++;
        inputPanel.add(priorityLabel, constraints);
        constraints.gridy++;
        inputPanel.add(priorityComboBox, constraints);
        constraints.gridy++;
        inputPanel.add(addButton, constraints);
        constraints.gridy++;
        inputPanel.add(editButton, constraints);
        constraints.gridy++;
        inputPanel.add(deleteButton, constraints);

        // Create a panel for the task list
        JPanel taskPanel = new JPanel(new BorderLayout());
        taskPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        taskPanel.add(new JScrollPane(taskTable), BorderLayout.CENTER);

        // Add input panel and task panel to the main frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(taskPanel, BorderLayout.CENTER);

        // Load tasks from file
        loadTasksFromFile();

        frame.setVisible(true);
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            for (Task task : taskList) {
                writer.println(task.title);
                writer.println(task.description);
                writer.println(task.priority);
                writer.println(task.completed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTasksFromFile() {
        File file = new File(filePath);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String title = line;
                    String description = reader.readLine();
                    String priority = reader.readLine();
                    boolean completed = Boolean.parseBoolean(reader.readLine());
                    Task task = new Task(title, description, priority);
                    task.completed = completed;
                    taskList.add(task);
                }
                ((TaskTableModel) taskTable.getModel()).fireTableDataChanged();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileWriterMain();
            }
        });
    }

    private class Task {
        private String title;
        private String description;
        private String priority;
        private boolean completed;

        public Task(String title, String description, String priority) {
            this.title = title;
            this.description = description;
            this.priority = priority;
            this.completed = false;
        }
    }

    private class TaskTableModel extends AbstractTableModel {
        private List<Task> tasks;
        private String[] columnNames = {"Title", "Description", "Priority", "Completed"};

        public TaskTableModel(List<Task> tasks) {
            this.tasks = tasks;
        }

        @Override
        public int getRowCount() {
            return tasks.size();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            Task task = tasks.get(row);
            switch (column) {
                case 0:
                    return task.title;
                case 1:
                    return task.description;
                case 2:
                    return task.priority;
                case 3:
                    return task.completed;
                default:
                    return null;
            }
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 3) {
                return Boolean.class;
            }
            return super.getColumnClass(columnIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 3) {
                boolean completed = (Boolean) aValue;
                tasks.get(rowIndex).completed = completed;

                // Save tasks to file
                saveTasksToFile();
            }
        }
    }
}
