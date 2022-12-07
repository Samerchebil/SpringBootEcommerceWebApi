package com.rene.ecommerce.repositories;

import com.rene.ecommerce.domain.users.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer>{
    @Transactional
    Admin findByEmail(String email);
}
