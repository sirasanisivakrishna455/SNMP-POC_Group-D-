package com.poc.raw_feed_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawFeedSummaryResponse {

    private long totalRecords;

    private long upCount;

    private long degradedCount;

    private long downCount;

    private double averageAvailability;

    private double totalBillingAmount;

    private double averageCpuUtilization;

    private double averageMemoryUtilization;

    private double averageNetworkLatency;
}