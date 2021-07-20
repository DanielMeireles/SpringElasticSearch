package br.com.dmeireles.springelasticsearch.controller.form;

import br.com.dmeireles.springelasticsearch.model.User;
import br.com.dmeireles.springelasticsearch.repository.UserRepository;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Optional;

public class UserForm {

    @Setter @NotNull @NotEmpty @Size(min = 5)
    private String name;

    @Setter @NotNull @Email
    private String email;

    @Setter @NotNull @NotEmpty @Size(min = 8)
    private String password;

    public User converter(UserRepository userRepository) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent())
            return updateUserObject(user.get());
        return new User(name, email, password);
    }

    public User update(String id, UserRepository userRepository) {
        Optional<User> user = userRepository.findById(id);
        return updateUserObject(user.get());
    }

    public User updateUserObject(User user) {
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

}
