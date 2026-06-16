package org.example.mockify.playlist.controllers.playlist;

import tools.jackson.databind.ObjectMapper;
import org.example.mockify.playlist.application.playlist.*;
import org.example.mockify.playlist.controllers.GlobalExceptionHandler;
import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlaylistController.class)
@Import(GlobalExceptionHandler.class)
class PlaylistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreatePlaylistUseCase createPlaylistUseCase;

    @MockitoBean
    private AddSongToPlaylistUseCase addSongToPlaylistUseCase;

    @MockitoBean
    private RemoveSongFromPlaylistUseCase removeSongFromPlaylistUseCase;

    @MockitoBean
    private DeletePlaylistUseCase deletePlaylistUseCase;

    @MockitoBean
    private GetPlaylistsUseCase getPlaylistsUseCase;

    @Test
    void should_return_201_on_playlist_creation() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        when(createPlaylistUseCase.createPlaylist(any())).thenReturn(playlistId.toString());

        mockMvc.perform(post("/api/v1/accounts/{accountId}/playlists", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("name", "Road Trip"))))
                .andExpect(status().isCreated());
    }

    @Test
    void should_return_200_on_get_playlists() throws Exception {
        UUID accountId = UUID.randomUUID();
        Playlist playlist = new Playlist(accountId, "Chill");
        when(getPlaylistsUseCase.getByAccountId(accountId)).thenReturn(List.of(playlist));

        mockMvc.perform(get("/api/v1/accounts/{accountId}/playlists", accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chill"));
    }

    @Test
    void should_return_200_on_add_song() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        doNothing().when(addSongToPlaylistUseCase).addSong(any());

        mockMvc.perform(post("/api/v1/accounts/{accountId}/playlists/{playlistId}/songs", accountId, playlistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("songId", UUID.randomUUID(), "position", 1))))
                .andExpect(status().isOk());
    }

    @Test
    void should_return_422_when_song_already_in_playlist() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        doThrow(new DomainException("song-already-in-playlist"))
                .when(addSongToPlaylistUseCase).addSong(any());

        mockMvc.perform(post("/api/v1/accounts/{accountId}/playlists/{playlistId}/songs", accountId, playlistId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("songId", UUID.randomUUID(), "position", 1))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.violations[0]").value("song-already-in-playlist"));
    }

    @Test
    void should_return_204_on_remove_song() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        UUID songId = UUID.randomUUID();
        doNothing().when(removeSongFromPlaylistUseCase).removeSong(any(), any());

        mockMvc.perform(delete("/api/v1/accounts/{accountId}/playlists/{playlistId}/songs/{songId}",
                        accountId, playlistId, songId))
                .andExpect(status().isNoContent());
    }

    @Test
    void should_return_204_on_delete_playlist() throws Exception {
        UUID accountId = UUID.randomUUID();
        UUID playlistId = UUID.randomUUID();
        doNothing().when(deletePlaylistUseCase).deletePlaylist(any());

        mockMvc.perform(delete("/api/v1/accounts/{accountId}/playlists/{playlistId}", accountId, playlistId))
                .andExpect(status().isNoContent());
    }
}
