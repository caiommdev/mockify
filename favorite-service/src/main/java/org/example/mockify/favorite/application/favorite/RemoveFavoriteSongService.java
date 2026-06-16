package org.example.mockify.favorite.application.favorite;

import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.domain.favorite.FavoriteList;
import org.example.mockify.favorite.domain.favorite.FavoriteListRepository;
import org.example.mockify.favorite.domain.song.SongId;
import org.example.mockify.shared.domain.DomainException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RemoveFavoriteSongService implements RemoveFavoriteSongUseCase {

    private final FavoriteListRepository favoriteListRepository;

    @Override
    public void removeFavorite(UUID accountId, UUID songId) {
        FavoriteList favoriteList = favoriteListRepository.findByAccountId(accountId)
                .orElseThrow(() -> new DomainException("favorite-list-not-found"));
        favoriteList.removeFavorite(SongId.from(songId));
        favoriteListRepository.save(favoriteList);
    }
}
