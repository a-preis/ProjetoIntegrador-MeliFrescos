package br.com.meli.PIFrescos.controller.forms;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import lombok.*;

import javax.validation.constraints.*;

/**
 * @author Ana Preis
 * */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDimensionForm {

    @NotNull(message = "ProductId field can't be empty")
    private Integer productId;

    @NotNull(message = "Height field can`t be empty")
    @DecimalMax(value = "300", message = "Height can't be greater than 300 cm.")
    @DecimalMin(value = "0", message = "Height can't be smaller than 0 cm.")
    private Float height;

    @NotNull(message = "Width field can`t be empty")
    @DecimalMax(value = "300", message = "Width can't be greater than 300 cm.")
    @DecimalMin(value = "0", message = "Width can't be smaller than 0 cm.")
    private Float width;

    @NotNull(message = "Weight field can`t be empty")
    @DecimalMax(value = "2000", message = "Weight can't be greater than 2000 g.")
    @DecimalMin(value = "0", message = "Weight can't be smaller than 0 g.")
    private Float weight;

    public ProductDimension convert(Product product, Float height, Float width, Float weight) {
        return new ProductDimension(product, height, width, weight);
    }
}