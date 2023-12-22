package com.fetchrewards.assessment;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetchrewards.assessment.controller.ReceiptController;
import com.fetchrewards.assessment.dto.ReceiptRequest;
import com.fetchrewards.assessment.service.ReceiptService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest({ReceiptController.class})
public class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String mmReceiptJson;
    private String simpleReceiptJson;
    private String morningReceiptJson;

    @MockBean
    private ReceiptService receiptService;

    @Before
    public void setup() throws Exception {
        mmReceiptJson = new String(Files.readAllBytes(Paths.get("src/test/resources/mm-receipt.json")));
        simpleReceiptJson = new String(Files.readAllBytes(Paths.get("src/test/resources/simple-receipt.json")));
        morningReceiptJson = new String(Files.readAllBytes(Paths.get("src/test/resources/morning-receipt.json")));
    }

    @Test
    public void testProcessAndGetPointsWithMMReceipt() throws Exception {
        // mock
        String mockReceiptId = UUID.randomUUID().toString();
        given(receiptService.generateReceiptId()).willReturn(mockReceiptId);
        given(receiptService.calculatePoints(any(ReceiptRequest.class))).willReturn(109);

        // handle request
        MvcResult result = mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mmReceiptJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // get id
        String responseString = result.getResponse().getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        String receiptId = jsonResponse.get("id").asText();

        // calculate points
        mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'points': 109}"));
    }

    @Test
    public void testProcessAndGetPointsWithSimpleReceipt() throws Exception {
        // mock
        String mockReceiptId = UUID.randomUUID().toString();
        given(receiptService.generateReceiptId()).willReturn(mockReceiptId);
        given(receiptService.calculatePoints(any(ReceiptRequest.class))).willReturn(31);

        // handle request
        MvcResult result = mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(simpleReceiptJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // get id
        String responseString = result.getResponse().getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        String receiptId = jsonResponse.get("id").asText();

        // calculate points
        mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'points': 31}"));
    }

    @Test
    public void testProcessAndGetPointsWithMorningReceipt() throws Exception {
        // mock
        String mockReceiptId = UUID.randomUUID().toString();
        given(receiptService.generateReceiptId()).willReturn(mockReceiptId);
        given(receiptService.calculatePoints(any(ReceiptRequest.class))).willReturn(15);

        // handle request
        MvcResult result = mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(morningReceiptJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // get id
        String responseString = result.getResponse().getContentAsString();
        JsonNode jsonResponse = objectMapper.readTree(responseString);
        String receiptId = jsonResponse.get("id").asText();

        // calculate points
        mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{'points': 15}"));
    }

}
