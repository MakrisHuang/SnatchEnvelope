package com.makris.site.service.impl;

import com.makris.site.mapper.EnvelopeMapper;
import com.makris.site.pojo.Envelope;
import com.makris.site.service.EnvelopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EnvelopeServiceImpl implements EnvelopeService{
    @Autowired
    EnvelopeMapper envelopeMapper;

    @Override
    public int insertEnvelope(Envelope envelope) {
        return envelopeMapper.insertEnvelope(envelope);
    }

    @Override
    public Envelope getEnvelopeByUserId(long id) {
        return envelopeMapper.getEnvelopeByUserId(id);
    }

    @Override
    public Envelope getEnvelopeById(long id) {
        return envelopeMapper.getEnvelopeById(id);
    }

    @Override
    public int decreaseEnvelope(long id) {
        return envelopeMapper.decreaseEnvelope(id);
    }
}
