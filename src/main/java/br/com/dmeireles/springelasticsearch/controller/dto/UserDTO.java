package br.com.dmeireles.springelasticsearch.controller.dto;

import br.com.dmeireles.springelasticsearch.model.User;
import lombok.Getter;

public class UserDTO {

    @Getter
    private String id;
    @Getter
    private String name;
    @Getter
    private String email;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

}
