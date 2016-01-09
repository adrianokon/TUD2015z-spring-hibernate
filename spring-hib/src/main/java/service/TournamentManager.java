package service;

import java.util.List;

import domain.Place;
import domain.Player;
import domain.Tournament;

public interface TournamentManager {
	Long addNewPlayer(Player player);
	List<Player> getAllPlayers();
	void deletePlayer(Player player);
	Player findPlayerById(Long id);
	
	Long addNewTournament(Tournament tournament);
	List<Tournament> getAllTournaments();
	void deleteTournament(Tournament tournament);
	Tournament findTournamentById(Long id);
	
	Long addNewPlace(Place place);
	List<Place> getAllPlaces();
	void disposePlace(Place place, Tournament tournament);
	Place findPlaceById(Long id);
	
	void signUpPlayer(Player player, Tournament tournament);
	void addTournamentToPlace(Tournament tournament, Place place);
}