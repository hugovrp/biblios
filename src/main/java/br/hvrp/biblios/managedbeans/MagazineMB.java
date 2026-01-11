package br.hvrp.biblios.managedbeans;

import java.io.Serializable;
import java.util.List;

import br.hvrp.biblios.dao.DAO;
import br.hvrp.biblios.model.Box;
import br.hvrp.biblios.model.Edition;
import br.hvrp.biblios.model.Magazine;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("magazineMB")
public class MagazineMB extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Magazine magazine = new Magazine();
    private DAO<Magazine> magazineDAO = new DAO<>(Magazine.class);
    private DAO<Edition> editionDAO = new DAO<>(Edition.class);
    private DAO<Box> boxDAO = new DAO<>(Box.class);
    private List<Magazine> allMagazines;
	
    public void save() {
        try {
            if(magazine.getId() == 0) {
                magazineDAO.insert(magazine);
            } else {
                magazineDAO.alter(magazine);
            }
            clean();
            
            showInfo(SUCCESS, "Revista salva com sucesso!");
        } catch (Exception e) {
        	showError(ERROR, "Erro ao salvar revista!");
        }
    }
    
    public void clean() {
        this.magazine = new Magazine();
        this.allMagazines = null;
    }
    
    public Magazine getMagazine() {
		return magazine;
	}
    
    public List<Edition> getEditions() {
		return editionDAO.listAll();
	}
    
    public List<Box> getBoxes() {
		return boxDAO.listAll();
	}
    
    public List<Magazine> getAllMagazines() {
    	if(allMagazines == null) {
            allMagazines = magazineDAO.listAll();
        }
        return allMagazines;
	}
    
	public void setMagazine(Magazine magazine) {
		this.magazine = magazine;
	}
}