package com.dku.council.global.config;

import com.dku.council.global.interceptor.VoidSuccessResponseInterceptor;
import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.cors}")
    private final String corsList;

    private final VoidSuccessResponseInterceptor vsrInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(vsrInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(parseCorsList(corsList))
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true);
    }

    private static String[] parseCorsList(String corsList) {
        String[] corsOrigins = corsList.split(",");
        for (int i = 0; i < corsOrigins.length; i++) {
            corsOrigins[i] = corsOrigins[i].strip();
        }
        return corsOrigins;
    }

    @Bean
    public FilterRegistrationBean<OpenEntityManagerInViewFilter> openEntityManagerInViewFilter() {
        FilterRegistrationBean<OpenEntityManagerInViewFilter> filter = new FilterRegistrationBean<>();
        filter.addUrlPatterns("/manage/*");
        filter.addUrlPatterns("/chatRoom/*");
        filter.setFilter(new OpenEntityManagerInViewFilter());
        return filter;
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public OncePerRequestFilter snakeCaseConverterFilter() {

        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

                final Map<String, String[]> parameters = new ConcurrentHashMap<>();

                for (String param : request.getParameterMap().keySet()) {
                    String camelCaseParam = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, param);

                    parameters.put(camelCaseParam, request.getParameterValues(param));
                    parameters.put(param, request.getParameterValues(param));
                }

                filterChain.doFilter(new HttpServletRequestWrapper(request) {

                    @Override
                    public String getParameter(String name) {
                        return parameters.containsKey(name) ? parameters.get(name)[0] : null;
                    }

                    @Override
                    public Enumeration<String> getParameterNames() {
                        return Collections.enumeration(parameters.keySet());
                    }

                    @Override
                    public String[] getParameterValues(String name) {
                        return parameters.get(name);
                    }

                    @Override
                    public Map<String, String[]> getParameterMap() {
                        return parameters;
                    }

                }, response);
            }

        };
    }
}
