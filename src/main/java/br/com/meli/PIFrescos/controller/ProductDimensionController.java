package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.ProductDTO;
import br.com.meli.PIFrescos.controller.dtos.ProductDimensionDTO;
import br.com.meli.PIFrescos.controller.forms.ProductDimensionForm;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.service.ProductDimensionService;
import br.com.meli.PIFrescos.service.ProductService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

/**
 * Api de CRUD da dimensão dos produtos
 * @author Ana Rute Preis e Silva
 */
@RestController
@RequestMapping("/fresh-products/dimension")
public class ProductDimensionController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDimensionService productDimensionService;

    @PostMapping("")
    public ResponseEntity<ProductDimensionDTO> create(@RequestBody @Valid ProductDimensionForm form,
                                                      UriComponentsBuilder uriBuilder){
        Product product = productService.findProductById(form.getProductId());
        ProductDimension dimension = this.productDimensionService.saveDimension(form.convert(product,
                form.getHeight(), form.getWidth(), form.getWeight()));
        URI uri = uriBuilder
                .path("")
                .buildAndExpand(product.getProductId())
                .toUri();
        return ResponseEntity.created(uri).body(new ProductDimensionDTO(dimension));
    }

    @PutMapping("")
    public ResponseEntity<ProductDimensionDTO> update(@RequestBody @Valid ProductDimensionForm form){
        Product product = productService.findProductById(form.getProductId());
        ProductDimension dimension = this.productDimensionService.updateDimension(form.convert(product,
                form.getHeight(), form.getWidth(), form.getWeight()));
        return ResponseEntity.ok(new ProductDimensionDTO(dimension));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDimensionDTO>> getAll(){
        List<ProductDimensionDTO> list = ProductDimensionDTO.convertList(productDimensionService.getAll());
        return ResponseEntity.ok(list);
    }
}
