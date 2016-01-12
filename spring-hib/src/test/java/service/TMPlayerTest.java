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

import domain.Player;
import domain.Tournament;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class TMPlayerTest {

	@Autowired
	TournamentManager tournamentManager;

	private List<Player> playerBackup;
	private List<Tournament> tournamentBackup;

	@Before
	public void dbBackup() {
		playerBackup = tournamentManager.getAllPlayers();
		tournamentBackup = tournamentManager.getAllTournaments();

		// ewentualne usuniecie danych testowych
		// graczy testNick i testNick2
		// oraz turniejow testName i testName2
		for (Player p : tournamentManager.getAllPlayers()) {
			if (p.getNick().equals("testNick") || p.getNick().equals("testNick2")) {
				tournamentManager.deletePlayer(p);
			}
		}
		for (Tournament t : tournamentManager.getAllTournaments()) {
			if (t.getName().equals("testName") || t.getName().equals("testName2")) {
				tournamentManager.deleteTournament(t);
			}
		}
	}

	@After
	public void dbRestore() {
		// usuwanie wszystkich rekordów z bazy
		for (Player p : tournamentManager.getAllPlayers()) {
			tournamentManager.deletePlayer(p);
		}
		for (Tournament t : tournamentManager.getAllTournaments()) {
			tournamentManager.deleteTournament(t);
		}

		// odtworzenie rekordów z backupu
		for (Player p : playerBackup) {
			tournamentManager.addNewPlayer(p);
		}
		for (Tournament t : tournamentBackup) {
			tournamentManager.addNewTournament(t);
		}
	}

	@Test
	public void addPlayerTest() {
		String testNick = "testNick";
		List<Player> retrievedPlayers = tournamentManager.getAllPlayers();

		for (Player p : retrievedPlayers) {
			if (p.getNick().equals(testNick)) {
				tournamentManager.deletePlayer(p);
				break;
			}
		}

		Player player = new Player();
		player.setNick(testNick);
		player.setCountry("abcd");
		player.setEarned_money(100.0);
		player.setRanking(10);
		player.setWins_count(3);
		tournamentManager.addNewPlayer(player);

		boolean nickPresent = false;
		for (Player p : tournamentManager.getAllPlayers()) {
			if (p.getNick().equals(testNick)) {
				nickPresent = true;
				break;
			}
		}
		assertTrue(nickPresent);
	}

	@Test
	public void getAllPlayersTest() {
		String testNick = "testNick";
		List<Player> retrievedPlayers = tournamentManager.getAllPlayers();

		for (Player p : retrievedPlayers) {
			if (p.getNick().equals(testNick)) {
				tournamentManager.deletePlayer(p);
				break;
			}
		}

		Player player = new Player();
		player.setNick(testNick);
		player.setCountry("abcd");
		player.setEarned_money(100.0);
		player.setRanking(10);
		player.setWins_count(3);
		tournamentManager.addNewPlayer(player);

		List<Player> actualPlayers = tournamentManager.getAllPlayers();
		assertTrue(retrievedPlayers.size() == actualPlayers.size() - 1);
	}

	@Test
	public void deletePlayer() {
		String testNick = "testNick";
		List<Player> retrievedPlayers = tournamentManager.getAllPlayers();
		Player player;

		if (retrievedPlayers.isEmpty() || retrievedPlayers.size() == 1) {
			player = new Player();
			player.setNick(testNick);
			player.setCountry("abcd");
			player.setEarned_money(100.0);
			player.setRanking(10);
			player.setWins_count(3);
			tournamentManager.addNewPlayer(player);
			player = new Player();
			player.setNick("testNick2");
			player.setCountry("Polska");
			player.setEarned_money(460.0);
			player.setRanking(7);
			player.setWins_count(5);
			tournamentManager.addNewPlayer(player);

			player = tournamentManager.getAllPlayers().get(0);
		} else {
			player = retrievedPlayers.get(0);
			testNick = player.getNick();
		}

		retrievedPlayers = tournamentManager.getAllPlayers();
		tournamentManager.deletePlayer(player);
		List<Player> actualTournaments = tournamentManager.getAllPlayers();
		// sprawdza czy usunęliśmy tylko 1 rekord
		assertTrue(actualTournaments.size() == retrievedPlayers.size() - 1);

		for (Player p : actualTournaments) {
			if (p.getNick().equals(testNick)) {
				assertTrue(false);
			}
		}
	}

	@Test
	public void findPlayerByIdTest() {
		List<Player> retrievedPlayers = tournamentManager.getAllPlayers();
		Player player;

		if (retrievedPlayers.isEmpty() || retrievedPlayers.size() == 1) {
			player = new Player();
			player.setNick("testNick");
			player.setCountry("abcd");
			player.setEarned_money(100.0);
			player.setRanking(10);
			player.setWins_count(3);
			tournamentManager.addNewPlayer(player);
			player = new Player();
			player.setNick("testNick2");
			player.setCountry("Polska");
			player.setEarned_money(460.0);
			player.setRanking(7);
			player.setWins_count(5);
			tournamentManager.addNewPlayer(player);

			player = tournamentManager.getAllPlayers().get(0);
		} else {
			player = retrievedPlayers.get(0);
		}

		Player actualPlayer = tournamentManager.findPlayerById(player.getId());
		assertTrue(player.getNick().equals(actualPlayer.getNick()));
	}

	@Test
	public void signUpPlayerTest() {
		Player player;

		player = new Player();
		player.setNick("testNick");
		player.setCountry("abcd");
		player.setEarned_money(100.0);
		player.setRanking(10);
		player.setWins_count(3);
		tournamentManager.addNewPlayer(player);
		player = new Player();
		player.setNick("testNick2");
		player.setCountry("Polska");
		player.setEarned_money(460.0);
		player.setRanking(7);
		player.setWins_count(5);
		tournamentManager.addNewPlayer(player);

		player = tournamentManager.getAllPlayers().get(0);

		Tournament tournament;

		tournament = new Tournament();
		tournament.setName("testName");
		tournament.setEntry_fee(100.0);
		tournament.setWin(1021321.0);
		tournamentManager.addNewTournament(tournament);
		tournament = new Tournament();
		tournament.setName("testName2");
		tournament.setEntry_fee(460.0);
		tournament.setWin(7312.0);
		tournamentManager.addNewTournament(tournament);

		tournament = tournamentManager.getAllTournaments().get(0);

		tournamentManager.signUpPlayer(player, tournament);

		Player actualPlayer = tournamentManager.findPlayerById(player.getId());
		Tournament actualTournament = tournamentManager.findTournamentById(tournament.getId());

		assertTrue(actualPlayer.getTournaments().contains(actualTournament));
		assertTrue(actualTournament.getPlayers().contains(actualPlayer));
	}

	@Test
	public void getPlayersByCountryTest() {

		Player player;

		player = new Player();
		player.setNick("testNick");
		player.setCountry("Polska");
		player.setEarned_money(100.0);
		player.setRanking(10);
		player.setWins_count(3);
		tournamentManager.addNewPlayer(player);
		player = new Player();
		player.setNick("testNick2");
		player.setCountry("Polska");
		player.setEarned_money(460.0);
		player.setRanking(7);
		player.setWins_count(5);
		tournamentManager.addNewPlayer(player);
		player = new Player();
		player.setNick("testNick3");
		player.setCountry("Azerbejdżan");
		player.setEarned_money(100.0);
		player.setRanking(15);
		player.setWins_count(8);
		tournamentManager.addNewPlayer(player);

		List<Player> listPlayers = tournamentManager.getPlayersByCountry("Polska");
		for (Player p : listPlayers) {
			if (!p.getCountry().equals("Polska")) {
				assertTrue(false);
			}
		}
	}
}
