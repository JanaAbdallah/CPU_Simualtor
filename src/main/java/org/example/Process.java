package org.example;

import java.util.ArrayList;

public class Process implements Comparable<Process>{

    int id;
    int arrivalTime;
    int burstTime;
    int Priority;
    int completionTime;
    int turnaroundTime;
    int waitingTime;
    int remainingTime;

    private String Name;
    private int arrival;
    private int burst;
    private int priority;
    private int PQuantum;
    private int excTime;
    private int startT;
    private int endT;
    private int WaitingT;
    private int TurnaroundT;
    private int factor;
    private int remainingQuantum;
    private int totalExecTimeTillNow;

    public Process(int id, int arrivalTime, int burstTime, int priority) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.Priority = priority;
        this.remainingTime = burstTime;
    }

    ArrayList<Integer> QuantumHistory = new ArrayList<Integer>();

    public Process(String Name , int BurstTime , int ArrivalTime , int Priority)
    {
        this.Name = Name;
        this.burst = BurstTime;
        this.arrival = ArrivalTime;
        this.priority = Priority;
        startT = -1;
        setExcTime(burst);
    }

    ///setters///
    public void setBurst(int BT)
    {
        burst= BT;
    }

    public void setTotalExecTimeTillNow(int ex)
    {
        this.totalExecTimeTillNow=ex;
    }
    public int getTotalExecTimeTillNow()
    {
        return this.totalExecTimeTillNow;
    }

    public void setFactor(int f)
    {
        this.factor=f;
    }

    public void setWaitingT(int waitingTime) { WaitingT = waitingTime; }

    public void setTurnaroundT(int turnaroundTime)
    {
        TurnaroundT = turnaroundTime;
    }

    public void setExcTime(int ex)
    {
        excTime= ex;
    }

    public void setStartTime(int S)
    {
        startT=S;
    }

    public void setEndTime(int endTime) {
        endT = endTime;
    }
    public void setQuantum(int quantum) //updating history and setting quantum
    {
        QuantumHistory.add(PQuantum);
        this.PQuantum = quantum;
    }

    public void setRemainingQuantum(int quantum) //updating history and setting quantum
    {
        this.remainingQuantum = quantum;
    }
    public int getRemainingQuantum() //updating history and setting quantum
    {
        return this.remainingQuantum;
    }

    ////getters////
    public String getName() {
        return Name;
    }

    public int getBurst() {
        return burst;
    }

    public int getArrival() {
        return arrival;
    }

    public int getPriority() {
        return priority;
    }

    public int getWaitingT() {
        return WaitingT;
    }

    public int getTurnaroundT()
    {
        return TurnaroundT;
    }

    public int getQuantum()
    {
        return this.PQuantum;
    }

    public int getStart()
    {
        return startT;
    }

    public int getExcTime()
    {
        return excTime;
    }

    public int getFactor() {
        return this.factor;
    }

    @Override
    public int compareTo(Process o) {
        return this.getArrival() - o.getArrival();
    }
}

