package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.controller.dto.search.SearchQueryDTO;
import br.com.dmeireles.springelasticsearch.controller.form.CreateProductForm;
import br.com.dmeireles.springelasticsearch.model.Product;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    IndexResponse save(Product product) throws IOException;

    UpdateResponse update(Product product) throws IOException;

    BulkResponse saveAll(List<CreateProductForm> products) throws IOException;

    SearchResponse searchAll(Pageable pagination) throws IOException;

    SearchResponse searchByName(String name, Pageable pagination) throws IOException;

    SearchResponse search(SearchQueryDTO searchQueryDTO) throws IOException;

}
