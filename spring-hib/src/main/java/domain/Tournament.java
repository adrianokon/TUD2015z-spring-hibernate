package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Check;

@NamedQuery(name = "tournament.all", query = "Select t from Tournament t")
@Entity
@Check(constraints = "entry_fee >= 0 AND win >= 0")
public class Tournament {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true, nullable = false)
	private String name;
	private Double entry_fee;
	private Double win;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Player> players = new ArrayList<Player>();

	public String getName() {
		return name;
	}

	public void setName(String nick) {
		this.name = nick;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Double getEntry_fee() {
		return entry_fee;
	}

	public void setEntry_fee(Double entry_fee) {
		this.entry_fee = entry_fee;
	}

	public Double getWin() {
		return win;
	}

	public void setWin(Double win) {
		this.win = win;
	}
}
