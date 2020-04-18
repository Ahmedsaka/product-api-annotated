package com.medalytics.productapiannotation.controller;

import com.medalytics.productapiannotation.model.Product;
import com.medalytics.productapiannotation.model.ProductEvent;
import com.medalytics.productapiannotation.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductRepository repository;

    @Autowired
    public ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<Product>> getProductById(@PathVariable String id){
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Product> saveProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping(value = "/{id}")
    public Mono<ResponseEntity<Void>> updateProduct(@PathVariable(value = "id") String id) {
        return repository.findById(id)
                .flatMap(existingProduct ->
                        repository.delete(existingProduct)
                            .then(Mono.just(ResponseEntity.ok().<Void>build()))
                            )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public Mono<Void> deleteAllProducts(){
        return repository.deleteAll();
    }

    @GetMapping(value = "/all")
    public Mono<ResponseEntity<List<Product>>> getAllProducts(){
        return repository.findAll().collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductEvent> getProductEvent(){
        return Flux.interval(Duration.ofSeconds(1))
                .map(val ->
                        new ProductEvent(val, "Product Event")
                        );
    }
}
