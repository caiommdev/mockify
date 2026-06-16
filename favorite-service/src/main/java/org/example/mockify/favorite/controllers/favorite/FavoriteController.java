package org.example.mockify.favorite.controllers.favorite;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.example.mockify.favorite.application.favorite.AddFavoriteSongCommand;
import org.example.mockify.favorite.application.favorite.AddFavoriteSongUseCase;
import org.example.mockify.favorite.application.favorite.RemoveFavoriteSongUseCase;
import org.example.mockify.favorite.controllers.favorite.response.SongResponse;
import org.example.mockify.favorite.domain.favorite.FavoriteList;
import org.example.mockify.favorite.domain.favorite.FavoriteListRepository;
import org.example.mockify.favorite.domain.song.SongRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts/{accountId}/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final AddFavoriteSongUseCase addFavoriteSongUseCase;
    private final RemoveFavoriteSongUseCase removeFavoriteSongUseCase;
    private final FavoriteListRepository favoriteListRepository;
    private final SongRepository songRepository;

    @GetMapping
    public List<SongResponse> getFavorites(@PathVariable UUID accountId) {
        return favoriteListRepository.findByAccountId(accountId)
                .map(FavoriteList::getFavorites)
                .orElse(List.of())
                .stream()
                .flatMap(f -> songRepository.findById(f.songId()).stream())
                .map(SongResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addFavorite(@PathVariable UUID accountId, @Valid @RequestBody AddFavoriteRequest request) {
        addFavoriteSongUseCase.addFavorite(new AddFavoriteSongCommand(accountId, request.songId()));
    }

    @DeleteMapping("/{songId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFavorite(@PathVariable UUID accountId, @PathVariable UUID songId) {
        removeFavoriteSongUseCase.removeFavorite(accountId, songId);
    }

    record AddFavoriteRequest(@NotNull UUID songId) {}
}
