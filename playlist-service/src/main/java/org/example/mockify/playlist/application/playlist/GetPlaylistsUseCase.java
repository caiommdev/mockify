package org.example.mockify.playlist.application.playlist;

import org.example.mockify.playlist.domain.playlist.Playlist;

import java.util.List;
import java.util.UUID;

public interface GetPlaylistsUseCase {

    List<Playlist> getByAccountId(UUID accountId);
}
