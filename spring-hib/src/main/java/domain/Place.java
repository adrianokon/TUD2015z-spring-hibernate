package domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Place {

@Id
@GeneratedValue(strategy = GenerationType.AUTO)	
   private Long   id;
   private String country;
   private String city;
   private String name;
   @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   private List<Tournament> tournaments = new ArrayList<Tournament>();

   public List<Tournament> getTournaments() {
	return tournaments;
   }

   public void setTournaments(List<Tournament> cars) {
	   this.tournaments = cars;
   }

public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
