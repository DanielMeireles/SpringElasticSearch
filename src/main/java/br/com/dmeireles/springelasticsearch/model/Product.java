package br.com.dmeireles.springelasticsearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@NoArgsConstructor
@Document(indexName = "product")
public class Product {

    @Getter @Setter @Id
    private String id;

    @Getter @Setter @Field(type = FieldType.Text, name = "name")
    private String name;

    @Getter @Setter @Field(type = FieldType.Text, name = "description")
    private String description;

    @Getter @Setter @Field(type = FieldType.Text, name = "category")
    private String category;

    public Product(String id, String name, String description, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

}
