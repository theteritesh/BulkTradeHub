package com.technoworld.BulkTradeHub.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.technoworld.BulkTradeHub.entity.OrderItems;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.entity.UserOrders;
import com.technoworld.BulkTradeHub.repository.OrderItemsRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.UserOrderRepository;
import com.technoworld.BulkTradeHub.repository.UserRepository;

@Controller
@RequestMapping("/seller")
public class SellerOrderController {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private ProductPostRepository productPostRepository;

    @Autowired
    private UserOrderRepository userOrderRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/manage-orders")
    public String manageOrdersPage() {
        return "seller/manageOrders";
    }

    @GetMapping("/api/my-sales")
    @ResponseBody
    public ResponseEntity<?> getMySales(Principal principal) {
        try {
            User seller = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            List<OrderItems> soldItems = orderItemsRepository.findBySellerIdOrderByIdDesc(seller.getId());

            List<Map<String, Object>> salesData = new ArrayList<>();

            for (OrderItems item : soldItems) {
                Map<String, Object> map = new HashMap<>();
                map.put("itemId", item.getId());
                map.put("quantity", item.getLotsQuntity());
                map.put("totalPrice", item.getSubTotal());
                map.put("status", item.getStatus());
                
                // Fetch Product Details
                Optional<ProductPost> productOpt = productPostRepository.findById(item.getProductPostId());
                if (productOpt.isPresent()) {
                    map.put("productName", productOpt.get().getProductName());
                } else {
                    map.put("productName", "Unknown Product");
                }

                // Fetch Order Details (for Date)
                Optional<UserOrders> orderOpt = userOrderRepository.findById(item.getOrderId());
                if (orderOpt.isPresent()) {
                    map.put("orderDate", orderOpt.get().getCreatedAt());
                    map.put("orderIdDisplay", orderOpt.get().getRazorpayOrderId());
                }

                // Fetch Buyer Details
                Optional<User> buyerOpt = userRepository.findById((int)item.getBuyerId());
                if (buyerOpt.isPresent()) {
                    User buyer = buyerOpt.get();
                    map.put("buyerName", buyer.getName());
                    map.put("buyerEmail", buyer.getEmail());
                    if (buyer.getProfile() != null) {
                        map.put("buyerPhone", buyer.getProfile().getPhoneNumber());
                        map.put("shippingAddress", buyer.getProfile().getAddress() + ", " + 
                                                   buyer.getProfile().getCity() + ", " + 
                                                   buyer.getProfile().getPincode());
                    } else {
                        map.put("buyerPhone", "N/A");
                        map.put("shippingAddress", "N/A");
                    }
                }

                salesData.add(map);
            }

            return ResponseEntity.ok(salesData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error fetching sales");
        }
    }

    @PostMapping("/api/update-status")
    @ResponseBody
    public ResponseEntity<?> updateOrderStatus(@RequestBody Map<String, Object> payload, Principal principal) {
        try {
            User seller = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();
            
            Integer itemId = Integer.parseInt(payload.get("itemId").toString());
            String newStatus = (String) payload.get("status");

            Optional<OrderItems> itemOpt = orderItemsRepository.findById(itemId);
            if (itemOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order Item not found");
            }

            OrderItems item = itemOpt.get();

            // Security Check: Ensure the logged-in user is the seller of this item
            if (item.getSellerId() != seller.getId()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to update this order");
            }

            item.setStatus(newStatus);
            orderItemsRepository.save(item);

            return ResponseEntity.ok("Status updated successfully");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status");
        }
    }
}
