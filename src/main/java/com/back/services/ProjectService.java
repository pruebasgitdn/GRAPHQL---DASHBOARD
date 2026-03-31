package com.back.services;

import com.back.entities.Project;
import com.back.entities.dto.CreateProjectInput;

import java.util.UUID;

public interface ProjectService {

    Project createProject (CreateProjectInput projectInput, UUID workspace);
}
