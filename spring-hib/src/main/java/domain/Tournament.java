package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Tournament {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
   private Long   id;
   private Double entry_fee;
   private Double win;
   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<Player> players = new ArrayList<Player>();

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