package com.poc.raw_feed_service.repository;

import com.poc.raw_feed_service.entity.RawFeedData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RawFeedRepository extends JpaRepository<RawFeedData , Long> {
    List<RawFeedData> findByNodeId(String nodeId);
    List<RawFeedData> findByStatusIgnoreCase(String status);
}
