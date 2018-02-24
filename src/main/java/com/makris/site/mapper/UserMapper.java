package com.makris.site.mapper;

import com.makris.site.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User findUserByName(String username);
    int insertUser(User user);
    int updateUser(User user);
    int deleteUser(long id);
}
