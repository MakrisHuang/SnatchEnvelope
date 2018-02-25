package com.makris.site.mapper;

import com.makris.site.pojo.UserSnatchEnvelope;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserSnatchEnvelopeMapper {
    int insertSnatchEnvelope(UserSnatchEnvelope userSnatchEnvelope);
}
