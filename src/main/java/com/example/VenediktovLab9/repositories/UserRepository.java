package com.example.VenediktovLab9.repositories;

import com.example.VenediktovLab9.mappers.UserRowMapper;
import com.example.VenediktovLab9.models.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User u) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEmail());
            return ps;
        }, kh);
        Number key = kh.getKey();
        if (key != null) u.setId(key.intValue());
        return u;
    }

    public Optional<User> findById(int id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new UserRowMapper(), id);
        return list.stream().findFirst();
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new UserRowMapper());
    }

    public int update(User u) {
        return jdbcTemplate.update("UPDATE users SET username=?, password=?, email=? WHERE id=?",
                u.getUsername(), u.getPassword(), u.getEmail(), u.getId());
    }

    public int deleteById(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }
}
