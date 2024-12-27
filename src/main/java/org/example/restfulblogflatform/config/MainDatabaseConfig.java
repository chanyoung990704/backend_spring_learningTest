package org.example.restfulblogflatform.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 메인 데이터베이스 설정을 위한 구성 클래스.
 * 이 클래스는 애플리케이션의 기본 데이터베이스와 관련된 DataSource, EntityManagerFactory,
 * TransactionManager를 설정합니다.
 */
@Configuration // Spring에서 설정 클래스로 인식되도록 지정
@EnableTransactionManagement // 트랜잭션 관리 활성화
@EnableJpaRepositories(
        basePackages = "org.example.restfulblogflatform.repository", // JPA Repository 패키지 경로 지정
        entityManagerFactoryRef = "mainEntityManagerFactory", // 사용할 EntityManagerFactory Bean 이름 지정
        transactionManagerRef = "mainTransactionManager" // 사용할 TransactionManager Bean 이름 지정
)
public class MainDatabaseConfig {

    /**
     * 메인 데이터베이스의 DataSource를 생성하는 Bean.
     * application.yml 또는 application.properties 파일에서 "spring.datasource.main"로 시작하는
     * 설정 값을 읽어와 DataSource를 생성합니다.
     *
     * @return DataSource - 메인 데이터베이스 연결에 사용되는 DataSource 객체
     */
    @Primary // 기본적으로 사용될 DataSource로 지정
    @Bean(name = "mainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.main") // 설정 파일에서 main 데이터베이스 관련 속성 읽기
    public DataSource dataSource() {
        return DataSourceBuilder.create().build(); // DataSource 객체 생성 및 반환
    }

    /**
     * 메인 데이터베이스의 EntityManagerFactory를 생성하는 Bean.
     * JPA 엔티티 클래스가 위치한 패키지를 스캔하고, 지정된 DataSource를 사용하여 EntityManagerFactory를 구성합니다.
     *
     * @param builder EntityManagerFactoryBuilder - EntityManagerFactory를 빌드하는 데 사용
     * @param dataSource DataSource - 메인 데이터베이스의 DataSource 객체
     * @return LocalContainerEntityManagerFactoryBean - EntityManagerFactory 설정 객체
     */
    @Primary // 기본적으로 사용될 EntityManagerFactory로 지정
    @Bean(name = "mainEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("mainDataSource") DataSource dataSource) { // "mainDataSource" Bean을 주입받음
        return builder
                .dataSource(dataSource) // 사용할 DataSource 지정
                .packages("org.example.restfulblogflatform.entity") // JPA 엔티티 클래스가 위치한 패키지 경로 지정
                .build();
    }

    /**
     * 메인 데이터베이스의 TransactionManager를 생성하는 Bean.
     * 지정된 EntityManagerFactory를 사용하여 트랜잭션 관리를 처리합니다.
     *
     * @param entityManagerFactory EntityManagerFactory - 메인 데이터베이스용 EntityManagerFactory 객체
     * @return PlatformTransactionManager - 트랜잭션 관리 객체
     */
    @Primary // 기본적으로 사용될 TransactionManager로 지정
    @Bean(name = "mainTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("mainEntityManagerFactory") EntityManagerFactory entityManagerFactory) { // "mainEntityManagerFactory" Bean을 주입받음
        return new JpaTransactionManager(entityManagerFactory); // JpaTransactionManager 생성 및 반환
    }
}

