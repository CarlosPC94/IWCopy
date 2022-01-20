package iw_part2.tienda.Controller;

import iw_part2.tienda.authentication.CustomAuthenticationFilter;
import iw_part2.tienda.authentication.CustomAuthorizationFilter;
import iw_part2.tienda.authentication.JWTAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public WebSecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), getApplicationContext());
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers(GET, "/images/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/user-images/**").permitAll();
        http.authorizeRequests().antMatchers(GET, "/api/**").permitAll();

        http.authorizeRequests().antMatchers(POST, "/api/auth/**").permitAll();

        http.authorizeRequests().antMatchers(GET, "/api/products/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/products/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/products/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/products/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(GET, "/api/categories/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/categories/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/categories/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(GET, "/api/subcategories/**").permitAll();
        http.authorizeRequests().antMatchers(POST, "/api/subcategories/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/subcategories/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(GET, "/api/order/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(POST, "/api/order/**").hasAnyAuthority("USER");
        http.authorizeRequests().antMatchers(PUT, "/api/order/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().antMatchers(DELETE, "/api/order/**").hasAnyAuthority("ADMIN");

        http.authorizeRequests().antMatchers(POST, "/api/**").permitAll();
        http.authorizeRequests().antMatchers(PUT, "/api/**").permitAll();
        http.authorizeRequests().antMatchers(DELETE, "/api/**").permitAll();
        // Ejemplo de como usar los filtros para proteger los endpoints
        // De momento no se usan para hacer pruebas por comodidad
        // http.authorizeRequests().antMatchers(GET, "/api/categories/**").hasAnyAuthority("USER");
        // http.authorizeRequests().antMatchers(POST, "/api/categories/**").hasAnyAuthority("ADMIN");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    /*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()

                .antMatchers(HttpMethod.POST, "/api/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/api/**").permitAll()
                .anyRequest().authenticated();
        http.cors();
    }*/
}

