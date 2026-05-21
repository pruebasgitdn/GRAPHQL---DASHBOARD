package com.back.services;

import java.util.UUID;

public interface WspaceInvitationService {

    String inviteUserToWspace(UUID workspaceId,String email);

    String confirmInvitation(String token);
}
