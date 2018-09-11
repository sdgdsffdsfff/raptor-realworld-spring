package io.spring.core.service;

import io.spring.core.user.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Service
public interface JwtService {
    String toToken(User user);

    Optional<String> getSubFromToken(String token);
}
