package io.spring.core.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@Repository
public interface UserRepository {
    void save(User user);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    void saveRelation(FollowRelation followRelation);

    Optional<FollowRelation> findRelation(String userId, String targetId);

    void removeRelation(FollowRelation followRelation);
}
