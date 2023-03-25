package com.future.task;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.ScheduledMethodRunnable;

import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @Title CustomTaskScheduler
 * @Package com.yl.config
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/20 3:07 PM
 * @Created by mz
 */
public class CustomTaskScheduler extends ThreadPoolTaskScheduler {

    private final Map<Object, ScheduledFuture<?>> scheduledTasks = new IdentityHashMap<>();


   public void cancelTask(Object identifier) {
        ScheduledFuture future = scheduledTasks.get(identifier);
        if (null != future) {
            future.cancel(true);
        }
    }


    @Override
    public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
        ScheduledFuture<?> future = super.schedule(task, trigger);
        scheduledTasks.put(task, future);
        return future;
    }

    /**
     * call parent method and store the result Future for cancel task,
     * you can expand other method of you used.
     *
     * @param task   the task need to be executed
     * @param period the time between two continues execute
     * @return the result of task
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, period);

        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        // Scheduled annotation only can used for no arguments method so hashCode plus method name is unique.
        scheduledTasks.put(runnable.getTarget(), future);

        return future;
    }

    /**
     * call parent method and store the result Future for cancel task,
     * you can expand other method of you used.
     *
     * @param task      the task need to be executed
     * @param startTime the task first executed time
     * @param period    the time between two continues execute
     * @return the result of task
     */
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(task, startTime, period);

        ScheduledMethodRunnable runnable = (ScheduledMethodRunnable) task;
        // Scheduled annotation only can used for no arguments method so hashCode plus method name is unique.
        scheduledTasks.put(runnable.getTarget(), future);
        return future;
    }

}
