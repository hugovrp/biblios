package br.hvrp.biblios.model;

import java.util.List;
import java.util.Objects;

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
	private Integer number;
	
	@Column(name = "cor")
	private String color;

	@OneToMany(mappedBy = "box")
	private List<Magazine> magazines;
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    Box other = (Box) obj;
	    return id != 0 && id == other.id;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}
	
	public int getId() {
		return id;
	}

	public Integer getNumber() {
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

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public void setMagazines(List<Magazine> magazines) {
		this.magazines = magazines;
	}
}
