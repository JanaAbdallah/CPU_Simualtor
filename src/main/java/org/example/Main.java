package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];
        Process[] priorityProcesses = new Process[n];
        Process[] sjfProcesses = new Process[n];
        Process[] srtfProcesses = new Process[n];
        List<Process> fcaiprocesses = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();

            processes[i] = new Process(i + 1, arrivalTime, burstTime, priority);

            // Create separate arrays for each scheduling algorithm to preserve original input
            priorityProcesses[i] = new Process(i + 1, arrivalTime, burstTime, priority);
            sjfProcesses[i] = new Process(i + 1, arrivalTime, burstTime, priority);
            srtfProcesses[i] = new Process(i + 1, arrivalTime, burstTime, priority);
        }

        for(int i = 0; i < n; i++) {
            System.out.print("Enter the name of process " + (i + 1) + ": ");
            String processName = scanner.next();
            System.out.print("Enter the arrival time of process " + (i + 1) + ": ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Enter the burst time of process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            System.out.print("Enter the priority of process " + (i + 1) + ": ");
            int priority = scanner.nextInt();
            System.out.print("Enter the quantum of process " + (i + 1) + ": ");
            int quantum = scanner.nextInt();

            // Create a process and add it to the list
            Process process = new Process(processName, burstTime, arrivalTime, priority);
            process.setQuantum(quantum);
            fcaiprocesses.add(process);
        }
        FCAI scheduler = new FCAI(fcaiprocesses);
        System.out.println("Starting FCAI Scheduling...");
        scheduler.logic();

        Scheduler priorityScheduler = new PriorityScheduler();
        Scheduler sjfScheduler = new SJFScheduler();
        Scheduler srtfScheduler = new SRTFScheduler();

        priorityScheduler.schedule(priorityProcesses);
        sjfScheduler.schedule(sjfProcesses);
        srtfScheduler.schedule(srtfProcesses);

        printResults("Priority Scheduling", priorityProcesses, priorityScheduler.getExecutionOrder());
        printResults("Shortest Job First (SJF) Scheduling", sjfProcesses, sjfScheduler.getExecutionOrder());
        printResults("Shortest Remaining Time First (SRTF) Scheduling", srtfProcesses, srtfScheduler.getExecutionOrder());

        // Create the GUI to visualize all three scheduling results
        SwingUtilities.invokeLater(() -> createAndShowGUI(priorityProcesses, sjfProcesses, srtfProcesses, priorityScheduler.getExecutionOrder(), sjfScheduler.getExecutionOrder(), srtfScheduler.getExecutionOrder()));
    }

    private static void printResults(String algorithm, Process[] processes, String executionOrder) {
        System.out.println("\n" + algorithm + ":");
        System.out.println("Execution Order: " + executionOrder);

        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        for (Process p : processes) {
            System.out.println("Process " + p.id + ":");
            System.out.println("  Arrival Time: " + p.arrivalTime);
            System.out.println("  Burst Time: " + p.burstTime);
            System.out.println("  Completion Time: " + p.completionTime);
            System.out.println("  Turnaround Time: " + p.turnaroundTime);
            System.out.println("  Waiting Time: " + p.waitingTime + "\n");

            totalTurnaroundTime += p.turnaroundTime;
            totalWaitingTime += p.waitingTime;
        }

        double avgTurnaroundTime = (double) totalTurnaroundTime / processes.length;
        double avgWaitingTime = (double) totalWaitingTime / processes.length;

        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        System.out.println("Average Waiting Time: " + avgWaitingTime);
    }

    private static void createAndShowGUI(Process[] priorityProcesses, Process[] sjfProcesses, Process[] srtfProcesses, String priorityOrder, String sjfOrder, String srtfOrder) {
        JFrame frame = new JFrame("Process Scheduling Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 800);

        // Main panel to hold the Gantt charts
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setPreferredSize(new Dimension(1200, 300)); // Adjust height based on process count

        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Assign unique colors to each process
        Color[] processColors = generateProcessColors(priorityProcesses.length);

        // Draw Priority Scheduling
        int y = 50;
        drawChart(panel, priorityProcesses, priorityOrder, 50, y, processColors, "Priority Scheduling");

        // Draw SJF Scheduling
        y += 150;
        drawChart(panel, sjfProcesses, sjfOrder, 50, y, processColors, "Shortest Job First (SJF) Scheduling");

        // Draw SRTF Scheduling
        y += 150;
        drawChart(panel, srtfProcesses, srtfOrder, 50, y, processColors, "Shortest Remaining Time First (SRTF) Scheduling");

        frame.add(scrollPane);
        frame.setVisible(true);
    }

    private static void drawChart(JPanel panel, Process[] processes, String executionOrder, int startX, int startY, Color[] processColors, String chartTitle) {
        JLabel titleLabel = new JLabel(chartTitle);
        titleLabel.setBounds(startX, startY - 30, 400, 20);
        panel.add(titleLabel);

        String[] orders = executionOrder.split(",");
        int x = startX;

        for (String order : orders) {
            int processId = Integer.parseInt(order) - 1;
            Process p = processes[processId];

            // Process block
            JLabel processBlock = new JLabel("P" + p.id, SwingConstants.CENTER);
            processBlock.setBounds(x, startY, p.burstTime * 20, 50);
            processBlock.setOpaque(true);
            processBlock.setBackground(processColors[processId]);
            processBlock.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            panel.add(processBlock);

            // Move to the next position
            x += p.burstTime * 20 + 10;
        }

        panel.setPreferredSize(new Dimension(Math.max(panel.getPreferredSize().width, x + 50), panel.getPreferredSize().height));
    }

    private static Color[] generateProcessColors(int numProcesses) {
        Color[] colors = new Color[numProcesses];
        for (int i = 0; i < numProcesses; i++) {
            // Generate unique colors for each process
            colors[i] = new Color((int) (Math.random() * 0xFFFFFF));
        }
        return colors;
    }

}
