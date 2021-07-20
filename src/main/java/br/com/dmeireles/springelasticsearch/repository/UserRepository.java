package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.model.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface UserRepository extends ElasticsearchRepository<User, String> {

    List<User> findByName(String name);

    List<User> findByNameContaining(String nameContaining);

    List<User> findByEmail(String email);

    List<User> findByEmailContaining(String emailContaining);

}
