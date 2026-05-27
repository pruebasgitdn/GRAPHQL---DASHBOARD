package com.back.services;

import java.util.List;
import java.util.UUID;

public interface WspaceInvitationService {

    String inviteUserToWspace(UUID workspaceId,String email);

    String sendMultipleInvitesToWspace(UUID workspaceId, List<String> emails);

    String confirmInvitation(String token);
}
