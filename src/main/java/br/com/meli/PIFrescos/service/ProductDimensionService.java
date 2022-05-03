package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.repository.ProductDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Ana Preis
 */
@Service
public class ProductDimensionService {

    @Autowired
    private ProductDimensionRepository repository;

    @Autowired
    private ProductService productService;

    /**
     * Antes de salvar uma nova dimensão, valida Id do produto e valida se esse produto já tem alguma dimensão cadastrada.
     * @author Ana Preis
     */
    public ProductDimension saveDimension(ProductDimension productDimension) {
        //
        Product product = productService.findProductById(productDimension.getProduct().getProductId());
        //
        Optional<ProductDimension> dimension = repository.findProductDimensionByProduct(product);
        if(dimension.isPresent()){
            throw new RuntimeException(product.getProductName() + " dimension already registered");
        }
        return repository.save(productDimension);
    }

    /**
     * Antes de atualizar uma dimensão, valida Id do produto.
     * @author Ana Preis
     */
    public ProductDimension updateDimension(ProductDimension productDimension) {
        Product product = productService.findProductById(productDimension.getProduct().getProductId());
        return repository.save(productDimension);
    }

}
