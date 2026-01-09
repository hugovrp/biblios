package br.hvrp.biblios.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "caixas")
public class Box {
	@Id
	@SequenceGenerator(name="box_id", sequenceName = "box_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="box_id")
	@Column(name = "id")
	private int id;
	
	@NotNull(message = "O número da caixa é obrigatório.")
	@Column(name = "numero", nullable = false, unique = true)
	private long number;
	
	@Column(name = "cor")
	private String color;

	@OneToMany(mappedBy = "box")
	private List<Magazine> magazines;
	
	public int getId() {
		return id;
	}

	public long getNumber() {
		return number;
	}

	public String getColor() {
		return color;
	}
	
	public List<Magazine> getMagazines() {
		return magazines;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNumber(long number) {
		this.number = number;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public void setMagazines(List<Magazine> magazines) {
		this.magazines = magazines;
	}
}
