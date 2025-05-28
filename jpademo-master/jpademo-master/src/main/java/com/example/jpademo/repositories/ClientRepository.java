package com.example.jpademo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jpademo.models.Client;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    public Client findByEmail(String email);
}