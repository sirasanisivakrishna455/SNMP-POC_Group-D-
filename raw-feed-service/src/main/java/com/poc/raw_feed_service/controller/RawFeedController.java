package com.poc.raw_feed_service.controller;

import com.poc.raw_feed_service.dto.ApiResponse;
import com.poc.raw_feed_service.dto.BillingSummaryResponse;
import com.poc.raw_feed_service.dto.RawFeedRequest;
import com.poc.raw_feed_service.dto.RawFeedSummaryResponse;
import com.poc.raw_feed_service.entity.RawFeedData;
import com.poc.raw_feed_service.service.RawFeedService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/raw-feed")
public class RawFeedController {

    private final RawFeedService rawFeedService;

    public RawFeedController(RawFeedService rawFeedService){
        this.rawFeedService = rawFeedService;
    }

    @PostMapping("/json")
    public ApiResponse uploadJson(@Valid @RequestBody List<RawFeedRequest> requests) {
        List<RawFeedData> savedData = rawFeedService.processJsonFeed(requests);

        return ApiResponse.builder()
                .success(true)
                .message("JSON raw feed processed successfully")
                .data(savedData)
                .build();
    }

    @Operation(summary = "Upload CSV File")
    @PostMapping(value = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse uploadCsv(@RequestParam("file") MultipartFile file) {
        List<RawFeedData> savedData = rawFeedService.processCsvFeed(file);

        return ApiResponse.builder()
                .success(true)
                .message("CSV raw feed processed successfully")
                .data(savedData)
                .build();
    }

    @GetMapping("/history")
    public List<RawFeedData> getHistory() {
        return rawFeedService.getHistory();
    }

    @GetMapping("/node/{nodeId}")
    public List<RawFeedData> getByNodeId(@PathVariable String nodeId) {
        return rawFeedService.getByNodeId(nodeId);
    }

    @GetMapping("/status/{status}")
    public List<RawFeedData> getByStatus(@PathVariable String status) {
        return rawFeedService.getByStatus(status);
    }

    @GetMapping("/summary")
    public RawFeedSummaryResponse getSummary() {
        return rawFeedService.getSummary();
    }

    @GetMapping("/sla-violations")
    public List<RawFeedData> getSlaViolations() {
        return rawFeedService.getSlaViolations();
    }

    @GetMapping("/billing-summary")
    public BillingSummaryResponse getBillingSummary() {
        return rawFeedService.getBillingSummary();
    }
}