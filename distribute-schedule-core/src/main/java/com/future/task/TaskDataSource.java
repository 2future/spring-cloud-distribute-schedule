package com.future.task;

import com.future.entity.TaskConfig;

import java.util.List;

/**
 * @Title TaskDataSource
 * @Package com.future.task
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 11:10 PM
 * @Created by mz
 */
public interface TaskDataSource {

  List<TaskConfig> getTaskList();

}
