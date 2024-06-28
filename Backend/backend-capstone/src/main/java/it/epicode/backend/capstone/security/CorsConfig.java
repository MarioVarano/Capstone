package it.epicode.backend.capstone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    //PERSONALIZZIAMO IL CORSFILTER SECONDO LE NOSTRE ESIGENZE
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Configura l'origine permessa
        configuration.addAllowedOrigin("http://localhost:4200");

        // Configura i metodi HTTP permessi
        configuration.addAllowedMethod("GET");
        configuration.addAllowedMethod("POST");
        configuration.addAllowedMethod("PUT");
        configuration.addAllowedMethod("DELETE");
        configuration.addAllowedMethod("OPTIONS");

        // Configura gli header permessi
        configuration.addAllowedHeader("*");

        // Configura le credenziali
        configuration.setAllowCredentials(true);

        // Configura gli header esposti
        configuration.addExposedHeader("Authorization");

        // Configura la cache delle preflight
        configuration.setMaxAge(3600L); // Cache delle preflight per 1 ora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }

}
