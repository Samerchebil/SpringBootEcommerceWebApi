package com.rene.ecommerce.domain.users;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rene.ecommerce.domain.Order;
import com.rene.ecommerce.domain.Product;

@Entity
@Table(name = "TB_CLIENTS")
public class Client extends User {
	public Client()
	{
		setType("Client");
		setNumberOfBuys(0);
		setHowMuchMoneyThisClientHasSpent(0.0);
	}
	private List<Product> boughtProducts;
	private Set<Product> productsWished;
	private List<Order> orders;

	@Column
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer numberOfBuys;

	@Column
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Double howMuchMoneyThisClientHasSpent;


	@JoinTable(	name = "user_roles",
			joinColumns = @JoinColumn(name = "client_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = Role.class)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	@Override
	@Id
	@Column(name = "client_id")
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


/*
	@Enumerated(EnumType.STRING)
	@ElementCollection(targetClass=RoleE.class)
	@CollectionTable(name="ROLES")
	@Column(name="ROLES")
	private List<RoleE> roles;


	public List<String> getRoles() {
		return roles.stream().map(RoleE::getDescription).collect(Collectors.toList());
	}

	public void setRoles(List<RoleE> role) {
		this.roles = role;
	}
*/

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
	public String getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}
	@OneToMany(mappedBy = "buyerOfTheProduct")

	public List<Product> getBoughtProducts() {
		return boughtProducts;
	}

	public void setBoughtProducts(List<Product> boughtProducts) {
		this.boughtProducts = boughtProducts;
	}

	public Integer getNumberOfBuys() {
		return numberOfBuys;
	}

	@ManyToMany(mappedBy = "whoWhishesThisProduct")
	public Set<Product> getProductsWished() {
		return productsWished;
	}

	public void setProductsWished(Set<Product> productsWished) {
		this.productsWished = productsWished;
	}

	@OneToMany(mappedBy = "buyer")
	@JsonIgnore
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public void addNumberOfBuys() {
		this.numberOfBuys = this.numberOfBuys + 1;
	}

	public void setNumberOfBuys(Integer numberOfBuys) {
		this.numberOfBuys = numberOfBuys;
	}

	public Double getHowMuchMoneyThisClientHasSpent() {
		return howMuchMoneyThisClientHasSpent;
	}

	public void setHowMuchMoneyThisClientHasSpent(Double howMuchMoneyThisClientHasSpent) {
		this.howMuchMoneyThisClientHasSpent = howMuchMoneyThisClientHasSpent;
	}

	public void addSpentMoneyWhenClientBuyAProduct(Double productPrice) {
		this.howMuchMoneyThisClientHasSpent += productPrice;
	}
			

	
	
	

}
