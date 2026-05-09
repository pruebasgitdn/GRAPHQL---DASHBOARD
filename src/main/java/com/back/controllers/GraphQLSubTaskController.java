package com.back.controllers;


import com.back.entities.dto.AuthResponse;
import com.back.entities.dto.CreateSubTask;
import com.back.entities.dto.LoginInput;
import com.back.entities.dto.SubTaskResponse;
import com.back.services.SubTaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GraphQLSubTaskController {

    private final SubTaskService subTaskService;


    @PreAuthorize("isAuthenticated()")
    @MutationMapping(name = "createSubTasks")
    public List<SubTaskResponse> createSubTasks(@Argument List<CreateSubTask> inputs,
                                                @Argument Long taskId
    ) {
        return subTaskService.createSubTasks(taskId,inputs);
    }


}
