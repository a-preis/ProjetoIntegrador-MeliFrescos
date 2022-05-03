package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.ProductDTO;
import br.com.meli.PIFrescos.controller.forms.ProductDimensionForm;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Api de CRUD da dimensão dos produtos
 * @author Ana Rute Preis e Silva
 */
@RestController
@RequestMapping("/fresh-products/dimension")
public class ProductDimensionController {

    @Autowired
    private ProductService productService;

//    @Autowired
//    private ProductDimensionService productDimensionService;

//    @PostMapping
//    public ResponseEntity<ProductDTO> create(@RequestBody @Valid ProductDimensionForm form){
//        Product product = productService.findProductById(form.getProductId());
//        ProductDimension dimension = this.productDimensionService.saveDimensions(form.convert(product,
//                form.getHeight(), form.getWidth(), form.getWeight()));
//        return ResponseEntity.created(new ProductDTO(dimension));
//    }
}
