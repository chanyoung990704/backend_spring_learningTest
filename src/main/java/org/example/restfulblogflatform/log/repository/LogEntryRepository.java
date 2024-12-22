package org.example.restfulblogflatform.log.repository;

import org.example.restfulblogflatform.log.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
}