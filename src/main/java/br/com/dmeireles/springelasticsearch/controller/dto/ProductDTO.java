package br.com.dmeireles.springelasticsearch.controller.dto;

import br.com.dmeireles.springelasticsearch.model.Product;
import lombok.Getter;
import org.springframework.data.domain.Page;

public class ProductDTO {

    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private String description;
    @Getter
    private String category;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.category = product.getCategory();
    }

    public static Page<ProductDTO> converter(Page<Product> products) {
        return products.map(ProductDTO::new);
    }

}
