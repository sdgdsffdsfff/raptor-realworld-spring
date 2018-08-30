package io.spring.api.raptor;

import org.springframework.web.bind.annotation.RestController;

//@RestController
public class AuthApiImpl implements AuthApi {
    @Override
    public UserResponse login(LoginUserRequest request) {
        return null;
    }

    @Override
    public UserResponse register(NewUserRequest request) {
        return null;
    }

    @Override
    public UserResponse getCurrentUser(GetUserRequest request) {
        return null;
    }

    @Override
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        return null;
    }
}
