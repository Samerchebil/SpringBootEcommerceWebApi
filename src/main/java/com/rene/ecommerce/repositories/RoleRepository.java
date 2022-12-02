package com.rene.ecommerce.repositories;

import com.rene.ecommerce.domain.users.Role;
import com.rene.ecommerce.enume.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository  extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole name);
}
