package br.com.ifpe.matafome_api.config;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.ifpe.matafome_api.modelo.seguranca.JwtAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final AuthenticationProvider authenticationProvider;

   
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfiguration(JwtAuthenticationFilter jwtAuthenticationFilter, AuthenticationProvider authenticationProvider) {

        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests -> requests
                .anyRequest().permitAll()

                            /*.requestMatchers(HttpMethod.GET, "/swagger-ui/*").permitAll()
                            .requestMatchers(HttpMethod.GET, "/api-docs/*").permitAll()
                            
                        .requestMatchers(HttpMethod.POST, "/api/cliente").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/empresa").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth").permitAll()
                        

                        //Configuração de autorizações de acesso para Produto
 	                    .requestMatchers(HttpMethod.POST, "/api/produto").hasAnyAuthority(Usuario.ROLE_EMPRESA_ADMIN, Usuario.ROLE_EMPRESA_USER) //Cadastro de produto
                        .requestMatchers(HttpMethod.PUT, "/api/produto/*").hasAnyAuthority(Usuario.ROLE_EMPRESA_ADMIN, Usuario.ROLE_EMPRESA_USER) //Alteração de produto
                        .requestMatchers(HttpMethod.DELETE, "/api/produto/*").hasAnyAuthority(Usuario.ROLE_EMPRESA_ADMIN) //Exclusão de produto
                        .requestMatchers(HttpMethod.GET, "/api/produto/").hasAnyAuthority(Usuario.ROLE_CLIENTE, Usuario.ROLE_EMPRESA_ADMIN, Usuario.ROLE_EMPRESA_USER) //Consulta de produto
                            
                        .anyRequest()
                        .authenticated()*/)
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        // Define permissões para todas as origens
        configuration.setAllowedOrigins(List.of("*")); // Use `setAllowedOriginPatterns` para permitir todas as origens com credenciais
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")); // Todos os métodos HTTP
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false);


        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
