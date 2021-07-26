package br.com.dmeireles.springelasticsearch.controller.form;

import br.com.dmeireles.springelasticsearch.model.Product;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CreateProductForm {

    @Setter @NotNull @NotEmpty @Size(min = 1)
    private String id;

    @Setter @NotNull @NotEmpty @Size(min = 5)
    private String name;

    @Setter @NotNull @NotEmpty @Size(min = 5)
    private String description;

    @Setter @NotNull @NotEmpty @Size(min = 3)
    private String category;

    public Product converter() {
        return new Product(id, name, description, category);
    }

}
