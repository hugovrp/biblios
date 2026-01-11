package br.hvrp.biblios.managedbeans;

import java.io.Serializable;
import java.util.List;

import br.hvrp.biblios.dao.DAO;
import br.hvrp.biblios.model.Series;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named("seriesMB")
public class SeriesMB extends BaseBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Series series = new Series();
	private DAO<Series> dao = new DAO<>(Series.class);
	private List<Series> allSeries;
	
	public void save() {
		try {
			if(series.getId() == 0) {
				dao.insert(series);
			} else {
				dao.alter(series);
			}
			
			this.series = new Series();
			this.allSeries = null;
			
			showInfo(SUCCESS, "Coleção salva com sucesso!");
		} catch(Exception e) {
			e.printStackTrace();
			showError(ERROR, "Erro ao salvar coleção.");
		}
	}
	
	public Series getSeries() {
		return series;
	}

	public DAO<Series> getDao() {
		return dao;
	}
	
	public List<Series> getAllSeries() {
		if(allSeries == null) {
			allSeries = dao.listAll();
		}
		return allSeries;
	}

	public void setSeries(Series series) {
		this.series = series;
	}

	public void setDao(DAO<Series> dao) {
		this.dao = dao;
	}

	public void setAllSeries(List<Series> allSeries) {
		this.allSeries = allSeries;
	}
}