package br.com.dmeireles.springelasticsearch.controller;

import br.com.dmeireles.springelasticsearch.controller.dto.UserDTO;
import br.com.dmeireles.springelasticsearch.controller.form.CreateUserForm;
import br.com.dmeireles.springelasticsearch.controller.form.UpdateUserForm;
import br.com.dmeireles.springelasticsearch.model.User;
import br.com.dmeireles.springelasticsearch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Page<UserDTO> list(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String nameContaining,
                              @RequestParam(required = false) String email,
                              @RequestParam(required = false) String emailContaining,
                              @PageableDefault(page = 0, size = 10) Pageable pagination) {

        if (name != null)
            return UserDTO.converter(userRepository.findByName(name, pagination));
        if (nameContaining != null)
            return UserDTO.converter(userRepository.findByNameContaining(nameContaining, pagination));
        if (email != null)
            return UserDTO.converter(userRepository.findByEmail(email, pagination));
        if (emailContaining != null)
            return UserDTO.converter(userRepository.findByEmailContaining(emailContaining, pagination));
        return UserDTO.converter(userRepository.findAll(pagination));

    }

    @GetMapping("/SearchByName")
    public Page<UserDTO> list(@RequestParam(required = true) String name,
                              @PageableDefault(page = 0, size = 10) Pageable pagination) {
            return UserDTO.converter(userRepository.findByName(name, pagination));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> detail(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent())
            return ResponseEntity.ok(new UserDTO(user.get()));
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UserDTO> register(@RequestBody @Valid CreateUserForm createUserForm, UriComponentsBuilder uriBuilder) {
        User user = createUserForm.converter(userRepository);
        userRepository.save(user);

        URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
        return ResponseEntity.created(uri).body(new UserDTO(user));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UserDTO> update(@PathVariable String id, @RequestBody @Valid UpdateUserForm updateUserForm) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            User user = updateUserForm.update(id, userRepository);
            return ResponseEntity.ok(new UserDTO(user));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> remove(@PathVariable String id) {
        Optional<User> optional = userRepository.findById(id);
        if (optional.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
