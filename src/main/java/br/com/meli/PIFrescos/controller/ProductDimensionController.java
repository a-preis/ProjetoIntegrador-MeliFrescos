package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.ProductDTO;
import br.com.meli.PIFrescos.controller.dtos.ProductDimensionDTO;
import br.com.meli.PIFrescos.controller.forms.ProductDimensionForm;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.service.ProductDimensionService;
import br.com.meli.PIFrescos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
                form.getHeight(), form.getWidth(), form.getLength(), form.getWeight()));
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
                form.getHeight(), form.getWidth(), form.getLength(), form.getWeight()));
        return ResponseEntity.ok(new ProductDimensionDTO(dimension));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteDimension(@PathVariable Integer id){
        Product product = productService.findProductById(id);
        Product productWithoutDimension = productDimensionService.deleteDimension(product);
        return ResponseEntity.ok(new ProductDTO(productWithoutDimension));
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDimensionDTO>> getAll(){
        List<ProductDimensionDTO> list = ProductDimensionDTO.convertList(productDimensionService.getAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDimensionDTO> getProductDimension(@PathVariable Integer id){
        Product product = productService.findProductById(id);
        ProductDimension dimension = productDimensionService.findByProduct(product);
        return ResponseEntity.ok(new ProductDimensionDTO(dimension));
    }

    @GetMapping("vol/{id}")
    public ResponseEntity<String> getProductVolume(@PathVariable Integer id){
        Product product = productService.findProductById(id);
        ProductDimension productDimension = productDimensionService.findByProduct(product);
        Float volume = productDimensionService.calculateVolume(productDimension);
        return ResponseEntity.ok("Volume da " + product.getProductName() + ": " + volume + " cm³.");
    }

    /**
     * Validações de formato dos parâmetros é realizada antes de chamar o método de service.
     */
    @GetMapping("/listBy")
    public ResponseEntity<List<ProductDimensionDTO>> filterBy(@RequestParam(required = false) Float maxHeight,
                                                               @RequestParam(required = false) Float maxWidth,
                                                               @RequestParam(required = false) Float maxLength,
                                                               @RequestParam(required = false) Float maxWeight,
                                                               @RequestParam(required = false) String order){

        // Se todos os parametros forem vazios, listar tudo.
        if(maxHeight == null && maxWidth == null && maxLength == null && maxWeight == null && order == null){
            List<ProductDimension> dimension = productDimensionService.getAll();
            return ResponseEntity.ok(ProductDimensionDTO.convertList(dimension));
        }

        //Se não for vazio, verificar se consigo converter cada um para Float
        if(maxHeight != null){
            try{
                Float.valueOf(maxHeight);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("maxHeight query parameter format not valid");
            }
        }

        if(maxWidth != null){
            try{
                Float.valueOf(maxWidth);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("maxWidth query parameter format not valid");
            }
        }

        if(maxLength != null){
            try{
                Float.valueOf(maxLength);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("maxLength query parameter format not valid");
            }
        }

        if(maxWeight != null){
            try{
                Float.valueOf(maxWeight);
            } catch (NumberFormatException e) {
                throw new NumberFormatException("maxWeight query parameter format not valid");
            }
        }

        //Valida query order
        if(order == null || order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc")) {
            List<ProductDimension> dimension = productDimensionService.filterByParams(maxHeight, maxWidth, maxLength, maxWeight, order);
            return ResponseEntity.ok(ProductDimensionDTO.convertList(dimension));
        } else {
            throw new RuntimeException("Invalid query for order");
        }
    }
}
