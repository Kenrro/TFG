package com.productservice.productsservice.service.product;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.productservice.productsservice.dto.products.DeleteProductResponsetDto;
import com.productservice.productsservice.dto.products.ProductCreateRequestDto;
import com.productservice.productsservice.entity.Product;
import com.productservice.productsservice.enums.ProductError;
import com.productservice.productsservice.exception.ProductGeneralException;
import com.productservice.productsservice.repository.ProductRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;  

@Service
@RequiredArgsConstructor
public class ProductCRUDService {

    private final ProductRepository productRepository;

    @Transactional
    public void create(ProductCreateRequestDto request) {
        Product product = Product.builder()
        .stablishmentCode(request.getStablishmentCode())
        .name(request.getName())
        .price(request.getPrice())
        .description(request.getDescription())
        .build();

        try {
            productRepository.save(product);
        } catch (DataIntegrityViolationException e) {
            throw new ProductGeneralException(ProductError.PRODUCT_ALREADY_EXISTS);
        } catch (ConstraintViolationException e) {
            throw new ProductGeneralException(ProductError.INVALID_PRODUCT_DATA);
        } catch (DataAccessException e) {
            throw new ProductGeneralException(ProductError.PRODUCT_CREATION_FAILED);
        }

    }
    @Transactional
    public void createAll(List<DeleteProductResponsetDto> request) {
        List<Product> products = request.stream().map(product->
            Product.builder()
            .id(product.getId())
            .stablishmentCode(product.getStablishmentCode())
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .build()
        ).toList();
        
        

        try {
            productRepository.saveAll(products);
        } catch (DataIntegrityViolationException e) {
            throw new ProductGeneralException(ProductError.PRODUCT_ALREADY_EXISTS);
        } catch (ConstraintViolationException e) {
            throw new ProductGeneralException(ProductError.INVALID_PRODUCT_DATA);
        } catch (DataAccessException e) {
            throw new ProductGeneralException(ProductError.PRODUCT_CREATION_FAILED);
        }

    }

    public Product findById(Long Id) {
        return productRepository.findById(Id)
        .orElseThrow(()-> new ProductGeneralException(ProductError.PRODUCT_NOT_FOUND));

    }
    public List<Product> findAllByStablishmentCode(String stablishmentCode) {
        return productRepository.findAllByStablishmentCode(stablishmentCode);
    }
    @Transactional
    public void updateProduct(Long id, ProductCreateRequestDto request) {
        // 1. Obtener el producto existente
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductGeneralException(ProductError.PRODUCT_NOT_FOUND));

        // 2. Obtener todas las propiedades del DTO
        Field[] fields = ProductCreateRequestDto.class.getDeclaredFields();

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object newValue = field.get(request);

                if (newValue != null) {
                    // Obtener el campo correspondiente en Product
                    Field productField = Product.class.getDeclaredField(field.getName());
                    productField.setAccessible(true);

                    Object currentValue = productField.get(product);

                    // Solo actualizar si el valor es diferente
                    if (!newValue.equals(currentValue)) {
                        productField.set(product, newValue);
                    }
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Ignorar campos que no existan o no puedan ser accedidos
                continue;
            }
        }

        // 3. Guardar cambios
        productRepository.save(product);
    }
    @Transactional
    public void deleteById(Long id, String code) {
        // Primero verificamos si existe el producto
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductGeneralException(ProductError.PRODUCT_NOT_FOUND));
        
        if (!code.equals(product.getStablishmentCode())) {
            throw new ProductGeneralException(ProductError.UNAUTHORIZED_PRODUCT_ACCESS);
        }

        // Borramos
        productRepository.delete(product);
    }
    public List<Product> deleteProductsByStablishment(String stablishmentCode) {
        List<Product> deletedProducts = productRepository.findAllByStablishmentCode(stablishmentCode); 
        int deletedCount = productRepository.deleteAllByStablishmentCode(stablishmentCode);
        return deletedProducts; 
    }
    public List<Product> findAllByIds(List<Long> ids) {
        return productRepository.findAllById(ids);
    }

}
