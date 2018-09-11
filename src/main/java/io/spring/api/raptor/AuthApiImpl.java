package io.spring.api.raptor;

import com.ppdai.framework.raptor.rpc.RaptorContext;
import io.spring.application.UserQueryService;
import io.spring.application.data.UserData;
import io.spring.context.UserContext;
import io.spring.core.service.JwtService;
import io.spring.core.user.EncryptService;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
/**
 * @Author：zhangchengxi
 * @Date：2018/9/6 19:48
 */
@RestController
public class AuthApiImpl implements AuthApi {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private UserQueryService userQueryService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private Mapper mapper;

    @Override
    public UserResponse login(LoginUserRequest request) {
        LoginUser user = request.getUser();
        Optional<User> optional = userRepository.findByEmail(user.getEmail());
        if (optional.isPresent() && encryptService.check(user.getPassword(), optional.get().getPassword())) {
            UserData userData = userQueryService.findById(optional.get().getId()).get();
            io.spring.api.raptor.User user1 = mapper.map(userData, io.spring.api.raptor.User.class);
            UserResponse userResponse = new UserResponse(user1);
            user1.setToken(jwtService.toToken(optional.get()));
            return userResponse;
        }

        return null;
    }

    @Override
    public UserResponse register(NewUserRequest request) {

        NewUser user = request.getUser();

        User coreUser = mapper.map(user, User.class);
        userRepository.save(coreUser);

        Optional<UserData> userDataOptional = userQueryService.findById(coreUser.getId());

        io.spring.api.raptor.User newUser = mapper.map(userDataOptional.get(), io.spring.api.raptor.User.class);

        UserResponse userResponse = new UserResponse();
        newUser.setToken(jwtService.toToken(coreUser));
        userResponse.setUser(newUser);

        return userResponse;
    }

    @Override
    public UserResponse getCurrentUser(GetUserRequest request) {

        User user = UserContext.getUser();
        io.spring.api.raptor.User user1 = mapper.map(user, io.spring.api.raptor.User.class);
        user1.setToken(RaptorContext.getContext().getRequestAttachment("Authorization"));
        UserResponse userResponse = new UserResponse(user1);
        return userResponse;
    }

    @Override
    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        UpdateUser user = request.getUser();
        User coreUser = UserContext.getUser();
        mapper.map(user, coreUser);
        userRepository.save(coreUser);

        io.spring.api.raptor.User raptorUser = mapper.map(coreUser, io.spring.api.raptor.User.class);
        raptorUser.setToken(RaptorContext.getContext().getRequestAttachment("Authorization"));
        return new UserResponse(raptorUser);
    }
}
