package nz.co.westpac.swis.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestClient;

@Configuration
public class AppConfig {

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        Class<?> classOnWired = injectionPoint.getMember().getDeclaringClass();
        return LoggerFactory.getLogger(classOnWired);
    }

    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }
}