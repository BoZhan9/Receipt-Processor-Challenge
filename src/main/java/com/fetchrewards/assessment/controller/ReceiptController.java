package com.fetchrewards.assessment.controller;

import com.fetchrewards.assessment.dto.PointsResponse;
import com.fetchrewards.assessment.dto.ReceiptRequest;
import com.fetchrewards.assessment.dto.ReceiptResponse;
import com.fetchrewards.assessment.service.ReceiptService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {
    private final ReceiptService receiptService;
    // for thread-safety
    private final Map<String, Integer> pointsMap = new ConcurrentHashMap<>();

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping("/process")
    public ReceiptResponse processReceipt(@RequestBody ReceiptRequest receiptRequest) {
        String id = receiptService.generateReceiptId(); // generate through UUID
        int points = receiptService.calculatePoints(receiptRequest); // apply rules
        pointsMap.put(id, points);
        return new ReceiptResponse(id);
    }

    @GetMapping("/{id}/points")
    public PointsResponse getPoints(@PathVariable String id) {
        // requirement
        return new PointsResponse(pointsMap.getOrDefault(id, 0));
    }
}
