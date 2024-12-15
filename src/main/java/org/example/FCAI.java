package org.example;

import java.util.*;

public class FCAI
{
    private double V1;
    private double V2;
    private List<Process> processes;
    private List<Process> statistics;

    public FCAI(List<Process> p)
    {
        processes=p;
        statistics = new ArrayList<>(p);
        V1 = calcV1();
        V2 = calcV2();
        for (Process process : processes)
        {
            int calc=calculateFCAIFactor(process);
            process.setFactor(calc);
        }
    }

    public double calcV1()
    {
        int lastArrival=0;
        //retrieve the max arrival time which is the last arrival
        for (Process process : processes)
        {
            if (process.getArrival() > lastArrival)
                lastArrival = process.getArrival();
        }
        return lastArrival/10.0;
    }


    public double calcV2()
    {
        int maxBurst=0;
        //retrieve the max burst
        for (Process process : processes)
        {
            if (process.getBurst() > maxBurst)
                maxBurst = process.getBurst();
        }
        return maxBurst /10.0;
    }

    public int calculateFCAIFactor(Process process)
    {
        int term1 = 10-process.getPriority();
        int term2 =(int) Math.ceil(process.getArrival()/V1) ;
        int term3 = (int) Math.ceil(process.getBurst()/V2);
        process.setFactor(term1+term2 +term3);
        return process.getFactor();
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void logic()
    {
        Queue<Process> queue = new ArrayDeque<>();
        int time = 0;
        Process workOn = null;

        // Print the header for the detailed execution timeline
        System.out.printf("%-10s %-10s %-10s %-20s %-10s %-10s %-40s\n",
                "Time", "Process", "Exec Time", "Remaining Burst", "Quantum", "Factor", "Action");

        // Add processes that have arrived by the current time to the queue

        queue.add(processes.get(0));

        while (true)
        {
            // If no process is being worked on, fetch the next process from the queue
            if (workOn == null || workOn.getBurst() == 0)
            {
                if (queue.isEmpty()) // No processes left
                {
                    // Print the final process statistics
                    System.out.println("\nFinal Process Statistics:");
                    float totalWaiting=0, totalTurnaround=0;
                    for (Process process : statistics)
                    {
                        System.out.println
                                ( "Process " + process.getName()
                                        + " | Waiting Time: " + process.getWaitingT()
                                        + " | Turnaround Time: " + process.getTurnaroundT()
                                );
                        totalWaiting+=process.getWaitingT();
                        totalTurnaround+=process.getTurnaroundT();
                    }

                    float avgWaiting, avgTurnaround;
                    avgWaiting=  totalWaiting /statistics.size();
                    avgTurnaround=  totalTurnaround /statistics.size();
                    System.out.println
                            ( "Average Waiting Time: " + avgWaiting
                                    + " | Average Turnaround Time: " +avgTurnaround);
                    break;
                }

                workOn = queue.poll();

                // ده بيحصل بس مع اول واحده
                if (workOn.getStart() == -1)
                {
                    workOn.setStartTime(time);
                }
            }

            int originalQuantum = workOn.getQuantum();
            workOn.setRemainingQuantum(originalQuantum);
            int startTime = time;
            workOn.setStartTime(startTime);
            int totalExecTime=0;

            while (true)
            {
                // Calculate 40% of the quantum or remaining burst, whichever is smaller
                int remainingQ = workOn.getRemainingQuantum();
                int executeTime = (int) Math.ceil(0.4 * remainingQ);

                executeTime = Math.min(executeTime, workOn.getBurst());
                totalExecTime+=executeTime;
                workOn.setTotalExecTimeTillNow(totalExecTime);

                remainingQ -= executeTime;

                workOn.setRemainingQuantum(remainingQ);

                time += executeTime;
                workOn.setEndTime(time);

                // Update burst time and FCAI factor
                int burstBefore = workOn.getBurst();
                workOn.setBurst(burstBefore - executeTime);
                int burstAfter = workOn.getBurst();

                int factorBefore = workOn.getFactor();
                workOn.setFactor(calculateFCAIFactor(workOn));
                int factorAfter = workOn.getFactor();

                for (Process process : processes)
                {
                    if (process.getArrival() <= time && !queue.contains(process) && process!=workOn && process.getBurst() > 0)
                    {
                        queue.add(process);
                    }
                }
////////////////////////////////////////////////////////////////////////////////////

                // Check for preemption: find the least FCAI factor in the queue
                Process pOfLeastFactor = workOn;
                int leastFactor = workOn.getFactor();

                if(!queue.isEmpty())
                {
                    for (Process process : queue)
                    {
                        if (process.getFactor() < leastFactor ||
                                (process.getFactor() == leastFactor && process != workOn))
                        {
                            leastFactor = process.getFactor();
                            pOfLeastFactor = process;
                        }
                    }

                }
                ////////////////////////////////////////////////////////////////////

                //condition1
                // خلصت الكوانتم ولسة باقيلها بيرست
                if (workOn.getBurst() > 0 && workOn.getRemainingQuantum()<=0)
                {
                    if(pOfLeastFactor != workOn)
                    {
                        workOn.setQuantum(originalQuantum + 2);

                        // Preempt the current process
                        queue.add(workOn);
                        workOn = pOfLeastFactor;
                        queue.remove(pOfLeastFactor);

                        System.out.printf("%-10s %-10s %-10d %-20d %-10s %-10s %-40s\n",
                                startTime + "-" + time, workOn.getName(), executeTime, burstAfter,
                                originalQuantum + " → " + workOn.getQuantum(),
                                factorBefore + " → " + factorAfter,
                                burstAfter > 0 ? "Process continues" : "Process completes");
                        break;
                    }
                    else
                    {
                        workOn.setQuantum(originalQuantum + 2);

                        // Preempt the current process
                        queue.add(workOn);


                        System.out.printf("%-10s %-10s %-10d %-20d %-10s %-10s %-40s\n",
                                startTime + "-" + time, workOn.getName(), workOn.getTotalExecTimeTillNow(),  workOn.getBurst(),
                                originalQuantum + " → " + workOn.getQuantum(), factorAfter,
                                burstAfter > 0 ? "Process continues" : "Process completes");

                        workOn = queue.poll();
                        break;
                    }
                }

                else if (pOfLeastFactor != workOn && workOn.getBurst() > 0 && originalQuantum > workOn.getTotalExecTimeTillNow())
                {
                    int unusedQuantum = originalQuantum-workOn.getTotalExecTimeTillNow();

                    workOn.setQuantum(originalQuantum + unusedQuantum);

                    // Preempt the current process
                    queue.add(workOn);
                    System.out.printf("%-10s %-10s %-10d %-20d %-10s %-10s %-40s\n",
                            startTime + "-" + time, workOn.getName(), workOn.getTotalExecTimeTillNow(),  workOn.getBurst(),
                            originalQuantum + " → " + workOn.getQuantum(), factorAfter,
                            burstAfter > 0 ? "Process continues" : "Process completes");

                    workOn = pOfLeastFactor;
                    queue.remove(pOfLeastFactor);
                    break;
                }
//////////////////////////////////////////////////////////////////////////////


                // Finalize process if it completes
                //condition2
                else if (workOn.getBurst() <= 0)
                {
                    workOn.setEndTime(time);
                    workOn.setTurnaroundT(time - workOn.getArrival());
                    workOn.setWaitingT(workOn.getTurnaroundT() - workOn.getExcTime());

                    System.out.printf("%-10s %-10s %-10d %-20d %-10s %-10s %-40s\n",
                            startTime + "-" + time, workOn.getName(), workOn.getTotalExecTimeTillNow(),  workOn.getBurst(),
                            "complete", "complete", "Process completes");

                    processes.remove(workOn);
                    workOn = null;
                    break;
                }
            }
        }
    }
}