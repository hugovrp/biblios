package br.hvrp.biblios.model;

import java.time.Year;
import java.util.List;

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
	private Year year;
	
	@ManyToOne
	@JoinColumn(name = "id_colecao", nullable = false)
	private Series series;
	
	@OneToMany(mappedBy = "edition", cascade = CascadeType.ALL)
	private List<Magazine> magazines;

	public int getId() {
		return id;
	}

	public int getEditionNumber() {
		return editionNumber;
	}

	public Year getYear() {
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

	public void setYear(Year year) {
		this.year = year;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public void setMagazines(List<Magazine> magazines) {
		this.magazines = magazines;
	}
}