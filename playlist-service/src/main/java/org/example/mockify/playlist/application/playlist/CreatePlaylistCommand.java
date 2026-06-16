package org.example.mockify.playlist.application.playlist;

import java.util.UUID;

public record CreatePlaylistCommand(UUID accountId, String name) {}
