package com.rene.ecommerce.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rene.ecommerce.domain.dto.ranking.ClientRankingDTO;
import com.rene.ecommerce.domain.dto.updated.UpdatedClient;
import com.rene.ecommerce.domain.users.Client;
import com.rene.ecommerce.services.ClientService;
import com.rene.ecommerce.services.RankingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping
@Api(value = "Client resource")
@CrossOrigin
public class ClientResource {

	@Autowired
	private ClientService service;
	
	@Autowired
	private RankingService ranking;

	@GetMapping("/clients")
	@ApiOperation(value = "Return all clients")
	public ResponseEntity<List<Client>> findAll() {

		return ResponseEntity.ok().body(service.findAll());
	}

	@ApiOperation(value = "Return your own profile as Client")
	@GetMapping("/client")
	public ResponseEntity<Client> find() {

		Client obj = service.returnClientWithoutParsingTheId();
		return ResponseEntity.ok().body(obj);
	}
	
	@ApiOperation(value = "Create a client")
	@PostMapping("/create/client")
	public ResponseEntity<Client> insert(@RequestBody Client obj) {

		service.insert(obj);

		return ResponseEntity.ok().body(obj);
	}

	@PutMapping("/update/client")
	@ApiOperation(value = "Update a client")
	public ResponseEntity<Client> update(@RequestBody UpdatedClient obj){

		Client cli =  service.update(obj);
		return ResponseEntity.ok().body(cli);
	}

	@PutMapping("/update/client/{id}")
	@ApiOperation(value = "Update a client by id")
	public ResponseEntity<Client> updateById(@RequestBody UpdatedClient obj, @PathVariable Integer id){

		Client cli =  service.updateById(id,obj);
		return ResponseEntity.ok().body(cli);
	}

	@DeleteMapping("/delete/client")
	@ApiOperation(value = "Delete a client")
	public ResponseEntity<Void> delete() {
		service.delete();
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/delete/client/{id}")
	@ApiOperation(value = "Delete a client by id")
	public ResponseEntity<Void> deleteById(@RequestParam Integer id) {
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}


	@ApiOperation(value = "Return a list of clients who buys the most")
	@GetMapping("/clients/ranking")
	public ResponseEntity<List<ClientRankingDTO>> returnRankingClient()
	{
		return ResponseEntity.ok().body(ranking.returnRankingClient());
	}
}
