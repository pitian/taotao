package com.pitian.ArrayList;

import java.util.LinkedList;

/**
 * 栈,先进后出
 */
public class MyStack {
    private LinkedList linkedList = new LinkedList();

    //入栈
    public void  push(Object o){
        linkedList.add(o);
    }
    //出栈
    public Object pop(){
        if(linkedList.isEmpty())
            return null;
        return linkedList.removeLast();
    }
    //栈第一个
    public Object peek(){
        return linkedList.getLast();
    }
}
