package services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")
public class GameDynamics {
	
	 @Autowired
	 TeamManagement match;
	 
	 int Format;
	 int NumofSets, NumofGame, gameScoreTeam1 = 0, gameScoreTeam2 = 0,setScoreTeam1 = 0,setScoreTeam2 = 0,matchScoreTeam1 = 0, matchScoreTeam2 = 0;
	 int representationalGamescoreteam1, representationalGamescoreteam2, matchwin = 0;
	 int server,reciever, Team1, Team2;
	 ResponseEntity<String> re;
	 int[] matchScorestat1 = new int[15];
	 int[] matchScorestat2 = new int[15];
	 int[] setScorestat1 = new int[15];
	 int[] setScorestat2 = new int[15];
	 int[] gameScorestat1 = new int[15];
	 int[] gameScorestat2 = new int[15];
	 int i = 0;
	 
	 public GameDynamics(TeamManagement match) {
	        this.match = match;
	        this.Format = match.Format;
	        this.NumofSets = match.getNumofSets();
	        this.NumofGame = match.getNumOfGame();
	    }
	 
	 public GameDynamics() {
		super();
	}

	public ResponseEntity<String> Toss() {
		 if(Math.random()>0.5) {
			 return ResponseEntity.ok("Heads");
		 }
		 return ResponseEntity.ok("Tails");
	 }
	 
	 public ResponseEntity<String> Server1(int teamID) {
		 
		 if(teamID<3 && teamID>0) {
			 server = teamID;
			 setScoreTeam1 = server;
			 reciever = 3-teamID;
			 setScoreTeam2 = reciever;
			 Team1 = server;
			 Team2 = reciever;
			 return ResponseEntity.ok("Server Team is" + teamID);
		 }
		 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Team ID");		 
	 }
	
	 
	 
	 public ResponseEntity<String> ScoreAdd(int scoringteam){
		 
		 String response = null;
		 
		 if(matchwin == 1) {
			 response = response+" Match has been completed, request is invalid";
		 }
		 
		 else {
		  if(Format == 1) {
			 if(scoringteam == 1) {
				 gameScoreTeam1++;
				 representationalGamescoreteam1 = gameScore(representationalGamescoreteam1);
			 }
			 else if(scoringteam == 2) {
				 gameScoreTeam2++;
				 representationalGamescoreteam2 = gameScore(representationalGamescoreteam2);
			 }
			 if(gameScoreTeam1 == gameScoreTeam2 && gameScoreTeam1 == 40) {
				 response = response + "It is duece";
			 }
			 response = response + "Game Score: Team1-" + representationalGamescoreteam1 + ", Team2-" + representationalGamescoreteam2 +
					 " | Set Score: Team1 - " + setScoreTeam1 + ", Team2 - " + setScoreTeam2 +
					 " | Match Score: Team1 - " + matchScoreTeam1 + ", Team2 - " + matchScoreTeam2 + " If not game win, change side of service";
			 if(representationalGamescoreteam1 == 50 && representationalGamescoreteam2 == 40) {
				 response = response+" Advantage to team1";
			 }
			 else if(representationalGamescoreteam2 == 50 && representationalGamescoreteam1 == 40) {
				 response = response + " Advantage to team2";
			 }
			 response = response+" || " +checkgameWin();
			 System.out.println(gameScoreTeam1+" and "+gameScoreTeam2+" "+representationalGamescoreteam1+" "+representationalGamescoreteam2);
		 }
		 
		 else if(Format == 2) {
			 if(scoringteam == 1) {
				 gameScoreTeam1++;
				 representationalGamescoreteam1 = gameScore(representationalGamescoreteam1);
			 }
			 else if(scoringteam == 2) {
				 gameScoreTeam2++;
				 representationalGamescoreteam2 = gameScore(representationalGamescoreteam2);
			 }
			 if(gameScoreTeam1 == gameScoreTeam2 && gameScoreTeam1 == 40) {
				 response = response + "It is duece";
			 }
			 response = response + "Game Score: Team1-" + representationalGamescoreteam1 + ", Team2-" + representationalGamescoreteam2 +
					 " | Set Score: Team1 - " + setScoreTeam1 + ", Team2 - " + setScoreTeam2 +
					 " | Match Score: Team1 - " + matchScoreTeam1 + ", Team2 - " + matchScoreTeam2 + " If not game win, change side of service"
					 + " If game win, and service back to your teram, change server";
			 if(representationalGamescoreteam1 == 50 &&representationalGamescoreteam2 == 40) {
				 response = response+" Advantage to team1";
			 }
			 else if(representationalGamescoreteam2 == 50 && representationalGamescoreteam1 == 40) {
				 response = response + " Advantage to team2";
			 }
			 response = response+" || " +checkgameWin();
			 System.out.println(gameScoreTeam1+" and "+gameScoreTeam2+" "+representationalGamescoreteam1+" "+representationalGamescoreteam2);
		  }
		 }
		 
		 response = response + " || " +checkSetWin((NumofGame/2)+1);
		 response = response +" || " +checkMatchWin((NumofSets/2)+1);
		 gameStat();
		 
         return ResponseEntity.ok(response);
	 }
	 
