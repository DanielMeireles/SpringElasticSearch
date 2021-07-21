package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.model.Product;
import org.elasticsearch.action.index.IndexResponse;

import java.io.IOException;

public interface ProductRepository {

    IndexResponse save(Product product) throws IOException;

}
