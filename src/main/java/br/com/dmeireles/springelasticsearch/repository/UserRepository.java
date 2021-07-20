package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserRepository extends ElasticsearchRepository<User, String> {

    Page<User> findByName(String name, Pageable pagination);

    Page<User> findByNameContaining(String nameContaining, Pageable pagination);

    Page<User> findByEmail(String email, Pageable pagination);

    Page<User> findByEmailContaining(String emailContaining, Pageable pagination);

    Optional<User> findByEmail(String email);

}
