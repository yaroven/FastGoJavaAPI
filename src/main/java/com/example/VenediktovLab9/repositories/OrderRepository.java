package com.example.VenediktovLab9.repositories;


import com.example.VenediktovLab9.models.Order;
import com.example.VenediktovLab9.models.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {

    @Autowired
    private OrderItemRepository orderItemRepository;
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    public Order createOrder(Integer userId, List<OrderItem> items) {
        String sql = "INSERT INTO orders (user_id, created_at) VALUES (?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, userId);
            ps.setObject(2, LocalDateTime.now());
            return ps;
        }, kh);

        Number key = kh.getKey();
        if (key == null) throw new IllegalStateException("Failed to create order");
        int orderId = key.intValue();

        for (OrderItem item : items) {
            item.setOrderId(orderId);
            orderItemRepository.save(item);
        }

        Order created = new Order();
        created.setId(orderId);
        created.setUserId(userId);
        created.setCreatedAt(LocalDateTime.now());
        created.setItems(items);
        return created;
    }

    public Optional<Order> findById(int id) {
        String sql = "SELECT id, user_id, created_at FROM orders WHERE id = ?";
        List<Order> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setUserId(rs.getInt("user_id"));
            o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return o;
        }, id);
        if (list.isEmpty()) return Optional.empty();
        Order order = list.get(0);
        order.setItems(orderItemRepository.findByOrderId(order.getId()));
        return Optional.of(order);
    }

    public List<Order> findAll() {
        List<Order> orders = jdbcTemplate.query("SELECT id, user_id, created_at FROM orders", (rs, rowNum) -> {
            Order o = new Order();
            o.setId(rs.getInt("id"));
            o.setUserId(rs.getInt("user_id"));
            o.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return o;
        });
        for (Order o : orders) {
            o.setItems(orderItemRepository.findByOrderId(o.getId()));
        }
        return orders;
    }

    public int deleteById(int id) {
        orderItemRepository.deleteByOrderId(id);
        return jdbcTemplate.update("DELETE FROM orders WHERE id = ?", id);
    }
}

