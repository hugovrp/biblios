package br.hvrp.biblios.managedbeans;

import java.io.Serializable;
import java.util.List;

import br.hvrp.biblios.dao.DAO;
import br.hvrp.biblios.model.Box;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("boxMB")
public class BoxMB extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Box box = new Box();
	private List<Box> boxes;
	private DAO<Box> dao = new DAO<>(Box.class);
	
	@PostConstruct
	public void init() {
		this.boxes = dao.listAll();
	}
	
	public void save() {
	    try {
	        System.out.println("Número recebido no MB: " + this.box.getNumber());

	        if(this.box.getNumber() == null) {
	        	showWarn(ATTENTION, "Preencha o número da caixa!");
	            return;
	        }

	        // Se o ID é 0, é uma caixa nova. Se for diferente de 0, é edição.
	        if(this.box.getId() == 0) {
	            dao.insert(this.box); 
	            showInfo(SUCCESS, "Caixa salva com sucesso!");
	        } else {
	            dao.alter(this.box);
	            showInfo(SUCCESS, "Caixa atualizada com sucesso!");
	        }
	            
	        clean();
	    } catch(Exception e) {
	        e.printStackTrace();
	        showError(ERROR, "Verifique se este número de caixa já existe.");
	    }
	}
	
	public void delete(Box box) {
		try {
			dao.remove(box);
            this.boxes = dao.listAll();
            
            showInfo(REMOVED, "Caixa excluída com sucesso!");
		} catch(Exception e) {
			showError(ERROR, "Essa caixa possui revistas e não pode ser excluída.");
		}
	}
	
	public void clean() {
		this.box = new Box(); 
        this.boxes = dao.listAll(); 
    }
	
	public void prepareNew() {
		this.box = new Box();
	}

	public Box getBox() {
		return box;
	}

	public List<Box> getBoxes() {
		return boxes;
	}

	public void setBox(Box box) {
		this.box = box;
	}
}
