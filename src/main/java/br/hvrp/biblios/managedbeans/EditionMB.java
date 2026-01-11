package br.hvrp.biblios.managedbeans;

import java.io.Serializable;
import java.util.List;

import br.hvrp.biblios.dao.DAO;
import br.hvrp.biblios.model.Edition;
import br.hvrp.biblios.model.Series;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("editionMB")
public class EditionMB extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private Edition edition = new Edition();
	private DAO<Edition> editionDao = new DAO<>(Edition.class);
	private DAO<Series> seriesDao = new DAO<>(Series.class);
	private List<Edition> allEditions;

	public void save() {
		try {
			if(edition.getId() == 0) {
				editionDao.insert(edition);
				showInfo(SUCCESS, "Edição cadastrada!");
			} else {
				editionDao.alter(edition);
				showInfo(SUCCESS, "Edição atualizada!");
			}
			
			clean();
	 
		} catch(Exception e) {
			e.printStackTrace();
			showError(ERROR, "Erro ao salvar edição!");
		}
	}
	
	public List<Series> getSeriesList() {
        return seriesDao.listAll();
    }
	
	public void clean() {
        this.edition = new Edition();
        this.allEditions = null;
    }
	
	public Edition getEdition() {
		return edition;
	}

	public DAO<Edition> getEditionDao() {
		return editionDao;
	}
	
	public List<Edition> getAllEditions() {
		if(allEditions == null) {
			allEditions = editionDao.listAll();
		}
		return allEditions;
	}

	public void setEdition(Edition edition) {
		this.edition = edition;
	}

	public void setEditionDao(DAO<Edition> editionDao) {
		this.editionDao = editionDao;
	}

	public void setAllEditions(List<Edition> allEditions) {
		this.allEditions = allEditions;
	}
}
