package services;

import java.util.Map;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import DTO.PlayersData;
import GameStructure.GameComm;
import GameStructure.GameFactory;
import repositories.PlayersRegistered;
import repositories.PlayersRepository;

@Service
public class GameService {
    
    @Autowired
    private RedisTemplate<String, GameComm> redisTemplate;
    
    @Autowired
    private GameFactory gameFactory;
    
	 @Autowired
	 PlayersRepository repo;
	 
	 @Autowired
	 PlayersData insert;

    public ResponseEntity<String> startNewGame() {
        String gameID = UUID.randomUUID().toString();
        
        GameComm newGame = gameFactory.createGame(gameID);

        redisTemplate.opsForValue().set(gameID, newGame);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body("Game initialized with ID: " + gameID);
    }
    
    public ResponseEntity<String> updateFormat(String gameID, int format) {
        GameComm game = redisTemplate.opsForValue().get(gameID);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameID);
        }

        System.out.println("Before update: " + game.getGameDynamics().getFormat());
        game.getGameDynamics().setFormat(format);
        redisTemplate.opsForValue().set(gameID, game);
        game = redisTemplate.opsForValue().get(gameID);
        ResponseEntity<String> re = ResponseEntity.status(HttpStatus.ACCEPTED).body("format set to given value"+game.getGameDynamics().getFormat());
        System.out.println("After update: " + game.getGameDynamics().getFormat()+"  "+gameID);

        
        if (re.getStatusCode() == HttpStatus.OK) {
            redisTemplate.opsForValue().set(gameID, game);
        }

        return re;
    }
    
    public ResponseEntity<String> SinglesTeaming(String gameid, int teamID, int playerid){
    	
    	GameComm game = redisTemplate.opsForValue().get(gameid);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
        }
        
        PlayersRegistered player = repo.findById(playerid)
        	    .orElseThrow(() -> new RuntimeException("Player not found with ID: " + playerid));


        ResponseEntity<String> re = game.getTeamManagement().TeamingPlayersingles(teamID, player);
        
        redisTemplate.opsForValue().set(gameid, game);
        
        System.out.println("Updated Team1: " + game.getTeamManagement().getTeam1().size());
        System.out.println("Updated Team2: " + game.getTeamManagement().getTeam2().size());
        System.out.println("Updated Team1: " + game.getTeamManagement().getTeam1());
        System.out.println("Updated Team2: " + game.getTeamManagement().getTeam2());
        return re;
    }
    
    public ResponseEntity<String> DoublesTeaming(String gameid, int teamID, int playerid){
    	
    	    GameComm game = redisTemplate.opsForValue().get(gameid);

            if (game == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
            }
            
            PlayersRegistered player = repo.getReferenceById(playerid);

            ResponseEntity<String> re = game.getTeamManagement().TeamingForDoubles(teamID, player);
        
            redisTemplate.opsForValue().set(gameid, game);

            return re;
        }
    
    public ResponseEntity<String> GameDimension(String gameid, int numofgames, int numofsets){
    	
	    GameComm game = redisTemplate.opsForValue().get(gameid);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
        }

        ResponseEntity<String> re = game.getTeamManagement().GameFormat(numofgames, numofsets);
    
        redisTemplate.opsForValue().set(gameid, game);

        return re;
    }
    
    public ResponseEntity<String> GameBegin(String gameid){
    	
	        GameComm game = redisTemplate.opsForValue().get(gameid);

            if (game == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
            }

            ResponseEntity<String> re = game.getTeamManagement().beginGame();
            
            game.getGameDynamics().setSetScoreTeam1(0);
            game.getGameDynamics().setSetScoreTeam2(0);
            game.getGameDynamics().setMatchScoreTeam1(0);
            game.getGameDynamics().setMatchScoreTeam2(0);
    
            redisTemplate.opsForValue().set(gameid, game);

            return re;
        }
    
    public ResponseEntity<String> Toss(String gameid){
    	
        GameComm game = redisTemplate.opsForValue().get(gameid);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
        }

        ResponseEntity<String> re = game.getGameDynamics().Toss();

        redisTemplate.opsForValue().set(gameid, game);

        return re;
    }
    
    public ResponseEntity<String> Server(String gameid, int teamid){
    	
        GameComm game = redisTemplate.opsForValue().get(gameid);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
        }

        ResponseEntity<String> re = game.getGameDynamics().Server1(teamid);

        redisTemplate.opsForValue().set(gameid, game);

        return re;
    }
    
    public ResponseEntity<String> addscoreto(String gameid, int teamid){
    	
        GameComm game = redisTemplate.opsForValue().get(gameid);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
        }

        ResponseEntity<String> re = game.getGameDynamics().ScoreAdd(teamid);
        System.out.println(game.getGameDynamics().getGameScoreTeam1());

        redisTemplate.opsForValue().set(gameid, game);

        return re;
    }
    
    public ResponseEntity<String> gameInsights(String gameid){
    	
    	GameComm game = redisTemplate.opsForValue().get(gameid);

        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
        }
    	
    	try {
    	    
    		  WebClient webClient = WebClient.create();

              String apiUrl = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";
              String apiKey = "Bearer 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";
              String entry = "Give Insights and trends based on the given game flow and score that is "+ game.getGameDynamics().getMatchScorestat1()
            		  + " as match scores of team 1 and "+ game.getGameDynamics().getMatchScorestat2()+" for team 2"+game.getGameDynamics().setScorestat1
            		  + " as set scores of team 1 and "+ game.getGameDynamics().setScorestat2+" for team 2"+game.getGameDynamics().gameScorestat1
            		  + " as game scores of team 1 and "+ game.getGameDynamics().gameScorestat2+" for team 2";

              String response = webClient.post()
                  .uri(apiUrl)
                  .header("Authorization", apiKey)
                  .contentType(MediaType.APPLICATION_JSON)
                  .bodyValue(Map.of("inputs", entry))
                  .retrieve()
                  .bodyToMono(String.class)
                  .block();

              return ResponseEntity.ok(response);
          } catch (Exception e) {
              e.printStackTrace();
              return ResponseEntity.status(500).body("Error: " + e.getMessage());
          }
    	}
    
    public ResponseEntity<String> gameProbabilities(String gameid){
    	
    	  GameComm game = redisTemplate.opsForValue().get(gameid);

          if (game == null) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found with ID: " + gameid);
          }
    	
    	  try {
    	    
    		    WebClient webClient = WebClient.create();

                String apiUrl = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";
                String apiKey = "Bearer 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";
                String entry = "Give winning probabilities of two teams, team1 and team 2 based on the given game flow and score that is "+ game.getGameDynamics().matchScorestat1
            		  + " as match scores of team 1 and "+ game.getGameDynamics().matchScorestat2+" for team 2"+game.getGameDynamics().setScorestat1
            		  + " as set scores of team 1 and "+ game.getGameDynamics().setScorestat2+" for team 2"+game.getGameDynamics().gameScorestat1
            		  + " as game scores of team 1 and "+ game.getGameDynamics().gameScorestat2+" for team 2";

                String response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of("inputs", entry))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

                return ResponseEntity.ok(response);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("Error: " + e.getMessage());
            }
    	  }
    
public ResponseEntity<String> gameDescription(){
    	
    	try {
    	    
    		  WebClient webClient = WebClient.create();

              String apiUrl = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";
              String apiKey = "Bearer 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";
              String entry = "Explain rules, regulations and everything about game of Lawn Tennis with standards";

              String response = webClient.post()
                  .uri(apiUrl)
                  .header("Authorization", apiKey)
                  .contentType(MediaType.APPLICATION_JSON)
                  .bodyValue(Map.of("inputs", entry))
                  .retrieve()
                  .bodyToMono(String.class)
                  .block();

              return ResponseEntity.ok(response);
          } catch (Exception e) {
              e.printStackTrace();
              return ResponseEntity.status(500).body("Error: " + e.getMessage());
          }
    	}
    
    
    
}
