package service;

import java.util.List;

import domain.Place;
import domain.Player;
import domain.Tournament;

public interface TournamentManager {
	List<Tournament> getTournamentsByPlayer(Player p);

	Long addNewPlayer(Player player);

	List<Player> getAllPlayers();

	void deletePlayer(Player player);

	Player findPlayerById(Long id);

	List<Player> getPlayersByCountry(String country);

	Long addNewTournament(Tournament tournament);

	List<Tournament> getAllTournaments();

	void deleteTournament(Tournament tournament);

	Tournament findTournamentById(Long id);

	Long addNewPlace(Place place);

	List<Place> getAllPlaces();

	void deletePlace(Place place);

	Place findPlaceById(Long id);

	void signUpPlayer(Player player, Tournament tournament);

	void addTournamentToPlace(Tournament tournament, Place place);
}