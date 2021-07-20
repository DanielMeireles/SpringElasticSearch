package br.com.dmeireles.springelasticsearch.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@NoArgsConstructor
@Document(indexName = "user")
public class User {

    @Getter @Setter @Id
    private String id;

    @Getter @Setter @Field(type = FieldType.Text, name = "name")
    private String name;

    @Getter @Setter @Field(type = FieldType.Text, name = "email")
    private String email;

    @Getter @Setter @Field(type = FieldType.Text, name = "password")
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

}
