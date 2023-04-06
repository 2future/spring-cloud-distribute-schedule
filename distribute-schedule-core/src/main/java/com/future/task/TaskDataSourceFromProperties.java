package com.future.task;

import com.future.entity.TaskConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Title TaskDataSourceFromProperties
 * @Package com.future.task
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 11:10 PM
 * @Created by mz
 */
public class TaskDataSourceFromProperties implements TaskDataSource {

    private List<Map<String, String>> configMap;

    private List<TaskConfig> taskConfigs = new ArrayList<>();

    public TaskDataSourceFromProperties(List<Map<String, String>> configMap) {
        if (configMap == null || configMap.size() == 0) {
            throw new NullPointerException("task config is null");
        }
        this.configMap = configMap;
        for (Map<String, String> taskConfigMap : configMap) {
            TaskConfig taskConfig = new TaskConfig();
            taskConfig.setTaskName(taskConfigMap.get("taskName"));
            taskConfig.setCron(taskConfigMap.get("cron"));
            taskConfigs.add(taskConfig);
        }
    }

    @Override
    public List<TaskConfig> getTaskList() {
        return taskConfigs;
    }

}
