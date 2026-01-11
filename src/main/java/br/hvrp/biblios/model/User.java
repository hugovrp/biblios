package br.hvrp.biblios.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "usuarios")
public class User {
	@Id
	@SequenceGenerator(name="user_id", sequenceName = "user_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_id")
	@Column(name = "id")
	private int id;
	
	@Column(name = "perfil")
	private String profile;
	
	@Column(name = "email_confirmado")
	private boolean confirmedEmail;
	
	@Column(name = "token_confirmacao", unique = true)
    private String confirmationToken;
	
	@Column(name = "data_expiracao_token")
	private LocalDateTime tokenExpirationDate;
	
	@NotBlank(message = "O CPF é obrigatório.")
	@Column(name = "cpf", nullable = false, unique = true)
	private String cpf;
	
	@Size(min = 4, max = 100)
	@NotBlank(message = "O nome é obrigatório.")
	@Column(name = "nome", nullable = false)
	private String name;
	
	@NotBlank(message = "O e-mail é obrigatório.")
	@Email(message = "Formato de email inválido!")
	@Column(name = "email", nullable = false)
	private String email;
	
	@NotNull(message = "O telefone é obrigatório.")
	@Column(name = "telefone", nullable = false)
	private String telephone;
	
	@NotBlank(message = "A senha é obrigatória.")
	@Column(name = "senha", nullable = false)
	private String password;
	
	@Size(min = 4, max = 50)
	@Column(name = "login", unique = true)
	private String login;
	
	@Column(name = "data_nascimento")
	@Temporal(TemporalType.DATE)
	private LocalDate birthDate;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Loan> loans;
	
	@PrePersist
	private void prePersistActions() {
	    // Lógica do Login Opcional
	    if (login == null || login.trim().isEmpty()) {
	        this.login = "user_" + UUID.randomUUID().toString().substring(0, 8);
	    }
	    
	    // Lógica do Token e Expiração
	    if (this.confirmationToken == null) {
	        this.confirmationToken = UUID.randomUUID().toString();
	        
	        this.tokenExpirationDate = LocalDateTime.now().plusHours(24);
	    }
	}
	
	public boolean isTokenValid() {
	    if (this.tokenExpirationDate == null) {
	        return false;
	    }
	    // Verifica se "agora" ainda é antes da data de expiração
	    return LocalDateTime.now().isBefore(this.tokenExpirationDate);
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) return true;
	    if (obj == null || getClass() != obj.getClass()) return false;
	    User other = (User) obj;
	    return id != 0 && id == other.id;
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}
	
	public int getId() {
		return id;
	}

	public String getProfile() {
		return profile;
	}

	public boolean isConfirmedEmail() {
		return confirmedEmail;
	}
	
	public String getConfirmationToken() {
        return confirmationToken;
    }
	
	public LocalDateTime getTokenExpirationDate() {
		return tokenExpirationDate;
	}

	public String getCpf() {
		return cpf;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getTelephone() {
		return telephone;
	}

	public String getPassword() {
		return password;
	}

	public String getLogin() {
		return login;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}
	
	public List<Loan> getLoans() {
		return loans;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setConfirmedEmail(boolean confirmedEmail) {
		this.confirmedEmail = confirmedEmail;
	}
	
	public void setConfirmationToken(String confirmationToken) {
        this.confirmationToken = confirmationToken;
    }
	
	public void setTokenExpirationDate(LocalDateTime tokenExpirationDate) {
		this.tokenExpirationDate = tokenExpirationDate;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}	
	
	public void setLoans(List<Loan> loans) {
		this.loans = loans;
	}
}