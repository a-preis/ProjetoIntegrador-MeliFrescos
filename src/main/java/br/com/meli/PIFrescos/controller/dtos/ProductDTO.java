package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.service.ProductDimensionService;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Classe ProductDTO para filtrar campos devolvidos na request
 * @author Juliano Alcione de Souza
 *
 * Refactor:
 * @author Julio César Gama, Ana Preis
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    private Integer productId;
    private String productName;
    private StorageType productType;
    private String productDescription;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ProductDimensionDTO dimension;

    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productType = product.getProductType();
        this.productDescription = product.getProductDescription();
    }

    public ProductDTO(Product product, ProductDimensionService service) {
        this.productId = product.getProductId();
        this.productName = product.getProductName();
        this.productType = product.getProductType();
        this.productDescription = product.getProductDescription();
        Optional<ProductDimension> dimension = service.findOptionalByProduct(product);
        dimension.ifPresent(productDimension -> this.dimension = new ProductDimensionDTO(productDimension));
    }

    public static List<ProductDTO> convertList(List<Product> products, ProductDimensionService service){
        return products.stream()
                .map(product -> new ProductDTO(product, service))
                .collect(Collectors.toList());
    }
}
