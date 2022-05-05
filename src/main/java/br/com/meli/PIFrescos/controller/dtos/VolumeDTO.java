package br.com.meli.PIFrescos.controller.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO para o volume do produto em cm3
 * @author Ana Preis
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VolumeDTO {

    private String productName;
    private Float volume;
}
