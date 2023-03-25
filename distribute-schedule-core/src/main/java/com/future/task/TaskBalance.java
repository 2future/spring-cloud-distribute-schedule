package com.future.task;

import com.future.entity.TaskConfig;
import com.future.pod.PodNumber;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Title TaskBalance
 * @Package com.future.task
 * @Description 任务平衡器
 * @Version 1.0.0
 * @Date 2023/3/25 3:15 PM
 * @Created by mz
 */
public class TaskBalance {

    private TaskManager taskManager;

    private PodNumber podNumber;

    private TaskDataSource taskDataSource;

    private String serviceId;

    public TaskBalance(TaskManager taskManager, PodNumber podNumber, TaskDataSource taskDataSource, String serviceId) {
        this.taskManager = taskManager;
        this.podNumber = podNumber;
        this.taskDataSource = taskDataSource;
        this.serviceId = serviceId;
        balanceCreate();
        balanceRemove();
    }

    /**
     * @param
     * @return void
     * @desc 创建任务
     * @author mz
     * @date 2023/3/25 11:26 PM
     */
    private void balanceCreate() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(3);
        executorService.scheduleAtFixedRate(() -> {
            List<TaskConfig> taskList = taskDataSource.getTaskList();
            if (taskList == null || taskList.size() == 0) {
                return;
            }
            // 进行任务创建
            for (TaskConfig taskConfig : taskList) {
                TaskRunner runnerByName = taskManager.getRunnerByName(taskConfig.getTaskName());
                if (runnerByName != null) {
                    taskManager.create(runnerByName,taskConfig.getCorn());
                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }


    private void balanceRemove() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(3);
        executorService.scheduleAtFixedRate(() -> {
            HashMap<String, Runnable> stringRunnableHashMap = taskManager.havingCurrentJob();
            int currentJobSize = stringRunnableHashMap.size();
            int taskNum = taskDataSource.getTaskNum();
            int podNum = podNumber.get(serviceId);
            if (podNum == 0) {
                return;
            }
            int avgTaskNum = taskNum / podNum;
            if (currentJobSize > avgTaskNum) {
                int i1 = currentJobSize - avgTaskNum;
                int cancelNum = 0;
                for (String s : stringRunnableHashMap.keySet()) {
                    if (cancelNum == i1) {
                        break;
                    }
                    //开始停止任务
                    taskManager.stop(s);
                    cancelNum++;
                }
            }
        }, 1, 10, TimeUnit.SECONDS);
    }


}
