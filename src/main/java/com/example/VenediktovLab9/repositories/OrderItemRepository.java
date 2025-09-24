package com.example.VenediktovLab9.repositories;



import com.example.VenediktovLab9.mappers.OrderItemRowMapper;
import com.example.VenediktovLab9.models.OrderItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class OrderItemRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderItemRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public OrderItem save(OrderItem oi) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity) VALUES (?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, oi.getOrderId());
            ps.setObject(2, oi.getProductId());
            ps.setObject(3, oi.getQuantity());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) oi.setId(key.intValue());
        return oi;
    }

    public List<OrderItem> findByOrderId(int orderId) {
        return jdbcTemplate.query("SELECT * FROM order_item WHERE order_id = ?", new OrderItemRowMapper(), orderId);
    }

    public int deleteByOrderId(int orderId) {
        return jdbcTemplate.update("DELETE FROM order_item WHERE order_id = ?", orderId);
    }
}
