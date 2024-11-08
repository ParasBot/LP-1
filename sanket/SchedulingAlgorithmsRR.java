import java.util.*;

class Process {
    int id;         // Process ID
    int burstTime;  // Burst time
    int waitingTime; // Waiting time
    int turnaroundTime; // Turnaround time

    Process(int id, int burstTime) {
        this.id = id;
        this.burstTime = burstTime;
    }
}

public class SchedulingAlgorithmsRR {

    // Method to calculate average waiting time and turnaround time for FCFS
    public static void fcfsScheduling(List<Process> processes) {
        int n = processes.size();
        int totalWaitingTime = 0;

        processes.get(0).waitingTime = 0; // First process has no waiting time
        for (int i = 1; i < n; i++) {
            processes.get(i).waitingTime = processes.get(i - 1).waitingTime + processes.get(i - 1).burstTime;
        }

        for (Process process : processes) {
            process.turnaroundTime = process.waitingTime + process.burstTime;
            totalWaitingTime += process.waitingTime;
        }

        System.out.println("FCFS Scheduling:");
        System.out.println("Process ID | Burst Time | Waiting Time | Turnaround Time");
        for (Process process : processes) {
            System.out.printf("%10d | %10d | %12d | %15d%n", process.id, process.burstTime, process.waitingTime, process.turnaroundTime);
        }

        System.out.printf("Average Waiting Time: %.2f%n", (double) totalWaitingTime / n);
        System.out.printf("Average Turnaround Time: %.2f%n", (double) totalWaitingTime / n + (double) totalWaitingTime / n);
    }

    // Method to calculate average waiting time and turnaround time for Round Robin
    public static void roundRobinScheduling(List<Process> processes, int timeQuantum) {
        Queue<Process> queue = new LinkedList<>(processes);
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;
        int time = 0;

        while (!queue.isEmpty()) {
            Process current = queue.poll();
            if (current.burstTime > timeQuantum) {
                time += timeQuantum;
                current.burstTime -= timeQuantum;
                queue.add(current); // Add back to the end of the queue
            } else {
                time += current.burstTime; // Process will finish
                current.waitingTime = time - current.burstTime - (time - current.waitingTime);
                current.turnaroundTime = time;
                totalWaitingTime += current.waitingTime;
                totalTurnaroundTime += current.turnaroundTime;
            }
        }

        System.out.println("\nRound Robin Scheduling:");
        System.out.println("Process ID | Waiting Time | Turnaround Time");
        for (Process process : processes) {
            System.out.printf("%10d | %12d | %15d%n", process.id, process.waitingTime, process.turnaroundTime);
        }

        System.out.printf("Average Waiting Time: %.2f%n", (double) totalWaitingTime / processes.size());
        System.out.printf("Average Turnaround Time: %.2f%n", (double) totalTurnaroundTime / processes.size());
    }

    public static void main(String[] args) {
        List<Process> processes = new ArrayList<>();
        
        // Example input for processes
        processes.add(new Process(1, 10)); // Process ID 1 with burst time 10
        processes.add(new Process(2, 5));  // Process ID 2 with burst time 5
        processes.add(new Process(3, 8));  // Process ID 3 with burst time 8

        int timeQuantum = 4; // Time quantum for Round Robin

        // Run FCFS Scheduling
        fcfsScheduling(processes);

        // Reset burst time for Round Robin
        processes.get(0).burstTime = 10;
        processes.get(1).burstTime = 5;
        processes.get(2).burstTime = 8;

        // Run Round Robin Scheduling
        roundRobinScheduling(processes, timeQuantum);
    }
}
