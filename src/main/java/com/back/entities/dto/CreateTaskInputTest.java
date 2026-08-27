package com.back.entities.dto;


import com.back.enums.TaskLabelType;
import com.back.enums.TaskPriority;
import com.back.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CreateTaskInputTest {

    @NotNull(message = "Indica el titulo de la tarea")
    @Size(max = 70,message = "Máximo 70 caracteres")
    private String title;

    @NotNull(message = "La tarea debe ir asociado a un proyecto")
    private long projectId;

    @NotNull(message = "La tarea debe ir asociado a un espacio de trabajo")
    private UUID workspaceId;

    @NotNull(message = "Ingresa fecha de entrega")
    private LocalDate dueDate;

    @NotNull(message = "Ingresa la descripcion de la tarea")
    @Size(max = 200,message = "Máximo 200 caracteres")
    private String description;

    @NotNull(message = "Ingresa el status de la tarea")
    private TaskStatus status;

    @NotNull(message = "Ingresa la prioridad de la tarea")
    private TaskPriority priority;

//    private LocalDateTime completedAt;
//
//    private Boolean isArchived;

    @NotNull(message = "Ingresa el tiempo estimado para llevar a cabo la tarea")
    private Double estimatedHours;

//    private Double actualHours;

    private List<TaskLabelType> labels;

    //private List<CreateSubTask> subTasks;


}
