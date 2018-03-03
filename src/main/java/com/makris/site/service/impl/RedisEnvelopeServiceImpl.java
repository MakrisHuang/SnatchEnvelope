package com.makris.site.service.impl;

import com.makris.site.pojo.UserSnatchEnvelope;
import com.makris.site.service.RedisEnvelopeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedisEnvelopeServiceImpl implements RedisEnvelopeService{
    private static final String PREFIX = "envelope_list_";
    private static final int TIME_SIZE = 1000;
    private static final Logger logger = LogManager.getLogger(RedisEnvelopeServiceImpl.class);
    private static int count = 0;

    @Autowired
    private RedisTemplate redisTemplate = null;

    @Override
    @Async
    public void saveUserSnatchEnvelopeByRedis(Long envelopeId, Double unitPrice) {
        logger.info("Start storing envelope data...");
        Long start = System.currentTimeMillis();
        BoundListOperations ops = redisTemplate.boundListOps(PREFIX + envelopeId);
        Long size = ops.size();
        logger.info(size);
        Long times = size % TIME_SIZE == 0 ? size / TIME_SIZE : size / TIME_SIZE + 1;
        logger.info("times: " + times);

        List<UserSnatchEnvelope> userSnatchEnvelopeList =
                new ArrayList<>(TIME_SIZE);
        for (int i = 0; i < times; i++){
            // 獲取至多TIME_SIZE個紅包資訊
            List userIdList = null;
            if (i == 0){
                userIdList = ops.range(i * TIME_SIZE, (i + 1) * TIME_SIZE);
            }else{
                userIdList = ops.range(i * TIME_SIZE + 1, (i + 1) * TIME_SIZE);
            }
            userSnatchEnvelopeList.clear();
            // 保存紅包資訊
            for (int j = 0; j < userIdList.size(); j++){
                String args = userIdList.get(j).toString();
                logger.info(args);
                String[] arr = args.split("-");
                String userIdStr = arr[0];
                String timeStr = arr[1];
                Long userId = Long.parseLong(userIdStr);
                Long time = Long.parseLong(timeStr);
                // 生成搶紅包資訊
                UserSnatchEnvelope userSnatchEnvelope = new UserSnatchEnvelope();
                userSnatchEnvelope.setEnvelopeId(envelopeId);
                userSnatchEnvelope.setUserId(userId);
                userSnatchEnvelope.setPriceSnatched(unitPrice);
                userSnatchEnvelope.setNote("搶紅包" + envelopeId);
                userSnatchEnvelope.setGrabTime(new Timestamp(time));
                userSnatchEnvelopeList.add(userSnatchEnvelope);
            }

            //插入搶紅包資訊
            count += executeBatch(userSnatchEnvelopeList);
        }
        // 刪除Redis列表
        redisTemplate.delete(PREFIX + envelopeId);
        Long end = System.currentTimeMillis();
        String log = "保存時間：" + (end - start) + "毫秒，共" + count + "條紀錄被保存";
        logger.info(log);
    }

    @Autowired
    DataSource dataSource = null;

    /*
            使用JDBC丕亮處理Redis暫存數據
            @param userSnatchEnvelopeList --搶紅包列表
            @return 搶紅包插入數量
     */
    private int executeBatch(List<UserSnatchEnvelope> userSnatchEnvelopeList){
        Connection conn = null;
        Statement stmt = null;
        int []count = null;
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            stmt =  conn.createStatement();
            for (UserSnatchEnvelope userSnatchEnvelope : userSnatchEnvelopeList){
                String sql1 = "update Envelope set remainAmount = remainAmount - 1 where id=" + userSnatchEnvelope.getEnvelopeId() + ";";
                String sql2 = "insert into UserSnatchEnvelope(envelopeId, userId, priceSnatched, grabTime, note)"
                        + "values (" + userSnatchEnvelope.getEnvelopeId() + ", "
                        + userSnatchEnvelope.getUserId() + ", "
                        + userSnatchEnvelope.getPriceSnatched().intValue() + ", "
                        + "'" + userSnatchEnvelope.getGrabTime() + "',"
                        + "'" + userSnatchEnvelope.getNote() + "');";
                logger.info("使用者紅包物件");
                logger.info("SQL str: " + sql2);
                stmt.addBatch(sql1);
                stmt.addBatch(sql2);
                // 執行批量
                count = stmt.executeBatch();
                conn.commit();
            }
        } catch (SQLException e){
            throw new RuntimeException("搶紅包批量執行程序錯誤");
        }finally {
            try{
                if (conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException e){
                e.printStackTrace();
            }
        }
        // 返回插入搶紅包資料記錄
        return count.length / 2;
    }
}
