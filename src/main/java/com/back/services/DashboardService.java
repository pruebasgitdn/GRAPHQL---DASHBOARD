package com.back.services;

import com.back.entities.dto.DashboardResponse;

import java.util.UUID;

public interface DashboardService {

    DashboardResponse dashboardStatsByWorkspace(UUID workspaceId);

}
