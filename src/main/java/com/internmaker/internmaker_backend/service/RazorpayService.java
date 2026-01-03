package com.internmaker.internmaker_backend.service;

import com.internmaker.internmaker_backend.entity.Enrollment;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    private final RazorpayClient razorpayClient;

    public RazorpayService() throws Exception {
        this.razorpayClient = new RazorpayClient("YOUR_KEY_ID", "YOUR_KEY_SECRET");
    }

    public String createOrder(Enrollment enrollment) throws Exception {
        // Razorpay expects amount in paise
        int amountInPaise = (int)(enrollment.getAmount() * 100);

        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "enroll_" + enrollment.getId());

        // optional notes
        JSONObject notes = new JSONObject();
        notes.put("enrollmentId", enrollment.getId());
        notes.put("userId", enrollment.getUser().getId());
        notes.put("courseId", enrollment.getCourse().getId());

        options.put("notes", notes);

        // Use lowercase 'orders' instead of 'Orders'
        Order order = razorpayClient.orders.create(options);


        return order.get("id"); // return this Razorpay order ID to frontend
    }
}
