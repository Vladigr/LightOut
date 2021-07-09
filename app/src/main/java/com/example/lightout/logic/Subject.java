package com.example.lightout.logic;

public abstract class Subject {
    protected Observer observer;
    public void attach(Observer observer){
        this.observer = observer;
    }
    abstract void notifyChange(int i, int j, boolean val);
}
