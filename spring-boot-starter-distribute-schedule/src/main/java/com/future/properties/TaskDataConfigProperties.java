package com.future.properties;

import com.future.entity.TaskConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Title TaskDataConfigProperties
 * @Package com.future.properties
 * @Description
 * @Version 1.0.0
 * @Date 2023/4/6 9:53 AM
 * @Created by mz
 */
@ConfigurationProperties(prefix = "com.too-future.task")
public class TaskDataConfigProperties {

    private List<TaskConfig> list;

    public List<TaskConfig> getList() {
        return list;
    }

    public void setList(List<TaskConfig> list) {
        this.list = list;
    }
}
