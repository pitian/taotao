package com.pitian.ArrayList;

import java.util.LinkedList;

/**
 * 队列:先进先出
 */
public class MyQueue {
    private LinkedList myQueue = new LinkedList();
    //入队
    public void push(Object o){
        myQueue.add(o);
    }
    //出队
    public Object pop(){
        return myQueue.removeFirst();
    }

    public Object peek(){
        return myQueue.getFirst();
    }
}
