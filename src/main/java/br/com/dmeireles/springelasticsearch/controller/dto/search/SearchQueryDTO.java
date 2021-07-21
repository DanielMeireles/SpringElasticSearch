package br.com.dmeireles.springelasticsearch.controller.dto.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchQueryDTO {

    private String query;
    private FilterRequestDTO filter;
    private Integer page;
    private Integer size;

}
