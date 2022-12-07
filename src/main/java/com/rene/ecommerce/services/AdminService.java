package com.rene.ecommerce.services;

import com.rene.ecommerce.domain.dto.updated.UpdatedAdmin;
import com.rene.ecommerce.domain.users.Admin;
import com.rene.ecommerce.domain.users.Role;
import com.rene.ecommerce.enume.ERole;
import com.rene.ecommerce.exceptions.*;
import com.rene.ecommerce.repositories.AdminRepository;
import com.rene.ecommerce.repositories.ClientRepository;
import com.rene.ecommerce.repositories.RoleRepository;
import com.rene.ecommerce.repositories.SellerRepository;
import com.rene.ecommerce.security.AdminSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
@Service
public class AdminService
{

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private SellerRepository sellerRepo;


    @Autowired
    private ClientRepository clientRepo;


    public Admin findById(Integer id) {
        AdminSS user = UserService.adminAuthenticated();
        if (user == null || !user.getId().equals(id)) {
            throw new AuthorizationException();
        }
        Optional<Admin> obj = adminRepo.findById(id);
        try {
            return obj.get();
        } catch (NoSuchElementException e)
        {
            throw new ObjectNotFoundException();
        }
    }

    public Admin returnAdminWithoutParsingTheId() {
        AdminSS user = UserService.adminAuthenticated();

        if (user == null) {
            throw new AuthorizationException();
        }
        Optional<Admin> obj = adminRepo.findById(user.getId());

        try {
            return obj.get();
        } catch (NoSuchElementException e)
        {
            throw new ObjectNotFoundException();
        }

    }

    public List<Admin> findAll() {
        return adminRepo.findAll();
    }

    public Admin findByEmail(String email) {
        AdminSS user = UserService.adminAuthenticated();

        if (user == null || !user.getUsername().equals(email)) {
            throw new AuthorizationException();
        }
        Admin obj = adminRepo.findByEmail(email);

        if (obj == null) {
            throw new ObjectNotFoundException();
        }

        return obj;
    }

    @Transactional
    public Admin insert(Admin obj){
        obj.setId(null);
        obj.setPassword(passwordEncoder.encode(obj.getPassword()));
        if (adminRepo.findByEmail(obj.getEmail()) == null) {
            try {
                Set<Role> adminRoles = new HashSet<>();
                adminRoles.add(roleRepo.findByName(ERole.ROLE_ADMIN).get());
                adminRoles.add(roleRepo.findByName(ERole.ROLE_USER).get());
                obj.setRoles(adminRoles);
                return adminRepo.save(obj);
            } catch (Exception e) {
                throw new DuplicateEntryException();
            }
        }
        throw new ClientOrSellerHasThisSameEntryException("Admin");
        }


    @Transactional
    public Admin update(UpdatedAdmin obj)
    {
            AdminSS user = UserService.adminAuthenticated();
            Admin adm = adminRepo.findById(user.getId()).get();

            if (user == null || !user.getId().equals(adm.getId())) {
                throw new AuthorizationException();
            }
            adm.setEmail(obj.getEmail());
            adm.setName(obj.getName());
            adm.setPassword(passwordEncoder.encode(obj.getPassword()));
            if(clientRepo.findByEmail(adm.getEmail()) == null){
               try {
                   if(sellerRepo.findByEmail(adm.getEmail()) == null){
                       try {
                            return adminRepo.save(adm);
                          } catch (Exception e) {
                            throw new DuplicateEntryException();
                       }
                   }
                   throw new ClientOrSellerHasThisSameEntryException("Admin");
                }catch (Exception e){
                   throw new DuplicateEntryException();
                }
               }
            throw new ClientOrSellerHasThisSameEntryException("Admin");
    }


        public void delete()
        {
        AdminSS user = UserService.adminAuthenticated();
        Admin adm = adminRepo.findById(user.getId()).get();
        if (user == null || !user.getId().equals(adm.getId())) {
            throw new AuthorizationException();
        }
        try {
            adminRepo.delete(adm);
        } catch (Exception e) {
            throw new ObjectNotFoundException();
        }
        }

}
