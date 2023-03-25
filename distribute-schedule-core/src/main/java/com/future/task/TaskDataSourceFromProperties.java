package com.future.task;

import com.future.entity.TaskConfig;

import java.util.List;

/**
 * @Title TaskDataSourceFromProperties
 * @Package com.future.task
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 11:10 PM
 * @Created by mz
 */
public class TaskDataSourceFromProperties implements TaskDataSource{

    @Override
    public List<TaskConfig> getTaskList() {
        return null;
    }

    @Override
    public TaskConfig getByName(String taskName) {
        return null;
    }

    @Override
    public int getTaskNum() {
        return 0;
    }
}
