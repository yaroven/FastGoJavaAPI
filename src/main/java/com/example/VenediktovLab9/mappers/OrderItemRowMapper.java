package com.example.VenediktovLab9.mappers;

import com.example.VenediktovLab9.models.OrderItem;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemRowMapper implements RowMapper<OrderItem> {
    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem oi = new OrderItem();
        oi.setId(rs.getInt("id"));
        oi.setOrderId(rs.getInt("order_id"));
        oi.setProductId(rs.getInt("product_id"));
        oi.setQuantity(rs.getInt("quantity"));
        return oi;
    }
}