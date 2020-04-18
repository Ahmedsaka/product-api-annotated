package com.medalytics.productapiannotation.repository;

import com.medalytics.productapiannotation.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository  extends ReactiveMongoRepository<Product, String> {

    Flux<Product> findByName(String name);
}
