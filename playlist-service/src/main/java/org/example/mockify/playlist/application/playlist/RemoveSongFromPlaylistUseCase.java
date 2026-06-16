package org.example.mockify.playlist.application.playlist;

import java.util.UUID;

public interface RemoveSongFromPlaylistUseCase {

    void removeSong(UUID playlistId, UUID songId);
}
