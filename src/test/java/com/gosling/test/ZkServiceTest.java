package com.gosling.test;

import org.apache.zookeeper.*;

/**
 * Created by gaol on 2017/4/24
 **/
public class ZkServiceTest{
    private static final int TIME_OUT = 5000;
    private static final String HOST = "192.168.175.131:2181";
    public static void main(String[] args) throws Exception{
        ZooKeeper zookeeper = new ZooKeeper(HOST, TIME_OUT, new MyWatcher());
        System.out.println("=====>"+zookeeper.getState());
        System.out.println("=========创建节点===========");
        if(zookeeper.exists("/test", false) == null){
            zookeeper.create("/test", "znode1".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        System.out.println("=============查看节点是否安装成功===============");
        System.out.println(new String(zookeeper.getData("/test", false, null)));

        System.out.println("=========修改节点的数据==========");
        String data = "zNode2";
        zookeeper.setData("/test", data.getBytes(), -1);

        System.out.println("========查看修改的节点是否成功=========");
        System.out.println(new String(zookeeper.getData("/test", false, null)));

        System.out.println("=======删除节点==========");
        zookeeper.delete("/test", -1);

        System.out.println("==========查看节点是否被删除============");
        System.out.println("节点状态：" + zookeeper.exists("/test", false));

        zookeeper.close();
    }
}


class MyWatcher  implements Watcher {

    @Override
    public void process(WatchedEvent event) {
        System.out.println(String.format("hello event! type=%s, stat=%s, path=%s",event.getType(),event.getState(),event.getPath()));
    }
}
