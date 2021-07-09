package com.example.lightout.logic;

public interface Observer {
    void update(int i, int j, boolean val);
    void updateEnd();
}
