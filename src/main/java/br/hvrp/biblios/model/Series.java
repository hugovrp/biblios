package br.hvrp.biblios.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "colecoes")
public class Series {
	@Id
	@SequenceGenerator(name="series_id", sequenceName = "series_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="series_id")
	@Column(name = "id")
	private int id;
	
	@NotBlank(message = "O nome da coleção é obrigatório.")
	@Column(name = "nome", nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
	private List<Edition> editions;

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Series other = (Series) obj;
	    return id != 0 && id == other.id;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}
	
	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Edition> getEditions() {
		return editions;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEditions(List<Edition> editions) {
		this.editions = editions;
	}	
}