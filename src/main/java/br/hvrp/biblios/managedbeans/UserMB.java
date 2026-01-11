package br.hvrp.biblios.managedbeans;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.hvrp.biblios.dao.UserDAO;
import br.hvrp.biblios.model.User;
import br.hvrp.biblios.util.EmailUtil;
import br.hvrp.biblios.util.HashUtil;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

@SessionScoped
@Named("userMB")
public class UserMB extends BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private User user = new User(); 
    private User loggedUser;      
    private String loginInput;
    private String passwordInput;
    private String tokenParam;

    public String register() {
        try {
            UserDAO dao = new UserDAO();
            user.setProfile("user");
            user.setConfirmedEmail(false);
            user.setPassword(HashUtil.hashPassword(user.getPassword()));
            
            user.setConfirmationToken(UUID.randomUUID().toString());
            user.setTokenExpirationDate(LocalDateTime.now().plusDays(1));

            dao.insert(user);
            
            try {
            	EmailUtil.sendConfirmationEmail(user.getEmail(), user.getName(), user.getConfirmationToken());
            	
            	showInfo(SUCCESS, "Registro realizado! Verifique o seu e-mail para ativar a conta.");
            } catch(Exception e) {
            	showWarn(SAVED, "Conta criada, mas houve um erro ao enviar o e-mail de confirmação. Contacte o administrador.");
                e.printStackTrace(); 
            }
            
            user = new User(); 
            return "pending_confirm?faces-redirect=true";
        } catch(Exception e) {
        	showError(ERROR, "Falha ao registar utilizador.");
            return null;
        }
    }

    public void activateAccount() {
        if(tokenParam != null) {
            UserDAO dao = new UserDAO();
            User found = dao.findByToken(tokenParam);
            if(found != null && found.isTokenValid()) {
                found.setConfirmedEmail(true);
                found.setConfirmationToken(null);
                dao.alter(found);
                
                // Loga o usuário automaticamente após confirmar
                this.loggedUser = found;
                
                try {
                	redirect("index.xhtml");
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void confirmAccount() {
        if(tokenParam != null) {
            UserDAO dao = new UserDAO();
            User found = dao.findByToken(tokenParam);
            
            if(found != null) {
                found.setConfirmedEmail(true);
                found.setConfirmationToken(null);
                dao.alter(found);
                
                showInfo(SUCCESS, "E-mail confirmado!");
            }
        }
    }
    
    public String login() {
        UserDAO dao = new UserDAO();
        User found = dao.findByEmail(loginInput);

        if(found != null && found.getPassword().equals(HashUtil.hashPassword(passwordInput))) {
            if(!found.isConfirmedEmail()) {
            	return "pending_confirm?faces-redirect=true";
            }
            this.loggedUser = found;
            
            this.loginInput = null;
            this.passwordInput = null;
            
            return "index?faces-redirect=true";
        }

        showError(ERROR, "Login ou senha inválidos.");
        return null;
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.loggedUser = null;
        return "login?faces-redirect=true";
    }
    
    public List<User> getAllUsers() {
        return new UserDAO().listAll();
    }
    
    public boolean isLoggedIn() {
        return loggedUser != null;
    }

    public User getUser() { 
    	return user; 
    }
    
    public User getLoggedUser() { 
    	return loggedUser; 
    }
    
    public String getLoginInput() { 
    	return loginInput; 
    }
    
    public String getPasswordInput() {
    	return passwordInput; 
    }
    
    public String getTokenParam() { 
    	return tokenParam; 
    }
    
    public void setLoginInput(String loginInput) { 
    	this.loginInput = loginInput; 
    }
    
    public void setPasswordInput(String passwordInput) { 
    	this.passwordInput = passwordInput; 
    }
    
    public void setTokenParam(String tokenParam) { 
    	this.tokenParam = tokenParam; 
    }
}