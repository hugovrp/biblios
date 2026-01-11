package br.hvrp.biblios.filter;

import java.io.IOException;

import br.hvrp.biblios.managedbeans.UserMB;
import jakarta.enterprise.inject.spi.CDI;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class AuthFilter extends HttpFilter implements Filter {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		UserMB userMB = CDI.current().select(UserMB.class).get();
	    String path = request.getRequestURI();

        // Permite acesso à página de login, cadastro e recursos estáticos
        boolean isLoginPage = path.contains("login.xhtml");
        boolean isRegisterPage = path.contains("register.xhtml");
        boolean isConfirmPage = path.contains("confirm.xhtml");
        boolean isPendentePage = path.contains("pending_confirm.xhtml");
        boolean isResource = path.contains("jakarta.faces.resource");
        boolean isLoggedIn = (userMB != null && userMB.getLoggedUser() != null);
        
        if(isLoggedIn || isLoginPage || isRegisterPage || isConfirmPage || isPendentePage || isResource) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
        }
	}
			
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Inicialização, se necessário
	}
	
	@Override
	public void destroy() {
		 // Finalização, se necessário
	}
}