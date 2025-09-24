package com.example.VenediktovLab9.repositories;

import com.example.VenediktovLab9.mappers.ProductRowMapper;
import com.example.VenediktovLab9.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Product save(Product p) {
        String sql = "INSERT INTO product (name, description, price, image_path) VALUES (?, ?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setObject(3, p.getPrice());
            ps.setString(4, p.getImageUrl());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) p.setId(key.intValue());
        return p;
    }

    public Optional<Product> findById(int id) {
        String sql = "SELECT * FROM product WHERE id = ?";
        List<Product> list = jdbcTemplate.query(sql, new ProductRowMapper(), id);
        return list.stream().findFirst();
    }

    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", new ProductRowMapper());
    }

    public int update(Product p) {
        String sql = "UPDATE product SET name=?, description=?, price=?, image_path=? WHERE id=?";
        return jdbcTemplate.update(sql, p.getName(), p.getDescription(), p.getPrice(), p.getImageUrl(), p.getId());
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM product WHERE id = ?", id);
    }
}
