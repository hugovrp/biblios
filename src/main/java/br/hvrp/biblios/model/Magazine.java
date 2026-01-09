package br.hvrp.biblios.model;

import br.hvrp.biblios.model.enums.AvailabilityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "revistas")
public class Magazine {
	@Id
	@SequenceGenerator(name="magazine_id", sequenceName = "magazine_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="magazine_id")
	@Column(name = "id")
	private int id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private AvailabilityStatus status = AvailabilityStatus.AVAILABLE;
	
	@ManyToOne
	@JoinColumn(name = "id_edicao")
	private Edition edition;
	
	@ManyToOne
	@JoinColumn(name = "id_caixa", nullable = false)
	private Box box;

	public int getId() {
		return id;
	}

	public AvailabilityStatus getStatus() {
		return status;
	}

	public Edition getEdition() {
		return edition;
	}

	public Box getBox() {
		return box;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setStatus(AvailabilityStatus status) {
		this.status = status;
	}

	public void setEdition(Edition edition) {
		this.edition = edition;
	}

	public void setBox(Box box) {
		this.box = box;
	}
}