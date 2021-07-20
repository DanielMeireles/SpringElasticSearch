package br.com.dmeireles.springelasticsearch.config.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class FormErrorDTO {

    @Getter
    private String field;
    @Getter
    private String error;

}
