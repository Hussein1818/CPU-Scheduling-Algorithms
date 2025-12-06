/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.rr_gui2;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Process {
    int processID;
    int burstTime;
    int remainingTime;
    int arrivalTime;
    int waitingTime;
    int turnaroundTime;
    int completionTime;

    public Process(int processID, int burstTime, int arrivalTime) {
        this.processID = processID;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
        this.arrivalTime = arrivalTime;
    }

    // Calculate waiting time and turnaround time once process completes
    public void calculateTimes(int currentTime) {
        completionTime = currentTime;
        waitingTime = completionTime - arrivalTime - burstTime;
        turnaroundTime = waitingTime + burstTime;
    }
}

public class RR_GUI2 {  // Renamed the class here
    private JFrame frame;
    private JTextField burstTimeField, arrivalTimeField, timeQuantumField;
    private JTable processTable, resultTable;
    private DefaultTableModel processTableModel, resultTableModel;
    private List<Process> processList;

    public RR_GUI2() {  // Constructor updated
        processList = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        // Main Frame
        frame = new JFrame("Round Robin Scheduling");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        JPanel buttonPanel = new JPanel();

        // Input Fields
        JLabel burstTimeLabel = new JLabel("Burst Time:");
        burstTimeField = new JTextField();
        JLabel arrivalTimeLabel = new JLabel("Arrival Time:");
        arrivalTimeField = new JTextField();
        JLabel timeQuantumLabel = new JLabel("Time Quantum:");
        timeQuantumField = new JTextField();

        inputPanel.add(burstTimeLabel);
        inputPanel.add(burstTimeField);
        inputPanel.add(arrivalTimeLabel);
        inputPanel.add(arrivalTimeField);
        inputPanel.add(timeQuantumLabel);
        inputPanel.add(timeQuantumField);

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
        getOutputButton.addActionListener(e -> computeRR());
        clearDataButton.addActionListener(e -> clearData()); // Action for clearing data

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

    private void computeRR() {
        if (processList.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "No processes to compute!");
            return;
        }

        // Read the time quantum
        int timeQuantum;
        try {
            timeQuantum = Integer.parseInt(timeQuantumField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid Time Quantum. Please enter a valid number.");
            return;
        }

        // Queue for Round Robin scheduling
        Queue<Process> queue = new LinkedList<>();
        int currentTime = 0;
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        // Add processes to the queue based on arrival time
        queue.addAll(processList);
        resultTableModel.setRowCount(0); // Clear previous results

        while (!queue.isEmpty()) {
            Process currentProcess = queue.poll();

            // If the process hasn't arrived yet, we skip it and move the time forward.
            if (currentTime < currentProcess.arrivalTime) {
                currentTime = currentProcess.arrivalTime;
            }

            // Process execution for time quantum
            int executionTime = Math.min(currentProcess.remainingTime, timeQuantum);
            currentProcess.remainingTime -= executionTime;
            currentTime += executionTime;

            // If the process is finished, calculate times
            if (currentProcess.remainingTime == 0) {
                currentProcess.calculateTimes(currentTime);
                totalWaitingTime += currentProcess.waitingTime;
                totalTurnaroundTime += currentProcess.turnaroundTime;
                resultTableModel.addRow(new Object[]{currentProcess.processID, currentProcess.waitingTime, currentProcess.turnaroundTime});
            }

            // If the process is not finished, put it back in the queue
            if (currentProcess.remainingTime > 0) {
                queue.add(currentProcess);
            }
        }

        // Calculate averages
        double avgWaitingTime = (double) totalWaitingTime / processList.size();
        double avgTurnaroundTime = (double) totalTurnaroundTime / processList.size();

        JOptionPane.showMessageDialog(frame,
                "Round Robin Scheduling Completed!\n" +
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
        timeQuantumField.setText("");
    }
/**
 *
 * @author TANBER
 */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RR_GUI2::new);  
    }
}
