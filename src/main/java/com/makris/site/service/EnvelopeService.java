package com.makris.site.service;

import com.makris.site.pojo.Envelope;

public interface EnvelopeService {
    int insertEnvelope(Envelope envelope);
    Envelope getEnvelopeByUserId(long id);
    Envelope getEnvelopeById(long id);
    int decreaseEnvelope(long id, int version);
}
