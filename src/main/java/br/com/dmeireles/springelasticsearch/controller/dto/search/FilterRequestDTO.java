package br.com.dmeireles.springelasticsearch.controller.dto.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class FilterRequestDTO {

    private Map<String, Object> match;
    private Map<String, RangeFilterDTO> range;

}
