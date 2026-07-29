package com.poc.raw_feed_service.service;

import com.poc.raw_feed_service.dto.BillingSummaryResponse;
import com.poc.raw_feed_service.dto.RawFeedRequest;
import com.poc.raw_feed_service.dto.RawFeedSummaryResponse;
import com.poc.raw_feed_service.entity.RawFeedData;
import com.poc.raw_feed_service.repository.RawFeedRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RawFeedService {

    private final RawFeedRepository rawFeedRepository;

    public RawFeedService(RawFeedRepository rawFeedRepository) {
        this.rawFeedRepository = rawFeedRepository;
    }

    public List<RawFeedData> processJsonFeed(List<RawFeedRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException("JSON raw feed input cannot be empty");
        }

        List<RawFeedData> dataList = new ArrayList<>();

        for (RawFeedRequest request : requests) {
            dataList.add(convertToEntity(request, "JSON"));
        }

        return rawFeedRepository.saveAll(dataList);
    }

    public List<RawFeedData> processCsvFeed(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("CSV file cannot be empty");
        }

        List<RawFeedData> dataList = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean isHeader = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                if (line.trim().isEmpty() || line.replace(",", "").trim().isEmpty()) {
                    continue;
                }

                String[] values = line.split(",");

                if (values.length < 10) {
                    throw new RuntimeException("Invalid CSV format at line " + lineNumber +
                            ". Expected at least 10 columns but found " + values.length);
                }

                RawFeedRequest request = RawFeedRequest.builder()
                        .timestamp(parseTimestamp(values[0].trim()))
                        .nodeId(parseNodeId(values[1].trim()))
                        .queryLatency(Double.parseDouble(values[2].trim()))
                        .cpuUtilization(Double.parseDouble(values[3].trim()))
                        .memoryUtilization(Double.parseDouble(values[4].trim()))
                        .diskIo(Double.parseDouble(values[5].trim()))
                        .networkLatency(Double.parseDouble(values[6].trim()))
                        .networkBandwidth(Double.parseDouble(values[7].trim()))
                        .activeQueries(Integer.parseInt(values[8].trim()))
                        .systemLoad(Double.parseDouble(values[9].trim()))
                        .build();

                dataList.add(convertToEntity(request, "CSV"));
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to process CSV file: " + e.getMessage());
        }

        if (dataList.isEmpty()) {
            throw new RuntimeException("CSV file does not contain valid data rows");
        }

        return rawFeedRepository.saveAll(dataList);
    }


    public List<RawFeedData> getHistory() {
        return rawFeedRepository.findAll();
    }

    public List<RawFeedData> getByNodeId(String nodeId) {
        return rawFeedRepository.findByNodeId(nodeId);
    }

    public List<RawFeedData> getByStatus(String status) {
        return rawFeedRepository.findByStatusIgnoreCase(status);
    }

    public RawFeedSummaryResponse getSummary() {
        List<RawFeedData> records = rawFeedRepository.findAll();

        long totalRecords = records.size();

        long upCount = records.stream()
                .filter(record -> "UP".equalsIgnoreCase(record.getStatus()))
                .count();

        long degradedCount = records.stream()
                .filter(record -> "DEGRADED".equalsIgnoreCase(record.getStatus()))
                .count();

        long downCount = records.stream()
                .filter(record -> "DOWN".equalsIgnoreCase(record.getStatus()))
                .count();

        double averageAvailability = records.stream()
                .mapToDouble(record -> record.getAvailabilityPercentage() == null ? 0.0 : record.getAvailabilityPercentage())
                .average()
                .orElse(0.0);

        double totalBillingAmount = records.stream()
                .mapToDouble(record -> record.getBillingAmount() == null ? 0.0 : record.getBillingAmount())
                .sum();

        double averageCpuUtilization = records.stream()
                .mapToDouble(record -> record.getCpuUtilization() == null ? 0.0 : record.getCpuUtilization())
                .average()
                .orElse(0.0);

        double averageMemoryUtilization = records.stream()
                .mapToDouble(record -> record.getMemoryUtilization() == null ? 0.0 : record.getMemoryUtilization())
                .average()
                .orElse(0.0);

        double averageNetworkLatency = records.stream()
                .mapToDouble(record -> record.getNetworkLatency() == null ? 0.0 : record.getNetworkLatency())
                .average()
                .orElse(0.0);

        RawFeedSummaryResponse response = new RawFeedSummaryResponse();
        response.setTotalRecords(totalRecords);
        response.setUpCount(upCount);
        response.setDegradedCount(degradedCount);
        response.setDownCount(downCount);
        response.setAverageAvailability(averageAvailability);
        response.setTotalBillingAmount(totalBillingAmount);
        response.setAverageCpuUtilization(averageCpuUtilization);
        response.setAverageMemoryUtilization(averageMemoryUtilization);
        response.setAverageNetworkLatency(averageNetworkLatency);

        return response;
    }

    public List<RawFeedData> getSlaViolations() {
        return rawFeedRepository.findAll()
                .stream()
                .filter(record ->
                        (record.getQueryLatency() != null && record.getQueryLatency() > 500)
                                || (record.getNetworkLatency() != null && record.getNetworkLatency() > 150)
                                || (record.getAvailabilityPercentage() != null && record.getAvailabilityPercentage() < 95)
                )
                .toList();
    }

    public BillingSummaryResponse getBillingSummary() {
        List<RawFeedData> records = rawFeedRepository.findAll();

        long totalRecords = records.size();

        double totalUsageValue = records.stream()
                .mapToDouble(record -> record.getUsageValue() == null ? 0.0 : record.getUsageValue())
                .sum();

        double totalBillingAmount = records.stream()
                .mapToDouble(record -> record.getBillingAmount() == null ? 0.0 : record.getBillingAmount())
                .sum();

        double averageUsageValue = records.stream()
                .mapToDouble(record -> record.getUsageValue() == null ? 0.0 : record.getUsageValue())
                .average()
                .orElse(0.0);

        double averageBillingAmount = records.stream()
                .mapToDouble(record -> record.getBillingAmount() == null ? 0.0 : record.getBillingAmount())
                .average()
                .orElse(0.0);

        double minimumBillingAmount = records.stream()
                .mapToDouble(record -> record.getBillingAmount() == null ? 0.0 : record.getBillingAmount())
                .min()
                .orElse(0.0);

        double maximumBillingAmount = records.stream()
                .mapToDouble(record -> record.getBillingAmount() == null ? 0.0 : record.getBillingAmount())
                .max()
                .orElse(0.0);

        long highBillingRecords = records.stream()
                .filter(record -> record.getBillingAmount() != null && record.getBillingAmount() > 1000)
                .count();

        return BillingSummaryResponse.builder()
                .totalRecords(totalRecords)
                .totalUsageValue(totalUsageValue)
                .totalBillingAmount(totalBillingAmount)
                .averageUsageValue(averageUsageValue)
                .averageBillingAmount(averageBillingAmount)
                .minimumBillingAmount(minimumBillingAmount)
                .maximumBillingAmount(maximumBillingAmount)
                .highBillingRecords(highBillingRecords)
                .build();
    }

    private RawFeedData convertToEntity(RawFeedRequest request, String feedType) {
        String status = calculateStatus(
                request.getCpuUtilization(),
                request.getMemoryUtilization(),
                request.getNetworkLatency(),
                request.getSystemLoad()
        );

        double availabilityPercentage = calculateAvailability(status);

        double usageValue = calculateUsageValue(
                request.getActiveQueries(),
                request.getNetworkBandwidth(),
                request.getSystemLoad()
        );

        double billingAmount = calculateBillingAmount(usageValue);

        RawFeedData data = new RawFeedData();

        data.setTimestamp(request.getTimestamp());
        data.setNodeId(request.getNodeId());
        data.setQueryLatency(request.getQueryLatency());
        data.setCpuUtilization(request.getCpuUtilization());
        data.setMemoryUtilization(request.getMemoryUtilization());
        data.setDiskIo(request.getDiskIo());
        data.setNetworkLatency(request.getNetworkLatency());
        data.setNetworkBandwidth(request.getNetworkBandwidth());
        data.setActiveQueries(request.getActiveQueries());
        data.setSystemLoad(request.getSystemLoad());
        data.setStatus(status);
        data.setAvailabilityPercentage(availabilityPercentage);
        data.setUsageValue(usageValue);
        data.setBillingAmount(billingAmount);
        data.setFeedType(feedType);
        data.setCreatedAt(LocalDateTime.now());

        return data;
    }

    private LocalDateTime parseTimestamp(String timestampValue) {
        try {
            return LocalDateTime.parse(timestampValue);
        } catch (Exception ignored) {
            try {
                long minutes = (long) Double.parseDouble(timestampValue);
                return LocalDateTime.of(2026, 7, 9, 10, 30).plusMinutes(minutes);
            } catch (Exception e) {
                throw new RuntimeException("Invalid timestamp value: " + timestampValue);
            }
        }
    }
    private String parseNodeId(String nodeIdValue) {
        if (nodeIdValue == null || nodeIdValue.isBlank()) {
            throw new RuntimeException("node_id cannot be empty");
        }

        if (nodeIdValue.toLowerCase().startsWith("node-")) {
            return nodeIdValue;
        }

        return "node-" + nodeIdValue;
    }

    private String calculateStatus(Double cpu, Double memory, Double networkLatency, Double systemLoad) {
        if (cpu > 90 || memory > 90 || networkLatency > 150 || systemLoad > 90) {
            return "DOWN";
        } else if (cpu > 80 || memory > 80 || networkLatency > 100 || systemLoad > 80) {
            return "DEGRADED";
        } else {
            return "UP";
        }
    }

    private double calculateAvailability(String status) {
        return switch (status.toUpperCase()) {
            case "UP" -> 99.9;
            case "DEGRADED" -> 95.0;
            case "DOWN" -> 80.0;
            default -> 0.0;
        };
    }

    private double calculateUsageValue(Integer activeQueries, Double networkBandwidth, Double systemLoad) {
        return activeQueries + networkBandwidth + systemLoad;
    }

    private double calculateBillingAmount(double usageValue) {
        return usageValue * 2.5;
    }
}