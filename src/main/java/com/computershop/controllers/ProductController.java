package com.computershop.controllers;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.computershop.dao.Category;
import com.computershop.dao.Product;
import com.computershop.dao.ProductImage;
import com.computershop.dto.CloudinaryImage;
import com.computershop.dto.ProductWithImage;
import com.computershop.exceptions.InvalidException;
import com.computershop.exceptions.NotFoundException;
import com.computershop.helpers.ConvertObject;
import com.computershop.repositories.CategoryRepository;
import com.computershop.repositories.ProductRepository;

@RestController
@RequestMapping(value = "/api/products")
@Transactional(rollbackFor = Exception.class)
public class ProductController {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;


    @GetMapping
    public ResponseEntity<?> getAllProducts(@RequestParam(name = "page", required = false) Integer pageNum,
                                            @RequestParam(name = "type", required = false) String type,
                                            @RequestParam(name = "search", required = false) String search) {
        if (search != null) {
        	String searchConvert = ConvertObject.fromSlugToString(search);
            List<Product> products = productRepository.findByNameContainingIgnoreCase(searchConvert);

            if (products.size() == 0) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok().body(products);
        }
        if (type != null) {
            if (type.compareTo("without-image") == 0) {
                List<Product> products = productRepository.findAll();

                if (products.size() == 0) {
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.ok().body(products);
            }
        }
        if (pageNum != null) {
            Page<Product> page = productRepository.findAll(PageRequest.of(pageNum.intValue(), 20));
            //
            List<ProductImage> listProdcutImages = new LinkedList<ProductImage>();

            List<Product> listProducts = page.getContent();
            for (int i = 0; i < listProducts.size(); i++) {
                if (listProducts.get(i).getProductImages().isEmpty()) {
                    continue;
                }
                listProdcutImages.add(listProducts.get(i).getProductImages().get(0));
            }
            
            if (page.getNumberOfElements() == 0) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            return ResponseEntity.ok().body(listProdcutImages);
        }

        List<Product> products = productRepository.findAll();
        if (products.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        List<ProductWithImage> listProductImages = new LinkedList<ProductWithImage>();
        for (int i = 0; i < products.size(); i++) {
        	ProductWithImage productWithImage = new ProductWithImage();
        	productWithImage.setProduct(products.get(i));
        	if (products.get(i).getProductImages().isEmpty()) {
            	productWithImage.setCloudinaryImage(null);
            }
            List<CloudinaryImage> cloudinaryImages = new LinkedList<CloudinaryImage>();
            for(int j = 0; j < products.get(i).getProductImages().size();j++ ) {
            	CloudinaryImage cloudImage = new CloudinaryImage();
            	cloudImage.setImageLink(products.get(i).getProductImages().get(j).getImageLink());
            	cloudImage.setProductId(products.get(i).getId());
            	cloudImage.setPublicId(products.get(i).getProductImages().get(j).getPublicId());
            	cloudinaryImages.add(cloudImage);
            }
            productWithImage.setCloudinaryImage(cloudinaryImages);
            listProductImages.add(productWithImage);
        }
        
        return ResponseEntity.ok().body(listProductImages);
    }
    
    // add method getAllByCategories
    @GetMapping("/categories/{category}")
    public ResponseEntity<?> getAllProductsByCaterogy(@PathVariable("category") String category) {
    	Optional<Category> optionalCategory = categoryRepository.findByNameIgnoreCase(ConvertObject.fromSlugToString(category));
    	if(optionalCategory.get() == null) {
    		throw new NotFoundException("Category with name " + category + " doesn't exists");
    	}
        List<Product> products = productRepository.findByCategory(optionalCategory.get());        
        if (products.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        List<ProductWithImage> listProductImages = new LinkedList<ProductWithImage>();
        for (int i = 0; i < products.size(); i++) {
        	ProductWithImage productWithImage = new ProductWithImage();
        	productWithImage.setProduct(products.get(i));
        	if (products.get(i).getProductImages().isEmpty()) {
            	productWithImage.setCloudinaryImage(null);
            }
            List<CloudinaryImage> cloudinaryImages = new LinkedList<CloudinaryImage>();
            for(int j = 0; j < products.get(i).getProductImages().size();j++ ) {
            	CloudinaryImage cloudImage = new CloudinaryImage();
            	cloudImage.setImageLink(products.get(i).getProductImages().get(j).getImageLink());
            	cloudImage.setProductId(products.get(i).getId());
            	cloudImage.setPublicId(products.get(i).getProductImages().get(j).getPublicId());
            	cloudinaryImages.add(cloudImage);
            }
            productWithImage.setCloudinaryImage(cloudinaryImages);
            listProductImages.add(productWithImage);
        }
        
        return ResponseEntity.ok().body(listProductImages);
    }
    
    @GetMapping("/best-selling")
    public ResponseEntity<?> getAllOrderByQuantitySold() {
    	
        List<Product> products = productRepository.findAllByQuantitySoldByDesc();        
        if (products.size() == 0) {
            return ResponseEntity.noContent().build();
        }
        List<ProductWithImage> listProductImages = new LinkedList<ProductWithImage>();
        for (int i = 0; i < products.size(); i++) {
        	ProductWithImage productWithImage = new ProductWithImage();
        	productWithImage.setProduct(products.get(i));
        	if (products.get(i).getProductImages().isEmpty()) {
            	productWithImage.setCloudinaryImage(null);
            }
            List<CloudinaryImage> cloudinaryImages = new LinkedList<CloudinaryImage>();
            for(int j = 0; j < products.get(i).getProductImages().size();j++ ) {
            	CloudinaryImage cloudImage = new CloudinaryImage();
            	cloudImage.setImageLink(products.get(i).getProductImages().get(j).getImageLink());
            	cloudImage.setProductId(products.get(i).getId());
            	cloudImage.setPublicId(products.get(i).getProductImages().get(j).getPublicId());
            	cloudinaryImages.add(cloudImage);
            }
            productWithImage.setCloudinaryImage(cloudinaryImages);
            listProductImages.add(productWithImage);
        }
        
        return ResponseEntity.ok().body(listProductImages);
    }
    
    
    @DeleteMapping("/{id}")
    @PreAuthorize("@authorizeService.authorizeAdmin(authentication, 'ADMIN')")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new NotFoundException("Product not found");
        }

        if (!optionalProduct.get().getProductImages().isEmpty()) {
            throw new InvalidException("Delete failed");
        }
        
        productRepository.deleteById(optionalProduct.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(optionalProduct.get());
    }
}