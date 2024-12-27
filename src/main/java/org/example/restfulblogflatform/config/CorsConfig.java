package org.example.restfulblogflatform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    /**
     * CORS(Cross-Origin Resource Sharing) 설정을 위한 Bean 생성 메서드.
     * 이 메서드는 애플리케이션에서 외부 도메인(예: 프론트엔드)과의 통신을 허용하기 위해
     * CORS 정책을 정의하고, 이를 처리하는 필터를 반환합니다.
     *
     * @return CorsFilter - 정의된 CORS 설정을 기반으로 동작하는 필터
     */
    @Bean
    public CorsFilter corsFilter() {
        // CORS 설정 정보를 담는 객체 생성
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // 자격 증명(쿠키, 인증 정보) 포함 허용
        config.setAllowCredentials(true);

        // 허용할 출처(origin) 설정 (예: 프론트엔드가 실행되는 로컬호스트 주소)
        config.addAllowedOrigin("http://localhost:3000");

        // 모든 헤더를 허용
        config.addAllowedHeader("*");

        // 모든 HTTP 메서드(GET, POST, PUT, DELETE 등)를 허용
        config.addAllowedMethod("*");

        // 설정된 CORS 정책을 모든 경로("/**")에 적용
        source.registerCorsConfiguration("/**", config);

        // CORS 설정을 처리하는 필터 반환
        return new CorsFilter(source);
    }

}
