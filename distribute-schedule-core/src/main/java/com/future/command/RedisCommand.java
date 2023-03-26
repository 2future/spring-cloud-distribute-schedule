package com.future.command;

import com.future.exception.ObtainTaskException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Title RedisCommand
 * @Package com.future.command
 * @Description redis 控制集群节点列表
 * @Version 1.0.0
 * @Date 2023/3/25 2:00 PM
 * @Created by mz
 */
public class RedisCommand implements Command {

    private Jedis jedis;

    private String prefix = "spring:cloud;distribute:schedule";

    private static ConcurrentHashMap<String, String> havingTask = new ConcurrentHashMap<>();

    public RedisCommand(Jedis jedis) {
        this.jedis = jedis;
        this.renewTime();
    }

    @Override
    public boolean isObtain(String serverId, String taskName) {
        String key = prefix + ":" + serverId + ":" + taskName;
        SetParams setParams = new SetParams();
        setParams.nx().ex(20);
        String set = jedis.set(key, "true", setParams);
        boolean b = "ok".equalsIgnoreCase(set);
        if (b) {
            String ok = havingTask.put(key, "ok");
            if (ok == null || ok.length() == 0) {
                for (int i = 0; i < 5; i++) {
                    ok = havingTask.put(key, "ok");
                    if (ok != null && ok.length() != 0) {
                        return true;
                    }
                    throw new ObtainTaskException();
                }
            }
        }
        return b;
    }

    /**
     * @param
     * @return void
     * @desc 续约时间
     * @author mz
     * @date 2023/3/25 2:43 PM
     */
    private void renewTime() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(3);
        executorService.scheduleAtFixedRate(() -> {
            for (String key : havingTask.keySet()) {
                jedis.expire(key, 20);
            }
        }, 10, 10, TimeUnit.SECONDS);
    }

    /**
     * @param serverId
     * @param taskName
     * @return boolean
     * @desc 移除任务
     * @author mz
     * @date 2023/3/25 2:43 PM
     */
    @Override
    public boolean remove(String serverId, String taskName) {
        String key = prefix + ":" + serverId + ":" + taskName;
        String remove = havingTask.remove(key);
        return remove != null && remove.length() > 0;
    }

}



