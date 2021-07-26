package br.com.dmeireles.springelasticsearch.controller.form;

import br.com.dmeireles.springelasticsearch.model.Product;
import br.com.dmeireles.springelasticsearch.repository.ProductRepository;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UpdateProductForm {

    @Setter @NotNull @NotEmpty @Size(min = 5)
    private String name;

    @Setter @NotNull @NotEmpty @Size(min = 5)
    private String description;

    @Setter @NotNull @NotEmpty @Size(min = 3)
    private String category;

    public Product update(String id) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setDescription(description);
        product.setCategory(category);
        return product;
    }

}
