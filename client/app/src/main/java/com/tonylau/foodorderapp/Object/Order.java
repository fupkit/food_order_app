package com.tonylau.foodorderapp.Object;

import java.util.ArrayList;
import java.util.List;

public class Order {
    public Long orderId = null;
    public List<OrderItem> orderItems = new ArrayList<>();
    public boolean done = false;
}
