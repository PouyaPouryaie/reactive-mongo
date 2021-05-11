package ir.rad.reactive.mongoreactive.service;

import ir.rad.reactive.mongoreactive.dto.ProductDto;
import ir.rad.reactive.mongoreactive.entity.Product;
import ir.rad.reactive.mongoreactive.repository.ProductRepository;
import ir.rad.reactive.mongoreactive.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<ProductDto> getProducts(){
        return productRepository.findAll()
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> getProduct(String id){
        return productRepository.findById(id)
                .map(AppUtils::entityToDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max){
        return productRepository.findByPriceBetween(Range.closed(min, max))
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono){
        return productDtoMono.map(AppUtils::dtoToEntity)
                .flatMap(productRepository::insert)
                .map(AppUtils::entityToDto);
    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id){
        return productRepository.findById(id)
                .flatMap(p -> productDtoMono.map(AppUtils::dtoToEntity))
                .doOnNext(p -> p.setId(id))
                .flatMap(productRepository::save)
                .map(AppUtils::entityToDto);
    }

    public Mono<Void> deleteProduct(String id){
        return productRepository.deleteById(id);
    }
}
