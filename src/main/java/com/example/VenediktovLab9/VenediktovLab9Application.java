package com.example.VenediktovLab9;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class VenediktovLab9Application implements CommandLineRunner {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(VenediktovLab9Application.class, args);
    }

    @Override
    public void run(String... args) {
        runJDBC();
    }

    void runJDBC() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS order_item");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders");
        jdbcTemplate.execute("DROP TABLE IF EXISTS product");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");

        jdbcTemplate.execute("""
        CREATE TABLE product(
            id SERIAL PRIMARY KEY,
            name VARCHAR(255),
            description VARCHAR(255),
            price FLOAT,
            image_path VARCHAR(255)
        )
    """);

        jdbcTemplate.execute("""
        CREATE TABLE users(
            id SERIAL PRIMARY KEY,
            username VARCHAR(30),
            password VARCHAR(120),
            email VARCHAR(50)
        )
    """);

        jdbcTemplate.execute("""
        CREATE TABLE orders(
            id SERIAL PRIMARY KEY,
            user_id INT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (user_id) REFERENCES users(id)
        )
    """);

        jdbcTemplate.execute("""
        CREATE TABLE order_item(
            id SERIAL PRIMARY KEY,
            order_id INT,
            product_id INT,
            quantity INT,
            FOREIGN KEY (order_id) REFERENCES orders(id),
            FOREIGN KEY (product_id) REFERENCES product(id)
        )
    """);
    }


}
