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

    /*@RequestMapping("/bla")
    private List<Object> ha () {
        return playerRepository.findAll().stream().map(player -> makePlayerDTO(player)).collect(toList());
    }*/


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

        if(authentication != null){
            Game newGame = new Game();
            gameRepository.save(newGame);
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(newGame, currentPlayer(authentication)));

            return new ResponseEntity<>(makeMap("newGamePlayer", gamePlayer.getId()), HttpStatus.CREATED);

        }else{
            return new ResponseEntity<>(makeMap("error", "Can't create game"), HttpStatus.UNAUTHORIZED);
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

    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getGameJoin (@PathVariable Long gameId, Authentication authentication){
        Game game = gameRepository.findOne(gameId);
        Player player = currentPlayer(authentication);

        if(authentication == null){
            return new ResponseEntity<>(makeMap("error", "Log in to join a game")
                    , HttpStatus.UNAUTHORIZED);
        }else if(game == null){
            return new ResponseEntity<>(makeMap("error", "No existing game")
                    , HttpStatus.FORBIDDEN);
        } else if (game.getGamePlayers().size() == 2){
            return new ResponseEntity<>(makeMap("error", "Game is full")
                    , HttpStatus.FORBIDDEN);
        }else{
            GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(game, player));
            return new ResponseEntity<>(makeMap("gamePlayerID", gamePlayer.getId())
                    , HttpStatus.CREATED);
        }
    }

    @RequestMapping(path="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placingShips(@PathVariable long gamePlayerId,
                                                            @RequestBody List<Ship> ships,
                                                            Authentication authentication) {
        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);

        if(authentication == null){
            return new ResponseEntity<>(makeMap("error", "No user is logged in")
                    , HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer == null){
            return new ResponseEntity<>(makeMap("error", "This user does not exist")
                    , HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer.getPlayer() != currentPlayer(authentication)){
            return new ResponseEntity<>(makeMap("error", "This is not your game")
                    , HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer.getShips().size() == 5) {
            return new ResponseEntity<>(makeMap("error", "Ships have been placed already")
                    , HttpStatus.FORBIDDEN);

        } else if (ships.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Missing Ships")
                    , HttpStatus.FORBIDDEN);

        }else{
            for (Ship ship : ships) {
                ship.setGamePlayer(gamePlayer);
                shipRepository.save(ship);
            }

            return new ResponseEntity<>(makeMap("success", "Ships are created")
                    , HttpStatus.CREATED);
        }
    }

    @RequestMapping(path="/games/players/{gamePlayerId}/salvos", method=RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> placingSalvos(@PathVariable long gamePlayerId,
                                                             @RequestBody Salvo salvo,
                                                             Authentication authentication){

        GamePlayer gamePlayer = gamePlayerRepository.findOne(gamePlayerId);

        if(authentication == null){
            return new ResponseEntity<>(makeMap("error", "No user is logged in")
                    , HttpStatus.UNAUTHORIZED);


        }else if(gamePlayer == null){
            return new ResponseEntity<>(makeMap("error", "The user does not exist")
                    , HttpStatus.UNAUTHORIZED);


        }else if(gamePlayer.getPlayer() != currentPlayer(authentication)) {
            return new ResponseEntity<>(makeMap("error", "This is not your game")
                    , HttpStatus.UNAUTHORIZED);

        }else if(gamePlayer.getSalvos().size() > 5) {
            return new ResponseEntity<>(makeMap("error", "Salvos have been placed already")
                    , HttpStatus.FORBIDDEN);

        }else if(salvo.getTurn() <= getLastTurn(gamePlayer)) {
            return new ResponseEntity<>(makeMap("error", "You already had your turn")
                    , HttpStatus.FORBIDDEN);

        }else{
            salvo.setTurn(getLastTurn(gamePlayer) + 1);
            salvo.setGamePlayer(gamePlayer);
            salvoRepository.save(salvo);

            return new ResponseEntity<>(makeMap("success", "Salvos are created")
                    , HttpStatus.CREATED);
        }
    }

    private int getLastTurn (GamePlayer gamePlayer) {
        int lastTurn = 0;
        for (Salvo salvo : gamePlayer.getSalvos()) {
            if (lastTurn < salvo.getTurn())
                lastTurn = salvo.getTurn();
        }
        return lastTurn;
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

        if (gamePlayer.getPlayer().getId() != user.getId()) {
            dto.put("error", "not your game");
        }else{
            GamePlayer opponent = opponentPlayer(gamePlayer);

            dto.put("currentPlayer", playerRepository.findByEmail(authentication.getName()));
            dto.put("game", makeGameDTO(gamePlayer.getGame()));
            dto.put("userInfo", makeGamePlayerDTO(gamePlayer));
            dto.put("userShips", gamePlayer.getShips()
                    .stream().map(ship -> makeShipDTO(ship))
                    .collect(toList()));
            dto.put("userSalvos", gamePlayer.getSalvos()
                    .stream().map(salvo -> makeSalvoDTO(salvo))
                    .collect(toList()));
            dto.put("userScore", gamePlayer.getScore());


            if(opponent != null) {
                Map<String, Integer> userFleet = allShipsFleet();
                Map<String, Integer> opponentFleet = allShipsFleet();
                dto.put("hits", allHits(gamePlayer, userFleet, opponentFleet));
                dto.put("opponentFleet", opponentFleet);
                dto.put("enemySalvos", opponent.getSalvos()
                        .stream()
                        .map(salvo -> makeSalvoDTO(salvo))
                        .collect(toList()));
            }
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
        dto.put("gamePlayerId", gamePlayer.getId());
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

    private GamePlayer opponentPlayer(GamePlayer gamePlayer) {
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


    private Player currentPlayer (Authentication authentication){
        return playerRepository.findByEmail(authentication.getName()).get(0);
    }


    private Map<String, Integer> allShipsFleet(){
        Map<String, Integer> fleet = new HashMap<String, Integer>();
        fleet.put("patrol", 2);
        fleet.put("destroyer", 3);
        fleet.put("submarine", 3);
        fleet.put("battleship", 4);
        fleet.put("carrier", 5);

        fleet.put("Total", 5);

        return fleet;
    }

    private Map<String, Object> salvosOnShips (Set<Ship> ships, Salvo salvo, Map<String, Integer> fleet){
        Map<String, Object> dto = new HashMap<String, Object>();

        List<Object> shipsSalvo = ships.stream()
                .filter(ship -> fleet.get(ship.getType()) != 0)
                .map(ship -> ShipSalvoLocations(ship, salvo, fleet))
                .filter(shipAcrossSalvo -> !shipAcrossSalvo.isEmpty())
                .collect(toList());

        if(!shipsSalvo.isEmpty()){
            dto.put("turn", salvo.getTurn());
            dto.put("hit", shipsSalvo);

        }else{
            dto.put("hit", null);
        }

        return dto;
    }

    private Map<String, Object> allHits (GamePlayer gamePlayer, Map<String, Integer> userFleet, Map<String, Integer> opponentFleet){
        Map<String, Object> dto = new HashMap<String, Object>();


        Set<Ship> userShip = gamePlayer.getShips();
        Set<Salvo> opponentsSalvo = opponentPlayer(gamePlayer).getSalvos();

        Set<Salvo> userSalvo = gamePlayer.getSalvos();
        Set<Ship> opponentShip = opponentPlayer(gamePlayer).getShips();


        dto.put("userHits", userSalvo.stream()
                .sorted((s1, s2) -> s1.getTurn() - s2.getTurn())
                .map(salvo -> salvosOnShips(opponentShip, salvo, opponentFleet))
                .collect(toList()));

        dto.put("receivedHits", opponentsSalvo.stream()
                .sorted((s1, s2) -> s1.getTurn() - s2.getTurn())
                .map(salvo -> salvosOnShips(userShip, salvo, userFleet))
                .collect(toList()));

        return dto;
    }

    private Map<String, Object> ShipSalvoLocations (Ship ship, Salvo salvo, Map<String, Integer> fleet){
        Map<String, Object> dto = new HashMap<String, Object>();

        List<String> salvoLocation = salvo.getLocations();

        List<String> shipLocation = ship.getLocations();
        List<String> shipsHit = shipLocation.stream()
                .filter(locations -> salvoLocation.contains(locations))
                .collect(toList());

        if(shipsHit.size() != 0){
            dto.put("type", ship.getType());
            dto.put("location", shipsHit);

            fleet.put(ship.getType(), fleet.get(ship.getType()) - shipsHit.size());

            if(fleet.get(ship.getType()) == 0){
                fleet.put("fleets", fleet.get("fleets") - 1);
            }
        }
        return dto;
    }


}