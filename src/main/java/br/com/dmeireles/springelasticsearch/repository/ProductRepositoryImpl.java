package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public IndexResponse save(Product product) throws IOException {
        IndexRequest indexRequest = Requests
                .indexRequest("product")
                .id(product.getId())
                .source(convertProductToMap(product));
        return restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    private Map<String, Object> convertProductToMap(Product product) {
        try {
            String json = objectMapper.writeValueAsString(product);
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
