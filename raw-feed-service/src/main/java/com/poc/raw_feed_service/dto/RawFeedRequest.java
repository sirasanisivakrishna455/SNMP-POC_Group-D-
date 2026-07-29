package com.poc.raw_feed_service.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RawFeedRequest {

    @NotNull(message = "timestamp is required")
    private LocalDateTime timestamp;

    @NotBlank(message = "nodeId is required")
    private String nodeId;

    @NotNull(message = "queryLatency is required")
    private Double queryLatency;

    @NotNull(message = "cpuUtilization is required")
    private Double cpuUtilization;

    @NotNull(message = "MemoryUtilization is required")
    private Double memoryUtilization;
    @NotNull(message = "diskIo is required")
    private Double diskIo;

    @NotNull(message = "networkLatency is required")
    private Double networkLatency;

    @NotNull(message = "networkBandwidth is required")
    private Double networkBandwidth;

    @NotNull(message = "activeQueries is required")
    private Integer activeQueries;

    @NotNull(message = "systemLoad is required")
    private Double systemLoad;
}
