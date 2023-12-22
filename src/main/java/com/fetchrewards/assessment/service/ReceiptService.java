package com.fetchrewards.assessment.service;

import com.fetchrewards.assessment.dto.ReceiptRequest;
import com.fetchrewards.assessment.model.Item;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class ReceiptService {
    public String generateReceiptId() {
        return UUID.randomUUID().toString();
    }

    public int calculatePoints(ReceiptRequest receiptRequest) {
        int points = 0;

        // One point for every alphanumeric character in the retailer name.
        points += receiptRequest.getRetailer().replaceAll("[^A-Za-z0-9]", "").length();

        //50 points if the total is a round dollar amount with no cents.
        if (receiptRequest.getTotal() == Math.floor(receiptRequest.getTotal())) {
            points += 50;
        }

        // 25 points if the total is a multiple of 0.25.
        if (receiptRequest.getTotal() % 0.25 == 0) {
            points += 25;
        }

        // 5 points for every two items on the receipt.
        points += (receiptRequest.getItems().size() / 2) * 5;

        // if trimmed length of the item description is a multiple of 3
        // multiply the price by 0.2 and round up to the nearest integer
        for (Item item : receiptRequest.getItems()) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                points += Math.ceil(item.getPrice() * 0.2);
            }
        }

        // 6 points if the day in the purchase date is odd.
        // ISO Date Time Format yyyy-MM-dd'T'HH:mm:ss.SSSXXX
        LocalDateTime purchaseDateTime = LocalDateTime.parse(
                receiptRequest.getPurchaseDate() + "T" + receiptRequest.getPurchaseTime(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME
        );
        if (purchaseDateTime.getDayOfMonth() % 2 != 0) {
            points += 6;
        }

        // 10 points if the time of purchase is after 2:00pm and before 4:00pm.
        if (purchaseDateTime.getHour() >= 14 && purchaseDateTime.getHour() < 16) {
            points += 10;
        }

        return points;
    }
}