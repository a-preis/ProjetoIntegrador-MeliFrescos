package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.repository.ProductDimensionRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Marcelo Leite/Juliano Alcione de Souza
 * Refactor: Ana Preis
 *
 */
@Service
public class ProductService {
  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private ProductDimensionRepository productDimensionRepository;

  public List<Product> listAllProducts() {
    List<Product> productList = productRepository.findAll();
    if(productList.isEmpty()) {
      throw new EntityNotFoundException("Product list is empty");
    }
    return productList;
  }

  // metodo para buscar produto pelo id no banco de dados
  public Product findProductById(Integer id) {
    Optional<Product> product = productRepository.findById(id);
    if(product.isEmpty())
      throw new EntityNotFoundException("product not found");

    return product.get();
  }

  public List<Product> listByType(String type){

    List<Product> products = productRepository.findAll().stream()
            .filter(product -> Objects.equals(product.getProductType().getStorageType(),type.toUpperCase()))
            .collect(Collectors.toList());

    if(products.isEmpty()){
      throw new EntityNotFoundException("No products found");
    }
      return products;
  }

  // Cria um novo produto, mas antes verifica se já existe um produto com o mesmo nome
  public Product createProduct(Product product) {
    if (productRepository.findByProductName(product.getProductName()) != null) {
      throw new RuntimeException("Product already exists");
    }
    return productRepository.save(product);
  }

  // verifica se o produto existe no banco de dados e faz a atualização
  public Product updateProduct(Product product) {
    if (!productRepository.findById(product.getProductId()).isPresent()) {
      throw new EntityNotFoundException("Product not found");
    }
    return productRepository.save(product);
  }

  @Transactional
  public void deleteProduct(Integer id){
    Optional<Product> productOptional = productRepository.findById(id);
    if(productOptional.isEmpty()){
      throw new EntityNotFoundException("Product not found");
    }
    productDimensionRepository.deleteProductDimensionByProduct(productOptional.get());
    productRepository.delete(productOptional.get());
  }
}
