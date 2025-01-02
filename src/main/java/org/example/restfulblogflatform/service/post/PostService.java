package org.example.restfulblogflatform.service.post;

import org.example.restfulblogflatform.dto.post.request.PostRequestDto;
import org.example.restfulblogflatform.dto.post.response.PostResponseDto;
import org.example.restfulblogflatform.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 게시글(Post) 관련 비즈니스 로직을 정의하는 서비스 인터페이스.
 *
 * 게시글 생성, 조회, 수정, 삭제와 같은 주요 기능을 제공합니다.
 * 구현체는 비즈니스 로직을 처리하고 데이터베이스와 상호작용합니다.
 */
public interface PostService {

    /**
     * 게시글 생성
     *
     * 새로운 게시글을 생성합니다. 게시글 작성자는 인증된 사용자여야 하며,
     * 제목과 내용을 포함한 요청 데이터를 기반으로 게시글을 저장합니다.
     *
     * @param postRequestDto 게시글 생성 요청 데이터 (제목, 내용 포함)
     * @param userId         게시글 작성자의 사용자 ID
     * @return 생성된 게시글의 정보를 담은 응답 DTO
     */
    PostResponseDto add(PostRequestDto postRequestDto, Long userId);

    /**
     * 게시글 단일 조회
     *
     * 특정 ID를 기반으로 게시글을 조회합니다. 게시글이 존재하지 않으면 예외를 발생시킵니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 엔티티
     */
    Post get(Long postId);

    /**
     * 게시글 단일 조회 (응답 DTO 형태)
     *
     * 특정 ID를 기반으로 게시글을 조회한 후, 데이터를 응답 DTO 형태로 반환합니다.
     * 게시글 엔티티를 클라이언트가 사용하기 위한 DTO로 변환합니다.
     *
     * @param postId 조회할 게시글의 ID
     * @return 조회된 게시글 정보를 담은 응답 DTO
     */
    PostResponseDto getResponseDto(Long postId);

    /**
     * 모든 게시글 조회 (페이징 처리)
     *
     * 게시글 데이터를 페이징 처리하여 조회합니다. 페이지 번호, 크기, 정렬 조건 등은
     * 클라이언트 요청에 따라 동적으로 처리되며, 페이징 메타데이터와 함께 게시글 목록을 반환합니다.
     *
     * @param pageable 페이징 요청 정보 (페이지 번호, 크기, 정렬 조건 등)
     * @return 페이징 처리된 게시글 데이터를 담은 Page 객체 (PostResponseDto)
     */
    Page<PostResponseDto> getAll(Pageable pageable);

    /**
     * 게시글 수정
     *
     * 특정 ID를 가진 게시글의 제목과 내용을 수정합니다. 수정하려는 게시글이 존재하지 않거나
     * 권한 조건을 만족하지 않으면 예외를 발생시킵니다. 수정된 게시글 정보를 응답 DTO 형태로 반환합니다.
     *
     * @param postId  수정할 게시글의 ID
     * @param title   수정할 새로운 제목
     * @param content 수정할 새로운 내용
     * @return 수정된 게시글 정보를 담은 응답 DTO
     */
    PostResponseDto update(Long postId, String title, String content);

    /**
     * 게시글 삭제
     *
     * 특정 ID를 가진 게시글을 삭제합니다. 삭제하려는 게시글이 존재하지 않거나
     * 권한 조건을 만족하지 않으면 예외를 발생시킵니다.
     *
     * @param postId 삭제할 게시글의 ID
     */
    void delete(Long postId);
}
