package com.foodtaste.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodtaste.model.MenuItem;
import com.foodtaste.service.MenuItemService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/items")
@Slf4j
@CrossOrigin(origins = "*")
public class MenuItemController {

	@Autowired
	private MenuItemService menuItemService;

	@PostMapping
	public ResponseEntity<MenuItem> addMenuItem(@Valid @RequestBody MenuItem menuItem) {
		log.info("Request to add menu item : {}", menuItem);

		MenuItem item = menuItemService.createItem(menuItem);
		return new ResponseEntity<>(item, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MenuItem> getItemById(@PathVariable Integer id) {
		log.info("Request to get item : {}", id);

		MenuItem menuItem = menuItemService.getItemById(id);
		return ResponseEntity.ok(menuItem);
	}

	@GetMapping("/search")
	public ResponseEntity<MenuItem> findMenuItemByName(@RequestParam(required = true) String name) {
		log.info("Request to get item by name : {}", name);
		MenuItem menuItem = menuItemService.findByName(name);
		return ResponseEntity.ok(menuItem);
	}

	@PutMapping("/{id}")
	public ResponseEntity<MenuItem> updateMenuItem(@PathVariable Integer id, @Valid @RequestBody MenuItem menuItem) {
		log.info("Request to update menu item : {}", menuItem);
		MenuItem item = menuItemService.updateItem(id, menuItem);
		return new ResponseEntity<>(item, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<List<MenuItem>> getAllItems() {
		log.info("Request to Get All menu items");
		List<MenuItem> items = menuItemService.getAllItem();
		return ResponseEntity.ok(items);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteItem(@PathVariable Integer id) {
		log.info("Request to delete item : {}", id);
		String deletedItem = menuItemService.deleteItem(id);
		return new ResponseEntity<>(deletedItem, HttpStatus.OK);
	}
}
