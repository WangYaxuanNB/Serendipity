package com.sum.base;

public class SelectionModel<T> {

    private T t;

    public void setValue(T t){
        this.t = t;
    }

    public T getValue(){
        return t;
    }
}
