package com.gosling.canal;

import com.alibaba.fastjson.JSON;
import com.gosling.model.User;
import com.gosling.rabbit.RabbitSender;
import com.gosling.utils.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.alibaba.otter.canal.client.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * 服务启动执行, 监听mysql binlog日志, 并通过rabbitMQ发送消息, 同步redis
 * Created by gaol on 2017/4/19
 **/
@Component
@Order(1)
public class CanalRunner implements CommandLineRunner {

    private static final Logger log = LogManager.getLogger(CanalRunner.class.getName());

    @Autowired
    private RabbitSender rabbitSender;

    @Override
    public void run(String... strings) throws Exception {
        log.info(">>>>>>>>>>>>>>>>>>>>服务时启动执行mysql binlog日志的监听, 并发送消息<<<<<<<<<<<<<<<<<<<<");
        /*CanalConnector connector = CanalConnectors.newSingleConnector(
                new InetSocketAddress("192.168.175.131", 11111), "example", "canal", "canal");*/

        CanalConnector connector = CanalConnectors.newClusterConnector("192.168.175.131:2181",
                "example", "canal", "canal");
        int batchSize = 1000;
        Long batchId;
        try {
            connector.connect();
            connector.subscribe(".*\\..*");
            connector.rollback();
            while (true) {
                // 获取指定数量的数据
                Message message = connector.getWithoutAck(batchSize);
                batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    getEntry(message.getEntries());
                }
                // 提交确认
                connector.ack(batchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connector.disconnect();
        }
    }

    private void getEntry(@NotNull List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }
            RowChange rowChage;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
            }

            EventType eventType = rowChage.getEventType();

            if ("pop_user".equals(entry.getHeader().getTableName())) {
                for (RowData rowData : rowChage.getRowDatasList()) {
                    if (eventType == EventType.DELETE) {
                        User user = getColumn(rowData.getBeforeColumnsList());
                        rabbitSender.send(Constants.Canal.CANAL_DELETE, JSON.toJSONString(user));
                    } else if (eventType == EventType.INSERT) {
                        User user = getColumn(rowData.getAfterColumnsList());
                        rabbitSender.send(Constants.Canal.CANAL_INSORUPD, JSON.toJSONString(user));
                    } else if (eventType == EventType.UPDATE) {
                        User user = getColumn(rowData.getAfterColumnsList());
                        rabbitSender.send(Constants.Canal.CANAL_INSORUPD, JSON.toJSONString(user));
                    }
                }
            }
        }
    }

    private User getColumn(@NotNull List<Column> columns) {
        Map<String, Object> map = new HashMap<>();
        for (Column column : columns) {
            map.put(column.getName(), column.getValue());
        }
        System.out.println("------------------>"+map.toString());
        return JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(map)), User.class);
    }
}
