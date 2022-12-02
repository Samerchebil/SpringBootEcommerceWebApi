package com.rene.ecommerce.domain.users;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rene.ecommerce.domain.Order;
import com.rene.ecommerce.domain.Product;

@Entity
@Table(name = "TB_SELLERS")
public class Seller extends User {

	public Seller() {
		setType("Seller");
		setNumberOfSells(0);
		setHowMuchMoneyThisSellerHasSold(0.0);
	}

	private List<Product> ownProducts = new ArrayList<>();
	private List<Order> orders;

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

	@Column
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Integer numberOfSells;

	@Column
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Double howMuchMoneyThisSellerHasSold;

	@Override
	@Column(name = "sellerId")
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




/*	public Collection<String> getRoles() {
		return roles.stream().map(RoleE::getDescription).collect(Collectors.toCollection(ArrayList::new));
	}

	public void setRoles(Collection<RoleE> roles) {
		this.roles = roles;
	}
*/

	@Column
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return super.getType();
	}

	@JsonIgnore
	@OneToMany(mappedBy = "productOwner")
	public List<Product> getOwnProducts() {
		return ownProducts;
	}

	public void setOwnProducts(List<Product> ownProducts) {
		this.ownProducts = ownProducts;
	}


	@JsonIgnore
	@OneToMany(mappedBy = "seller")
	public List<Order> getOrders() {
		return orders;
	}

	public void setOrders(List<Order> orders) {
		this.orders = orders;
	}

	public Integer getNumberOfSells() {
		return numberOfSells;
	}

	public void addNumberOfSells() {
		this.numberOfSells = this.numberOfSells + 1;
	}

	public void setNumberOfSells(Integer numberOfSells) {
		this.numberOfSells = numberOfSells;
	}

	public Double getHowMuchMoneyThisSellerHasSold() {
		return howMuchMoneyThisSellerHasSold;
	}

	public void setHowMuchMoneyThisSellerHasSold(Double howMuchMoneyThisSellerHasSold) {
		this.howMuchMoneyThisSellerHasSold = howMuchMoneyThisSellerHasSold;
	}
	
	public void addSoldMoneyWhenSellerSellAProduct(Double productPrice) {
		this.howMuchMoneyThisSellerHasSold += productPrice;
	}

	
	
	

}
