package org.example;
import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();

        Process[] processes = new Process[n];

        for (int i = 0; i < n; i++) {
            System.out.println("Enter details for Process " + (i + 1) + ":");
            System.out.print("Arrival Time: ");
            int arrivalTime = scanner.nextInt();
            System.out.print("Burst Time: ");
            int burstTime = scanner.nextInt();
            System.out.print("Priority: ");
            int priority = scanner.nextInt();

            processes[i] = new Process(i + 1, arrivalTime, burstTime, priority);
        }

        System.out.println("Choose a scheduling algorithm:");
        System.out.println("1. Priority Scheduling");
        System.out.println("2. Shortest Job First (SJF) Scheduling");
        System.out.println("3. Shortest Remaining Time First (SRTF) Scheduling");
        int choice = scanner.nextInt();

        Scheduler scheduler;

        switch (choice) {
            case 1:
                scheduler = new PriorityScheduler();
                break;
            case 2:
                scheduler = new SJFScheduler();
                break;
            case 3:
                scheduler = new SRTFScheduler();
                break;
            default:
                System.out.println("Invalid choice.");
                return;
        }

        scheduler.schedule(processes);

        // Print output to console
        System.out.println("\nExecution Order: " + scheduler.getExecutionOrder());
        System.out.println("\nProcess Details:");
        int totalTurnaroundTime = 0;
        int totalWaitingTime = 0;

        for (Process p : processes) {
            System.out.println("Process " + p.id + ":");
            System.out.println("  Arrival Time: " + p.arrivalTime);
            System.out.println("  Burst Time: " + p.burstTime);
            System.out.println("  Completion Time: " + p.completionTime);
            System.out.println("  Turnaround Time: " + p.turnaroundTime);
            System.out.println("  Waiting Time: " + p.waitingTime + "\n");

            // Accumulate total waiting time and turnaround time
            totalTurnaroundTime += p.turnaroundTime;
            totalWaitingTime += p.waitingTime;
        }

        // Calculate average turnaround and waiting times
        double avgTurnaroundTime = (double) totalTurnaroundTime / n;
        double avgWaitingTime = (double) totalWaitingTime / n;

        // Print averages
        System.out.println("Average Turnaround Time: " + avgTurnaroundTime);
        System.out.println("Average Waiting Time: " + avgWaitingTime);

        // Create the GUI to visualize the execution order
        SwingUtilities.invokeLater(() -> createAndShowGUI(processes, scheduler.getExecutionOrder()));
    }

    private static void createAndShowGUI(Process[] processes, String executionOrder) {
        JFrame frame = new JFrame("Process Scheduling Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                String[] orders = executionOrder.split(",");
                int x = 50;
                for (String order : orders) {
                    Process p = processes[Integer.parseInt(order) - 1];
                    g.setColor(Color.BLUE);
                    g.fillRect(x, 100, p.burstTime * 20, 50);  // Draw each process as a bar
                    g.setColor(Color.BLACK);
                    g.drawString("P" + p.id, x + 10, 130);
                    x += p.burstTime * 20 + 10;  // Move the x position for the next process
                }
            }
        };

        frame.add(panel);
        frame.setVisible(true);
    }
}
