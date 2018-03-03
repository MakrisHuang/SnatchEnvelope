package com.makris.site.service;

public interface UserSnatchEnvelopeService {
    int snatchEnvelope(long envelopeId, long userId);
    long grabEnvelopeByRedis(long envelopeId, long userId);
}
