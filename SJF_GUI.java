/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.sjf_gui;
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

    // Methods to calculate waiting and turnaround times
    public void calculateTimes(int startTime) {
        waitingTime = Math.max(0, startTime - arrivalTime);
        turnaroundTime = waitingTime + burstTime;
    }
}

public class SJF_GUI {
    private JFrame frame;
    private JTextField burstTimeField, arrivalTimeField;
    private JTable processTable, resultTable;
    private DefaultTableModel processTableModel, resultTableModel;
    private List<Process> processList;

    public SJF_GUI() {
        processList = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        // Main Frame
        frame = new JFrame("SJF Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        JPanel buttonPanel = new JPanel();

        // Input Fields
        JLabel burstTimeLabel = new JLabel("Burst Time:");
        burstTimeField = new JTextField();
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        arrivalTimeField = new JTextField();

        inputPanel.add(burstTimeLabel);
        inputPanel.add(burstTimeField);
        inputPanel.add(arrivalTimeLabel);
        inputPanel.add(arrivalTimeField);

        // Buttons
        JButton addProcessButton = new JButton("Add Process");
        JButton getOutputButton = new JButton("Get Output");
        JButton clearDataButton = new JButton("Clear Data"); // Clear Data button
        buttonPanel.add(addProcessButton);
        buttonPanel.add(getOutputButton);
        buttonPanel.add(clearDataButton); // Add Clear Data button

        // Process Table
        processTableModel = new DefaultTableModel(new String[]{"Process ID", "Burst Time", "Arrival Time"}, 0);
        processTable = new JTable(processTableModel);

        // Result Table
        resultTableModel = new DefaultTableModel(new String[]{"Process ID", "Waiting Time", "Turnaround Time"}, 0);
        resultTable = new JTable(resultTableModel);

        // Scroll Panes
        JScrollPane processScrollPane = new JScrollPane(processTable);
        JScrollPane resultScrollPane = new JScrollPane(resultTable);

        // Action Listeners
        addProcessButton.addActionListener(e -> addProcess());
        getOutputButton.addActionListener(e -> computeSJF());
        clearDataButton.addActionListener(e -> clearData()); // Action for Clear Data button

        // Adding Components
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, processScrollPane, resultScrollPane);
        splitPane.setResizeWeight(0.5);
        mainPanel.add(splitPane, BorderLayout.SOUTH);

        frame.add(mainPanel);
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

    private void computeSJF() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No processes to compute!");
            return;
        }

        // Sort by Arrival Time first, then by Burst Time
        processList.sort(Comparator.comparingInt((Process p) -> p.arrivalTime)
                                   .thenComparingInt(p -> p.burstTime));

        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        resultTableModel.setRowCount(0); // Clear previous results

        // Compute waiting time and turnaround time for each process
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
                "SJF Scheduling Completed!\n" +
                        "Average Waiting Time: " + avgWaitingTime + "\n" +
                        "Average Turnaround Time: " + avgTurnaroundTime);
    }

    // Clear all process data and reset tables and input fields
    private void clearData() {
        processList.clear();
        processTableModel.setRowCount(0); // Clear process table
        resultTableModel.setRowCount(0); // Clear result table
        burstTimeField.setText(""); // Clear input fields
        arrivalTimeField.setText("");
    }
/**
 *
 * @author TANBER
 */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SJF_GUI::new);
    }
}
