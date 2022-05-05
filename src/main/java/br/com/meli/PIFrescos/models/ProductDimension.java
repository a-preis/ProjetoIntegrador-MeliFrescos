package br.com.meli.PIFrescos.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * @author Ana Preis
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProductDimension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;

    @NotNull(message = "Height field can`t be empty")
    @DecimalMax(value = "300", message = "Height can't be greater than 300 cm.")
    @DecimalMin(value = "0", message = "Height can't be smaller than 0 cm.")
    private Float height;

    @NotNull(message = "Width field can`t be empty")
    @DecimalMax(value = "300", message = "Width can't be greater than 300 cm.")
    @DecimalMin(value = "0", message = "Width can't be smaller than 0 cm.")
    private Float width;

    @NotNull(message = "Length field can`t be empty")
    @DecimalMax(value = "300", message = "Length can't be greater than 300 cm.")
    @DecimalMin(value = "0", message = "Length can't be smaller than 0 cm.")
    private Float length;

    @NotNull(message = "Weight field can`t be empty")
    @DecimalMax(value = "200000", message = "Weight can't be greater than 200.000 g.")
    @DecimalMin(value = "0", message = "Weight can't be smaller than 0 g.")
    private Float weight;

    public ProductDimension(Product product, Float height, Float width, Float length, Float weight) {
        this.product = product;
        this.height = height;
        this.width = width;
        this.length = length;
        this.weight = weight;
    }
}