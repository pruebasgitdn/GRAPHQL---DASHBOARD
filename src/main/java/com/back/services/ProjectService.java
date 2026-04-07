package com.back.services;

import com.back.entities.Project;
import com.back.entities.dto.CreateProjectInput;
import com.back.entities.dto.EditProjectInput;
import com.back.entities.dto.ProjectResponse;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    ProjectResponse createProject (CreateProjectInput projectInput);

    List<ProjectResponse> allProjects();

    ProjectResponse getProject (Long id);

    List<ProjectResponse> findAllByWorkspaceId (UUID id);

    ProjectResponse editProject(Long projectId,EditProjectInput editProjectInput);

    Boolean deleteProject(Long projectId);

}
