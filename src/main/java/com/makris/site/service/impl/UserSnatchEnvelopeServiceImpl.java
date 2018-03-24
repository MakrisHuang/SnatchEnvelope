package com.makris.site.service.impl;

import com.makris.site.mapper.UserSnatchEnvelopeMapper;
import com.makris.site.pojo.Envelope;
import com.makris.site.pojo.UserSnatchEnvelope;
import com.makris.site.service.EnvelopeService;
import com.makris.site.service.RedisEnvelopeService;
import com.makris.site.service.UserSnatchEnvelopeService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.sql.Timestamp;
import java.util.Date;

@Service
@Transactional
public class UserSnatchEnvelopeServiceImpl implements UserSnatchEnvelopeService{
    private static final Logger logger = LogManager.getLogger(UserSnatchEnvelopeServiceImpl.class);

    @Autowired
    EnvelopeService envelopeService;

    @Autowired
    UserSnatchEnvelopeMapper userSnatchEnvelopeMapper;

    private static final int FAILED = 0;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int snatchEnvelope(long envelopeId, long userId) {
        for (int i = 0; i < 3; i++) {
            Envelope envelope = envelopeService.getEnvelopeById(envelopeId);
            // 小紅包庫存大於0
            if (envelope.getRemainAmount() > 0) {
                int update = envelopeService.decreaseEnvelope(envelopeId, envelope.getVersion());
                // 沒有數據更新，代表其他thread已修改過，重新再搶一次
                if (update == 0) {
                    continue;
                }
                // 產生搶紅包物件
                UserSnatchEnvelope userSnatchEnvelope = new UserSnatchEnvelope();
                userSnatchEnvelope.setEnvelopeId(envelopeId);
                userSnatchEnvelope.setUserId(userId);
                userSnatchEnvelope.setPriceSnatched(envelope.getUnitPrice());
                Date date = new Date();
                userSnatchEnvelope.setGrabTime(new Timestamp(date.getTime()));
                userSnatchEnvelope.setNote("搶到紅包來自大紅包id： " + envelopeId);
                // 插入紅包資料
                return userSnatchEnvelopeMapper.insertSnatchEnvelope(userSnatchEnvelope);
            }else{
                return FAILED;
            }
        }
        return FAILED;
    }

    @Autowired
    private RedisTemplate redisTemplate = null;

    @Autowired
    private RedisEnvelopeService redisEnvelopeService = null;

    // Lua腳本
    String LuaScript = "local listKey = 'envelope_list_'..KEYS[1] \n" // KEYS[1]: 紅包id
            + "local envelope = 'envelope_'..KEYS[1] \n"
            + "local stock = tonumber(redis.call('hget', envelope, 'stock'))\n"
            + "if stock <= 0 then return 0 end \n"
            + "stock = stock - 1 \n"
            + "redis.call('hset', envelope, 'stock', tostring(stock)) \n"
            + "redis.call('rpush', listKey, ARGV[1]) \n" // ARGV[1]: userId-currentTime
            + "if stock == 0 then return 2 end \n"
            + "return 1 \n";
    // 用以下變數儲存Redis返回的32位元SHA1編碼，使用他去執行緩存的Lua腳本
    String sha1 = null;

    @Override
    public long grabEnvelopeByRedis(long envelopeId, long userId) {
        // 當前搶紅包用戶和日期訊息
        String args = userId + "-" + System.currentTimeMillis();
        Long result = null;
        Jedis jedis = (Jedis)redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try{
            // 如果腳本沒有加載過，那麼進行加載，這樣就會返回一個sha1編碼
            if (sha1 == null){
                sha1 = jedis.scriptLoad(LuaScript);
            }
            // 執行腳本並返回結果
            Object res = jedis.evalsha(sha1, 1, envelopeId + "", args);
            result = (Long) res;
//            logger.info("[grabEnvelopeByRedis] result: " + result);
            // 返回2時為最後一個紅包，此時將搶紅包資訊透過Async保存到資料庫中
            if (result == 2){
                // 獲取單個小紅包金額
                String unitPriceStr = jedis.hget("envelope_" + envelopeId, "unitPrice");

                // 觸發保存資料庫操作
                Double unitPrice = Double.parseDouble(unitPriceStr);
                redisEnvelopeService.saveUserSnatchEnvelopeByRedis(envelopeId, unitPrice);
            }
        } finally {
            // 確保jedis順利關閉
            if (jedis != null && jedis.isConnected()){
                jedis.close();
            }
        }
        return result;
    }

    @Override
    public boolean resetRedisForSnatchEnvelope() {
        String resetLuaScript = "redis.call('del', KEYS[1]) \n" +
                "redis.call('hset', KEYS[2], 'stock', 3000) \n" +
                "redis.call('hset', KEYS[3], 'price', 10) \n" +
                "return 1 \n";
        String sha2 = null;
        Jedis jedis = (Jedis)redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        try{
            if (sha2 == null){
                sha2 = jedis.scriptLoad(resetLuaScript);
            }
            Object res = jedis.evalsha(sha2, 3, "envelope_list_1", "envelope_1", "envelope_1");
            Long result = (Long)res;
            return (result == 1) ? true : false;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
