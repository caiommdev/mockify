package org.example.mockify.playlist.application.playlist;

import org.example.mockify.playlist.domain.playlist.Playlist;
import org.example.mockify.playlist.domain.playlist.PlaylistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreatePlaylistServiceTest {

    @Mock
    private PlaylistRepository playlistRepository;

    @InjectMocks
    private CreatePlaylistService createPlaylistService;

    @Test
    void should_create_and_save_playlist() {
        UUID accountId = UUID.randomUUID();
        CreatePlaylistCommand command = new CreatePlaylistCommand(accountId, "Road Trip");
        when(playlistRepository.save(any(Playlist.class))).thenAnswer(inv -> inv.getArgument(0));

        String id = createPlaylistService.createPlaylist(command);

        ArgumentCaptor<Playlist> captor = ArgumentCaptor.forClass(Playlist.class);
        verify(playlistRepository).save(captor.capture());
        Playlist saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Road Trip");
        assertThat(saved.getAccountId()).isEqualTo(accountId);
        assertThat(id).isNotNull();
    }
}