	 public int gameScore(int currentScore) {
		 return switch (currentScore) {
		 case 0 -> 15;
		 case 15 -> 30;
		 case 30 -> 40;
		 case 40 -> 50;
		 default->40;
		 
		 };
	 }
	 
	 public void restoreSetScore() {
		 setScoreTeam1 = 0;
		 setScoreTeam2 = 0;
		 representationalGamescoreteam1 = 0;
		 representationalGamescoreteam2 = 0;
		 gameScoreTeam1 = 0;
		 gameScoreTeam2 = 0;
	 }
	 
	 public void restoreGameScore() {
		 gameScoreTeam1 = 0;
		 gameScoreTeam2 = 0;
		 representationalGamescoreteam1 = 0;
		 representationalGamescoreteam2 = 0;
	 }
	 
	 public String checkMatchWin(int setstowin) {
		 if(matchScoreTeam1>=setstowin && matchScoreTeam1-matchScoreTeam2 >= 2) {
			 matchwin = 1;
			 return "Match has been won by team 1";
		 }
		 else if(matchScoreTeam2>=setstowin && matchScoreTeam2-matchScoreTeam1 >= 2) {
			 matchwin = 1;
			 return "Match has been won by team 2";
		 }
		 return null;
	 }
	 
	 public String checkSetWin(int setsToWin) {
		 
		 if(setScoreTeam1>=setsToWin && setScoreTeam1-setScoreTeam2 >= 2) {
			 matchScoreTeam1++;
			 restoreGameScore();
			 restoreSetScore();
			 return "Team 1 won the set, next set begins with team"+ (3-server) +" serving";
		 }
		 else if(setScoreTeam2>=setsToWin && setScoreTeam2-setScoreTeam1 >= 2) {
			 matchScoreTeam2++;
			 restoreGameScore();
			 restoreSetScore();
			 return "Team 2 won the set, next set begins with team"+ (3-server) +" serving";
		 }
		 
		 
		 return null;
	 }
	 
	 public String checkgameWin() {
		 
		 if(gameScoreTeam1>=4 && gameScoreTeam1-gameScoreTeam2>=2) {
			 setScoreTeam1++;
			 restoreGameScore();
			 return "Team 1 won the game, next set begins with team"+ (3-server) +" serving";
		 }
		 else if(gameScoreTeam1>=4 && gameScoreTeam1-gameScoreTeam2>=2) {
			 setScoreTeam2++;
			 restoreGameScore();
			 return "Team 2 won the game, next set begins with team"+ (3-server) +" serving";
		 }
		 
		 if((setScoreTeam1+setScoreTeam2)%2 == 1) {
			 return " Also need to change sides";
		 }
		 
		 return null;
	 }
	 

