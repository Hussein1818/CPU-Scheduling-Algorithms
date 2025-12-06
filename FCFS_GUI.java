/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.fcfs_gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Process {
    int processID;
    int burstTime;
    int arrivalTime;
    int waitingTime;
    int turnaroundTime;

    public Process(int processID, int burstTime, int arrivalTime) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
    }

    public void calculateTimes(int startTime) {
        waitingTime = Math.max(0, startTime - arrivalTime);
        turnaroundTime = waitingTime + burstTime;
    }
}

public class FCFS_GUI {
    private JFrame frame;
    private JTextField burstTimeField, arrivalTimeField;
    private JTable processTable, resultTable;
    private DefaultTableModel processTableModel, resultTableModel;
    private List<Process> processList;

    public FCFS_GUI() {
        processList = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        // Main Frame
        frame = new JFrame("FCFS Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JPanel buttonPanel = new JPanel();

        // Colors and Fonts
        Color bgColor = new Color(222, 242, 236);
        Color fgColor = new Color(0,0,0);
        Font labelFont = new Font("Arial", Font.BOLD, 22);
        Font tableFont = new Font("Arial", Font.PLAIN, 18);

        // Input Fields
        JLabel burstTimeLabel = new JLabel("Burst Time:");
        burstTimeLabel.setForeground(fgColor);
        burstTimeLabel.setFont(labelFont);
        burstTimeField = new JTextField();
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        arrivalTimeLabel.setForeground(fgColor);
        arrivalTimeLabel.setFont(labelFont);
        arrivalTimeField = new JTextField();

        inputPanel.setBackground(bgColor);
        inputPanel.add(burstTimeLabel);
        inputPanel.add(burstTimeField);
        inputPanel.add(arrivalTimeLabel);
        inputPanel.add(arrivalTimeField);

        // Buttons
        JButton addProcessButton = new JButton("Add Process");
        JButton getOutputButton = new JButton("Get Output");
        JButton clearDataButton = new JButton("Clear Data");

        // Button Styles
        for (JButton button : new JButton[]{addProcessButton, getOutputButton, clearDataButton}) {
            button.setBackground(new Color(213, 181, 199));
            button.setForeground(Color.blue);
            button.setFont(labelFont);
        }

        buttonPanel.setBackground(bgColor);
        buttonPanel.add(addProcessButton);
        buttonPanel.add(getOutputButton);
        buttonPanel.add(clearDataButton);

        // Process Table
        processTableModel = new DefaultTableModel(new String[]{"Process ID", "Burst Time", "Arrival Time"}, 0);
        processTable = new JTable(processTableModel);
        processTable.setFont(tableFont);
        processTable.setBackground(bgColor);
        processTable.setForeground(fgColor);
        processTable.getTableHeader().setBackground(new Color(100, 100, 100));
        processTable.getTableHeader().setForeground(Color.WHITE);

        // Result Table
        resultTableModel = new DefaultTableModel(new String[]{"Process ID", "Waiting Time", "Turnaround Time"}, 0);
        resultTable = new JTable(resultTableModel);
        resultTable.setFont(tableFont);
        resultTable.setBackground(bgColor);
        resultTable.setForeground(fgColor);
        resultTable.getTableHeader().setBackground(new Color(100, 100, 100));
        resultTable.getTableHeader().setForeground(Color.WHITE);

        // Scroll Panes
        JScrollPane processScrollPane = new JScrollPane(processTable);
        JScrollPane resultScrollPane = new JScrollPane(resultTable);

        // Action Listeners
        addProcessButton.addActionListener(e -> addProcess());
        getOutputButton.addActionListener(e -> computeFCFS());
        clearDataButton.addActionListener(e -> clearData());

        // Adding Components
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, processScrollPane, resultScrollPane);
        splitPane.setResizeWeight(0.5);
        mainPanel.add(splitPane, BorderLayout.SOUTH);

        frame.add(mainPanel);
        frame.getContentPane().setBackground(bgColor); // Frame background
        frame.setVisible(true);
    }

    private void addProcess() {
        try {
            int burstTime = Integer.parseInt(burstTimeField.getText());
            int arrivalTime = Integer.parseInt(arrivalTimeField.getText());
            int processID = processList.size() + 1;

            processList.add(new Process(processID, burstTime, arrivalTime));
            processTableModel.addRow(new Object[]{processID, burstTime, arrivalTime});

            burstTimeField.setText("");
            arrivalTimeField.setText("");

            JOptionPane.showMessageDialog(frame, "Process Added Successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid Input. Please enter numbers only.");
        }
    }

    private void computeFCFS() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No processes to compute!");
            return;
        }

        processList.sort(Comparator.comparingInt(p -> p.arrivalTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        resultTableModel.setRowCount(0);

        for (Process process : processList) {
            process.calculateTimes(currentTime);

            totalWaitingTime += process.waitingTime;
            totalTurnaroundTime += process.turnaroundTime;

            currentTime += process.burstTime;

            resultTableModel.addRow(new Object[]{process.processID, process.waitingTime, process.turnaroundTime});
        }

        double avgWaitingTime = (double) totalWaitingTime / processList.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processList.size();

        JOptionPane.showMessageDialog(frame,
                "FCFS Scheduling Completed!\n" +
                        "Average Waiting Time: " + avgWaitingTime + "\n" +
                        "Average Turnaround Time: " + avgTurnaroundTime);
    }

    private void clearData() {
        processList.clear();
        processTableModel.setRowCount(0);
        resultTableModel.setRowCount(0);
        burstTimeField.setText("");
        arrivalTimeField.setText("");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FCFS_GUI::new);
    }
}
