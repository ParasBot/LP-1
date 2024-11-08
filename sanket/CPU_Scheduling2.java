import java.util.*;

class Process {
    int id;              // Process ID
    int burstTime;      // Burst time of the process
    int arrivalTime;     // Arrival time of the process
    int priority;       // Priority of the process
    int completionTime;  // Completion time of the process
    int turnaroundTime;  // Turnaround time of the process
    int waitingTime;     // Waiting time of the process

    public Process(int id, int burstTime, int arrivalTime, int priority) {
        this.id = id;
        this.burstTime = burstTime;
        this.arrivalTime = arrivalTime;
        this.priority = priority;
    }
}

public class CPU_Scheduling2 {

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

    // Priority Scheduling Algorithm (Non-Preemptive)
    public static void priorityScheduling(List<Process> processes) {
        int currentTime = 0;
        int completed = 0;
        int n = processes.size();
        boolean[] isCompleted = new boolean[n];

        System.out.println("\nPriority Scheduling (Non-Preemptive):");
        while (completed < n) {
            int idx = -1;
            int highestPriority = Integer.MAX_VALUE;

            // Find the process with the highest priority that has arrived
            for (int i = 0; i < n; i++) {
                if (processes.get(i).arrivalTime <= currentTime && !isCompleted[i]) {
                    if (processes.get(i).priority < highestPriority) {
                        highestPriority = processes.get(i).priority;
                        idx = i;
                    }
                }
            }

            // If no process is available, idle the CPU
            if (idx == -1) {
                currentTime++;
                continue;
            }

            Process currentProcess = processes.get(idx);
            currentProcess.completionTime = currentTime + currentProcess.burstTime;
            currentProcess.turnaroundTime = currentProcess.completionTime - currentProcess.arrivalTime;
            currentProcess.waitingTime = currentProcess.turnaroundTime - currentProcess.burstTime;

            isCompleted[idx] = true; // Mark process as completed
            currentTime += currentProcess.burstTime; // Update current time
            completed++;

            System.out.printf("Process %d: CT = %d, TAT = %d, WT = %d%n", currentProcess.id,
                    currentProcess.completionTime, currentProcess.turnaroundTime, currentProcess.waitingTime);
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
            System.out.print("Enter Priority for Process " + (i + 1) + ": ");
            int priority = scanner.nextInt();
            processes.add(new Process(i + 1, burstTime, arrivalTime, priority));
        }

        // Sort processes based on arrival time for FCFS
        processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

        // Call FCFS scheduling
        fcfsScheduling(processes);

        // Call Priority scheduling
        priorityScheduling(processes);

        scanner.close();
    }
}
