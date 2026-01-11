package br.hvrp.biblios.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "edicoes")
public class Edition {
	@Id
	@SequenceGenerator(name="edition_id", sequenceName = "edition_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="edition_id")
	@Column(name = "id")
	private int id;
	
	@NotNull(message = "A edição da revista é obrigatória.")
	@Column(name = "numero_edicao", nullable = false)
	private int editionNumber;
	
	@NotNull(message = "O ano da revista é obrigatório.")
	@Column(name = "ano", nullable = false)
	private Integer year;
	
	@ManyToOne
	@JoinColumn(name = "id_colecao", nullable = false)
	private Series series;
	
	@OneToMany(mappedBy = "edition", cascade = CascadeType.ALL)
	private List<Magazine> magazines;

	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Edition other = (Edition) obj;
	    return id != 0 && id == other.id;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}
	
	public int getId() {
		return id;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public Integer getYear() {
		return year;
	}

	public Series getSeries() {
		return series;
	}

	public List<Magazine> getMagazines() {
		return magazines;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setEditionNumber(int editionNumber) {
		this.editionNumber = editionNumber;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public void setMagazines(List<Magazine> magazines) {
		this.magazines = magazines;
	}
}