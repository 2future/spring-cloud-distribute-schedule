package com.future.exception;

/**
 * @Title CronException
 * @Package com.future.exception
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 1:40 PM
 * @Created by mz
 */
public class CronException extends RuntimeException {

    private static String msg = "cron is illegal!";

    public CronException(String message) {
        super(message);
    }

    public CronException() {
        super(msg);
    }

}
