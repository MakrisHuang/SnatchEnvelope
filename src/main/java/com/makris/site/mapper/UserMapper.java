package com.makris.site.mapper;

import com.makris.site.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int insertUser(User user);
}
