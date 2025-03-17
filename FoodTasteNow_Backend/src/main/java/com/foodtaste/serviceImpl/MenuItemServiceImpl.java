package com.foodtaste.serviceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodtaste.enums.CategoryEnum;
import com.foodtaste.exception.MenuItemException;
import com.foodtaste.model.MenuItem;
import com.foodtaste.repository.MenuItemRepo;
import com.foodtaste.service.MenuItemService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MenuItemServiceImpl implements MenuItemService {

	@Autowired
	private MenuItemRepo menuItemRepository;

	@PostConstruct
	public void insertInitialData() {
		if (menuItemRepository.count() == 0) {
			List<MenuItem> menuItems = List.of(
					new MenuItem(null, "Gulab Jamun", new BigDecimal("30.00"), 17, CategoryEnum.DESSERT,
							"https://i.pinimg.com/originals/a4/70/fe/a470fefcda1f459f3d9d5c3b5aa1a502.jpg"),
					new MenuItem(null, "Butter Chicken", new BigDecimal("200.00"), 18, CategoryEnum.NORTH_INDIAN,
							"https://therecipecritic.com/wp-content/uploads/2017/12/PataksButterChicken11.jpg"),
					new MenuItem(null, "Masala Dosa", new BigDecimal("50.00"), 18, CategoryEnum.SOUTH_INDIAN,
							"https://apollosugar.com/wp-content/uploads/2018/12/Masala-Dosa.jpg"),
					new MenuItem(null, "Chocolate Cake", new BigDecimal("450.00"), 17, CategoryEnum.CAKES,
							"https://cdn.igp.com/f_auto,q_auto,t_pnopt9prodlp/products/p-chocolate-almond-cake-half-kg--67974-m.jpg"),
					new MenuItem(null, "Cheese Pizza", new BigDecimal("299.00"), 20, CategoryEnum.FAST_FOOD,
							"https://assets.teenvogue.com/photos/5ab665d06d36ed4396878433/master/pass/GettyImages-519526540.jpg"),
					new MenuItem(null, "Pani Puri", new BigDecimal("40.00"), 19, CategoryEnum.STREET_FOOD,
							"https://external-preview.redd.it/3EAHTofTOsKD2TfbVoVSFq5AFZOr-PsPmgrYx7R_-P8.jpg?auto=webp&s=206fc5eb1198baadafca62392022c0b100a39bf5"),
					new MenuItem(null, "Greek Salad", new BigDecimal("100.00"), 18, CategoryEnum.HEALTHY_DIET_FOOD,
							"https://www.olivetomato.com/wp-content/uploads/2019/06/Best-Greek-Salad-Recipe-1.jpg"),
					new MenuItem(null, "Cold Coffee", new BigDecimal("100.00"), 20, CategoryEnum.BEVERAGES,
							"https://mytastycurry.com/wp-content/uploads/2020/04/Cafe-style-cold-coffee-with-icecream.jpg"),
					new MenuItem(null, "Chola Bhatura", new BigDecimal("70.00"), 19, CategoryEnum.NORTH_INDIAN,
							"https://static.vecteezy.com/system/resources/previews/015/933/458/large_2x/chole-bhature-is-a-north-indian-food-dish-a-combination-of-chana-masala-and-bhatura-or-puri-free-photo.jpg"),
					new MenuItem(null, "Tomato Pizza", new BigDecimal("399.00"), 10, CategoryEnum.FAST_FOOD,
							"https://cdn.pixabay.com/photo/2024/04/03/20/39/ai-generated-8673813_1280.png"));
			menuItemRepository.saveAll(menuItems);
		}
	}

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
