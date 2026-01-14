package br.com.sgci.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity // Habilita a configuração de segurança web do Spring
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http ) throws Exception {
        http
            
        	// 1. Habilita o CORS usando a configuração definida abaixo
            .cors(cors -> cors.configurationSource(corsConfigurationSource( )))

            // 2. Desabilita o CSRF, que não é necessário para APIs stateless e pode atrapalhar
            .csrf(csrf -> csrf.disable())

            // 3. Define as regras de autorização para as requisições HTTP
            .authorizeHttpRequests(auth -> auth
            	// Permite todas as requisições preflight (OPTIONS) de forma explícita.
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()            		
                // Permite que qualquer um acesse os endpoints de 'pessoas'
                .requestMatchers("/pessoas/**").permitAll()
                // Para qualquer outra requisição, o usuário precisa estar autenticado
                .anyRequest().authenticated()
            );

        return http.build( );
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Define a origem permitida (seu frontend)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:9000" ));
        // Define os métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Define os cabeçalhos permitidos
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        // Permite o envio de credenciais (como cookies), se necessário
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Aplica a configuração de CORS a todos os endpoints da aplicação
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}
