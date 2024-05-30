package com.hampus.authenticate_and_authorize.AuthFiles;

import com.hampus.authenticate_and_authorize.models.MyUser;
import com.hampus.authenticate_and_authorize.repo.IUserRepository;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TwoFactorAuthenticator extends DaoAuthenticationProvider
{
    private IUserRepository repository;

    public TwoFactorAuthenticator(IUserRepository repository, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder){
        setUserDetailsService(userDetailsService);
        setPasswordEncoder(passwordEncoder);
        this.repository = repository;
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String verificationCode
                = ((CustomWebAuthenticationDetails) auth.getDetails())
                .getVerificationCode();
        MyUser user = repository.findByEmail(auth.getName());
        if (user == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        Totp totp = new Totp( user.getSecret());
        if (!totp.verify(verificationCode)) {
            throw new BadCredentialsException("Invalid verification code");
        }
        Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(
                user, result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
    //denna token-class är den vi returnerar i authenticate-metoden som vi just implementerat
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
