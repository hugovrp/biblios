package br.hvrp.biblios.managedbeans;

import java.io.IOException;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public abstract class BaseBean {
	
	protected final String SUCCESS = "Sucesso",
						   ATTENTION = "Atenção", 
						   SAVED = "Registro Salvo",
						   LIMIT = "Limite Atingido",
						   REMOVED = "Removido",
						   UNAVAILABLE = "Indisponível",
						   ERROR = "Erro";
	
	protected  void showInfo(String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, title, msg));
    }

	protected  void showWarn(String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, title, msg));
    }
	
	protected  void showError(String title, String msg) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, title, msg));
    }
	
	protected void redirect(String page) throws IOException {
		FacesContext.getCurrentInstance().getExternalContext().redirect(page);
	}
}
