package com.makris.site.service.impl;

import com.makris.site.mapper.UserSnatchEnvelopeMapper;
import com.makris.site.pojo.Envelope;
import com.makris.site.pojo.UserSnatchEnvelope;
import com.makris.site.service.EnvelopeService;
import com.makris.site.service.UserSnatchEnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class UserSnatchEnvelopeServiceImpl implements UserSnatchEnvelopeService{
    @Autowired
    EnvelopeService envelopeService;

    @Autowired
    UserSnatchEnvelopeMapper userSnatchEnvelopeMapper;

    private static final int FAILED = 0;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int snatchEnvelope(long envelopeId, long userId) {
        Envelope envelope = envelopeService.getEnvelopeById(envelopeId);
        // 小紅包庫存大於0
        if (envelope.getRemainAmount() > 0){
            envelopeService.decreaseEnvelope(envelopeId);
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
        }
        return FAILED;
    }
}
