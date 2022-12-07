package com.rene.ecommerce.services;

import java.util.*;

import javax.transaction.Transactional;

import com.rene.ecommerce.domain.dto.updated.UpdatedClient;
import com.rene.ecommerce.domain.users.Client;
import com.rene.ecommerce.domain.users.Role;
import com.rene.ecommerce.enume.ERole;
import com.rene.ecommerce.repositories.AdminRepository;
import com.rene.ecommerce.repositories.RoleRepository;
import com.rene.ecommerce.security.AdminSS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rene.ecommerce.domain.dto.updated.UpdatedSeller;
import com.rene.ecommerce.domain.users.Seller;
import com.rene.ecommerce.exceptions.AuthorizationException;
import com.rene.ecommerce.exceptions.ClientOrSellerHasThisSameEntryException;
import com.rene.ecommerce.exceptions.DuplicateEntryException;
import com.rene.ecommerce.exceptions.ObjectNotFoundException;
import com.rene.ecommerce.exceptions.UserHasProductsRelationshipsException;
import com.rene.ecommerce.repositories.ClientRepository;
import com.rene.ecommerce.repositories.SellerRepository;
import com.rene.ecommerce.security.SellerSS;

@Service
public class SellerService {

	@Autowired
	private SellerRepository sellerRepo;

	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private AdminRepository adminRepo;


	private ERole eRole;
	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Seller findById(Integer id) {
		SellerSS user = UserService.sellerAuthenticated();
		if (user == null || !user.getId().equals(id)) {
			throw new AuthorizationException();
		}
		Optional<Seller> obj = sellerRepo.findById(id);
		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}
	}
	
	public Seller returnClientWithoutParsingTheId() {
		SellerSS user = UserService.sellerAuthenticated();
		if (user == null) {
			throw new AuthorizationException();
		}
		try {
			return findById(user.getId());
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}
	}

	public List<Seller> findAll() {
		return sellerRepo.findAll();
	}

	@Transactional
	public Seller insert(Seller obj) {
		obj.setId(null);
		obj.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (sellerRepo.findByEmail(obj.getEmail()) == null) {
			try {
				Set<Role> sellerRoles = new HashSet<>();
				sellerRoles.add(roleRepo.findByName(ERole.ROLE_SELLER).get());
				sellerRoles.add(roleRepo.findByName(ERole.ROLE_USER).get());
				obj.setRoles(sellerRoles);
				return sellerRepo.save(obj);
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}

		throw new ClientOrSellerHasThisSameEntryException("client");

	}

	@Transactional
	public Seller update(UpdatedSeller obj) {
		SellerSS user = UserService.sellerAuthenticated();
		Seller sel = findById(user.getId());
		if (user == null || !user.getId().equals(sel.getId())) {
			throw new AuthorizationException();
		}
		sel.setEmail(obj.getEmail());
		sel.setName(obj.getName());
		sel.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (clientRepo.findByEmail(sel.getEmail()) == null) {
			try {
				if (adminRepo.findByEmail(sel.getEmail()) == null) {
					try {
						return sellerRepo.save(sel);
					} catch (Exception e) {
						throw new DuplicateEntryException();
					}
				}
				throw new ClientOrSellerHasThisSameEntryException("client");
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}
		throw new ClientOrSellerHasThisSameEntryException("client");
	}

	public Seller updateById (Integer id, UpdatedSeller obj) {
		AdminSS user = UserService.adminAuthenticated();
		if (user == null ) {
			throw new AuthorizationException();
		}

		Seller sel = sellerRepo.findById(id).get();
		sel.setEmail(obj.getEmail());
		sel.setName(obj.getName());
		sel.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (clientRepo.findByEmail(sel.getEmail()) == null) {

			try {
				if (adminRepo.findByEmail(sel.getEmail()) == null) {
					try {
						return sellerRepo.save(sel);
					} catch (Exception e) {
						throw new DuplicateEntryException();
					}
				}
				throw new ClientOrSellerHasThisSameEntryException("client");
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}
		throw new ClientOrSellerHasThisSameEntryException("client");
	}



	public void delete() {
		SellerSS user = UserService.sellerAuthenticated();
		Seller sel = findById(user.getId());
		if (sel.getNumberOfSells() == 0) {
			sellerRepo.deleteById(user.getId());
		}
		else {
			throw new UserHasProductsRelationshipsException();
		}
	}

	public void deleteById(Integer id) {
		AdminSS user = UserService.adminAuthenticated();
		if (user == null ) {
			throw new AuthorizationException();
		}
		Seller sel = sellerRepo.findById(id).get();
		// verify if the client hasn't bought any products
		// doing this by numberOfBuys because the performance
		if (sel.getNumberOfSells() == 0) {
			try
			{
				sellerRepo.deleteById(id);
			}
			catch (Exception e)
			{
				throw new ObjectNotFoundException();
			}
		}
		else {
			throw new UserHasProductsRelationshipsException();
		}
	}
}