	public void gameStat() {
		 
		 matchScorestat1[i] = matchScoreTeam1;
		 matchScorestat2[i] = matchScoreTeam2;
		 setScorestat1[i] = setScoreTeam1;
		 setScorestat2[i] = matchScoreTeam2;
		 gameScorestat1[i] = gameScoreTeam1;
		 gameScorestat2[i] = gameScoreTeam2;
		 
		 i = (i+1)%15;
	 }
	
	public int getI() {
		return i;
	}
	
	public void setI(int i) {
		this.i = i;
	}
	
	 public int getMatchwin() {
		return matchwin;
	}

	public void setMatchwin(int matchwin) {
		this.matchwin = matchwin;
	}

	public int getRepresentationalGamescoreteam1() {
		return representationalGamescoreteam1;
	}

	public void setRepresentationalGamescoreteam1(int representationalGamescoreteam1) {
		this.representationalGamescoreteam1 = representationalGamescoreteam1;
	}

	public int getRepresentationalGamescoreteam2() {
		return representationalGamescoreteam2;
	}

	public void setRepresentationalGamescoreteam2(int representationalGamescoreteam2) {
		this.representationalGamescoreteam2 = representationalGamescoreteam2;
	}

	public int[] getMatchScorestat1() {
		return matchScorestat1;
	}

	public void setMatchScorestat1(int[] matchScorestat1) {
		this.matchScorestat1 = matchScorestat1;
	}

	public int[] getMatchScorestat2() {
		return matchScorestat2;
	}

	public void setMatchScorestat2(int[] matchScorestat2) {
		this.matchScorestat2 = matchScorestat2;
	}

	public int[] getSetScorestat1() {
		return setScorestat1;
	}

	public void setSetScorestat1(int[] setScorestat1) {
		this.setScorestat1 = setScorestat1;
	}

	public int[] getSetScorestat2() {
		return setScorestat2;
	}

	public void setSetScorestat2(int[] setScorestat2) {
		this.setScorestat2 = setScorestat2;
	}

	public int[] getGameScorestat1() {
		return gameScorestat1;
	}

	public void setGameScorestat1(int[] gameScorestat1) {
		this.gameScorestat1 = gameScorestat1;
	}

	public int[] getGameScorestat2() {
		return gameScorestat2;
	}

	public void setGameScorestat2(int[] gameScorestat2) {
		this.gameScorestat2 = gameScorestat2;
	}

	public int getGameScoreTeam1() {
		return gameScoreTeam1;
	}

	public void setGameScoreTeam1(int gameScoreTeam1) {
		this.gameScoreTeam1 = gameScoreTeam1;
	}

	public int getGameScoreTeam2() {
		return gameScoreTeam2;
	}

	public void setGameScoreTeam2(int gameScoreTeam2) {
		this.gameScoreTeam2 = gameScoreTeam2;
	}

	public int getSetScoreTeam1() {
		return setScoreTeam1;
	}

	public void setSetScoreTeam1(int setScoreTeam1) {
		this.setScoreTeam1 = setScoreTeam1;
	}

	public int getSetScoreTeam2() {
		return setScoreTeam2;
	}

	public void setSetScoreTeam2(int setScoreTeam2) {
		this.setScoreTeam2 = setScoreTeam2;
	}

	public int getMatchScoreTeam1() {
		return matchScoreTeam1;
	}

	public void setMatchScoreTeam1(int matchScoreTeam1) {
		this.matchScoreTeam1 = matchScoreTeam1;
	}

	public int getMatchScoreTeam2() {
		return matchScoreTeam2;
	}

	public void setMatchScoreTeam2(int matchScoreTeam2) {
		this.matchScoreTeam2 = matchScoreTeam2;
	}


	public int getFormat() {
		return Format;
	}

	public void setFormat(int format) {
		Format = format;
	}

	public int getServer() {
		return server;
	}

	public void setServer(int server) {
		this.server = server;
	}

	public int getTeam1() {
		return Team1;
	}

	public void setTeam1(int team1) {
		Team1 = team1;
	}

	public int getTeam2() {
		return Team2;
	}

	public void setTeam2(int team2) {
		Team2 = team2;
	}
}


