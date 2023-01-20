package com.game.service;

import com.game.entity.Player;
import org.springframework.data.domain.Sort;
import java.util.List;


public interface PlayerService {

    List<Player> getAllPlayers(Sort sort);

    Player getPlayerById(Long id);

    void deleteById(Long id);

    Player save(Player player);

    Player update(Player player, Long id);
}
