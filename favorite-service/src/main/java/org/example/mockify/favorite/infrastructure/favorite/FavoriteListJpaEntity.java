package org.example.mockify.favorite.infrastructure.favorite;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.mockify.favorite.domain.favorite.FavoriteList;
import org.example.mockify.favorite.domain.favorite.FavoriteListId;
import org.example.mockify.favorite.domain.favorite.FavoriteSong;
import org.example.mockify.favorite.domain.song.SongId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "favorite_lists")
@Getter
@NoArgsConstructor
public class FavoriteListJpaEntity {

    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "favorite_songs", joinColumns = @JoinColumn(name = "list_id"))
    private List<FavoriteSongJpaElement> favoriteSongs = new ArrayList<>();

    public static FavoriteListJpaEntity fromDomain(FavoriteList domain) {
        FavoriteListJpaEntity entity = new FavoriteListJpaEntity();
        entity.id = domain.getId().value();
        entity.accountId = domain.getAccountId();
        entity.favoriteSongs = domain.getFavorites().stream()
                .map(f -> new FavoriteSongJpaElement(f.songId().value(), f.addedAt()))
                .toList();
        return entity;
    }

    public FavoriteList toDomain() {
        List<FavoriteSong> favorites = favoriteSongs.stream()
                .map(e -> new FavoriteSong(SongId.from(e.getSongId()), e.getAddedAt()))
                .toList();
        return new FavoriteList(FavoriteListId.from(id), accountId, favorites);
    }
}
