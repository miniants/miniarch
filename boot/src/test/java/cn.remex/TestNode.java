package cn.remex;

import cn.remex.core.RemexApplication;
import cn.remex.db.ContainerFactory;
import cn.remex.db.DbRvo;
import cn.remex.db.model.cert.AuthUser;
import cn.remex.db.rsql.sqlutil.Node;
import org.junit.BeforeClass;
import org.junit.Test;
import teep.models.ClassroomInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangy on 2016/1/16 0016.
 */
public class TestNode {
//    @BeforeClass
//    public static void before(){
//        RemexApplication.getContext();
//    }
//    @Test
//    public void testWithColumn(){
//        DbRvo dbRvo = ContainerFactory.getSession().createDbCvo(AuthUser.class).withColumns(a -> {
//            a.getName();
//            a.getId();
//        }).ready().query();
//
//        System.out.println(dbRvo.getRecordCount());
//    }



    @Test
    public void testNode() {
        Node<ClassroomInfo> node = new Node<ClassroomInfo>(ClassroomInfo.class);

        node.addIndex("name");
        node.addIndex("classroomSchedules[].classroomInfo.id");
        node.addIndex("classroomSchedules[].id");
        node.addIndex("buildingInfo.name");
        node.addIndex("buildingInfo.id");

        ClassroomInfo ci = new ClassroomInfo();
        Map<String,Object> list;

        list = new HashMap<>();
        node.start(ci,list);
        list.put("name", "aaa");
        list.put("classroomSchedules[].classroomInfo.id", "ddd");
        list.put("classroomSchedules[].id", "ddddddd");
        list.put("buildingInfo.name", "ccc");
        list.put("buildingInfo.id", "111ccc");
        list.forEach((k, v) -> node.eval(k, v));
        node.evalEnd();

        list = new HashMap<>();
        node.start(ci,list);
        list.put("classroomSchedules[].classroomInfo.id", "aaaaa");
        list.put("classroomSchedules[].id", "ccccc");
        list.put("buildingInfo.name","ccc");
        list.put("buildingInfo.id", "111ccc");
        list.forEach((k,v)->node.eval(k,v));



        System.out.println();
    }
}
