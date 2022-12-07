package com.rene.ecommerce.services;

import java.util.*;

import javax.transaction.Transactional;

import com.rene.ecommerce.domain.users.Role;
import com.rene.ecommerce.enume.ERole;
import com.rene.ecommerce.repositories.AdminRepository;
import com.rene.ecommerce.repositories.RoleRepository;
import com.rene.ecommerce.security.AdminSS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.rene.ecommerce.domain.dto.updated.UpdatedClient;
import com.rene.ecommerce.domain.users.Client;
import com.rene.ecommerce.exceptions.AuthorizationException;
import com.rene.ecommerce.exceptions.ClientOrSellerHasThisSameEntryException;
import com.rene.ecommerce.exceptions.DuplicateEntryException;
import com.rene.ecommerce.exceptions.ObjectNotFoundException;
import com.rene.ecommerce.exceptions.UserHasProductsRelationshipsException;
import com.rene.ecommerce.repositories.ClientRepository;
import com.rene.ecommerce.repositories.SellerRepository;
import com.rene.ecommerce.security.ClientSS;
@Slf4j
@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private RoleRepository roleRepo;

	@Autowired
	private AdminRepository adminRepo;


	@Autowired
	private SellerRepository sellerRepo;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;


	public Client findById(Integer id) {

		ClientSS user = UserService.clientAuthenticated();

		if (user == null || !user.getId().equals(id)) {
			throw new AuthorizationException();
		}
		Optional<Client> obj = clientRepo.findById(id);

		try {
			return obj.get();
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	public Client returnClientWithoutParsingTheId() {
		ClientSS user = UserService.clientAuthenticated();

		if (user == null) {
			throw new AuthorizationException();
		}

		try {
			return findById(user.getId());
		} catch (NoSuchElementException e) {
			throw new ObjectNotFoundException();
		}

	}

	public List<Client> findAll() {
		return clientRepo.findAll();
	}

	@Transactional
	public Client insert(Client obj) {
		obj.setId(null);
		obj.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (clientRepo.findByEmail(obj.getEmail()) == null) {
			try {
				Set<Role> clientRoles = new HashSet<>();
				clientRoles.add(roleRepo.findByName(ERole.ROLE_CLIENT).get());
				clientRoles.add(roleRepo.findByName(ERole.ROLE_USER).get());
				obj.setRoles(clientRoles);
				return clientRepo.save(obj);
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}
		throw new ClientOrSellerHasThisSameEntryException("Seller");
	}

	@Transactional
	public Client update(UpdatedClient obj) {
		ClientSS user = UserService.clientAuthenticated();
		Client cli = findById(user.getId());

		if (user == null || !user.getId().equals(cli.getId()))
		{
			throw new AuthorizationException();
		}

		cli.setEmail(obj.getEmail());
		cli.setName(obj.getName());
		cli.setPassword(passwordEncoder.encode(obj.getPassword()));

		if (sellerRepo.findByEmail(cli.getEmail()) == null) {
			try {
				if (adminRepo.findByEmail(cli.getEmail()) == null) {
					try {
						return clientRepo.save(cli);
					} catch (Exception e) {
						throw new DuplicateEntryException();
					}
				}
				throw new ClientOrSellerHasThisSameEntryException("seller");
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}

		throw new ClientOrSellerHasThisSameEntryException("seller");

	}

	public Client updateById (Integer id, UpdatedClient obj) {
		AdminSS user = UserService.adminAuthenticated();
		if (user == null ) {
			throw new AuthorizationException();
		     }

		Client cli = clientRepo.findById(id).get();
		cli.setEmail(obj.getEmail());
		cli.setName(obj.getName());
		cli.setPassword(passwordEncoder.encode(obj.getPassword()));
		if (sellerRepo.findByEmail(cli.getEmail()) == null) {
			try {
				if (adminRepo.findByEmail(cli.getEmail()) == null) {
					try {
						return clientRepo.save(cli);
					} catch (Exception e) {
						throw new DuplicateEntryException();
					}
				}
				throw new ClientOrSellerHasThisSameEntryException("seller");
			} catch (Exception e) {
				throw new DuplicateEntryException();
			}
		}

		throw new ClientOrSellerHasThisSameEntryException("seller");
	}


	public void delete() {
		ClientSS user = UserService.clientAuthenticated();

		Client cli = findById(user.getId());
		// verify if the client hasn't bought any products
		// doing this by numberOfBuys because the performance
		if (cli.getNumberOfBuys() == 0) {
		try {
			clientRepo.deleteById(cli.getId());
		} catch (Exception e) {
			log.error("Error deleting client: " + e.getMessage());
			throw new ObjectNotFoundException();
		}
		}

		else {
			throw new UserHasProductsRelationshipsException();

		}

	}

	public void deleteById (Integer id) {
		AdminSS user = UserService.adminAuthenticated();
		if (user == null ) {
			throw new AuthorizationException();
		}
		Client cli = clientRepo.findById(id).get();
		// verify if the client hasn't bought any products
		// doing this by numberOfBuys because the performance
		if (cli.getNumberOfBuys() == 0) {
			try
			{
				clientRepo.deleteById(id);
			}
			catch (Exception e)
			{
				log.error("Error deleting client with exception: " + e.getMessage());
				throw new ObjectNotFoundException();
			}
		}

		else {
			throw new UserHasProductsRelationshipsException();
		     }

	}

}
