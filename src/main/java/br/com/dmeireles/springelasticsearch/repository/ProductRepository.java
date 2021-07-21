package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.controller.form.ProductForm;
import br.com.dmeireles.springelasticsearch.model.Product;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;

import java.io.IOException;
import java.util.List;

public interface ProductRepository {

    IndexResponse save(Product product) throws IOException;

    BulkResponse saveAll(List<ProductForm> products) throws IOException;

}
