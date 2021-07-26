package br.com.dmeireles.springelasticsearch.controller;

import br.com.dmeireles.springelasticsearch.controller.dto.ProductDTO;
import br.com.dmeireles.springelasticsearch.controller.dto.search.SearchQueryDTO;
import br.com.dmeireles.springelasticsearch.controller.form.CreateProductForm;
import br.com.dmeireles.springelasticsearch.controller.form.UpdateProductForm;
import br.com.dmeireles.springelasticsearch.model.Product;
import br.com.dmeireles.springelasticsearch.repository.ProductRepository;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ProductDTO> register(@RequestBody @Valid CreateProductForm createProductForm,
                                               UriComponentsBuilder uriBuilder) {
        try {
            Product product = createProductForm.converter();
            productRepository.save(product);

            URI uri = uriBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri();
            return ResponseEntity.created(uri).body(new ProductDTO(product));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable String id,
                                             @RequestBody @Valid UpdateProductForm updateProductForm,
                                             UriComponentsBuilder uriBuilder) {
        try {
            Product product = updateProductForm.update(id);
            productRepository.update(product);

            URI uri = uriBuilder.path("/product/{id}").buildAndExpand(product.getId()).toUri();
            return ResponseEntity.created(uri).body(new ProductDTO(product));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/bulkInsert")
    public ResponseEntity<RestStatus> registerProducts(@RequestBody List<@Valid CreateProductForm> products,
                                                       UriComponentsBuilder uriBuilder) {
        try {
            RestStatus status = productRepository.saveAll(products).status();
            URI uri = uriBuilder.path("/product").build().toUri();
            return ResponseEntity.created(uri).body(status);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/bulkUpdate")
    public ResponseEntity<RestStatus> updateProducts(@RequestBody List<@Valid CreateProductForm> products,
                                                     UriComponentsBuilder uriBuilder) {
        return registerProducts(products, uriBuilder);
    }

    @PostMapping("/search")
    public ResponseEntity<SearchResponse> searchProduct(@RequestBody SearchQueryDTO searchQueryDTO) {
        try {
            SearchResponse search = productRepository.search(searchQueryDTO);
            return ResponseEntity.ok(search);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<SearchResponse> searchProductByName(@RequestParam(required = true) String name,
                                                        @PageableDefault(page = 0, size = 10) Pageable pagination) {
        try {
            SearchResponse search = productRepository.searchByName(name, pagination);
            return ResponseEntity.ok(search);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
