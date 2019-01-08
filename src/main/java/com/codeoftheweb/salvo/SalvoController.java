package com.codeoftheweb.salvo;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

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
    public Map<String, Object> getAll(Authentication authentication) {

        Map<String, Object> map = new HashMap<>();
        if(authentication != null)
            map.put("currentPlayer", playerRepository.findByEmail(authentication.getName()));
        map.put("games", gameRepository.findAll());
        return map;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createNewGame(Authentication authentication) {

        if(authentication == null){
            return new ResponseEntity<>(makeMap("error", "No logged in player to create game")
                    , HttpStatus.UNAUTHORIZED);
        }else{
            Game game = gameRepository.save(new Game());
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, currentPlayer(authentication)));

            return new ResponseEntity<>(makeMap("newGamePlayer", gamePlayer.getId()), HttpStatus.CREATED);
        }
    }

    @RequestMapping(path = "/players", method = RequestMethod.GET)
    public ResponseEntity<Map<String,Object>> getPlayer(Authentication authentication) {

        if (authentication != null){
            return  new ResponseEntity<Map<String, Object>>(makeMap("succed", playerRepository.findByEmail(authentication.getName())), HttpStatus.OK);
        } else {
            return new ResponseEntity<Map<String, Object>>(makeMap("error","log in"), HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String,Object>> createPlayer(String email, String password) {

        if (email.isEmpty()){
            return new ResponseEntity<Map<String, Object>>(makeMap("error","Type in a name"), HttpStatus.FORBIDDEN);
        } else if (!playerRepository.findByEmail(email).isEmpty()){
            return new ResponseEntity<Map<String, Object>>(makeMap("error","Name already in use"), HttpStatus.UNAUTHORIZED);
        } else if (password.isEmpty()) {
            return new ResponseEntity<Map<String, Object>>(makeMap("error","use a valid password"), HttpStatus.UNAUTHORIZED);
        } else {
            Player newPlayer = new Player(email,password);
            playerRepository.save(newPlayer);
            return  new ResponseEntity<Map<String, Object>>(makeMap("succed", "player created"), HttpStatus.CREATED);
        }
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
        return scoreRepository.findAll().stream().map(score -> makeScoreDTO(score)).collect(toList());
    }

    @RequestMapping(value = "/game_view/{id}", method = RequestMethod.GET)
    public Map<String,Object> getURLById (@PathVariable Long id,
                                          Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gamePlayer = gamePlayerRepository.findOne(id);
        Player user = playerRepository.findByEmail(authentication.getName()).get(0);

        if (gamePlayer.getPlayer().getId() == user.getId()) {
        GamePlayer enemy = getEnemyGamePlayer(gamePlayer);

        dto.put("game", makeGameDTO(gamePlayer.getGame()));
        dto.put("userInfo", makeGamePlayerDTO(gamePlayer));
        dto.put("userShips", gamePlayer.getShips()
                .stream().map(ship -> makeShipDTO(ship))
                .collect(toList()));
        dto.put("userSalvos", gamePlayer.getSalvos()
                .stream().map(salvo -> makeSalvoDTO(salvo))
                .collect(toList()));
        dto.put("userScore", gamePlayer.getScore());
        dto.put("enemySalvos", enemy.getSalvos()
                .stream()
                .map(salvo -> makeSalvoDTO(salvo))
                .collect(toList()));
        }else{
            dto.put("error", "not your game");
        }
        return dto;
    }

    @RequestMapping(value = "/leader-board")
    public List<Object> getAllLeads() {
        List<Object> list = new ArrayList<>();
        for (Player player : playerRepository.findAll()) {
            Map<String,Object> map = new HashMap<>();
            map.put("email", player.getEmail());
            double total = 0.0;
            int wins = 0;
            int ties = 0;
            int lost = 0;

            for (Score score : player.getScores()) {
                total += score.getScore();
                if (score.getScore() == 1.0) {
                    wins += 1;
                } else if (score.getScore() == 0.5) {
                    ties += 1;
                } else if (score.getScore() == 0.0) {
                    lost += 1;
                } else {

                }
            }
            map.put("total", total);
            map.put("wins", wins);
            map.put("ties", ties);
            map.put("lost", lost);


            list.add(map);
        }
        return list;
    }




    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("creationDate", game.getCreationDate().toString());
        dto.put("gamePlayer", game.getGamePlayers().stream()
                .map(gamePlayer -> makeGamePlayerDTO(gamePlayer)).collect(toList()));
        return dto;
    }

    private Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gamePlayer.getId());
        dto.put("playerId", gamePlayer.getPlayer().getId());
        dto.put("email", gamePlayer.getPlayer().getEmail());
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

    private GamePlayer getEnemyGamePlayer(GamePlayer gamePlayer) {
        return gamePlayer.getGame().getGamePlayers()
                .stream()
                .filter(gp -> gp.getId() != gamePlayer.getId())
                .findFirst()
                .orElse(null);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private Map<String, Object> auPlayerDTO (Authentication authentication){
        Map<String, Object> dto = new LinkedHashMap<String, Object>();

        dto.put("id", currentPlayer(authentication).getId());
        dto.put("name", currentPlayer(authentication).getEmail());

        return dto;
    }

    private Player currentPlayer (Authentication authentication){
        return (Player) playerRepository.findByEmail(authentication.getName());
    }



}

