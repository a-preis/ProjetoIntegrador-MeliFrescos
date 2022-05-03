package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.repository.ProductDimensionCustomRepository;
import br.com.meli.PIFrescos.repository.ProductDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Autowired
    ProductDimensionCustomRepository customRepository;

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

    /**
     * Retorna todas as dimensões cadastradas.
     * @author Ana Preis
     */
    public List<ProductDimension> getAll() {
        return repository.findAll();
    }

    /**
     * Retorna a dimensão do produto recebido cadastradas.
     * @author Ana Preis
     */
    public ProductDimension findByProduct(Product product) {
        Optional<ProductDimension> dimension = repository.findProductDimensionByProduct(product);
        if(dimension.isEmpty()){
            throw new RuntimeException(product.getProductName() + " dimension not found.");
        }
        return dimension.get();
    }

    /**
     * Filtra a lista de ProductDimension pelos parâmetros passados na URL.
     * @author Ana Preis
     */
    public List<ProductDimension> filterByParams(Float maxHeight, Float maxWidth, Float maxLength, Float maxWeight, String order){
        return customRepository.find(maxHeight, maxWidth, maxLength, maxWeight, order);
    }
}
