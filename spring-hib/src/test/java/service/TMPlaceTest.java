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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/beans.xml" })
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
@Transactional
public class TMPlaceTest {

	@Autowired
	TournamentManager tournamentManager;

	private List<Place> placeBackup;

	@Before
	public void dbBackup() {
		placeBackup = tournamentManager.getAllPlaces();
	}

	@After
	public void dbRestore() {
		// usuwanie wszystkich rekordów z bazy
		for (Place p : tournamentManager.getAllPlaces()) {
			tournamentManager.deletePlace(p);
		}

		// odtworzenie rekordów z backupu
		for (Place p : placeBackup) {
			tournamentManager.addNewPlace(p);
		}
	}

	@Test
	public void addPlaceTest() {
		String testNick = "test";
		List<Place> retrievedPlaces = tournamentManager.getAllPlaces();

		for (Place p : retrievedPlaces) {
			if (p.getName().equals(testNick)) {
				tournamentManager.deletePlace(p);
				break;
			}
		}

		Place place = new Place();
		place.setName(testNick);
		place.setCountry("abcd");
		place.setCity("foo");
		tournamentManager.addNewPlace(place);

		boolean namePresent = false;
		for (Place p : tournamentManager.getAllPlaces()) {
			if (p.getName().equals(testNick)) {
				namePresent = true;
				break;
			}
		}
		assertTrue(namePresent);
	}

	@Test
	public void getAllPlacesTest() {
		String testName = "test";
		List<Place> retrievedPlaces = tournamentManager.getAllPlaces();

		for (Place p : retrievedPlaces) {
			if (p.getName().equals(testName)) {
				tournamentManager.deletePlace(p);
				break;
			}
		}

		Place place = new Place();
		place.setName(testName);
		place.setCountry("abcd");
		place.setCity("foo");
		tournamentManager.addNewPlace(place);

		List<Place> actualPlaces = tournamentManager.getAllPlaces();
		assertTrue(retrievedPlaces.size() == actualPlaces.size() - 1);
	}

	@Test
	public void deletePlace() {
		String testName = "test";
		List<Place> retrievedPlaces = tournamentManager.getAllPlaces();
		Place player;

		if (retrievedPlaces.isEmpty()) {
			player = new Place();
			player.setName(testName);
			player.setCountry("abcd");
			player.setCity("foo");
			tournamentManager.addNewPlace(player);

			player = tournamentManager.getAllPlaces().get(0);
		} else {
			player = retrievedPlaces.get(0);
			testName = player.getName();
		}

		retrievedPlaces = tournamentManager.getAllPlaces();
		tournamentManager.deletePlace(player);
		List<Place> actualPlaces = tournamentManager.getAllPlaces();
		// sprawdza czy usunęliśmy tylko 1 rekord
		assertTrue(actualPlaces.size() == retrievedPlaces.size() - 1);

		for (Place p : tournamentManager.getAllPlaces()) {
			if (p.getName().equals(testName)) {
				assertTrue(false);
			}
		}
	}

	@Test
	public void findPlaceByIdTest() {
		List<Place> retrievedPlaces = tournamentManager.getAllPlaces();
		Place place;

		if (retrievedPlaces.isEmpty() || retrievedPlaces.size() == 1) {
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
		} else {
			place = retrievedPlaces.get(0);
		}

		Place actualPlace = tournamentManager.findPlaceById(place.getId());
		assertTrue(place.getName().equals(actualPlace.getName()));
	}
}
