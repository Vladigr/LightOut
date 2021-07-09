package com.example.lightout.logic;

public abstract class Subject {
    protected Observer observer;
    public void attach(Observer observer){
        this.observer = observer;
    }
    public void notifyChange(int i, int j, boolean val){
        observer.update(i, j, val);
    };
    public void notifyEnd(){
        observer.updateEnd();
    };
}
