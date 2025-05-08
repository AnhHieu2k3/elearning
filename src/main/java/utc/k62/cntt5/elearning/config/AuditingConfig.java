package utc.k62.cntt5.elearning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import utc.k62.cntt5.elearning.security.SecurityUtils;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.of(SecurityUtils.getCurrentUserLogin().orElse("system"));
            }
        };
    }
}
