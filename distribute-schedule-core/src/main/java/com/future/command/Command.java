package com.future.command;

/**
 * @Title Command
 * @Package com.future.command
 * @Description
 * @Version 1.0.0
 * @Date 2023/3/25 2:00 PM
 * @Created by mz
 */
public interface Command {

    /**
     * @param serverId
     * @param taskName
     * @return boolean
     * @desc 获取任务是否成功
     * @author mz
     * @date 2023/3/25 2:02 PM
     */
    boolean isObtain(String serverId, String taskName);

    /**
     * @param serverId
     * @param taskName
     * @return boolean
     * @desc 移除任务
     * @author mz
     * @date 2023/3/25 2:43 PM
     */
    boolean remove(String serverId, String taskName);


}
