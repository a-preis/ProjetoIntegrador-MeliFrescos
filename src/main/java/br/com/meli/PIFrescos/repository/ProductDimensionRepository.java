package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Ana Preis
 */
@Repository
public interface ProductDimensionRepository  extends JpaRepository<ProductDimension, Integer> {

    Optional<ProductDimension> findProductDimensionByProduct(Product product);


}
