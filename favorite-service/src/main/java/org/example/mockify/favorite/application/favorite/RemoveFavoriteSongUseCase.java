package org.example.mockify.favorite.application.favorite;

import java.util.UUID;

public interface RemoveFavoriteSongUseCase {

    void removeFavorite(UUID accountId, UUID songId);
}
