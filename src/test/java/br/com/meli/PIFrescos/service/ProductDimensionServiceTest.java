package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.ProductDimension;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.ProductDimensionCustomRepository;
import br.com.meli.PIFrescos.repository.ProductDimensionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;


/**
 * @author Ana Preis
 */
@ExtendWith(MockitoExtension.class)
public class ProductDimensionServiceTest {

    @Mock
    private ProductDimensionRepository repository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductDimensionCustomRepository customRepository;

    @InjectMocks
    private ProductDimensionService service;

    Product product = new Product();
    Product product2 = new Product();
    Product product3 = new Product();
    ProductDimension dimension = new ProductDimension();
    ProductDimension dimension2 = new ProductDimension();
    ProductDimension dimension3 = new ProductDimension();
    List<ProductDimension> dimensionList = new ArrayList<>();

    /**
     * Define os objetos Product e ProductDimension iniciais
     */
    @BeforeEach
    void setUp(){
        product.setProductId(1);
        product.setProductName("Banana");
        product.setProductType(StorageType.FRESH);
        product.setProductDescription("description Banana");

        dimension.setId(1);
        dimension.setProduct(product);
        dimension.setHeight(15.0f);
        dimension.setWidth(20.0f);
        dimension.setLength(20.0f);
        dimension.setWeight(150.0f);

        product2.setProductId(2);
        product2.setProductName("Lasanha");
        product2.setProductType(StorageType.FROZEN);
        product2.setProductDescription("description Lasanha");

        dimension2.setId(2);
        dimension2.setProduct(product2);
        dimension2.setHeight(10.0f);
        dimension2.setWidth(15.0f);
        dimension2.setLength(15.0f);
        dimension2.setWeight(200.0f);

        product3.setProductId(3);
        product3.setProductName("Coca-Cola 2L");
        product3.setProductType(StorageType.REFRIGERATED);
        product3.setProductDescription("description CocaCola");

        dimension3.setId(3);
        dimension3.setProduct(product3);
        dimension3.setHeight(40.0f);
        dimension3.setWidth(10.0f);
        dimension3.setLength(10.0f);
        dimension3.setWeight(3000.0f);

        dimensionList.add(dimension);
        dimensionList.add(dimension2);
        dimensionList.add(dimension3);
    }

    /**
     * Limpa a lista após cada teste
     */
    @AfterEach
    void tearDown(){ dimensionList.clear(); }

    /**
     * Teste do método saveDimension()
     */
    @Test
    void saveDimension(){
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.empty());
        Mockito.when(repository.save(dimension)).thenReturn(dimension);

        ProductDimension result = service.saveDimension(dimension);

        assertEquals(dimension, result);
    }

    /**
     * Teste do caso de erro no método saveDimension(), deve mandar uma RuntimeException com a mensagem correta
     */
    @Test
    void shouldNotSaveDimension(){
        String message = "Banana dimension already registered";
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.ofNullable(dimension));

        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> service.saveDimension(dimension));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * Teste do método updateDimension()
     */
    @Test
    void updateDimension(){
        dimension.setWeight(200.0f);
        Mockito.when(productService.findProductById(1)).thenReturn(product);
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.ofNullable(dimension));
        Mockito.when(repository.save(dimension)).thenReturn(dimension);

        ProductDimension result = service.updateDimension(dimension);

        assertEquals(dimension, result);
    }

    /**
     * Teste do método deleteDimension()
     */
    @Test
    void deleteDimension(){
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.ofNullable(dimension));

        service.deleteDimension(product);

        verify(repository).delete(any());
    }

    /**
     * Teste do método getAll()
     */
    @Test
    void getAll(){
        Mockito.when(repository.findAll()).thenReturn(dimensionList);

        List<ProductDimension> result = service.getAll();

        assertEquals(dimensionList, result);
    }

    /**
     * Teste do caso de erro do método getAll() onde não há registros de dimensão. Deve mandar uma EntityNotFoundException
     * com a mensagem correta.
     */
    @Test
    void shouldNotGetAll(){
        String message = "ProductDimension list is empty!";
        Mockito.when(repository.findAll()).thenReturn(new ArrayList<>());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.getAll());

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * Teste do método findByProduct()
     */
    @Test
    void findByProduct(){
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.ofNullable(dimension));

        ProductDimension result = service.findByProduct(product);

        assertEquals(dimension, result);
    }

    /**
     * Teste do caso de erro do método findByProduct() onde não há registro de dimensão para a id solicitara.
     * Deve mandar uma EntityNotFoundException com a mensagem correta.
     */
    @Test
    void shouldNotFindByProduct(){
        String message = "Banana dimension not found.";
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.empty());

        EntityNotFoundException exception = Assertions.assertThrows(EntityNotFoundException.class, () -> service.findByProduct(product));

        assertThat(exception.getMessage()).isEqualTo(message);
    }

    /**
     * Teste do método findOptionalByProduct()
     */
    @Test
    void shouldFindOptionalByProduct(){
        Mockito.when(repository.findProductDimensionByProduct(product)).thenReturn(Optional.ofNullable(dimension));
        Optional<ProductDimension> expect = Optional.of(dimension);

        Optional<ProductDimension> result= service.findOptionalByProduct(product);

        assertEquals(expect, result);
    }

    /**
     * Teste do método filterByParams()
     */
    @Test
    void filterByParams(){
        Mockito.when(customRepository.find(100.0f, 100.0f, 100.0f, 5000.0f, "asc")).thenReturn(dimensionList);

        List<ProductDimension> result = service.filterByParams(100.0f, 100.0f, 100.0f, 5000.0f, "asc");

        assertEquals(dimensionList, result);
    }

    /**
     * Teste do método calculateVolume()
     */
    @Test
    void calculateVolume(){
        Float volume = dimension.getHeight() * dimension.getLength() * dimension.getWidth();

        Float result = service.calculateVolume(dimension);

        assertEquals(volume, result);
    }


}
