package br.com.dmeireles.springelasticsearch.controller.form;

import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UserForm {

    @Setter @NotNull @NotEmpty @Size(min = 5)
    private String name;

    @Setter @NotNull @Email
    private String email;

    @Setter @NotNull @NotEmpty @Size(min = 8)
    private String password;

}
