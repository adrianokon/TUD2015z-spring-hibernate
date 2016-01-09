package service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import domain.Place;
import domain.Player;
import domain.Tournament;

@Component
@Transactional
public class TournamentManagerHibernateImpl implements TournamentManager{

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Long addNewPlayer(Player player) {
		player.setId(null);
		sessionFactory.getCurrentSession().persist(player);
		return player.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Player> getAllPlayers() {
		return sessionFactory.getCurrentSession().getNamedQuery("player.all")
				.list();
	}

	@Override
	public void deletePlayer(Player player) {
		player = (Player) sessionFactory.getCurrentSession().get(Player.class,
				player.getId());
		
		// lazy loading here
		for (Tournament t : player.getTournaments()) {
			t.getPlayers().remove(player);
			sessionFactory.getCurrentSession().update(t);
		}
		sessionFactory.getCurrentSession().delete(player);
	}

	@Override
	public Player findPlayerById(Long id) {
		return (Player) sessionFactory.getCurrentSession().get(Player.class, id);
	}

	@Override
	public Long addNewTournament(Tournament tournament) {
		tournament.setId(null);
		sessionFactory.getCurrentSession().persist(tournament);
		return tournament.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Tournament> getAllTournaments() {
		return sessionFactory.getCurrentSession().getNamedQuery("tournament.all")
				.list();
	}

	@Override
	public void deleteTournament(Tournament tournament) {
		tournament = (Tournament) sessionFactory.getCurrentSession().get(Tournament.class,
				tournament.getId());
		
		// lazy loading here
		for (Player p : tournament.getPlayers()) {
			p.getTournaments().remove(tournament);
			sessionFactory.getCurrentSession().update(p);
		}
		sessionFactory.getCurrentSession().delete(tournament);
	}

	@Override
	public Tournament findTournamentById(Long id) {
		return (Tournament) sessionFactory.getCurrentSession().get(Tournament.class,
				id);
	}

	@Override
	public Long addNewPlace(Place place) {
		place.setId(null);
		sessionFactory.getCurrentSession().persist(place);
		return place.getId();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Place> getAllPlaces() {
		return sessionFactory.getCurrentSession().getNamedQuery("place.all")
				.list();
	}

	@Override
	public void disposePlace(Place place, Tournament tournament) {
		place = (Place) sessionFactory.getCurrentSession().get(Place.class,
				place.getId());
	
		place.getTournaments().remove(tournament);
		sessionFactory.getCurrentSession().update(place);
	}

	@Override
	public Place findPlaceById(Long id) {
		return (Place) sessionFactory.getCurrentSession().get(Place.class, id);
	}

	@Override
	public void signUpPlayer(Player player, Tournament tournament) {
		player = (Player) sessionFactory.getCurrentSession().get(
				Player.class, player.getId());
		tournament = (Tournament) sessionFactory.getCurrentSession()
				.get(Tournament.class, tournament.getId());
		
		player.getTournaments().add(tournament);
		tournament.getPlayers().add(player);
		sessionFactory.getCurrentSession().update(player);
		sessionFactory.getCurrentSession().update(tournament);
	}

	@Override
	public void addTournamentToPlace(Tournament tournament, Place place) {
		place = (Place) sessionFactory.getCurrentSession().get(
				Place.class, place.getId());
		tournament = (Tournament) sessionFactory.getCurrentSession()
				.get(Tournament.class, tournament.getId());
		
		place.getTournaments().add(tournament);
		sessionFactory.getCurrentSession().update(place);
	}
}
