package com.back.services;

import com.back.entities.Project;
import com.back.entities.dto.CreateProjectInput;

import java.util.List;
import java.util.UUID;

public interface ProjectService {

    Project createProject (CreateProjectInput projectInput);

    List<Project> allProjects();

}
