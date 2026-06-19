package com.back.controllers;
import com.back.entities.dto.CommentResponse;
import com.back.entities.dto.DashboardResponse;
import com.back.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class DashboardGraphQLController {


    private final DashboardService dashboardService;

    @QueryMapping(name = "dashboardWorkspace")
    public DashboardResponse dashboardWorkspace(@Argument(name = "workspaceId") UUID workspaceId
    ){
        return dashboardService.dashboardStatsByWorkspace(workspaceId);
    }


}
