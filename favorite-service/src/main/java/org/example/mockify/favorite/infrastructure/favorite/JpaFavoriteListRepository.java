package org.example.mockify.favorite.infrastructure.favorite;

import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.domain.favorite.FavoriteList;
import org.example.mockify.favorite.domain.favorite.FavoriteListRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaFavoriteListRepository implements FavoriteListRepository {

    private final FavoriteListSpringDataRepository springData;

    @Override
    public FavoriteList save(FavoriteList favoriteList) {
        return springData.save(FavoriteListJpaEntity.fromDomain(favoriteList)).toDomain();
    }

    @Override
    public Optional<FavoriteList> findByAccountId(UUID accountId) {
        return springData.findByAccountId(accountId).map(FavoriteListJpaEntity::toDomain);
    }

    @Override
    public FavoriteList findOrCreateFor(UUID accountId) {
        return findByAccountId(accountId).orElseGet(() -> {
            FavoriteList newList = new FavoriteList(accountId);
            return save(newList);
        });
    }
}
