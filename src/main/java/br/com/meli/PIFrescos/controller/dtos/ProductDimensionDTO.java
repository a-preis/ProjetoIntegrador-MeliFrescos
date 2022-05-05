package br.com.meli.PIFrescos.controller.dtos;

import br.com.meli.PIFrescos.models.ProductDimension;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDimensionDTO {

    private String productName;
    private Float height;
    private Float width;
    private Float length;
    private Float weight;

    public ProductDimensionDTO(ProductDimension productDimension) {
        this.productName = productDimension.getProduct().getProductName();
        this.height = productDimension.getHeight();
        this.width = productDimension.getWidth();
        this.length = productDimension.getLength();
        this.weight = productDimension.getWeight();
    }

    public static List<ProductDimensionDTO> convertList(List<ProductDimension> productDimensionList){
        return productDimensionList.stream().map(ProductDimensionDTO::new).collect(Collectors.toList());
    }
}
