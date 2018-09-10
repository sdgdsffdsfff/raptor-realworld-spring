package io.spring.core.user;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
public interface EncryptService {
    String encrypt(String password);
    boolean check(String checkPassword, String realPassword);
}
