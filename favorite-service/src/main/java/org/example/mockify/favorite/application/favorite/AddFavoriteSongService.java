package org.example.mockify.favorite.application.favorite;

import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.domain.favorite.FavoriteList;
import org.example.mockify.favorite.domain.favorite.FavoriteListRepository;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.shared.domain.DomainException;
import org.example.mockify.favorite.domain.song.SongRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddFavoriteSongService implements AddFavoriteSongUseCase {

    private final FavoriteListRepository favoriteListRepository;
    private final SongRepository songRepository;

    @Override
    public void addFavorite(AddFavoriteSongCommand command) {
        SongId songId = SongId.from(command.songId());
        songRepository.findById(songId)
                .orElseThrow(() -> new DomainException("song-not-found"));

        FavoriteList favoriteList = favoriteListRepository.findOrCreateFor(command.accountId());
        favoriteList.addFavorite(songId);
        favoriteListRepository.save(favoriteList);
    }
}
