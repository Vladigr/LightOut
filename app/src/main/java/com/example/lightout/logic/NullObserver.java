package com.example.lightout.logic;


//NullObserver : in case we want to change the board and the UI is not relevant
//we swap with the boardObserver with one instance of null observer (in random)
public class NullObserver implements Observer {
    @Override
    public void update(int i, int j, boolean val) {

    }

    @Override
    public void updateEnd() {

    }
}
