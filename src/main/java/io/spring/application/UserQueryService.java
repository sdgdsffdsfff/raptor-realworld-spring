package io.spring.application;

import io.spring.application.data.UserData;
import io.spring.infrastructure.mybatis.readservice.UserReadService;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Service
public class UserQueryService  {
    private UserReadService userReadService;

    public UserQueryService(UserReadService userReadService) {
        this.userReadService = userReadService;
    }

    public Optional<UserData> findById(String id) {
        return Optional.ofNullable(userReadService.findById(id));
    }
}

