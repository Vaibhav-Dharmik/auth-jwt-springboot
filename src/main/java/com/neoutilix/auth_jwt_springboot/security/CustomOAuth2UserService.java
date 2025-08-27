package com.neoutilix.auth_jwt_springboot.security;

import com.neoutilix.auth_jwt_springboot.entity.Role;
import com.neoutilix.auth_jwt_springboot.entity.User;
import com.neoutilix.auth_jwt_springboot.repository.RoleRepository;
import com.neoutilix.auth_jwt_springboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // google or github
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = extractEmail(registrationId, attributes);
        String providerId = extractProviderId(registrationId, attributes);
        if (email == null) {
            log.warn("OAuth2 email not found for provider {}", registrationId);
            throw new OAuth2AuthenticationException("Email not provided by OAuth2 provider");
        }
        User user = userRepository.findByEmail(email).orElseGet(() -> createOAuthUser(email, registrationId, providerId, attributes));
        // Update avatar/name if changed
        user.setAvatar(extractAvatar(registrationId, attributes));
        user.setFirstName(extractName(registrationId, attributes));
        userRepository.save(user);
        return new DefaultOAuth2User(oauth2User.getAuthorities(), oauth2User.getAttributes(), "sub");
    }

    private User createOAuthUser(String email, String provider, String providerId, Map<String, Object> attrs) {
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
        User user = User.builder()
                .email(email)
                .password("oauth2-user") // not used
                .provider(provider)
                .providerId(providerId)
                .firstName(extractName(provider, attrs))
                .avatar(extractAvatar(provider, attrs))
                .build();
        user.getRoles().add(userRole);
        return userRepository.save(user);
    }

    private String extractEmail(String provider, Map<String, Object> a) {
        if ("google".equals(provider)) {
            return (String) a.get("email");
        }
        if ("github".equals(provider)) {
            return (String) a.get("email"); // may be null if private

                }return null;
    }

    private String extractProviderId(String provider, Map<String, Object> a) {
        if ("google".equals(provider)) {
            return (String) a.get("sub");
        }
        if ("github".equals(provider)) {
            return String.valueOf(a.get("id"));
        }
        return null;
    }

    private String extractName(String provider, Map<String, Object> a) {
        if ("google".equals(provider)) {
            return (String) a.get("name");
        }
        if ("github".equals(provider)) {
            return (String) a.getOrDefault("name", a.get("login"));
        }
        return null;
    }

    private String extractAvatar(String provider, Map<String, Object> a) {
        if ("google".equals(provider)) {
            return (String) a.get("picture");
        }
        if ("github".equals(provider)) {
            return (String) a.get("avatar_url");
        }
        return null;
    }
}
