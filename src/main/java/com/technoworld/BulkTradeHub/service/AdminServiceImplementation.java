package com.technoworld.BulkTradeHub.service;

import java.lang.StackWalker.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.technoworld.BulkTradeHub.entity.Brand;
import com.technoworld.BulkTradeHub.entity.Category;
import com.technoworld.BulkTradeHub.entity.Product;
import com.technoworld.BulkTradeHub.entity.ProductPost;
import com.technoworld.BulkTradeHub.entity.Profile;
import com.technoworld.BulkTradeHub.entity.RetailShopProfile;
import com.technoworld.BulkTradeHub.entity.User;
import com.technoworld.BulkTradeHub.repository.BrandRepository;
import com.technoworld.BulkTradeHub.repository.CategoryRepository;
import com.technoworld.BulkTradeHub.repository.ProductPostRepository;
import com.technoworld.BulkTradeHub.repository.ProductRepository;
import com.technoworld.BulkTradeHub.repository.ProfileRepository;
import com.technoworld.BulkTradeHub.repository.RetailShopProfileRepository;
import com.technoworld.BulkTradeHub.repository.UserRepository;

@Service
public class AdminServiceImplementation {
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductPostRepository productPostRepository;
	
	@Autowired
	private RetailShopProfileRepository retailShopProfileRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private BrandRepository brandRepository;

	public List<User> getAllUser() {
		List<User> lst = userRepository.findAll();
		return lst;
	}

	public List<RetailShopProfile> getAllRetailShopProfileData() {
		List<RetailShopProfile> lst = retailShopProfileRepository.findAll();
		return lst;
	}

	public List<Product> getAllRetailShopProduct() {
		List<Product> lst = productRepository.findAll();
		return lst;
	}

	public List<Category> getAllCategory() {
		List<Category> lst = categoryRepository.findAll();
		return lst;
	}

	public List<Brand> getAllBrand(){
		List<Brand> lst =brandRepository.findAll();		
		return lst;
	}
	// Approve Button Logic for Category

	public void updateCategoryById(int id) {
		Optional<Category> updateData = categoryRepository.findById(id);
		if (updateData.isPresent()) {
			Category c = updateData.get();
			LocalDateTime now = LocalDateTime.now();
			if ("Pending Approval".equals(c.getStatus())) {
				c.setStatus("Active");
				c.setUpdatedAt(now);
				categoryRepository.save(c);
			} else if ("Edit Requested".equals(c.getStatus())) {
				c.setStatus("Edit Approved");
				c.setUpdatedAt(now);
				categoryRepository.save(c);
			} else if ("Deletion Requested".equals(c.getStatus())) {
				c.setStatus("Deleted");
				c.setUpdatedAt(now);
				categoryRepository.save(c);
			} else {
				categoryRepository.save(c);
			}

		}

	}
	// Reject Button Logic for Category

	public void rejectCategoryRequest(int id) {
		Optional<Category> reject = categoryRepository.findById(id);
		if (reject.isPresent()) {
			Category c = reject.get();
			c.setStatus("Rejected");
			categoryRepository.save(c);

		}
	}

	// Approve Button Logic for Brand

	public void updateBrandById(int id) {
		Optional<Brand> brand = brandRepository.findById(id);

		if (brand.isPresent()) {
			Brand b = brand.get();

			LocalDateTime now = LocalDateTime.now();
			if ("Pending Approval".equals(b.getStatus())) {
				b.setStatus("Active");
				b.setUpdatedAt(now);
				brandRepository.save(b);
			} else if ("Edit Requested".equals(b.getStatus())) {
				b.setStatus("Edit Approved");
				b.setUpdatedAt(now);
				brandRepository.save(b);
			} else if ("Deletion Requested".equals(b.getStatus())) {
				b.setStatus("Deleted");
				b.setUpdatedAt(now);
				brandRepository.save(b);
			} else {
				brandRepository.save(b);
			}

		}

	}

	// Reject Button Logic for Category

	public void rejectBarndRequest(int id) {
		Optional<Brand> brand = brandRepository.findById(id);

		if (brand.isPresent()) {
			Brand b = brand.get();
			b.setStatus("Rejected");
			brandRepository.save(b);

		}
	}
	
	public List<User> findAllRetailer() {
		List<User> user = userRepository.findAllRetailUsers();
		return user;
	}
	
	public List<User> findAllSalemans() {
		List<User> user = userRepository.findAllSalesamneUsers();
		return user;
	}

	
	public List<Category> getCategoryByUserId(int id) {
		List<Category> category = categoryRepository.getUserById(id);
		return category;
	}
	
	public void deleteCategoryByUserId(int id) {
		categoryRepository.deleteById(id);
	}
	
	
	public List<Brand> getBrandByUserId(int id) {
		List<Brand> category = brandRepository.getUserById(id);
		return category;
	}
	
	public void deleteBrandByUserId(int id) {
		brandRepository.deleteById(id);
	}
	
	
	public List<Product> getProductByUserId(int id){
		List<Product> lst = productRepository.getProductByUserId(id);
		return lst;
	}
	
	public void deleteProductByUserId(long id) {
		productRepository.deleteById(id);
	}
	
	public List<Product> getAllProduct(){
		return productRepository.findAll();
		
	}
	public void deleteProduct(long id) {
		productRepository.deleteById(id);
	}
	
	public List<ProductPost> postedProducts(){
		return productPostRepository.findAll();
		
	}
}
