package com.rene.ecommerce.resources;

import com.rene.ecommerce.domain.dto.updated.UpdatedAdmin;
import com.rene.ecommerce.domain.dto.updated.UpdatedClient;
import com.rene.ecommerce.domain.users.Admin;
import com.rene.ecommerce.domain.users.Client;
import com.rene.ecommerce.services.AdminService;
import com.rene.ecommerce.services.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Api(value = "Admin resource")
@CrossOrigin
public class AdminResource {

    @Autowired
    private AdminService service;

    @GetMapping("/admins")
    @ApiOperation(value = "Return all admins")
    public ResponseEntity<List<Admin>> findAll() {
        return ResponseEntity.ok().body(service.findAll());
    }

    @ApiOperation(value = "Return your own profile as an Admin")
    @GetMapping("/admin")
    public ResponseEntity<Admin> find() {

        Admin obj = service.returnAdminWithoutParsingTheId();
        return ResponseEntity.ok().body(obj);
    }

    @ApiOperation(value = "Create an admin")
    @PostMapping("/create/admin")
    public ResponseEntity<Admin> insert(@RequestBody Admin obj) {
        service.insert(obj);
        return ResponseEntity.ok().body(obj);
    }

    @PutMapping("/update/admin")
    @ApiOperation(value = "Update a client")
    public ResponseEntity<Admin> update(@RequestBody UpdatedAdmin obj){
        Admin adm =  service.update(obj);
        return ResponseEntity.ok().body(adm);
    }

    @DeleteMapping("/delete/admin")
    @ApiOperation(value = "Delete an admin")
    public ResponseEntity<Void> delete() {
        service.delete();
        return ResponseEntity.noContent().build();
    }


}
