package freeuni.macs.macscode.config;

import com.sun.security.auth.module.UnixSystem;
import freeuni.macs.macscode.service.PermissionsProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class GeneralConfig {

    @Bean
    public PermissionsProvider permissionsProvider() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            return new PermissionsProvider(1000, 1000);
        }
        UnixSystem unixSystem = new UnixSystem();
        return new PermissionsProvider(unixSystem.getUid(), unixSystem.getUid());
    }

}
