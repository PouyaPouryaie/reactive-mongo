package ir.rad.reactive.mongoreactive;

import ir.rad.reactive.mongoreactive.controller.ProductController;
import ir.rad.reactive.mongoreactive.dto.ProductDto;
import ir.rad.reactive.mongoreactive.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class ProductControllerTests {

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private ProductService productService;

	@Test
	public void addProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(
				new ProductDto("102","tv",10, 1000.0));

		when(productService.saveProduct(productDtoMono))
				.thenReturn(productDtoMono);

		webTestClient.post().uri("/products")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void getProductsTest(){
		Flux<ProductDto> flux = Flux.just(new ProductDto("102","tv",10, 1000.0),
				new ProductDto("103","mobile",5, 2000.0));

		when(productService.getProducts())
				.thenReturn(flux);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new ProductDto("102","tv",10, 1000.0))
				.expectNext(new ProductDto("103","mobile",5, 2000.0))
				.verifyComplete();
	}

	@Test
	public void getProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(new ProductDto("102","tv",10, 1000.0));

		when(productService.getProduct(any())).thenReturn(productDtoMono);

		Flux<ProductDto> responseBody = webTestClient.get().uri("/products/100")
				.exchange()
				.expectStatus().isOk()
				.returnResult(ProductDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(productDto -> productDto.getName().equals("tv"))
				.verifyComplete();
	}

	@Test
	public void updateProductTest(){
		Mono<ProductDto> productDtoMono = Mono.just(
				new ProductDto("102","tv",10, 1000.0));

		when(productService.updateProduct(productDtoMono, "102"))
				.thenReturn(productDtoMono);

		webTestClient.put().uri("/products/update/102")
				.body(Mono.just(productDtoMono), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void deleteProductTest(){
		given(productService.deleteProduct(any())).willReturn(Mono.empty());

		webTestClient.delete().uri("/products/delete/102")
				.exchange()
				.expectStatus().isOk();
	}


}
