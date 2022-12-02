package com.rene.ecommerce.domain.users;


import com.rene.ecommerce.enume.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, name = "roleName")
    private ERole name;

    public Role() {

    }

    public Role(ERole name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }
}