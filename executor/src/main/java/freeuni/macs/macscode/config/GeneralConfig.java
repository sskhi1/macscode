package freeuni.macs.macscode.config;

import com.sun.security.auth.module.UnixSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GeneralConfig {

    @Bean
    public UnixSystem unixSystem() {
        return new UnixSystem();
    }

}
