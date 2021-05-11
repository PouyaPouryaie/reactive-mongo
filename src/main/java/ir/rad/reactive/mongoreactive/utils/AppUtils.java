package ir.rad.reactive.mongoreactive.utils;

import ir.rad.reactive.mongoreactive.dto.ProductDto;
import ir.rad.reactive.mongoreactive.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDto entityToDto(Product source){
        ProductDto destination = new ProductDto();
        BeanUtils.copyProperties(source, destination);
        return destination;
    }

    public static Product dtoToEntity(ProductDto source){
        Product destination = new Product();
        BeanUtils.copyProperties(source, destination);
        return destination;
    }
}