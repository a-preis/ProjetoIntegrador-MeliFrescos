package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.repository.ProductDimensionCustomRepository;
import br.com.meli.PIFrescos.repository.ProductDimensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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
    private ProductDimensionCustomRepository customRepository;

    /**
     * Antes de salvar uma nova dimensão, valida Id do produto e valida se esse produto já tem alguma dimensão cadastrada.
     * @author Ana Preis
     */
    public ProductDimension saveDimension(ProductDimension productDimension) {
        Optional<ProductDimension> dimension = repository.findProductDimensionByProduct(productDimension.getProduct());
        if(dimension.isPresent()){
            throw new RuntimeException(productDimension.getProduct().getProductName() + " dimension already registered");
        }
        return repository.save(productDimension);
    }

    /**
     * Antes de atualizar uma dimensão, valida Id do produto.
     * @author Ana Preis
     */
    @Transactional
    public ProductDimension updateDimension(ProductDimension productDimension) {
        Product product = productService.findProductById(productDimension.getProduct().getProductId());
        ProductDimension newDimension = findByProduct(product);
        newDimension.setHeight(productDimension.getHeight());
        newDimension.setWidth(productDimension.getWidth());
        newDimension.setLength(productDimension.getLength());
        newDimension.setWeight(productDimension.getWeight());
        return repository.save(newDimension);
    }

    public Product deleteDimension(Product product){
        ProductDimension dimension = findByProduct(product);
        repository.delete(dimension);
        return product;
    }

    /**
     * Retorna todas as dimensões cadastradas.
     * @author Ana Preis
     */
    public List<ProductDimension> getAll() {
        List<ProductDimension> dimensionList = repository.findAll();
        if(dimensionList.isEmpty()){
            throw new EntityNotFoundException("ProductDimension list is empty!");
        }
        return dimensionList;
    }

    /**
     * Retorna a dimensão do produto recebido cadastradas.
     * @author Ana Preis
     */
    public ProductDimension findByProduct(Product product) {
        Optional<ProductDimension> dimension = repository.findProductDimensionByProduct(product);
        if(dimension.isEmpty()){
            throw new EntityNotFoundException(product.getProductName() + " dimension not found.");
        }
        return dimension.get();
    }

    /**
     * Retorna a dimensão do produto recebido cadastradas.
     * @author Ana Preis
     */
    public Optional<ProductDimension> findOptionalByProduct(Product product) {
        Optional<ProductDimension> dimension = repository.findProductDimensionByProduct(product);
        return dimension;
    }

    /**
     * Filtra a lista de ProductDimension pelos parâmetros passados na URL.
     * @author Ana Preis
     */
    public List<ProductDimension> filterByParams(Float maxHeight, Float maxWidth, Float maxLength, Float maxWeight, String order){
        return customRepository.find(maxHeight, maxWidth, maxLength, maxWeight, order);
    }

    /**
     * Calcula o volume do produto
     * @author Ana Preis
     */
    public Float calculateVolume(ProductDimension productDimension){
        return (productDimension.getHeight() * productDimension.getLength() * productDimension.getWidth());
    }
}
