package com.li.activiti;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.GroupQuery;

import java.util.List;

/**
 * @author hcf
 * @date 2019/5/24 15:41
 */
public class SaveGroup {

    public static void main(String[] args) {

        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();

        IdentityService identityService = engine.getIdentityService();
        // 插入5条数据到
        // createGroup(identityService, "1", "GroupA", "typeA");
        // createGroup(identityService, "2", "GroupB", "typeB");
        // createGroup(identityService, "3", "GroupC", "typeC");
        // createGroup(identityService, "4", "GroupD", "typeD");
        // createGroup(identityService, "5", "GroupE", "typeE");

        System.out.println("====================查询=========================");
        // 查询 所有group列表
        // List<Group> groups = identityService.createGroupQuery().list();
        // 从第2条开始查询，查3条
        List<Group> groups = identityService.createGroupQuery().listPage(2,3);

        groups.forEach(g->{
            System.out.println("id："+g.getId()+"---> name: "+g.getName());
        });

        Group group = identityService.createGroupQuery().groupId("1").singleResult();
        System.out.println("根据id查询单个对象：" + "id："+group.getId()+"---> name: "+group.getName());

        System.out.println("====================统计=========================");
        // 统计
        long size = identityService.createGroupQuery().count();
        System.out.println("共有"+size+"条数据");
        // 自定义sql统计
        long count = identityService.createNativeGroupQuery()
                // 自定义sql
                .sql("select count(*) from act_id_group where ID_ = #{id}")
                // 参数设置
                .parameter("id","1")
                .count();
        System.out.println("自定义sql查询，并设置参数。共有"+count+"条数据");

        System.out.println("====================排序=========================");
        List<Group> groupList = identityService.createGroupQuery()
                .orderByGroupId().desc()
                .orderByGroupName().asc()
                .list();
        for (Group g : groupList) {
            System.out.println("id降序，name升序："+g.getId()+"--->"+g.getName());
        }


    }

    /**
     * 保存Group数据到act_id_group
     * @param identityService
     * @param id
     * @param name
     * @param type
     */
    private static void createGroup(IdentityService identityService, String id, String name, String type) {
        Group group = identityService.newGroup(id);
        group.setName(name);
        group.setType(type);
        // 保存数据到act_id_group表
        identityService.saveGroup(group);
    }
}
