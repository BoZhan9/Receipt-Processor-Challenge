package com.fetchrewards.assessment.dto;

import com.fetchrewards.assessment.model.Item;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptRequest {
    private String retailer;
    private String purchaseDate;
    private String purchaseTime;
    private List<Item> items;
    private double total;
}
