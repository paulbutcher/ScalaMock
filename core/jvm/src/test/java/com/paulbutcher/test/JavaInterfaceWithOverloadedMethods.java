package com.paulbutcher.test;

import java.util.concurrent.Callable;

public interface JavaInterfaceWithOverloadedMethods<T> {
    void send(T record);
    void send(T record, Callable<T> onComplete);
}
