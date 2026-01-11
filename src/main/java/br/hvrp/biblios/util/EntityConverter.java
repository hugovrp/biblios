package br.hvrp.biblios.util;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import java.io.Serializable;

@FacesConverter("entityConverter")
public class EntityConverter implements Converter<Object>, Serializable {
	private static final long serialVersionUID = 1L;

	@Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value == null || value.isEmpty()) {
            return null;
        }
        // Recupera o objeto guardado no mapa de atributos do componente
        return component.getAttributes().get(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if(value == null) {
            return "";
        }
        
        String id = String.valueOf(getEntityId(value));
        if(id != null) {
            component.getAttributes().put(id, value);
            return id;
        }
        return value.toString();
    }

    private Object getEntityId(Object bean) {
        try {
            // Tenta pegar o campo id
            var field = bean.getClass().getDeclaredField("id");
            field.setAccessible(true);
            return field.get(bean);
        } catch(Exception e) {
            return null;
        }
    }
}