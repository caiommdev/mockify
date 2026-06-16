package org.example.mockify.favorite.application.favorite;

import org.example.mockify.favorite.domain.favorite.FavoriteList;
import org.example.mockify.favorite.domain.favorite.FavoriteListRepository;
import org.example.mockify.favorite.domain.song.Duration;
import org.example.mockify.favorite.domain.song.Genre;
import org.example.mockify.favorite.domain.song.Song;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.favorite.domain.song.SongRepository;
import org.example.mockify.shared.domain.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AddFavoriteSongServiceTest {

    @Mock
    private FavoriteListRepository favoriteListRepository;

    @Mock
    private SongRepository songRepository;

    private AddFavoriteSongService service;
    private final UUID accountId = UUID.randomUUID();
    private final UUID songUuid = UUID.randomUUID();
    private final SongId songId = SongId.from(songUuid);

    @BeforeEach
    void setUp() {
        service = new AddFavoriteSongService(favoriteListRepository, songRepository);
    }

    @Test
    void should_add_song_to_favorites() {
        Song song = new Song(songId, "Test Song", "Artist", new Duration(200000), Genre.POP);
        FavoriteList favoriteList = new FavoriteList(accountId);

        given(songRepository.findById(songId)).willReturn(Optional.of(song));
        given(favoriteListRepository.findOrCreateFor(accountId)).willReturn(favoriteList);
        given(favoriteListRepository.save(any(FavoriteList.class))).willAnswer(inv -> inv.getArgument(0));

        service.addFavorite(new AddFavoriteSongCommand(accountId, songUuid));

        verify(favoriteListRepository).save(any(FavoriteList.class));
    }

    @Test
    void should_throw_when_song_not_found() {
        given(songRepository.findById(any(SongId.class))).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.addFavorite(new AddFavoriteSongCommand(accountId, songUuid)))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("song-not-found");
    }
}
