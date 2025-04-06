package com.example.capstone.aibeauty.repository;

import com.example.capstone.aibeauty.domain.CosmeticProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CosmeticProductRepository extends JpaRepository<CosmeticProduct, Long> {
    Optional<CosmeticProduct> findByCategoryAndScore(String category, String score);
}
