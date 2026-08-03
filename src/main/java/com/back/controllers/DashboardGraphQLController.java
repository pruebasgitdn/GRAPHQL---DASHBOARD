package com.back.controllers;
import com.back.entities.Task;
import com.back.entities.dto.*;
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


    @QueryMapping(name = "dashboardTest")
    public String dashboardTest(@Argument(name = "workspaceId") UUID workspaceId
    ){
        return dashboardService.dashboardTest(workspaceId);
    }

    @QueryMapping(name = "dashboardTestDos")
    public List<Task> dashboardTestDos(@Argument(name = "workspaceId") UUID workspaceId
    ){
        return dashboardService.dashboardTestDos(workspaceId);
    }

    @QueryMapping(name = "dashboardTestTres")
    public List<Task> dashboardTestTres(@Argument(name = "workspaceId") UUID workspaceId
    ){
        return dashboardService.dashboardTestTres(workspaceId);
    }

    @QueryMapping(name = "perra")
    public List<TaskTrendCL> perra(@Argument(name = "workspaceId") UUID workspaceId
    ){
        return dashboardService.perrota(workspaceId);
    }

    @QueryMapping(name = "donomar")
    public List<ProjectResponse> donomar(@Argument(name = "workspaceId") UUID workspaceId
    ){
        return dashboardService.donomar(workspaceId);
    }

    @QueryMapping(name = "currentDb")
    public String currentDb(){
        return dashboardService.current_database();
    }


    @QueryMapping(name = "inet_server_addr")
    public String inet_server_addr(){
        return dashboardService.inet_server_addr();
    }

    @QueryMapping(name = "nancy")
    public Long nancy(){
        return dashboardService.nancy();
    }










}
