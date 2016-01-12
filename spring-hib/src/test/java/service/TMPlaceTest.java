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

		// ewentualne usuniecie danych testowych
		// miejsc testName i testName2
		for (Place p : tournamentManager.getAllPlaces()) {
			if (p.getName().equals("testName") || p.getName().equals("testName2")) {
				tournamentManager.deletePlace(p);
			}
		}
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
		String testName = "testName";
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

		boolean namePresent = false;
		for (Place p : tournamentManager.getAllPlaces()) {
			if (p.getName().equals(testName)) {
				namePresent = true;
				break;
			}
		}
		assertTrue(namePresent);
	}

	@Test
	public void getAllPlacesTest() {
		String testName = "testName";
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
		String testName = "testName";
		List<Place> retrievedPlaces = tournamentManager.getAllPlaces();
		Place place;

		if (retrievedPlaces.isEmpty() || retrievedPlaces.size() == 1) {
			place = new Place();
			place.setName(testName);
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
			testName = place.getName();
		}

		retrievedPlaces = tournamentManager.getAllPlaces();
		tournamentManager.deletePlace(place);
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
