package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.ProductDTO;
import br.com.meli.PIFrescos.controller.dtos.ProductDimensionDTO;
import br.com.meli.PIFrescos.controller.dtos.VolumeDTO;
import br.com.meli.PIFrescos.controller.forms.ProductDimensionForm;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.service.ProductDimensionService;
import br.com.meli.PIFrescos.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "API REST Dimensão de produtos")
@CrossOrigin(origins = "*")
public class ProductDimensionController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductDimensionService productDimensionService;

    @PostMapping("")
    @ApiOperation(value = "Este método registra a dimensão de um produto já cadastrado e retorna estas dimensões")
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
    @ApiOperation(value = "Este método atualiza uma dimensão cadastrada e retorna estas dimensões")
    public ResponseEntity<ProductDimensionDTO> update(@RequestBody @Valid ProductDimensionForm form){
        Product product = productService.findProductById(form.getProductId());
        ProductDimension dimension = this.productDimensionService.updateDimension(form.convert(product,
                form.getHeight(), form.getWidth(), form.getLength(), form.getWeight()));
        return ResponseEntity.ok(new ProductDimensionDTO(dimension));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Este método deleta a dimensão cadastrada do produto com a id passada por parâmetro e retorna o produto")
    public ResponseEntity<ProductDTO> deleteDimension(@PathVariable Integer id){
        Product product = productService.findProductById(id);
        Product productWithoutDimension = productDimensionService.deleteDimension(product);
        return ResponseEntity.ok(new ProductDTO(productWithoutDimension));
    }

    @GetMapping("/list")
    @ApiOperation(value = "Este método lista todas as dimensões cadastradas")
    public ResponseEntity<List<ProductDimensionDTO>> getAll(){
        List<ProductDimensionDTO> list = ProductDimensionDTO.convertList(productDimensionService.getAll());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Este método exibe a dimensão cadastradas do produdo com a id passada por parâmetro")
    public ResponseEntity<ProductDimensionDTO> getProductDimension(@PathVariable Integer id){
        Product product = productService.findProductById(id);
        ProductDimension dimension = productDimensionService.findByProduct(product);
        return ResponseEntity.ok(new ProductDimensionDTO(dimension));
    }

    @GetMapping("vol/{id}")
    @ApiOperation(value = "Este método exibe o volume em cm3 do produdo com a id passada por parâmetro")
    public ResponseEntity<VolumeDTO> getProductVolume(@PathVariable Integer id){
        Product product = productService.findProductById(id);
        ProductDimension productDimension = productDimensionService.findByProduct(product);
        Float volume = productDimensionService.calculateVolume(productDimension);
        VolumeDTO response = new VolumeDTO(product.getProductName(), volume);
        return ResponseEntity.ok(response);
    }

    /**
     * Validações de formato dos parâmetros é realizada antes de chamar o método de service.
     */
    @GetMapping("/listBy")
    @ApiOperation(value = "Este método exibe a lista de dimensões de produtos filtradas pelas queries da URI")
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

        //Valida query order
        if(order == null || order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc")) {
            List<ProductDimension> dimension = productDimensionService.filterByParams(maxHeight, maxWidth, maxLength, maxWeight, order);
            return ResponseEntity.ok(ProductDimensionDTO.convertList(dimension));
        } else {
            throw new RuntimeException("Invalid query for order");
        }
    }
}
