package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;

    @RequestMapping("/games")
    public List <Object> getAllGames() {
        return gameRepository.findAll()
                .stream()
                .map(game -> makeGameDTO(game)).collect(Collectors.toList());
    }

    @RequestMapping("/players")
    public List <Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @RequestMapping("/gamePlayers")
    public List <GamePlayer> getAllGamePlayers() {
        return gamePlayerRepository.findAll();
    }

    @RequestMapping("/ships")
    public List<Ship> getAllShips() {
        return shipRepository.findAll();
    }

    @RequestMapping("/salvos")
    public List<Salvo> getAllSalvos() {
        return salvoRepository.findAll();
    }

    @RequestMapping("/scores")
    public List<Object> getAllScores() {
        return scoreRepository.findAll().stream().map(score -> makeScoreDTO(score)).collect(Collectors.toList());
    }

    @RequestMapping(value = "/game_view/{id}")
    public Map<String,Object> getURLById (@PathVariable Long id) {
        GamePlayer gamePlayer = gamePlayerRepository.findOne(id);
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("game", makeGameDTO(gamePlayer.getGame()));
        dto.put("userInfo", makeGamePlayerDTO(gamePlayer));
        dto.put("userShips", gamePlayer.getShips()
                .stream().map(ship -> makeShipDTO(ship))
                .collect(Collectors.toList()));
        dto.put("userSalvos", gamePlayer.getSalvos()
                .stream().map(salvo -> makeSalvoDTO(salvo))
                .collect(Collectors.toList()));
        dto.put("userScore", gamePlayer.getScore());
        return dto;
    }

    /*@RequestMapping(value = "/leaderboard")
    public Map<String,Object> getAll() {
        GamePlayer gamePlayer = (GamePlayer) gamePlayerRepository;
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("game", makeGameDTO(gameRepository.getGame()));
        dto.put("userInfo", makeGamePlayerDTO(gamePlayer));
        dto.put("userShips", gamePlayer.getShips()
                .stream().map(ship -> makeShipDTO(ship))
                .collect(Collectors.toList()));
        dto.put("userSalvos", gamePlayer.getSalvos()
                .stream().map(salvo -> makeSalvoDTO(salvo))
                .collect(Collectors.toList()));
        dto.put("userScore", gamePlayer.getScore());
        return dto;
    }*/



    /*create new request mapping like above, for a leader board and fetch that in your games.html*/

    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("creationDate", game.getCreationDate().toString());
        dto.put("gamePlayer", game.getGamePlayers().stream()
                .map(gamePlayer -> makeGamePlayerDTO(gamePlayer)).collect(Collectors.toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
        return dto;
    }

    private Map<String, Object> makePlayerDTO (Player player){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", player.getId());
        dto.put("email", player.getEmail());
        return dto;
    }

    private Map<String, Object> makeShipDTO (Ship ship){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", ship.getId());
        dto.put("type", ship.getType());
        dto.put("locations", ship.getLocations());
        return dto;
    }

    private Map<String, Object> makeSalvoDTO (Salvo salvo){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", salvo.getId());
        dto.put("turn", salvo.getTurn());
        dto.put("locations", salvo.getLocations());
        return dto;
    }

    private Map<String, Object> makeScoreDTO (Score score){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("player", score.getPlayer().getEmail());
        dto.put("score", score.getScore());
        return dto;
    }




}

