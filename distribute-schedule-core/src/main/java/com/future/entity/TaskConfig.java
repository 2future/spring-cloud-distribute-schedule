package com.future.entity;

/**
 * @Title TaskConfig
 * @Package com.future.entity
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 11:11 PM
 * @Created by mz
 */
public class TaskConfig {

    private String taskName;

    private String cron;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
