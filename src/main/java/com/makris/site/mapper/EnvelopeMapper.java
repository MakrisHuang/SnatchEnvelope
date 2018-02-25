package com.makris.site.mapper;

import com.makris.site.pojo.Envelope;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface EnvelopeMapper {
    int insertEnvelope(Envelope envelope);
    Envelope getEnvelopeByUserId(long id);
    Envelope getEnvelopeById(long id);
    int decreaseEnvelope(@Param("id") long id, @Param("version") int version);
}
