package com.poc.raw_feed_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "raw_feed_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawFeedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime timestamp;

    private String nodeId;

    private Double queryLatency;

    private Double cpuUtilization;

    private Double memoryUtilization;

    private Double diskIo;

    private Double networkLatency;

    private Double networkBandwidth;

    private Integer activeQueries;

    private Double systemLoad;

    private String status;

    private Double availabilityPercentage;

    private Double usageValue;

    private Double billingAmount;

    private String feedType;

    private LocalDateTime createdAt;
}