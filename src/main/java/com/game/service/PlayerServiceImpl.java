package com.game.service;

import com.game.entity.Player;
import com.game.repository.PlayerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public List<Player> getAllPlayers(Sort sort) {
        return playerRepository.findAll(sort);
    }

    public Player getPlayerById(Long id) {
        if (id <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        try {
            playerRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return playerRepository.findById(id).get();
    }

    public void deleteById(Long id) {
        playerRepository.delete(getPlayerById(id));
    }


    public Player save(Player player) {
        if (player.getName() == null || player.getName().length() > 12
                || player.getTitle() == null || player.getTitle().length() > 30
                || player.getExperience() == null || player.getExperience() < 0
                || player.getExperience() > 10_000_000
                || player.getBirthday() == null || player.getBirthday().getTime() < 0
                || player.getBirthday().after(new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTime())
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return playerRepository.save(player.fillRestFields());
    }

    public Player update(Player player, Long id) {
        Player p = getPlayerById(id);

        if ((player.getBirthday() != null && player.getBirthday().getTime() < 0)
                || (player.getName() != null && player.getName().length() > 12)
                || (player.getTitle() != null && player.getTitle().length() > 30)
                || (player.getExperience() != null && player.getExperience() < 0)
                || (player.getExperience() != null && player.getExperience() > 10_000_000)
                || (player.getBirthday() != null && player.getBirthday().after(new GregorianCalendar(3000, Calendar.DECEMBER, 31).getTime()))
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        if (player.getName() != null) p.setName(player.getName());
        if (player.getTitle() != null) p.setTitle(player.getTitle());
        if (player.getRace() != null) p.setRace(player.getRace());
        if (player.getProfession() != null) p.setProfession(player.getProfession());
        if (player.getBirthday() != null) p.setBirthday(player.getBirthday());
        if (player.getBanned() != null) p.setBanned(player.getBanned());
        if (player.getExperience() != null && player.getExperience() >= 0) p.setExperience(player.getExperience());

        return playerRepository.save(p.fillRestFields());
    }
}