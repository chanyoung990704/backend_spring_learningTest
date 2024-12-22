package org.example.restfulblogflatform.log.service;

import org.example.restfulblogflatform.log.entity.LogEntry;
import org.example.restfulblogflatform.log.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LogService {

    private final LogEntryRepository logEntryRepository;

    public LogService(@Qualifier("logEntryRepository") LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    public void saveLog(String level, String message, String exception) {
        LogEntry logEntry = new LogEntry();
        logEntry.setLevel(level);
        logEntry.setMessage(message);
        logEntry.setException(exception);

        logEntryRepository.save(logEntry); // DB에 저장
    }
}
