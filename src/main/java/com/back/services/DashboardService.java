package com.back.services;

import com.back.entities.Task;
import com.back.entities.dto.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface DashboardService {

    DashboardResponse dashboardStatsByWorkspace(UUID workspaceId);

    String dashboardTest(UUID workspaceId);

    List<Task> dashboardTestDos(UUID workspaceId);

    List<Task>  dashboardTestTres(UUID workspaceId);

    List<TaskTrendCL>  perrota(UUID workspaceId);

    List<ProjectResponse>  donomar(UUID workspaceId);


    String current_database();
    String inet_server_addr();
    Long nancy();


}
