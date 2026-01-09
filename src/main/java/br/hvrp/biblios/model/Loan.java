package br.hvrp.biblios.model;

import java.util.Calendar;

import br.hvrp.biblios.model.enums.LoanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "emprestimos")
public class Loan {
	@Id
	@SequenceGenerator(name="loan_id", sequenceName = "loan_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="loan_id")
	@Column(name = "id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "id_usuario", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "id_revista", nullable = false)
	private Magazine magazine;
	
	@Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus status = LoanStatus.ACTIVE;
	
	@Column(name = "data_emprestimo", nullable = false)
    private Calendar loanDate;

	@Column(name = "data_prevista_devolucao", nullable = false)
    private Calendar expectedReturnDate;

	@Column(name = "data_devolucao")
    private Calendar actualReturnDate;

	@PrePersist
	public void prePersist() {
	    if (this.loanDate == null) {
	        this.loanDate = Calendar.getInstance();
	    }
	    if (this.expectedReturnDate == null) {
	        Calendar expected = (Calendar) this.loanDate.clone();
	        expected.add(Calendar.DAY_OF_MONTH, 7); // Regra dos 7 dias
	        this.expectedReturnDate = expected;
	    }
	}
	
	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Magazine getMagazine() {
		return magazine;
	}

	public LoanStatus getStatus() {
		return status;
	}

	public Calendar getLoanDate() {
		return loanDate;
	}

	public Calendar getExpectedReturnDate() {
		return expectedReturnDate;
	}

	public Calendar getActualReturnDate() {
		return actualReturnDate;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}

	public void setStatus(LoanStatus status) {
		this.status = status;
	}

	public void setLoanDate(Calendar loanDate) {
		this.loanDate = loanDate;
	}

	public void setExpectedReturnDate(Calendar expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}

	public void setActualReturnDate(Calendar actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
}