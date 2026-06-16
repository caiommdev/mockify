package org.example.mockify.playlist.application.playlist;

import java.util.UUID;

public record AddSongToPlaylistCommand(UUID playlistId, UUID songId, int position) {}
