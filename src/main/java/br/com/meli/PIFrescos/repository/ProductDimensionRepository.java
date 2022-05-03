package br.com.meli.PIFrescos.repository;

import br.com.meli.PIFrescos.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ana Preis
 */
@Repository
public interface ProductDimensionRepository  extends JpaRepository<Product, Integer> {
}
