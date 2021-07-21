package br.com.dmeireles.springelasticsearch.controller.dto.search;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RangeFilterDTO {

    private Double lte;
    private Double gte;
    private Double lt;
    private Double gt;

}
