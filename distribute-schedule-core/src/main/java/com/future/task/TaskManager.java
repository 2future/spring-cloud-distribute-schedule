package com.future.task;

import com.future.command.Command;
import org.springframework.scheduling.support.CronTrigger;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Title TashManager
 * @Package com.future.task
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 4:11 PM
 * @Created by mz
 */
public class TaskManager {

    private CustomTaskScheduler customTaskScheduler;

    private Command command;

    private String currentServiceId;

    private List<TaskRunner> taskRunners;

    private volatile ConcurrentHashMap<String, Runnable> concurrentJobMap = new ConcurrentHashMap();

    public TaskManager(Command command, String currentServiceId, List<TaskRunner> taskRunners) {
        this.customTaskScheduler = new CustomTaskScheduler();
        this.customTaskScheduler.setPoolSize(50);
        this.customTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        this.command = command;
        this.currentServiceId = currentServiceId;
        this.taskRunners = taskRunners;
    }

    /**
     * @param taskRunner
     * @return void
     * @desc 创建任务
     * @author mz
     * @date 2023/3/25 4:22 PM
     */
    public boolean create(TaskRunner taskRunner, String cron) {
        boolean obtain = command.isObtain(currentServiceId, taskRunner.getTaskName());
        //抢占任务成功 开始创建任务
        if (obtain) {
            Runnable runnable = () -> taskRunner.run();
            String processCron = cron != null ? cron : taskRunner.getCron();
            CronTrigger cronTrigger = new CronTrigger(processCron);
            customTaskScheduler.schedule(runnable, cronTrigger);
            concurrentJobMap.put(taskRunner.getTaskName(), runnable);
            return true;
        }
        return false;
    }

    /**
     * @param taskName
     * @return void
     * @desc 停止任务
     * @author mz
     * @date 2023/3/25 4:21 PM
     */
    public void stop(String taskName) {
        boolean commandRemoved = command.remove(currentServiceId, taskName);
        if (commandRemoved) {
            Runnable remove = concurrentJobMap.remove(taskName);
            if (remove != null) {
                customTaskScheduler.cancelTask(remove);
            }
        }
    }

    /**
     * @param
     * @return java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.Runnable>
     * @desc 获取当前任务列表
     * @author mz
     * @date 2023/3/25 4:26 PM
     */
    public HashMap<String, Runnable> havingCurrentJob() {
        HashMap<String, Runnable> hashMapReturn = new HashMap<>();
        for (String s : concurrentJobMap.keySet()) {
            hashMapReturn.put(s, concurrentJobMap.get(s));
        }
        return hashMapReturn;
    }

    /**
     * @param taskName
     * @return com.future.task.TaskRunner
     * @desc taskName 获取代码中定义任务
     * @author mz
     * @date 2023/3/25 11:30 PM
     */
    public TaskRunner getRunnerByName(String taskName) {
        if (taskRunners == null || taskRunners.size() == 0) {
            return null;
        }
        for (TaskRunner taskRunner : taskRunners) {
            if (Objects.equals(taskName, taskRunner.getTaskName())) {
                return taskRunner;
            }
        }
        return null;
    }

}
