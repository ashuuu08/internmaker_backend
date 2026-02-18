package com.internmaker.internmaker_backend.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.HexFormat;

@Service
public class RazorpayService {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    public String createOrder(double amount) throws Exception {
        if (keyId == null || keyId.equals("YOUR_RAZORPAY_KEY_ID")) {
            // Return a mock order if no real keys are provided to avoid 500 error during
            // testing
            JSONObject mockOrder = new JSONObject();
            mockOrder.put("id", "order_mock_" + System.currentTimeMillis());
            mockOrder.put("amount", (int) (amount * 100));
            mockOrder.put("currency", "INR");
            mockOrder.put("status", "created");
            return mockOrder.toString();
        }
        RazorpayClient client = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (amount * 100)); // Convert to paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = client.orders.create(orderRequest);
        return order.toString();
    }

    public boolean verifySignature(String orderId, String paymentId, String signature) {
        if (orderId != null && orderId.startsWith("order_mock_")) {
            return true; // Auto-verify mock orders
        }
        try {
            String data = orderId + "|" + paymentId;
            SecretKeySpec signingKey = new SecretKeySpec(keySecret.getBytes(), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            return HexFormat.of().formatHex(rawHmac).equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyPaymentId(String paymentId, double expectedAmount) {
        System.out.println("DEBUG: Verifying Payment ID: " + paymentId + " (Expected: ₹" + expectedAmount + ")");

        if (paymentId == null || paymentId.isEmpty())
            return false;

        // 1. Check for Placeholder Keys - If so, allow ANY 'pay_' ID for testing
        if (keyId == null || keyId.equals("YOUR_RAZORPAY_KEY_ID")) {
            System.out.println("WARNING: Placeholder Razorpay Keys detected. Auto-accepting ID for testing.");
            return paymentId.startsWith("pay_");
        }

        // 2. Real Verification
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);
            com.razorpay.Payment payment = client.payments.fetch(paymentId);

            String status = payment.get("status");
            int amount = payment.get("amount"); // in paise

            System.out.println("DEBUG: Payment Status from Razorpay: " + status + ", Amount: " + amount + " paise");

            boolean isValid = ("captured".equals(status) || "authorized".equals(status))
                    && amount == (int) (expectedAmount * 100);

            if (!isValid) {
                System.out.println("ERROR: Payment verification failed. Status=" + status + ", Amount=" + amount);
            }
            return isValid;
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Razorpay Fetch Failed! Error: " + e.getMessage());
            return false;
        }
    }
}