package com.example.VenediktovLab9.services;

import com.example.VenediktovLab9.models.Product;
import com.example.VenediktovLab9.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepo;

    public ProductService(ProductRepository productRepo) {
        this.productRepo = productRepo;
    }

    public List<Product> getAll() {
        return productRepo.findAll();
    }

    public Optional<Product> getById(int id) {
        return productRepo.findById(id);
    }

    public Product create(Product p) {
        return productRepo.save(p);
    }

    public Product update(Product p) {
        productRepo.update(p);
        return p;
    }

    public void delete(int id) {
        productRepo.deleteById(id);
    }
}
