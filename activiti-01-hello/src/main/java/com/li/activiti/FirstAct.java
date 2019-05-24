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
        // 创建流程引擎
        ProcessEngine engine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml").buildProcessEngine();

        // 获得流程存储服务组件
        RepositoryService repositoryService = engine.getRepositoryService();
        // 获得运行时服务组件
        RuntimeService runtimeService = engine.getRuntimeService();
        //获得流程任务组件
        TaskService taskService = engine.getTaskService();

        // 发布流程文件
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

        //关闭流程引擎
        engine.close();

    }
}
