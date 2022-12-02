package com.rene.ecommerce.services.details;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.rene.ecommerce.domain.users.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rene.ecommerce.domain.users.Client;
import com.rene.ecommerce.domain.users.Seller;
import com.rene.ecommerce.repositories.ClientRepository;
import com.rene.ecommerce.repositories.SellerRepository;
import com.rene.ecommerce.security.ClientSS;
import com.rene.ecommerce.security.SellerSS;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ClientRepository clientRepo;

	@Autowired
	private SellerRepository sellerRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Client cli = clientRepo.findByEmail(email);

		if (cli == null) { //lazmik tbadel il condition
			Seller sel = sellerRepo.findByEmail(email);

			SellerSS selSS = new SellerSS();

			selSS.setId(sel.getId());
			selSS.setEmail(sel.getEmail());
			selSS.setPassword(sel.getPassword());
			Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		/*	sel.getRoles().forEach(role->{
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			});
		*/
			List authoritiesList = new ArrayList(authorities);
			selSS.setAuthorities(authoritiesList);
			return selSS;
		}

		ClientSS cliSS = new ClientSS();

		cliSS.setId(cli.getId());
		cliSS.setEmail(cli.getEmail());
		cliSS.setPassword(cli.getPassword());
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		/*cli.getRoles().forEach(role->{
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		});*/
		List authoritiesList = new ArrayList(authorities);
		cliSS.setAuthorities(authoritiesList);
		return cliSS;
	}

}
