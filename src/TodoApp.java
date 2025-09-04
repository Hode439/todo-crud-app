import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Task {
    int id;
    String name;
    String priority;

    Task(int id, String name) {
        this.id = id;
        this.name = name;
        this.priority = "none"; // default
    }

    @Override
    public String toString() {
        // showing task as "id. name [priority]"
        return id + ". " + name + " [priority: " + priority + "]";
    }
}

public class TodoApp extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private JList<Task> taskList;
    private JTextField taskInput;
    private int taskCounter = 1; // to keep serial nos

    public TodoApp() {
        // setup frame
        setTitle("todo list with priority");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);

        taskInput = new JTextField();

        JButton addBtn = new JButton("add");
        JButton updateBtn = new JButton("update");
        JButton deleteBtn = new JButton("delete");

        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(taskInput, BorderLayout.CENTER);
        inputPanel.add(addBtn, BorderLayout.EAST);

        JPanel btnPanel = new JPanel();
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(taskList), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // add task
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = taskInput.getText().trim();
                if (!text.isEmpty()) {
                    Task t = new Task(taskCounter++, text);
                    taskListModel.addElement(t);
                    taskInput.setText("");
                }
            }
        });

        // update task
        updateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = taskList.getSelectedIndex();
                if (index != -1) {
                    Task t = taskListModel.get(index);
                    String newText = JOptionPane.showInputDialog("update task:", t.name);
                    if (newText != null && !newText.trim().isEmpty()) {
                        t.name = newText.trim();
                        taskList.repaint(); // refresh display
                    }
                }
            }
        });

        // delete task
        deleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = taskList.getSelectedIndex();
                if (index != -1) {
                    taskListModel.remove(index);
                }
            }
        });

        // right click menu for priority
        JPopupMenu priorityMenu = new JPopupMenu();
        JMenu setPriority = new JMenu("set priority");

        JMenuItem high = new JMenuItem("high");
        JMenuItem medium = new JMenuItem("medium");
        JMenuItem low = new JMenuItem("low");

        setPriority.add(high);
        setPriority.add(medium);
        setPriority.add(low);
        priorityMenu.add(setPriority);

        // assign action to menu items
        ActionListener priorityListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int index = taskList.getSelectedIndex();
                if (index != -1) {
                    Task t = taskListModel.get(index);
                    t.priority = ((JMenuItem) e.getSource()).getText();
                    taskList.repaint();
                }
            }
        };

        high.addActionListener(priorityListener);
        medium.addActionListener(priorityListener);
        low.addActionListener(priorityListener);

        // mouse listener for right click
        taskList.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int row = taskList.locationToIndex(e.getPoint());
                    taskList.setSelectedIndex(row);
                    if (row != -1) {
                        priorityMenu.show(taskList, e.getX(), e.getY());
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new TodoApp().setVisible(true);
            }
        });
    }
}
