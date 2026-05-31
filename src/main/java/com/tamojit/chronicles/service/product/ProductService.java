package com.tamojit.chronicles.service.product;

import com.tamojit.chronicles.dto.ImageDto;
import com.tamojit.chronicles.dto.ProductDto;
import com.tamojit.chronicles.exceptions.ResourceNotFoundException;
import com.tamojit.chronicles.model.Category;
import com.tamojit.chronicles.model.Image;
import com.tamojit.chronicles.model.Product;
import com.tamojit.chronicles.repository.CategoryRepository;
import com.tamojit.chronicles.repository.ImageRepository;
import com.tamojit.chronicles.repository.ProductRepository;
import com.tamojit.chronicles.request.AddProductRequest;
import com.tamojit.chronicles.request.UpdateProductRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;

    @Override
    public Product addProduct(AddProductRequest request) {
        /* Checking if the category is found -->
         * If Yes, set new Prod. under Category.
         * If No, save as new category & set prod in DB.
         */

        Category category = Optional.ofNullable(categoryRepository.findByName(request.getCategory()))
            .orElseGet(() -> {
                Category newCategory = new Category(request.getCategory());
                return categoryRepository.save(newCategory);
            });

        return productRepository.save(createProduct(request, category));
    }

    private Product createProduct(AddProductRequest request, Category category) {
        return new Product(
            request.getName(),
            request.getBrand(),
            request.getPrice(),
            request.getInventory(),
            request.getDescription(),
            category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product Not Found!"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
            .ifPresentOrElse(productRepository::delete,
                () -> {
                    throw new ResourceNotFoundException("Product Not Found!");
                });
    }

    @Override
    public Product updateProduct(UpdateProductRequest request, Long productId) {
        return productRepository.findById(productId)
            .map(existingProduct -> updateExistingProduct(existingProduct, request)) // if found --> update
            .map(productRepository::save) // save after update
            .orElseThrow(() -> new ResourceNotFoundException("Product Not Found!")); // error on not found
    }

    private Product updateExistingProduct(Product existingProduct, UpdateProductRequest request) {
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setPrice(request.getPrice());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setDescription(request.getDescription());

        Category category = categoryRepository.findByName(request.getCategory());
        existingProduct.setCategory(category);

        return existingProduct;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductsByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductsByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductsByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand, name);
    }

    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }

    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products) {
        return products.stream()
            .map(this::convertToDto)
            .toList();
    }

    @Override
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imagesDto = images.stream()
            .map(image -> modelMapper.map(image, ImageDto.class))
            .toList();

        productDto.setImages(imagesDto);
        return productDto;
    }
}
