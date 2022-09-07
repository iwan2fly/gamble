package kr.co.glog.common.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordEncoderImpl implements PasswordEncoder {

    public static final  Integer BCRYPT_SALT  = 12;

    @Override
    public String encode(CharSequence rawPassword) {

        String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(BCRYPT_SALT));

        return hashed;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }
}
