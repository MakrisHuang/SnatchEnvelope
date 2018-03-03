package com.makris.site.service;

/*
    保存redis搶紅包列表
    @param envelopeId --紅包編號
    @param unitPrice --紅包金額
 */
public interface RedisEnvelopeService {
    void saveUserSnatchEnvelopeByRedis(Long envelopeId, Double unitPrice);
}
