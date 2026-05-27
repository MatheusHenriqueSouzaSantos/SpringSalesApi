package com.example.projetoApiVendasEmSpring.product;

import com.example.projetoApiVendasEmSpring.SystemUser;
import com.example.projetoApiVendasEmSpring.appUser.dto.AuditAppUserDto;
import com.example.projetoApiVendasEmSpring.appUser.entity.AppUser;
import com.example.projetoApiVendasEmSpring.appUser.entity.UserRole;
import com.example.projetoApiVendasEmSpring.appUser.repository.AppUserRepository;
import com.example.projetoApiVendasEmSpring.excepetions.ResourceNotFoundException;
import com.example.projetoApiVendasEmSpring.product.dto.ProductCreateDto;
import com.example.projetoApiVendasEmSpring.product.dto.ProductOutputDto;
import com.example.projetoApiVendasEmSpring.product.entity.Product;
import com.example.projetoApiVendasEmSpring.product.repository.ProductRepository;
import com.example.projetoApiVendasEmSpring.product.service.ProductServiceImpl;
import com.example.projetoApiVendasEmSpring.security.userDetails.UserDetailsImpl;
import com.example.projetoApiVendasEmSpring.stock.dto.SummaryStockDto;
import com.example.projetoApiVendasEmSpring.stock.entity.Stock;
import com.example.projetoApiVendasEmSpring.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private ProductServiceImpl service;

    @Test
    @DisplayName("should find a product by id successfully")
    public void findProductByIdSuccessfully(){
        //arrange
        AppUser appUser=new AppUser(null, "testUser","testUser@email.com","123", UserRole.USER);

        Product productReceivedBd=new Product(appUser,"123","testProduct","this is test product", new BigDecimal("100"));

        productReceivedBd.setStock(new Stock(appUser,productReceivedBd,10));

        ProductOutputDto productResponseExpected =new ProductOutputDto(UUID.fromString("3f5e8c2a-9b71-4c6e-a1d3-7f2b8e9d4c10"), Instant.now(),
                new AuditAppUserDto("testUser","testUser@email.com"),null,null,true,"123","testProduct",
                "this is test product",new BigDecimal("100"),new SummaryStockDto(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),3));

        when(repository.findById(any())).thenReturn(Optional.of(productReceivedBd));

        //act
        ProductOutputDto dto=service.findProductById(UUID.fromString("3f5e8c2a-9b71-4c6e-a1d3-7f2b8e9d4c10"));

        //assert

        assertEquals(productResponseExpected.sku(),dto.sku());
        assertEquals(productResponseExpected.name(),dto.name());
        assertEquals(productResponseExpected.description(),dto.description());
        assertEquals(productResponseExpected.price(),dto.price());

    }

    @Test
    @DisplayName("should throw a resource not found exception, because informed id do not exists")
    public void findNonExistProductById(){
        //arrange
        when(repository.findById(any())).thenReturn(Optional.empty());
        //act and assert
        assertThrows(ResourceNotFoundException.class,()->{
            service.findProductById(UUID.fromString("3f5e8c2a-9b71-4c6e-a1d3-7f2b8e9d4c10"));
        });
    }

    @Test
    @DisplayName("should create a product successfully")
    public void createProductSuccessfully(){
        //arrange
        ProductCreateDto productCreateDto=new ProductCreateDto("123","testProduct","this is test product",new BigDecimal("1000"));
        AppUser appUser=new AppUser(null, "testUser","testUser@email.com","123", UserRole.USER);
        ProductOutputDto expectedProductOutputDto =new ProductOutputDto(UUID.fromString( "550e8400-e29b-41d4-a716-446655440000"),Instant.now(),
                new AuditAppUserDto(appUser.getFullName(),appUser.getEmail()),null,null,true,productCreateDto.sku(), productCreateDto.name(),
                productCreateDto.description(),productCreateDto.price(),new SummaryStockDto(UUID.fromString("3f5e8c2a-9b71-4c6e-a1d3-7f2b8e9d4c10"),10));
        when(appUserRepository.findAppUserByIdExceptSystemUser(SystemUser.ID,UUID.fromString("550e8400-e29b-41d4-a716-446655440000")))
                .thenReturn(Optional.of(appUser));
        when(repository.existsBySku("123")).thenReturn(false);
        when(stockRepository.save(any())).thenReturn(null);

        UserDetailsImpl loggedUser=new UserDetailsImpl(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),"testUser@email.com","123",
                true, List.of(new SimpleGrantedAuthority(UserRole.USER.toString())));
        //act
        ProductOutputDto savedProduct=service.createProduct(productCreateDto,loggedUser);

        //assert
        verify(repository).save(any());
        verify(stockRepository).save(any());
        assertEquals(expectedProductOutputDto.sku(),savedProduct.sku());
        assertEquals(expectedProductOutputDto.name(),savedProduct.name());
        assertEquals(expectedProductOutputDto.description(),savedProduct.description());
        assertEquals(expectedProductOutputDto.price(),savedProduct.price());
        assertNotNull(savedProduct.stock());
    }
}
