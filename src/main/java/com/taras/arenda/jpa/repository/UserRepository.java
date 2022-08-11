package com.taras.arenda.jpa.repository;

import com.taras.arenda.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Long> {
}
