package com.future.exception;

/**
 * @Title ObtainTaskException
 * @Package com.future.exception
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 2:53 PM
 * @Created by mz
 */
public class ObtainTaskException extends RuntimeException{

    private static String msg = "Obtain Task fail!";

    public ObtainTaskException() {
        super(msg);
    }

}
