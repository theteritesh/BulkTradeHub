package com.technoworld.BulkTradeHub.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.technoworld.BulkTradeHub.entity.User;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public Map<String, Object> createOrderObject(Double amount,Principal principal) throws Exception {
    	User user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", (int)(amount * 100));
        options.put("currency", "INR");
        options.put("receipt", "user_id"+user.getId());
        options.put("payment_capture", true);

        Order order = client.orders.create(options);

        // Convert to HashMap
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("id", order.get("id"));
        orderMap.put("amount", order.get("amount"));
        orderMap.put("currency", order.get("currency"));
        orderMap.put("status", order.get("status"));
        orderMap.put("receipt", order.get("receipt"));
        orderMap.put("created_at", order.get("created_at"));
        orderMap.put("userName", user.getName());
        orderMap.put("userEmail", user.getEmail());
        orderMap.put("userPhone", user.getProfile().getPhoneNumber());

        return orderMap;
    }


}

