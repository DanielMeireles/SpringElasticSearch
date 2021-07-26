package br.com.dmeireles.springelasticsearch.controller.form;

import br.com.dmeireles.springelasticsearch.model.User;
import br.com.dmeireles.springelasticsearch.repository.UserRepository;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UpdateUserForm {

    @Setter @NotNull @Email
    private String email;

    @Setter @NotNull @NotEmpty @Size(min = 8)
    private String password;

    public User update(String id, UserRepository userRepository) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.get();
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

}
