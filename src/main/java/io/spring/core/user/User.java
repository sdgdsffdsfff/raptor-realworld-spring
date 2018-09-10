package io.spring.core.user;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class User {
    private String id;
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;

    public User(String email, String username, String password, String bio, String image) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.image = image;
    }

    public void update(String email, String username, String password, String bio, String image) {
        if (!"".equals(email)) {
            this.email = email;
        }

        if (!"".equals(username)) {
            this.username = username;
        }

        if (!"".equals(password)) {
            this.password = password;
        }

        if (!"".equals(bio)) {
            this.bio = bio;
        }

        if (!"".equals(image)) {
            this.image = image;
        }
    }
}
