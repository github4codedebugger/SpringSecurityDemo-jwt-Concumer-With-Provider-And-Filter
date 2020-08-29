package com.ss.config;

import com.ss.config.model.UserDetailsImpl;
import com.ss.config.model.UsernamePasswordAuthenticationTokenImpl;
import com.ss.model.ValidateResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class JwtAuthProvider extends AbstractUserDetailsAuthenticationProvider {

    @Autowired
    private ConfigUtils configUtils;

    @Override
    @SneakyThrows
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken) {
        // NO-OP required
    }

    @Override
    @SneakyThrows
    protected UserDetails retrieveUser(String userName, UsernamePasswordAuthenticationToken authToken) {
        try {
            UsernamePasswordAuthenticationTokenImpl token = (UsernamePasswordAuthenticationTokenImpl) authToken;

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("x-auth-token", token.getToken());
            headers.add(HttpHeaders.AUTHORIZATION, configUtils.getBasicAuth());

            HttpEntity<UsernamePasswordAuthenticationTokenImpl> entity = new HttpEntity<>(headers);
            ValidateResponse resData = new RestTemplate().exchange(configUtils.getValidateUrl(), HttpMethod.GET, entity,
                    ValidateResponse.class).getBody();

            List<GrantedAuthority> grantedAuthorities =
                    AuthorityUtils.commaSeparatedStringToAuthorityList(resData.getRoles());

            return new UserDetailsImpl(resData.getUserName(), resData.getUserId(), grantedAuthorities);
        } catch (Exception ex) {
            throw new RuntimeException("Invalid AccessToken");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationTokenImpl.class.isAssignableFrom(authentication);
    }
}
