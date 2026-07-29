package com.poc.raw_feed_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingSummaryResponse {

    private long totalRecords;

    private double totalUsageValue;

    private double totalBillingAmount;

    private double averageUsageValue;

    private double averageBillingAmount;

    private double minimumBillingAmount;

    private double maximumBillingAmount;

    private long highBillingRecords;
}