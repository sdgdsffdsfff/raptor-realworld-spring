package io.spring.infrastructure.mybatis.readservice;

import io.spring.application.data.UserData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Component
@Mapper
public interface UserReadService {

    UserData findByUsername(@Param("username") String username);

    UserData findById(@Param("id") String id);
}

