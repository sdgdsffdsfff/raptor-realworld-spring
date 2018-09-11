package io.spring.application.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserData {
    private String id;
    private String email;
    private String username;
    private String bio;
    private String image;
}
