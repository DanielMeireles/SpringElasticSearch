package br.com.dmeireles.springelasticsearch.repository;

import br.com.dmeireles.springelasticsearch.controller.dto.search.FilterRequestDTO;
import br.com.dmeireles.springelasticsearch.controller.dto.search.RangeFilterDTO;
import br.com.dmeireles.springelasticsearch.controller.dto.search.SearchQueryDTO;
import br.com.dmeireles.springelasticsearch.controller.form.CreateProductForm;
import br.com.dmeireles.springelasticsearch.model.Product;
import br.com.dmeireles.springelasticsearch.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    ObjectMapper objectMapper;

    private static final String INDEX = "product";

    @Override
    public IndexResponse save(Product product) throws IOException {
        IndexRequest indexRequest = Requests
                .indexRequest(INDEX)
                .id(product.getId())
                .source(convertProductToMap(product));
        return restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    }

    @Override
    public UpdateResponse update(Product product) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest()
                .index(INDEX)
                .id(product.getId())
                .doc(convertProductToMap(product));
        return restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
    }

    @Override
    public BulkResponse saveAll(List<CreateProductForm> productsForm) throws IOException {
        BulkRequest bulkRequest = Requests.bulkRequest();
        productsForm.forEach(createProductForm -> {
            IndexRequest indexRequest = Requests
                    .indexRequest(INDEX)
                    .id(createProductForm.converter().getId())
                    .source(convertProductToMap(createProductForm.converter()));
            bulkRequest.add(indexRequest);
        });
        return restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse searchByName(String name, Pageable pagination) throws IOException {
        SearchRequest searchRequest = Requests.searchRequest(INDEX);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("name", name));
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .from(pagination.getPageNumber())
                .size(pagination.getPageSize())
                .query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    @Override
    public SearchResponse search(SearchQueryDTO searchQuery) throws IOException {
        SearchRequest searchRequest = Requests.searchRequest(INDEX);
        BoolQueryBuilder boolQueryBuilder = createBoolQuery(searchQuery);
        this.facedQuery(searchQuery, boolQueryBuilder);
        SearchSourceBuilder searchSourceBuilder = createPagination(searchQuery, boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        return restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
    }

    private BoolQueryBuilder createBoolQuery(SearchQueryDTO searchQuery) {
        return QueryBuilders.boolQuery()
                .should(QueryBuilders.matchQuery("id", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("name", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("description", searchQuery.getQuery()))
                .should(QueryBuilders.matchQuery("category", searchQuery.getQuery()));
    }

    private void facedQuery(SearchQueryDTO searchQuery, BoolQueryBuilder boolQueryBuilder) {
        if (searchQuery.getFilter() != null) {
            FilterRequestDTO filter = searchQuery.getFilter();
            this.createRangeFilter(filter, boolQueryBuilder);
            this.createMatchFilter(filter, boolQueryBuilder);
        }
    }

    private void createRangeFilter(FilterRequestDTO filter, BoolQueryBuilder boolQueryBuilder) {
        if (filter.getRange() != null) {
            filter.getRange().keySet().forEach(keyToFilter -> {
                RangeFilterDTO valueToFilter = filter.getRange().get(keyToFilter);

                RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(keyToFilter);

                if (valueToFilter.getLte() != null)
                    rangeQueryBuilder.lte(valueToFilter.getLte());

                if (valueToFilter.getLt() != null)
                    rangeQueryBuilder.lt(valueToFilter.getLt());

                if (valueToFilter.getGt() != null)
                    rangeQueryBuilder.gt(valueToFilter.getGt());

                if (valueToFilter.getGte() != null)
                    rangeQueryBuilder.gte(valueToFilter.getGte());

                boolQueryBuilder.filter(rangeQueryBuilder);
            });

        }
    }

    private void createMatchFilter(FilterRequestDTO filter, BoolQueryBuilder boolQueryBuilder) {
        if (filter.getMatch() != null) {
            filter.getMatch().keySet().forEach(keyToFilter -> {
                Object valueToFilter = filter.getMatch().get(keyToFilter).toString().toLowerCase();
                boolQueryBuilder.filter(QueryBuilders.termQuery(keyToFilter, valueToFilter));
            });
        }
    }

    private SearchSourceBuilder createPagination(SearchQueryDTO searchQuery, BoolQueryBuilder boolQueryBuilder) {
        return SearchSourceBuilder.searchSource()
                .from(searchQuery.getPage() * searchQuery.getSize())
                .size(searchQuery.getSize())
                .query(boolQueryBuilder);
    }

    private Map<String, Object> convertProductToMap(Product product) {
        try {
            String json = objectMapper.writeValueAsString(product);
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>(){});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

}
