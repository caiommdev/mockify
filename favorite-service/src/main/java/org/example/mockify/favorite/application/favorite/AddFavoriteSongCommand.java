package org.example.mockify.favorite.application.favorite;

import java.util.UUID;

public record AddFavoriteSongCommand(UUID accountId, UUID songId) {}
