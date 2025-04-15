package controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.view.RedirectView;

import DTO.GamesAvailable;
import DTO.PlayersData;
import PlayGame.GameScorePallet;
import repositories.GamesInDatabase;
import repositories.PlayersRegistered;
import repositories.PlayersRepository;



@RestController
@RequestMapping("/api/gamesandplayers")
public class Redirector {

	@Autowired
	GamesAvailable p;
	
	@GetMapping("/games")
	public List<GamesInDatabase> GetListOfGames() {
		return p.GetAllGames();
	}
	
	@GetMapping("/playgame/LawnTennis")
	public RedirectView redirectToLawnTennis() {
		
		return new RedirectView("/api/lawntennis");
	}
	
	@Autowired
	PlayersData pd;
	
	@Autowired
	PlayersRepository repo;
	
	@GetMapping("/players")
	public List<PlayersRegistered> GetListOfPlayers(){
		return repo.findAll();
	}
	
	@PostMapping("/addplayer")
	public int AddPlayerdata(@RequestBody PlayersRegistered Player) {
		
		int id = repo.getLastID()+1;
		Player.setID(id);
		repo.save(Player);
		return repo.getLastID();
		//pd.DataRegistration();
	}
	
	@Autowired
	GameScorePallet game_to_play;
	
	@GetMapping("/playgame")
	public void DisplaygameToPlay() {
		game_to_play.getGameToPlay();
	}
	
	@GetMapping("/chat/{Entry}")
	public ResponseEntity<String> chatwithai(@PathVariable("Entry") String entry){
		
		  try {
		    
			  WebClient webClient = WebClient.create();

	            String apiUrl = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";
	            String apiKey = "Bearer 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";

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
