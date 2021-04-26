package base.utils.security;

import base.datasource.sqlDb.DatabaseService;
import base.datasource.sqlDb.entity.RemoveUser;
import base.utils.logging.LoggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

@Configuration
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private LoggerService loggerService;

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    final int length = 8;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Optional<RemoveUser> opm = databaseService.findRemoveByLogin(userName);

        if (opm.isEmpty()) {
            loggerService.log().warn("Unknown user: "+userName);
            throw new UsernameNotFoundException("Unknown user: "+userName);
        }
        RemoveUser connectUser = opm.get();
        return  User.builder()
                .username(connectUser.getLogin())
                //.password(encoder.encode(connectUser.getPassword()))
                .password(connectUser.getPassword())
                .roles(connectUser.getRole())
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(length);  // 16 - нельзя, сборка + тесты 5 минут
    }

    public String encode(String password) {
        long start = System.currentTimeMillis();

        String res = encoder.encode(password);

        long executionTime = System.currentTimeMillis() - start;
        loggerService.log().trace("encode: выполнен за " + executionTime + "мс" );
        return res;
    }
}
