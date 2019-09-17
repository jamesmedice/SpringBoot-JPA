package com.medici.app.spring.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.medici.app.spring.model.Actor;
import com.medici.app.spring.sevice.ActorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 
 * @author a73s
 *
 */
@RestController
@RequestMapping("/actor")
@Api(value = "ActorResources", consumes = "application/json", produces = "application/json", protocols = "http", description = "Operations Related to Actor")
public class ActorController {

	@Autowired
	private ActorService actorService;

	@GetMapping("/{id}")
	@ApiOperation(consumes = "application/json", produces = "application/json", protocols = "http", value = "existsById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully found Actor by Id"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"), @ApiResponse(code = 404, message = "The resource  not found") })
	public ResponseEntity existsById(@ApiParam("Id of user, Can not be null") @PathVariable int id) throws Exception {
		boolean flag = actorService.existsById(id);
		return new ResponseEntity(flag, HttpStatus.OK);
	}

	@GetMapping("/{id}/id")
	@ApiOperation(consumes = "application/json", produces = "application/json", protocols = "http", value = "findById")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved Actor"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"), @ApiResponse(code = 404, message = "The resource  not found") })
	public ResponseEntity findById(@ApiParam("Id of user, Can not be null") @PathVariable int id) throws Exception {
		Optional<Actor> actor = actorService.findById(id);
		return new ResponseEntity(actor, HttpStatus.OK);
	}

	@PostMapping("/save")
	@ApiOperation(consumes = "application/json", produces = "application/json", protocols = "http", value = "save")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully Saved Actor"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"), @ApiResponse(code = 404, message = "The resource  not found") })
	public ResponseEntity save(@RequestBody Actor entity) {
		Actor actor = actorService.save(entity);
		return new ResponseEntity(actor, HttpStatus.OK);
	}

	@GetMapping("/all")
	@ApiOperation(consumes = "application/json", produces = "application/json", protocols = "http", value = "findAll")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved All Actors"),
			@ApiResponse(code = 401, message = "The request has not been applied because it lacks valid authentication credentials for the target resource"),
			@ApiResponse(code = 403, message = "The server understood the request but refuses to authorize it"), @ApiResponse(code = 404, message = "The resource  not found") })
	public ResponseEntity findAll() throws Exception {
		Iterable<Actor> actor = actorService.findAll();
		return new ResponseEntity(actor, HttpStatus.OK);
	}

}
