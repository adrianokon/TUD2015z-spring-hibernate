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
public class Player {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)	
   private Long    id;
   private String  nick;
   private String  country;
   private Integer ranking;
   private Double  earned_money;
   private Integer wins_count;
   @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<Tournament> tournaments = new ArrayList<Tournament>();

   public List<Tournament> getTournaments() {
	   return tournaments;
   }

   public void setTournaments(List<Tournament> tournaments) {
	   this.tournaments = tournaments;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNick() {
      return nick;
   }

   public void setNick(String nick) {
      this.nick = nick;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public Integer getRanking() {
      return ranking;
   }

   public void setRanking(Integer ranking) {
      this.ranking = ranking;
   }

   public Double getEarned_money() {
      return earned_money;
   }

   public void setEarned_money(Double earned_money) {
      this.earned_money = earned_money;
   }

   public Integer getWins_count() {
      return wins_count;
   }

   public void setWins_count(Integer wins_count) {
      this.wins_count = wins_count;
   }
}
