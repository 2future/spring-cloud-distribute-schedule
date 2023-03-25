package com.future.pod;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;

import java.util.List;

/**
 * @Title PodNumber
 * @Package com.future.pod
 * @Description 节点数获取
 * @Version 1.0.0
 * @Date 2023/3/25 1:30 PM
 * @Created by mz
 */
public class PodNumber {

    private DiscoveryClient discoveryClient;

    public PodNumber(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    /**
     * @param serviceId
     * @return int
     * @desc 获取制定服务的节点数
     * @author mz
     * @date 2023/3/25 1:33 PM
     */
    public int get(String serviceId) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return instances != null ? instances.size() : 0;
    }

    /**
     * @param serviceId
     * @param allTasksNum
     * @return int
     * @desc 获取当前节点能拥有的最大任务数
     * @author mz
     * @date 2023/3/25 1:33 PM
     */
    public int getTaskNum(String serviceId, int allTasksNum) {
        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
        return instances == null || instances.size() == 0 ? allTasksNum : allTasksNum / instances.size();
    }

}
