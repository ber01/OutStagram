package com.outstagram.boot.favorite;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class FavoriteController {

    @GetMapping("/api/{articleId}")
    public Flux<String> getAllFavoriteMember(@PathVariable String articleId) {
        return Flux.just();
    }

    @PostMapping("/api/{articleId}/favorite")
    public void favoriteArticle(@PathVariable String articleId) {

    }
}
