package org.example.mockify.favorite.domain.favorite;

import java.util.Optional;
import java.util.UUID;

public interface FavoriteListRepository {

    FavoriteList save(FavoriteList favoriteList);

    Optional<FavoriteList> findByAccountId(UUID accountId);

    FavoriteList findOrCreateFor(UUID accountId);
}
