package org.example.restfulblogflatform.service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 기본 검증 로직을 제공하는 추상 클래스
 * @param <T> 엔티티 타입
 * @param <ID> 엔티티의 ID 타입
 * @param <R> Repository 타입
 */
@RequiredArgsConstructor
public abstract class BaseValidator<T, ID, R extends JpaRepository<T, ID>> {
    
    protected final R repository;

    /**
     * 엔티티 존재 여부를 검증합니다.
     */
    public void validateExists(ID id) {
        if (!repository.existsById(id)) {
            throw getNotFoundException();
        }
    }

    /**
     * 엔티티를 조회하거나 예외를 던집니다.
     */
    public T getOrThrow(ID id) {
        return repository.findById(id)
                .orElseThrow(this::getNotFoundException);
    }

    /**
     * 각 도메인별 NotFound 예외를 반환하는 추상 메서드
     */
    protected abstract RuntimeException getNotFoundException();
}