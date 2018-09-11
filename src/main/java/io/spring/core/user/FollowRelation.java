package io.spring.core.user;

import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Data
@NoArgsConstructor
public class FollowRelation {
    private String userId;
    private String targetId;

    public FollowRelation(String userId, String targetId) {

        this.userId = userId;
        this.targetId = targetId;
    }
}
