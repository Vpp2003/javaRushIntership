package com.game.controller;

import com.game.entity.Player;
import com.game.entity.Profession;
import com.game.entity.Race;
import com.game.service.PlayerServiceImpl;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest")
public class PlayerController {

    private final PlayerServiceImpl playerServiceImpl;

    public PlayerController(PlayerServiceImpl playerServiceImpl) {
        this.playerServiceImpl = playerServiceImpl;
    }

    @GetMapping("/players")
    public List<Player> getPlayersList(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel,
            @RequestParam(required = false) PlayerOrder order,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize) {

        int skip = (pageNumber == null ? 0 : pageNumber) * (pageSize = (pageSize == null ? 3 : pageSize));

        List<Player> players = playerServiceImpl.getAllPlayers(Sort.by(order == null ? "id" : order.getFieldName()));

        return players.stream()
                .filter(player -> player.isPlayerFit(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel))
                .skip(skip)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    @GetMapping("/players/count")
    public Integer getPlayersCount(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Race race,
            @RequestParam(required = false) Profession profession,
            @RequestParam(required = false) Long after,
            @RequestParam(required = false) Long before,
            @RequestParam(required = false) Boolean banned,
            @RequestParam(required = false) Integer minExperience,
            @RequestParam(required = false) Integer maxExperience,
            @RequestParam(required = false) Integer minLevel,
            @RequestParam(required = false) Integer maxLevel
    ) {

        List<Player> players = playerServiceImpl.getAllPlayers(Sort.by("id"));
        return players.stream()
                .filter(player -> player.isPlayerFit(name, title, race, profession, after, before, banned, minExperience, maxExperience, minLevel, maxLevel))
                .collect(Collectors.toList()).size();
    }

    @PostMapping("/players")
    public Player createPlayer(@Validated @RequestBody Player player) {
        return playerServiceImpl.save(player);
    }

    @GetMapping("/players/{id}")
    public Player getPlayer(@PathVariable Long id) {
        return playerServiceImpl.getPlayerById(id);
    }

    @PostMapping("/players/{id}")
    public Player updatePlayer(@Validated @RequestBody Player player,
                               @PathVariable Long id) {
        return playerServiceImpl.update(player, id);
    }

    @DeleteMapping("/players/{id}")
    public void deletePlayer(@PathVariable Long id) {
        playerServiceImpl.deleteById(id);
    }
}
