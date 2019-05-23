package com.li.activiti;

import org.activiti.engine.*;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

/**
 * @author licheng
 * @description
 * @create 2019/5/23 21:39
 */
public class FirstAct {

    public static void main(String[] args) {
        // 启动工作流程
        ProcessEngine engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();

        // 存储服务
        RepositoryService repositoryService = engine.getRepositoryService();
        // 运行时服务
        RuntimeService runtimeService = engine.getRuntimeService();
        //任务服务
        TaskService taskService = engine.getTaskService();

        // 发布流程
        repositoryService.createDeployment().addClasspathResource("first.bpmn").deploy();
        // 启动流程
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("myProcess_1");

        // 普通员工完成请假任务
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("当前流程节点：" +task.getName());
        taskService.complete(task.getId());

        // 经理审核任务
        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("当前流程节点：" +task.getName());
        taskService.complete(task.getId());

        // 经理审核完毕流程结束
        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("流程结束：" +task);

    }
}
