package com.future.task;

/**
 * @Title TaskRunner
 * @Package com.future.task
 * @Description 任务
 * @Version 1.0.0
 * @Date 2023/3/25 1:36 PM
 * @Created by mz
 */
public interface TaskRunner {

    void run();

    /**
     * @param
     * @return java.lang.String
     * @desc 任务id(默认类名)
     * @author mz
     * @date 2023/3/25 1:39 PM
     */
    default String getTaskName() {
        return this.getClass().getName();
    }

    /**
     * @param
     * @return java.lang.String
     * @desc cron 表达式
     * @author mz
     * @date 2023/3/25 1:41 PM
     */
    default String getCron(){
        return null;
    };

}
