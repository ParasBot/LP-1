import java.util.*;

class Process {
    int id;        // Process ID
    int burstTime; // Burst time of the process
    int arrivalTime; // Arrival time of the process
    int remainingTime; // Remaining time of the process
    int completionTime; // Completion time of the process
    int turnaroundTime; // Turnaround time of the process
    int waitingTime; // Waiting time of the process

    public Process(int id, int burstTime, int arrivalTime) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.remainingTime = burstTime; // Initially remaining time equals burst time
    }
}

public class CPU_Scheduling1 {

    // FCFS Scheduling Algorithm
    public static void fcfsScheduling(List<Process> processes) {
        int currentTime = 0;

        System.out.println("FCFS Scheduling:");
        for (Process process : processes) {
            if (currentTime < process.arrivalTime) {
                currentTime = process.arrivalTime; // CPU remains idle until the process arrives
            }
            process.completionTime = currentTime + process.burstTime;
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;

            currentTime += process.burstTime; // Update current time
            System.out.printf("Process %d: CT = %d, TAT = %d, WT = %d%n", process.id,
                    process.completionTime, process.turnaroundTime, process.waitingTime);
        }
    }

    // SJF Scheduling Algorithm (Preemptive)
    public static void sjfScheduling(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        int[] waitingTime = new int[n];
        int[] completionTime = new int[n];
        
        PriorityQueue<Process> queue = new PriorityQueue<>(Comparator.comparingInt(p -> p.remainingTime));

        while (completed < n) {
            // Add processes that have arrived to the queue
            for (Process process : processes) {
                if (process.arrivalTime <= currentTime && process.remainingTime > 0) {
                    queue.add(process);
                }
            }

            if (queue.isEmpty()) {
                currentTime++; // If no process is available, idle the CPU
                continue;
            }

            Process currentProcess = queue.poll(); // Get the process with the smallest remaining time
            currentProcess.remainingTime--;

            // If a process is finished
            if (currentProcess.remainingTime == 0) {
                currentProcess.completionTime = currentTime + 1;
                waitingTime[currentProcess.id - 1] = currentTime + 1 - currentProcess.arrivalTime - currentProcess.burstTime;
                completed++;
            }

            currentTime++; // Move to the next time unit
        }

        // Print the results
        System.out.println("\nSJF (Preemptive) Scheduling:");
        for (Process process : processes) {
            process.turnaroundTime = process.completionTime - process.arrivalTime;
            process.waitingTime = process.turnaroundTime - process.burstTime;
            System.out.printf("Process %d: CT = %d, TAT = %d, WT = %d%n", process.id,
                    process.completionTime, process.turnaroundTime, process.waitingTime);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of processes: ");
        int n = scanner.nextInt();
        
        List<Process> processes = new ArrayList<>();

        // Input process details
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Burst Time for Process " + (i + 1) + ": ");
            int burstTime = scanner.nextInt();
            System.out.print("Enter Arrival Time for Process " + (i + 1) + ": ");
            int arrivalTime = scanner.nextInt();
            processes.add(new Process(i + 1, burstTime, arrivalTime));
        }

        // Sort processes based on arrival time for FCFS
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        // Call FCFS scheduling
        fcfsScheduling(processes);

        // Reset process remaining time for SJF
        for (Process process : processes) {
            process.remainingTime = process.burstTime;
        }

        // Call SJF scheduling
        sjfScheduling(processes);

        scanner.close();
    }
}
