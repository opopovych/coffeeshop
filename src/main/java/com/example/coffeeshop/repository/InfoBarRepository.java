package com.example.coffeeshop.repository;

import com.example.coffeeshop.model.InfoBarSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InfoBarRepository extends JpaRepository<InfoBarSettings,Long> {
}
