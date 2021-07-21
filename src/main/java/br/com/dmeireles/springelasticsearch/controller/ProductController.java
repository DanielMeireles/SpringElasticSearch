package br.com.dmeireles.springelasticsearch.controller;

import br.com.dmeireles.springelasticsearch.controller.dto.ProductDTO;
import br.com.dmeireles.springelasticsearch.controller.form.ProductForm;
import br.com.dmeireles.springelasticsearch.model.Product;
import br.com.dmeireles.springelasticsearch.repository.ProductRepository;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @PostMapping
    public ResponseEntity<ProductDTO> register(@RequestBody @Valid ProductForm productForm, UriComponentsBuilder uriBuilder) {
        try {
            Product product = productForm.converter();
            productRepository.save(product);

            URI uri = uriBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri();
            return ResponseEntity.created(uri).body(new ProductDTO(product));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<RestStatus> registerProducts(@RequestBody List<@Valid ProductForm> products, UriComponentsBuilder uriBuilder) {
        try {
            RestStatus status = productRepository.saveAll(products).status();
            URI uri = uriBuilder.path("/product").build().toUri();
            return ResponseEntity.created(uri).body(status);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
