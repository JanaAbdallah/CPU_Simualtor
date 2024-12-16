# CPU Schedulers Simulator

## Project Overview
This project is an implementation of various CPU scheduling algorithms as part of the Operating Systems course (CS341) at Cairo University. The simulator demonstrates different scheduling techniques used in operating systems to manage process execution.

## Implemented Scheduling Algorithms
The project includes implementations of the following scheduling algorithms:

### Non-Preemptive Priority Scheduling
- Processes are scheduled based on their priority.
- Uses context switching.

### Non-Preemptive Shortest Job First (SJF)
- Processes are scheduled based on their burst time.
- Includes a solution to prevent starvation.

### Shortest Remaining Time First (SRTF)
- Preemptive scheduling algorithm.
- Processes are scheduled based on their remaining burst time.
- Includes a solution to prevent starvation.

### FCAI Scheduling (Custom Algorithm)
An adaptive scheduling algorithm that combines:
- **Priority**
- **Arrival time**
- **Remaining burst time**
- **Dynamic quantum allocation**
- Hybrid non-preemptive and preemptive execution

## Key Features
### Input Parameters for Each Process:
- **Process Name**
- **Process Color** (for graphical representation)
- **Arrival Time**
- **Burst Time**
- **Priority Number**

### Output Metrics:
- **Processes execution order**
- **Waiting Time for each process**
- **Turnaround Time for each process**
- **Average Waiting Time**
- **Average Turnaround Time**
- **Quantum time update history** (for FCAI Scheduling)

### Bonus:
- **Graphical representation of process execution**

## FCAI Scheduling Algorithm Details
The FCAI Scheduling algorithm uses a unique factor calculation:
Where:
- **V1** = Last arrival time of all processes / 10
- **V2** = Max burst time of all processes / 10

### Quantum Allocation Rules:
- Each process starts with a unique quantum.
- Quantum updates:
  - **Q = Q + 2** if the process completes its quantum.
  - **Q = Q + unused quantum** if the process is preempted.

### Execution Characteristics:
- Non-preemptive for the first 40% of the quantum.
- Preemption allowed after 40% execution.

## Technical Details
- **Programming Language:** Java
- **Course:** CS341 - Operating Systems 1
- **Institution:** Cairo University, Faculty of Computers & Artificial Intelligence


## How to Use
1. Compile the Java project.
2. Run the simulator.
3. Input process details when prompted.
4. View scheduling results and analysis.


## Contributors
- [Jana Abdullah] - Shortest- Remaining Time First (SRTF) Scheduling
- [Malak Sherif] - FCAI Scheduling
- [Raghad Ahmed] - Shortest- Job First (SJF) Scheduling
- [Afnan AbdelKareem] - Priority Scheduler
- [Afnan Sayed] - FCAI Scheduling
