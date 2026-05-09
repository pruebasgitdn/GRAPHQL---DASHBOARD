package com.back.entities.dto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubTask {

    @NotNull(message = "Indica el titulo de la subtarea")
    private String title;

    @NotNull(message = "Indica el estado")
    private Boolean completed;

}
