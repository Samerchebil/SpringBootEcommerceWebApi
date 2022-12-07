package com.rene.ecommerce.domain.users;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "TB_ADMIN")
public class Admin extends User{

        public Admin()
        {
            setType("Admin");
        }

    @JoinTable(	name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roleName"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    @Column(name = "adminId",unique = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        // TODO Auto-generated method stub
        return super.getId();
    }

    @Column
    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return super.getName();
    }

    @Column(unique = true)
    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        return super.getEmail();
    }

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getPassword() {
        // TODO Auto-generated method stub
        return super.getPassword();
    }
    @Column
    @Override
    public String getType()
    {
        // TODO Auto-generated method stub
        return super.getType();
    }

}
