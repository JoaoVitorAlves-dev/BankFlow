package com.bankflow.joao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST, "/usuarios/auth/registrar", "/usuarios/auth/refresh/", "/usuarios/auth/logar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/usuarios/me").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}", "/usuarios/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/agencia").authenticated()

                        .requestMatchers(HttpMethod.POST, "/enderecos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/enderecos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/enderecos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/enderecos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/enderecos/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/enderecos/me").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/enderecos/me/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/enderecos/me/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/agencias").permitAll()
                        .requestMatchers(HttpMethod.POST, "/agencias").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/agencias/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/agencias/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/gerentes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/gerentes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/gerentes/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/gerentes/{id}/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/gerentes/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/gerentes/me").authenticated()

                        .requestMatchers(HttpMethod.POST, "/contas/agencia").authenticated()
                        .requestMatchers(HttpMethod.GET, "/contas/minha").authenticated()
                        .requestMatchers(HttpMethod.GET, "/contas/agencia").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/contas/agencia/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/contas/agencia/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/contas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/contas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/contas/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/contas/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/chavepix").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/chavepix").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/chavepix/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/chavepix/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/chavepix/{id}/desativar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/chavepix/gerente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/chavepix/minhas").authenticated()
                        .requestMatchers(HttpMethod.POST, "/chavepix/minhas").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/chavepix/minhas/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/chavepix/minhas/{id}/desativar").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/chavepix/minhas/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/boletos/me").authenticated()
                        .requestMatchers(HttpMethod.POST, "/boletos/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/boletos/{id}/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/boletos/gerente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/boletos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/boletos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/boletos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/boletos/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/investimentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/investimentos/meus").authenticated()
                        .requestMatchers(HttpMethod.GET, "/investimentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/investimentos/gerente").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/investimentos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/investimentos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/investimentos/meus/{id}").authenticated()

                        .requestMatchers(HttpMethod.POST, "/cartaos/gerente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/cartaos/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/cartaos/gerente").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/cartaos/me/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/cartaos/gerente/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/cartaos/gerente/{id}/status").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/cartaos/gerente/{id}").authenticated()
                        .requestMatchers(HttpMethod.POST, "/cartaos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/cartaos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/cartaos/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/cartaos/{id}/status").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/cartaos/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/faturas/minhas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/faturas/gerente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/faturas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/faturas/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/faturas/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/faturas/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/emprestimos/meus").authenticated()
                        .requestMatchers(HttpMethod.POST, "/emprestimos/meus").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/emprestimos/meus/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/emprestimos/gerente").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/emprestimos/gerente/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/emprestimos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/emprestimos").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/emprestimos/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/emprestimos/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/parcelas/minhas").authenticated()
                        .requestMatchers(HttpMethod.GET, "/parcelas/gerente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/parcelas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/parcelas").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/parcelas/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/parcelas/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/transacoes/gerente").authenticated()
                        .requestMatchers(HttpMethod.GET, "/transacoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/transacoes").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/transacoes/minhas").authenticated()

                        .requestMatchers(HttpMethod.GET, "/lancamentos").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/lancamentos/meus").authenticated()
                        .requestMatchers(HttpMethod.GET, "/lancamentos/gerente").authenticated()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }
}
