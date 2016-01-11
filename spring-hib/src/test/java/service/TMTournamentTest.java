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

	@Before
	public void dbBackup() {
		tournamentBackup = tournamentManager.getAllTournaments();
	}

	@After
	public void dbRestore() {
		// usuwanie wszystkich rekordów z bazy
		for (Place p : tournamentManager.getAllPlaces()) {
			tournamentManager.deletePlace(p);
		}

		// odtworzenie rekordów z backupu
		for (Tournament t : tournamentBackup) {
			tournamentManager.addNewTournament(t);
		}
	}

	@Test
	public void addTournamentTest() {
		String testName = "test";
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
		String testNick = "test";
		List<Tournament> retrievedTournaments = tournamentManager.getAllTournaments();

		for (Tournament p : retrievedTournaments) {
			if (p.getName().equals(testNick)) {
				tournamentManager.deleteTournament(p);
				break;
			}
		}

		Tournament tournament = new Tournament();
		tournament.setName(testNick);
		tournament.setEntry_fee(100.0);
		tournament.setWin(10000.0);
		tournamentManager.addNewTournament(tournament);

		List<Tournament> actualTournaments = tournamentManager.getAllTournaments();
		assertTrue(retrievedTournaments.size() == actualTournaments.size() - 1);
	}

	@Test
	public void deleteTournament() {
		String testName = "test";
		List<Tournament> retrievedTournaments = tournamentManager.getAllTournaments();
		Tournament tournament;

		if (retrievedTournaments.isEmpty()) {
			tournament = new Tournament();
			tournament.setName(testName);
			tournament.setEntry_fee(100.0);
			tournament.setWin(102131.0);
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

}
