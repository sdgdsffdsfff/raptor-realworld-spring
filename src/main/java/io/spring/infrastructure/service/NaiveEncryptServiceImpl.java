package io.spring.infrastructure.service;

import io.spring.core.user.EncryptService;
import org.springframework.stereotype.Service;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Service
public class NaiveEncryptServiceImpl implements EncryptService {
    @Override
    public String encrypt(String password) {
        return password;
    }

    @Override
    public boolean check(String checkPassword, String realPassword) {
        return checkPassword.equals(realPassword);
    }
}
