package com.rene.ecommerce.services.details;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.rene.ecommerce.domain.users.Admin;
import com.rene.ecommerce.domain.users.Role;
import com.rene.ecommerce.repositories.AdminRepository;
import com.rene.ecommerce.security.AdminSS;
import com.rene.ecommerce.security.UserSS;
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

	@Autowired
	private AdminRepository adminRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		Client cli = clientRepo.findByEmail(email);
		Admin adm = adminRepo.findByEmail(email);
		if (cli == null) {//lazmik tbadel il condition
			if (adm == null) {
				Seller sel = sellerRepo.findByEmail(email);
				SellerSS selSS = new SellerSS();
				selSS.setId(sel.getId());
				selSS.setEmail(sel.getEmail());
				selSS.setPassword(sel.getPassword());
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				sel.getRoles().forEach(role->{
					authorities.add(new SimpleGrantedAuthority(""+role.getName()));
				});
				List<String> authoritiesList = new ArrayList(authorities);
				selSS.setAuthoritiesB(authorities);
				return selSS;
			}
			else {
				AdminSS admSS = new AdminSS();
				admSS.setId(adm.getId());
				admSS.setEmail(adm.getEmail());
				admSS.setPassword(adm.getPassword());
				List<SimpleGrantedAuthority> authorities = new ArrayList<>();
				adm.getRoles().forEach(role->{
					authorities.add(new SimpleGrantedAuthority(""+role.getName()));
				});
				List<String> authoritiesList = new ArrayList(authorities);
				admSS.setAuthoritiesB(authorities);
				return admSS;
			}
		}
			ClientSS cliSS = new ClientSS();
			cliSS.setId(cli.getId());
			cliSS.setEmail(cli.getEmail());
			cliSS.setPassword(cli.getPassword());
			List<SimpleGrantedAuthority> authorities = new ArrayList<>();
			cli.getRoles().forEach(role->{
				authorities.add(new SimpleGrantedAuthority(""+role.getName()));
			});

			List authoritiesList = new ArrayList(authorities);
			cliSS.setAuthoritiesB(authorities);
			return cliSS;
	}

}
