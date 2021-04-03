package com.diary_online.diary_online.model.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@NoArgsConstructor
public class SuccessDTO {

    private String message;

    public SuccessDTO(String msg){
        this.message = msg;
    }
}
