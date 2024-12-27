package org.example.restfulblogflatform.log.service;

import org.example.restfulblogflatform.log.entity.LogEntry;
import org.example.restfulblogflatform.log.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 로그(Log) 데이터를 처리하는 서비스 클래스.
 * 로그 데이터를 생성하고 데이터베이스에 저장하는 기능을 제공합니다.
 */
@Service // Spring의 Service 계층으로 등록
public class LogService {

    private final LogEntryRepository logEntryRepository; // 로그 데이터를 처리하는 JPA Repository

    /**
     * LogService 생성자.
     * @param logEntryRepository 로그 엔트리를 저장 및 조회하는 JPA Repository
     */
    public LogService(@Qualifier("logEntryRepository") LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    /**
     * 로그 데이터를 생성하고 데이터베이스에 저장합니다.
     *
     * @param level 로그 레벨 (예: INFO, ERROR)
     * @param message 로그 메시지
     * @param exception 예외 메시지 (선택적, null 가능)
     */
    public void saveLog(String level, String message, String exception) {
        // 새로운 LogEntry 객체 생성 및 필드 설정
        LogEntry logEntry = new LogEntry();
        logEntry.setLevel(level); // 로그 레벨 설정
        logEntry.setMessage(message); // 로그 메시지 설정
        logEntry.setException(exception); // 예외 메시지 설정 (null 가능)

        // JPA Repository를 통해 데이터베이스에 저장
        logEntryRepository.save(logEntry);
    }
}