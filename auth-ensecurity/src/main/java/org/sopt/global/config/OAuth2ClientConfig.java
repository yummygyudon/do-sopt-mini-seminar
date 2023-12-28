package org.sopt.global.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

//@Configuration
//@AutoConfigureBefore(SecurityConfig.class)
//@ConditionalOnClass({ EnableWebSecurity.class, ClientRegistration.class })
//@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//@Import({ OAuth2ClientRegistrationRepositoryConfiguration.class, OAuth2WebSecurityConfiguration.class })
public class OAuth2ClientConfig {
}
