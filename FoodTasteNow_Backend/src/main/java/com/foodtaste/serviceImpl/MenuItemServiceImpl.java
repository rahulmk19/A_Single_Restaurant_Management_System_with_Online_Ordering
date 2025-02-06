package com.foodtaste.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodtaste.exception.MenuItemException;
import com.foodtaste.model.MenuItem;
import com.foodtaste.repository.MenuItemRepository;
import com.foodtaste.service.MenuItemService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

	@Autowired
	private MenuItemRepository menuItemRepository;

	@Override
	public MenuItem createItem(MenuItem menuItem) {
		log.info("Creating MenuItem with details: {}", menuItem);

		Optional<MenuItem> isExistingMenuItem = menuItemRepository.findByNameIgnoreCase(menuItem.getName());
		if (isExistingMenuItem.isPresent()) {
			log.warn("MenuItem creation failed. Duplicate name: {}", menuItem.getName());
			throw new MenuItemException("A MenuItem already exists with the name: " + menuItem.getName());
		}

		MenuItem savedItem = menuItemRepository.save(menuItem);
		log.info("MenuItem created successfully: {}", savedItem);
		return savedItem;
	}

	@Override
	public MenuItem getItemById(Integer id) {
		log.info("Fetching MenuItem by ID: {}", id);

		return menuItemRepository.findById(id)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with ID: " + id));
	}

	@Override
	public MenuItem findByName(String name) {
		
		if (name == null || name.isBlank()) {
	        throw new MenuItemException("The 'name' parameter must not be empty or null.");
	    }
		log.info("Fetching MenuItem by name (case-insensitive): {}", name);

		return menuItemRepository.findByNameIgnoreCase(name)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with name: " + name));
	}

	@Override
	public MenuItem updateItem(Integer id, MenuItem menuItem) {
		log.info("Updating MenuItem with ID: {}", id);

		MenuItem existingMenuItem = menuItemRepository.findById(id)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with ID: " + id));

		Optional<MenuItem> duplicateMenuItem = menuItemRepository.findByNameIgnoreCase(menuItem.getName());
		if (duplicateMenuItem.isPresent() && !duplicateMenuItem.get().getId().equals(id)) {
			throw new MenuItemException("A MenuItem already exists with the name: " + menuItem.getName());
		}

		existingMenuItem.setName(menuItem.getName());
		existingMenuItem.setPrice(menuItem.getPrice());
		existingMenuItem.setQuantity(menuItem.getQuantity());
		existingMenuItem.setCategory(menuItem.getCategory());
		return menuItemRepository.save(existingMenuItem);
	}

	@Override
	public String deleteItem(Integer id) {
		log.info("Deleting MenuItem with ID: {}", id);

		MenuItem existingMenuItem = menuItemRepository.findById(id)
				.orElseThrow(() -> new MenuItemException("MenuItem not found with ID: " + id));

		menuItemRepository.delete(existingMenuItem);
		return "MenuItem deleted successfully with ID: " + id;
	}

	@Override
	public List<MenuItem> getAllItem() {
		log.info("Fetching all MenuItems");

		List<MenuItem> allItem = menuItemRepository.findAll();
		return allItem;
	}

}
