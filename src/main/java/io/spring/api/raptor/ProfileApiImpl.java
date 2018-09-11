package io.spring.api.raptor;

import io.spring.api.exception.ResourceNotFoundException;
import io.spring.application.ProfileQueryService;
import io.spring.application.data.ProfileData;
import io.spring.context.UserContext;
import io.spring.core.user.FollowRelation;
import io.spring.core.user.User;
import io.spring.core.user.UserRepository;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ProfileApiImpl implements ProfileApi {
    private ProfileQueryService profileQueryService;
    private UserRepository userRepository;
    private Mapper mapper;

    @Autowired
    public ProfileApiImpl(ProfileQueryService profileQueryService, UserRepository userRepository, Mapper mapper) {
        this.profileQueryService = profileQueryService;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public ProfileResponse getProfile(ProfileRequest request) {
        String username = request.getUsername();
        User user = UserContext.getUser();
        ProfileData profileData = profileQueryService.findByUsername(username, user).orElseThrow(ResourceNotFoundException::new);
        Profile profile = mapper.map(profileData, Profile.class);
        return new ProfileResponse(profile);
    }

    @Override
    public ProfileResponse followUser(ProfileRequest request) {
        io.spring.core.user.User user = UserContext.getUser();
        return userRepository.findByUsername(request.getUsername()).map(target -> {
            FollowRelation followRelation = new FollowRelation(user.getId(), target.getId());
            userRepository.saveRelation(followRelation);
            ProfileData profileData = profileQueryService.findByUsername(request.getUsername(), user).get();
            return new ProfileResponse(mapper.map(profileData, Profile.class));
        }).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public ProfileResponse unFollowUser(ProfileRequest request) {
        io.spring.core.user.User user = UserContext.getUser();
        Optional<io.spring.core.user.User> userOptional = userRepository.findByUsername(request.getUsername());
        if (userOptional.isPresent()) {
            io.spring.core.user.User target = userOptional.get();
            return userRepository.findRelation(user.getId(), target.getId())
                    .map(relation -> {
                        userRepository.removeRelation(relation);
                        ProfileData profileData = profileQueryService.findByUsername(request.getUsername(), user).get();
                        return new ProfileResponse(mapper.map(profileData, Profile.class));
                    }).orElseThrow(ResourceNotFoundException::new);
        } else {
            throw new ResourceNotFoundException();
        }
    }
}
