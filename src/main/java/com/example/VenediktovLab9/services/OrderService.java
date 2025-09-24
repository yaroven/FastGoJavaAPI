package com.example.VenediktovLab9.services;

import com.example.VenediktovLab9.models.Order;
import com.example.VenediktovLab9.models.OrderItem;
import com.example.VenediktovLab9.repositories.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepo;

    public OrderService(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    public List<Order> getAll() {
        return orderRepo.findAll();
    }

    public Optional<Order> getById(int id) {
        return orderRepo.findById(id);
    }

    @Transactional
    public Order create(Integer userId, List<OrderItem> items) {
        return orderRepo.createOrder(userId, items);
    }

    public void delete(int id) {
        orderRepo.deleteById(id);
    }
}
