package com.makris.site.service.impl;

import com.makris.site.mapper.EnvelopeMapper;
import com.makris.site.pojo.Envelope;
import com.makris.site.service.EnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EnvelopeServiceImpl implements EnvelopeService{
    @Autowired
    EnvelopeMapper envelopeMapper;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int insertEnvelope(Envelope envelope) {
        return envelopeMapper.insertEnvelope(envelope);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Envelope getEnvelopeByUserId(long id) {
        return envelopeMapper.getEnvelopeByUserId(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Envelope getEnvelopeById(long id) {
        return envelopeMapper.getEnvelopeById(id);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int decreaseEnvelope(long id, int version) {
        return envelopeMapper.decreaseEnvelope(id, version);
    }
}
