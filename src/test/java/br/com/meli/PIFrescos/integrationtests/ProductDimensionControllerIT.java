package br.com.meli.PIFrescos.integrationtests;

import br.com.meli.PIFrescos.controller.dtos.ProductDTO;
import br.com.meli.PIFrescos.controller.dtos.ProductDimensionDTO;
import br.com.meli.PIFrescos.controller.dtos.TokenDto;
import br.com.meli.PIFrescos.controller.dtos.VolumeDTO;
import br.com.meli.PIFrescos.controller.forms.ProductDimensionForm;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.repository.ProductDimensionRepository;
import br.com.meli.PIFrescos.repository.UserRepository;
import br.com.meli.PIFrescos.service.ProductDimensionService;
import br.com.meli.PIFrescos.service.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ana Preis
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class ProductDimensionControllerIT {

    @MockBean
    private ProductDimensionService service;

    @MockBean
    private ProductDimensionRepository repository;

    @MockBean
    private ProductService productService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    Product mockProduct = new Product();
    Product mockProduct2 = new Product();
    Product mockProduct3 = new Product();
    ProductDimension dimension = new ProductDimension();
    ProductDimension dimension2 = new ProductDimension();
    ProductDimension dimension3 = new ProductDimension();
    List<ProductDimension> dimensionList = new ArrayList<>();

    String payload = "{ \n"
            + " \"productId\": \"1\","
            + " \"height\": \"10.0\","
            + " \"width\": \"10.0\","
            + " \"length\": \"10.0\","
            + " \"weight\": \"10.0\""
            + "}";

    String loginPayload = "{"
            + "\"email\": \"meli@gmail.com\", "
            + "\"password\": \"123456\""
            + "}";

    private String accessToken;
    User userMock = new User();
    Profile profile= new Profile();

    @BeforeEach
    public void setUp() throws Exception {
        mockProduct.setProductId(1);
        mockProduct.setProductType(StorageType.FRESH);
        mockProduct.setProductName("Uva");
        mockProduct.setProductDescription("Mock description");

        dimension.setId(1);
        dimension.setProduct(mockProduct);
        dimension.setHeight(15.0f);
        dimension.setWidth(20.0f);
        dimension.setLength(20.0f);
        dimension.setWeight(150.0f);

        mockProduct2.setProductId(2);
        mockProduct2.setProductType(StorageType.FROZEN);
        mockProduct2.setProductName("Peixe");
        mockProduct2.setProductDescription("Mock description");

        dimension2.setId(2);
        dimension2.setProduct(mockProduct2);
        dimension2.setHeight(10.0f);
        dimension2.setWidth(15.0f);
        dimension2.setLength(15.0f);
        dimension2.setWeight(200.0f);

        mockProduct3.setProductId(3);
        mockProduct3.setProductType(StorageType.REFRIGERATED);
        mockProduct3.setProductName("Maça");
        mockProduct3.setProductDescription("Mock description");

        dimension3.setId(3);
        dimension3.setProduct(mockProduct3);
        dimension3.setHeight(40.0f);
        dimension3.setWidth(10.0f);
        dimension3.setLength(10.0f);
        dimension3.setWeight(3000.0f);

        profile.setId(1L);
        profile.setName("ADMIN");
        userMock.setId(1);
        userMock.setFullname("John Doe");
        userMock.setEmail("john@mercadolivre.com.br");
        userMock.setPassword("$2a$10$GtzVniP9dVMmVW2YxytuvOG9kHu9nrwAxe8/UXSFkaECmIJ4UJcHy");
        userMock.setProfiles(List.of(profile));
        userMock.setRole(UserRole.ADMIN);

        dimensionList.add(dimension);
        dimensionList.add(dimension2);
        dimensionList.add(dimension3);

        this.accessToken = this.userLogin();
    }

    /**
     * This method returned the mock user token.
     * @return String
     * @author Antonio Hugo
     *
     */
    private String userLogin() throws Exception {
        Mockito.when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(userMock));
        MvcResult result = mockMvc.perform(post("/auth")
                .contentType("application/json").content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<TokenDto> typeReference = new TypeReference<TokenDto>() {};
        TokenDto token = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        return "Bearer " + token.getToken();
    }

    @Test
    public void shouldCreateDimension() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        ProductDimensionForm form = objectMapper.readValue(payload, ProductDimensionForm.class);
        Mockito.when(productService.findProductById(form.getProductId())).thenReturn(mockProduct);
        Mockito.when(service.saveDimension(any())).thenReturn(form.convert(dimension.getProduct(), dimension.getHeight(),
                dimension.getWidth(), dimension.getLength(), dimension.getWeight()));

        MvcResult result = mockMvc.perform(post("/fresh-products/dimension")
                .header("Authorization", accessToken)
                .contentType("application/json").content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productName").value("Uva"))
                .andReturn();

        ProductDimensionDTO dimensionResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProductDimensionDTO.class);
        assertEquals(mockProduct.getProductName(), dimensionResponse.getProductName());
    }

    @Test
    public void shouldUpdateDimension() throws Exception {
        String payload = "{ \n"
                + " \"productId\": \"1\","
                + " \"height\": \"15.0\","
                + " \"width\": \"10.0\","
                + " \"length\": \"10.0\","
                + " \"weight\": \"10.0\""
                + "}";
        dimension.setHeight(15.0f);
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        ProductDimensionForm form = objectMapper.readValue(payload, ProductDimensionForm.class);
        Mockito.when(productService.findProductById(form.getProductId())).thenReturn(mockProduct);
        Mockito.when(service.updateDimension(any())).thenReturn(form.convert(dimension.getProduct(), dimension.getHeight(),
                dimension.getWidth(), dimension.getLength(), dimension.getWeight()));

        MvcResult result = mockMvc.perform(put("/fresh-products/dimension")
                .header("Authorization", accessToken)
                .contentType("application/json").content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Uva"))
                .andReturn();

        ProductDimensionDTO dimensionResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProductDimensionDTO.class);
        assertEquals(mockProduct.getProductName(), dimensionResponse.getProductName());
    }

    @Test
    public void shouldDeleteDimension() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);
        Mockito.when(service.deleteDimension(any())).thenReturn(mockProduct);

        MvcResult result = mockMvc.perform(delete("/fresh-products/dimension/1")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Uva"))
                .andReturn();

        ProductDTO response = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProductDTO.class);
        assertEquals(mockProduct.getProductName(), response.getProductName());
    }

    @Test
    public void shouldGetAll() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);
        Mockito.when(service.getAll()).thenReturn(dimensionList);
        List<ProductDimensionDTO> dtoList = ProductDimensionDTO.convertList(dimensionList);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension/list")
                .header("Authorization", accessToken))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<ProductDimensionDTO>> typeReference = new TypeReference<>() {};
        List<ProductDimensionDTO> dimensionListResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertEquals(dtoList.get(0).getProductName(), dimensionListResponse.get(0).getProductName());
    }

    @Test
    public void shouldGetProductDimension() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);
        Mockito.when(service.findByProduct(mockProduct)).thenReturn(dimension);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension/1")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Uva"))
                .andReturn();

        ProductDimensionDTO dimensionResponse = objectMapper.readValue(result.getResponse().getContentAsString(),
                ProductDimensionDTO.class);
        assertEquals(mockProduct.getProductName(), dimensionResponse.getProductName());
    }

    @Test
    public void shouldGetProductVolume() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Float volume = dimension.getHeight() * dimension.getWidth() * dimension.getLength();
        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);
        Mockito.when(service.findByProduct(mockProduct)).thenReturn(dimension);
        Mockito.when(service.calculateVolume(dimension)).thenReturn(volume);
        VolumeDTO expect = new VolumeDTO(mockProduct.getProductName(), volume);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension/vol/1")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isOk())
                .andReturn();

        VolumeDTO response = objectMapper.readValue(result.getResponse().getContentAsString(), VolumeDTO.class);
        assertEquals(expect.getVolume(), response.getVolume());
    }

    @Test
    public void shouldGetFilteredDimensionsAllParam() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);
        Mockito.when(service.filterByParams(100.0f, 100.0f, 100.0f, 100000.0f, "asc")).thenReturn(dimensionList);
        List<ProductDimensionDTO> expected =  ProductDimensionDTO.convertList(dimensionList);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension" +
                "/listBy?maxHeight=100&maxWidth=100&maxLength=100&maxWeight=100000&order=asc")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<ProductDimensionDTO>> typeReference = new TypeReference<>() {};
        List<ProductDimensionDTO> dimensionListResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertEquals(expected.get(0).getProductName(), dimensionListResponse.get(0).getProductName());

    }

    @Test
    public void shouldGetFilteredDimensionsNoParam() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);
        Mockito.when(service.getAll()).thenReturn(dimensionList);
        List<ProductDimensionDTO> expected =  ProductDimensionDTO.convertList(dimensionList);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension" +
                "/listBy")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<ProductDimensionDTO>> typeReference = new TypeReference<>() {};
        List<ProductDimensionDTO> dimensionListResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertEquals(expected.get(0).getProductName(), dimensionListResponse.get(0).getProductName());

    }

    @Test
    public void shouldReturn400IfParamInvalid() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension" +
                "/listBy?maxHeight=lalalal&maxWidth=100&maxLength=100&maxWeight=100000&order=asc")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Failed to convert value of type " +
                        "'java.lang.String' to required type 'java.lang.Float'; " +
                        "nested exception is java.lang.NumberFormatException: For input string: \"lalalal\""))
                .andReturn();
    }

    @Test
    public void shouldReturn400IfOrderInvalid() throws Exception {
        Mockito.when(userRepository.findById(any())).thenReturn(Optional.ofNullable(userMock));

        Mockito.when(productService.findProductById(any())).thenReturn(mockProduct);

        MvcResult result = mockMvc.perform(get("/fresh-products/dimension" +
                "/listBy?maxHeight=100&maxWidth=100&maxLength=100&maxWeight=100000&order=lalala")
                .header("Authorization", accessToken)
                .contentType("application/json").content(""))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid query for order"))
                .andReturn();
    }
}
