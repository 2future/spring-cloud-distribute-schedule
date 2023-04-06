package com.future;

import com.future.command.Command;
import com.future.command.RedisCommand;
import com.future.entity.TaskConfig;
import com.future.pod.PodNumber;
import com.future.properties.TaskDataConfigProperties;
import com.future.task.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title JobAutoConfig
 * @Package com.future.properties
 * @Description job starter
 * @Version 1.0.0
 * @Date 2023/4/6 9:12 AM
 * @Created by mz
 */
@ConditionalOnBean(value = {TaskRunner.class, JedisPool.class, DiscoveryClient.class})
@Configuration
@EnableConfigurationProperties(TaskDataConfigProperties.class)
public class JobAutoConfig {


    @Bean
    @ConditionalOnBean(value = {JedisPool.class})
    public Command createCommand(@Autowired JedisPool jedisPool) {
        Command command = new RedisCommand(jedisPool);
        return command;
    }


    @Bean
    @ConditionalOnBean(value = {DiscoveryClient.class})
    public PodNumber createPodNumber(@Autowired DiscoveryClient discoveryClient) {
        PodNumber podNumber = new PodNumber(discoveryClient);
        return podNumber;
    }


    @Bean
    @ConditionalOnBean(value = {TaskRunner.class})
    public TaskManager createTaskManager(@Autowired Command command, @Value("${spring.application.name}") String currentServiceId,
                                         @Autowired List<TaskRunner> taskRunners) {
        TaskManager taskManager = new TaskManager(command, currentServiceId, taskRunners);
        return taskManager;
    }


    @Bean
    @ConditionalOnProperty(prefix = "com.too-future.task")
    public TaskDataSource taskDataSource(@Autowired(required = false) TaskDataConfigProperties taskDataConfigProperties) {
        List<TaskConfig> list = taskDataConfigProperties.getList();
        List<Map<String, String>> configMap = new ArrayList<>();
        for (TaskConfig taskConfig : list) {
            Map<String, String> configMapItem = new HashMap<>();
            configMapItem.put("taskName", taskConfig.getTaskName());
            configMapItem.put("cron", taskConfig.getCron());
            configMap.add(configMapItem);
        }
        TaskDataSource taskDataSource = new TaskDataSourceFromProperties(configMap);
        return taskDataSource;
    }


    @Bean
    public TaskBalance createTaskBalance(@Autowired TaskManager taskManager, @Autowired PodNumber podNumber,
                                         @Autowired TaskDataSource taskDataSource,
                                         @Value("${spring.application.name}") String currentServiceId) {
        TaskBalance taskBalance = new TaskBalance(taskManager, podNumber, taskDataSource, currentServiceId);
        return taskBalance;
    }

}
