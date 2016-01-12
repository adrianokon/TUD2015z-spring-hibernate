package service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import domain.Place;
import domain.Tournament;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class TMTournamentTest {

	@Autowired
	TournamentManager tournamentManager;

	private List<Tournament> tournamentBackup;
	private List<Place> placeBackup;

	@Before
	public void dbBackup() {
		tournamentBackup = tournamentManager.getAllTournaments();
		placeBackup = tournamentManager.getAllPlaces();

		// ewentualne usuniecie danych testowych
		// turniejow testName i testName2
		// oraz miejsc testName i testName2
		for (Tournament t : tournamentManager.getAllTournaments()) {
			if (t.getName().equals("testName") || t.getName().equals("testName2")) {
				tournamentManager.deleteTournament(t);
			}
		}
		for (Place p : tournamentManager.getAllPlaces()) {
			if (p.getName().equals("testName") || p.getName().equals("testName2")) {
				p.getTournaments().clear();
				tournamentManager.deletePlace(p);
			}
		}
	}

	@After
	public void dbRestore() {
		// usuwanie wszystkich rekordów z bazy
		for (Place p : tournamentManager.getAllPlaces()) {
			p.getTournaments().clear();
			tournamentManager.deletePlace(p);
		}
		for (Tournament t : tournamentManager.getAllTournaments()) {
			tournamentManager.deleteTournament(t);
		}

		// odtworzenie rekordów z backupu
		for (Tournament t : tournamentBackup) {
			tournamentManager.addNewTournament(t);
		}
		for (Place p : placeBackup) {
			tournamentManager.addNewPlace(p);
		}
	}

	@Test
	public void addTournamentTest() {
		String testName = "testName";
		List<Tournament> retrievedTournaments = tournamentManager.getAllTournaments();

		for (Tournament p : retrievedTournaments) {
			if (p.getName().equals(testName)) {
				tournamentManager.deleteTournament(p);
				break;
			}
		}

		Tournament tournament = new Tournament();
		tournament.setName(testName);
		tournament.setEntry_fee(100.0);
		tournament.setWin(10000.0);
		tournamentManager.addNewTournament(tournament);

		boolean namePresent = false;
		for (Tournament p : tournamentManager.getAllTournaments()) {
			if (p.getName().equals(testName)) {
				namePresent = true;
				break;
			}
		}
		assertTrue(namePresent);
	}

	@Test
	public void getAllTournamentsTest() {
		String testName = "testName";
		List<Tournament> retrievedTournaments = tournamentManager.getAllTournaments();

		for (Tournament p : retrievedTournaments) {
			if (p.getName().equals(testName)) {
				tournamentManager.deleteTournament(p);
				break;
			}
		}

		Tournament tournament = new Tournament();
		tournament.setName(testName);
		tournament.setEntry_fee(100.0);
		tournament.setWin(10000.0);
		tournamentManager.addNewTournament(tournament);

		List<Tournament> actualTournaments = tournamentManager.getAllTournaments();
		assertTrue(retrievedTournaments.size() == actualTournaments.size() - 1);
	}

	@Test
	public void deleteTournament() {
		String testName = "testName";
		List<Tournament> retrievedTournaments = tournamentManager.getAllTournaments();
		Tournament tournament;

		if (retrievedTournaments.isEmpty() || retrievedTournaments.size() == 1) {
			tournament = new Tournament();
			tournament.setName(testName);
			tournament.setEntry_fee(100.0);
			tournament.setWin(102131.0);
			tournamentManager.addNewTournament(tournament);
			tournament = new Tournament();
			tournament.setName("testName2");
			tournament.setEntry_fee(460.0);
			tournament.setWin(7312.0);
			tournamentManager.addNewTournament(tournament);

			tournament = tournamentManager.getAllTournaments().get(0);
		} else {
			tournament = retrievedTournaments.get(0);
			testName = tournament.getName();
		}

		retrievedTournaments = tournamentManager.getAllTournaments();
		tournamentManager.deleteTournament(tournament);
		List<Tournament> actualTournaments = tournamentManager.getAllTournaments();
		// sprawdza czy usunęliśmy tylko 1 rekord
		assertTrue(actualTournaments.size() == retrievedTournaments.size() - 1);

		for (Tournament p : actualTournaments) {
			if (p.getName().equals(testName)) {
				assertTrue(false);
			}
		}
	}

	@Test
	public void findTournamentByIdTest() {
		List<Tournament> retrievedTournaments = tournamentManager.getAllTournaments();
		Tournament tournament;

		if (retrievedTournaments.isEmpty() || retrievedTournaments.size() == 1) {
			tournament = new Tournament();
			tournament.setName("testName");
			tournament.setEntry_fee(100.0);
			tournament.setWin(1021321.0);
			tournamentManager.addNewTournament(tournament);
			tournament = new Tournament();
			tournament.setName("testNick2");
			tournament.setEntry_fee(460.0);
			tournament.setWin(7312.0);
			tournamentManager.addNewTournament(tournament);

			tournament = tournamentManager.getAllTournaments().get(0);
		} else {
			tournament = retrievedTournaments.get(0);
		}

		Tournament actualTournament = tournamentManager.findTournamentById(tournament.getId());
		assertTrue(tournament.getName().equals(actualTournament.getName()));
	}

	@Test
	public void addTournamentToPlaceTest() {
		Tournament tournament;

		tournament = new Tournament();
		tournament.setName("testName");
		tournament.setEntry_fee(100.0);
		tournament.setWin(102131.0);
		tournamentManager.addNewTournament(tournament);
		tournament = new Tournament();
		tournament.setName("testName2");
		tournament.setEntry_fee(460.0);
		tournament.setWin(7312.0);
		tournamentManager.addNewTournament(tournament);

		tournament = tournamentManager.getAllTournaments().get(0);

		Place place;

		place = new Place();
		place.setName("testName");
		place.setCountry("abcd");
		place.setCity("foo");
		tournamentManager.addNewPlace(place);
		place = new Place();
		place.setName("testName2");
		place.setCountry("Polska");
		place.setCity("foofoo");
		tournamentManager.addNewPlace(place);

		place = tournamentManager.getAllPlaces().get(0);

		tournamentManager.addTournamentToPlace(tournament, place);

		Place actualPlace = tournamentManager.findPlaceById(place.getId());
		Tournament actualTournament = tournamentManager.findTournamentById(tournament.getId());

		assertTrue(actualPlace.getTournaments().contains(actualTournament));
	}
}
