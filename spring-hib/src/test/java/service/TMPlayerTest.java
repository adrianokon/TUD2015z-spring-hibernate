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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class TMPlayerTest {

	@Autowired
	TournamentManager tournamentManager;

	private List<Player> playerBackup;

	@Before
	public void dbBackup() {
		playerBackup = tournamentManager.getAllPlayers();
	}

	@After
	public void dbRestore() {
		// usuwanie wszystkich rekordów z bazy
		for (Player p : tournamentManager.getAllPlayers()) {
			tournamentManager.deletePlayer(p);
		}

		// odtworzenie rekordów z backupu
		for (Player p : playerBackup) {
			tournamentManager.addNewPlayer(p);
		}
	}

	@Test
	public void addPlayerTest() {
		String testNick = "test";
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
		String testNick = "test";
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
		String testNick = "test";
		List<Player> retrievedPlayers = tournamentManager.getAllPlayers();
		Player player;

		if (retrievedPlayers.isEmpty()) {
			player = new Player();
			player.setNick(testNick);
			player.setCountry("abcd");
			player.setEarned_money(100.0);
			player.setRanking(10);
			player.setWins_count(3);
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
}
