package com.foodtaste.service;

import java.util.List;

import com.foodtaste.model.MenuItem;

public interface MenuItemService {

	MenuItem createItem(MenuItem menuItem);

	MenuItem updateItem(Integer id, MenuItem menuItem);

	MenuItem getItemById(Integer id);

	MenuItem findByName(String name);

	String deleteItem(Integer id);

	List<MenuItem> getAllItem();

}
