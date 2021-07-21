package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.controller.dto.search.FilterRequestDTO;
import br.com.dmeireles.springelasticsearch.controller.dto.search.RangeFilterDTO;
import br.com.dmeireles.springelasticsearch.controller.dto.search.SearchQueryDTO;
import br.com.dmeireles.springelasticsearch.controller.form.ProductForm;
import br.com.dmeireles.springelasticsearch.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
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

    @Override
    public BulkResponse saveAll(List<ProductForm> productsForm) throws IOException {
        BulkRequest bulkRequest = Requests.bulkRequest();
        productsForm.forEach(productForm -> {
            IndexRequest indexRequest = Requests
                    .indexRequest("product")
                    .id(productForm.converter().getId())
                    .source(convertProductToMap(productForm.converter()));
            bulkRequest.add(indexRequest);
        });
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse search(SearchQueryDTO searchQuery) throws IOException {
        SearchRequest searchRequest = Requests.searchRequest("product");

        // boolean query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("id", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("name", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("description", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("category", searchQuery.getQuery()));


        // facet query
        if (searchQuery.getFilter() != null) {
            FilterRequestDTO filter = searchQuery.getFilter();
            if (filter.getRange() != null) {
                for (String keyToFilter : filter.getRange().keySet()) {
                    RangeFilterDTO valueToFilter = filter.getRange().get(keyToFilter);

                    RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(keyToFilter);

                    if (valueToFilter.getLte() != null) {
                        rangeQueryBuilder.lte(valueToFilter.getLte());
                    }

                    if (valueToFilter.getLt() != null) {
                        rangeQueryBuilder.lt(valueToFilter.getLt());
                    }

                    if (valueToFilter.getGt() != null) {
                        rangeQueryBuilder.gt(valueToFilter.getGt());
                    }

                    if (valueToFilter.getGte() != null) {
                        rangeQueryBuilder.gte(valueToFilter.getGte());
                    }

                    boolQueryBuilder.filter(rangeQueryBuilder);
                }

            } else if (filter.getMatch() != null) {
                for (String keyToFilter : filter.getMatch().keySet()) {
                    Object valueToFilter = filter.getMatch().get(keyToFilter).toString().toLowerCase();

                    boolQueryBuilder.filter(QueryBuilders.termQuery(keyToFilter, valueToFilter));
                }
            }
        }

        // pagination
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .from(searchQuery.getPage() * searchQuery.getSize())
                .size(searchQuery.getSize())
                .query(boolQueryBuilder);

        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
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
