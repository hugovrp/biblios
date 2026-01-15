# 📚 Biblios - Sistema de Gerenciamento de Biblioteca

> Sistema web para gerenciamento de biblioteca de revistas em quadrinhos, desenvolvido em Java com JSF, permitindo controle de acervo, empréstimos e usuários.

[![Java](https://img.shields.io/badge/Java-17+-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![JSF](https://img.shields.io/badge/JSF-4.0-blue?style=for-the-badge&logo=java)](https://jakarta.ee/specifications/faces/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue?style=for-the-badge&logo=postgresql)](https://www.postgresql.org/)
[![Hibernate](https://img.shields.io/badge/Hibernate-6.x-darkred?style=for-the-badge&logo=hibernate)](https://hibernate.org/)
[![Maven](https://img.shields.io/badge/Maven-3.x-red?style=for-the-badge&logo=apachemaven)](https://maven.apache.org/)

---

## 📋 Sobre o Projeto

**Biblios** é um sistema completo de gerenciamento de biblioteca especializada em revistas em quadrinhos que permite:

- 📦 **Gestão de Caixas** - Organização física do acervo em caixas numeradas
- 📖 **Controle de Coleções** - Cadastro de séries e edições
- 📕 **Gerenciamento de Revistas** - Controle individual de cada exemplar
- 👥 **Cadastro de Usuários** - Registro com confirmação de e-mail
- 📤 **Sistema de Empréstimos** - Controle de empréstimos com prazo de 7 dias
- ⏰ **Gestão de Devoluções** - Rastreamento de status e atrasos
- 📊 **Relatórios** - Visualização de empréstimos ativos, atrasados e histórico por usuário
- 🔐 **Autenticação Segura** - Login com hash SHA-256 e confirmação de e-mail

> **Disciplina**: Desenvolvimento de Aplicações Web  
> **Curso**: Sistemas para Internet  
> **Tipo**: Trabalho Individual

---

## 🔧 Configuração do Banco de Dados

### 1. Criar o banco de dados PostgreSQL
```sql
CREATE DATABASE Biblios;
```

### 2. Configurar credenciais

Edite o arquivo `src/main/resources/META-INF/persistence.xml`:
```xml
<persistence-unit name="Biblios">
    <properties>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:postgresql://localhost:5432/Biblios" />
        <property name="jakarta.persistence.jdbc.user" value="seu_usuario" />
        <property name="jakarta.persistence.jdbc.password" value="sua_senha" />
        <property name="hibernate.hbm2ddl.auto" value="update" />
    </properties>
</persistence-unit>
```

> 📝 **Nota**: O Hibernate criará automaticamente todas as tabelas necessárias através da configuração `hibernate.hbm2ddl.auto = update`

---

## 📧 Configuração de E-mail

Para o sistema de confirmação de e-mail funcionar, configure as credenciais SMTP no arquivo `EmailUtil.java`:
```java
properties.put("mail.smtp.host", "smtp.gmail.com");
properties.put("mail.smtp.port", "587");
// Configure seu e-mail e senha de aplicativo
final String username = "seu_email@gmail.com";
final String password = "sua_senha_app";
```

> ⚠️ **Importante**: Para Gmail, você precisa gerar uma [senha de aplicativo](https://support.google.com/accounts/answer/185833) com autenticação de dois fatores habilitada.

---

## 💻 Arquitetura do Sistema

### Padrão MVC com JSF
```
┌──────────────┐      ┌─────────────────────┐      ┌──────────────┐
│     View     │ ───> │   ManagedBean       │ ───> │    Model     │
│   (XHTML)    │ <─── │   (Controller)      │ <─── │  (Entity)    │
└──────────────┘      └─────────────────────┘      └──────────────┘
                               │                            │
                               ↓                            ↓
                      ┌─────────────────┐          ┌──────────────┐
                      │   AuthFilter    │          │     DAO      │
                      │  (Servlet)      │          │   (JPA)      │
                      └─────────────────┘          └──────────────┘
                                                            │
                                                            ↓
                                                   ┌──────────────┐
                                                   │  PostgreSQL  │
                                                   └──────────────┘
```

---

## 🎯 Funcionalidades Principais

### 1. Sistema de Autenticação e Registro

**Registro com Confirmação de E-mail**:
```java
public String register() {
    UserDAO dao = new UserDAO();
    user.setProfile("user");
    user.setConfirmedEmail(false);
    user.setPassword(HashUtil.hashPassword(user.getPassword()));
    
    user.setConfirmationToken(UUID.randomUUID().toString());
    user.setTokenExpirationDate(LocalDateTime.now().plusDays(1));
    
    dao.insert(user);
    
    EmailUtil.sendConfirmationEmail(user.getEmail(), user.getName(), user.getConfirmationToken());
    
    return "pending_confirm?faces-redirect=true";
}
```

**Filtro de Autenticação**:
```java
@WebFilter("/*")
public class AuthFilter extends HttpFilter implements Filter {
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        UserMB userMB = CDI.current().select(UserMB.class).get();
        String path = request.getRequestURI();
        
        boolean isPublicPage = path.contains("login.xhtml") || 
                               path.contains("register.xhtml") ||
                               path.contains("confirm.xhtml");
        boolean isLoggedIn = (userMB != null && userMB.getLoggedUser() != null);
        
        if(isLoggedIn || isPublicPage || path.contains("jakarta.faces.resource")) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/login.xhtml");
        }
    }
}
```

---

### 2. Gestão de Coleções e Edições

**Estrutura Hierárquica**:
- **Séries** (Coleções): Ex: "Batman", "Superman"
- **Edições**: Números específicos de cada série por ano
- **Revistas**: Exemplares físicos de cada edição
```java
@ViewScoped
@Named("editionMB")
public class EditionMB extends BaseBean {
    public void save() {
        if(edition.getId() == 0) {
            editionDao.insert(edition);
            showInfo(SUCCESS, "Edição cadastrada!");
        } else {
            editionDao.alter(edition);
            showInfo(SUCCESS, "Edição atualizada!");
        }
        clean();
    }
}
```

---

### 3. Sistema de Empréstimos

**Regras de Negócio**:
- ✅ Usuário pode ter apenas 1 empréstimo ativo por vez
- ⏰ Prazo de devolução: 7 dias
- 🔄 Status automático: ACTIVE → OVERDUE → RETURNED

**Solicitar Empréstimo**:
```java
public void requestLoan(Magazine magazine) {
    User user = userMB.getLoggedUser();
    
    // Valida se usuário já tem empréstimo ativo
    if(loanDAO.hasActiveLoan(user)) {
        showError(LIMIT, "Você já possui um empréstimo ativo.");
        return;
    }
    
    // Valida disponibilidade da revista
    Magazine freshMagazine = magazineDAO.findById(magazine.getId());
    if(freshMagazine.getStatus() != AvailabilityStatus.AVAILABLE) {
        showError(UNAVAILABLE, "Esta revista não está disponível.");
        return;
    }
    
    // Cria empréstimo
    Loan newLoan = new Loan();
    newLoan.setUser(user);
    newLoan.setMagazine(freshMagazine);
    newLoan.setStatus(LoanStatus.ACTIVE);
    
    // Atualiza status da revista
    freshMagazine.setStatus(AvailabilityStatus.BORROWED);
    
    magazineDAO.alter(freshMagazine);
    loanDAO.insert(newLoan);
    
    showInfo(SUCCESS, "Empréstimo realizado com sucesso!");
}
```

**Devolução**:
```java
public void returnMagazine(Loan loan) {
    loan.setStatus(LoanStatus.RETURNED);
    loan.setActualReturnDate(Calendar.getInstance());
    
    Magazine mag = loan.getMagazine();
    mag.setStatus(AvailabilityStatus.AVAILABLE);
    
    magazineDAO.alter(mag);
    loanDAO.alter(loan);
    
    showInfo(SUCCESS, "Devolução registrada.");
}
```

---

### 4. Relatórios

**Tipos de Relatórios Disponíveis**:

1. **Empréstimos Ativos**: Todos os empréstimos em andamento
2. **Empréstimos Atrasados**: Ultrapassaram o prazo de 7 dias
3. **Histórico por Usuário**: Todos os empréstimos de um usuário específico
```java
public void generateReport() {
    switch(reportType) {
        case "ALL_BORROWED":
            filteredLoans = loanDAO.findAllActive();
            break;
        case "OVERDUE":
            filteredLoans = loanDAO.findOverdue();
            break;
        case "USER":
            if(selectedUserForReport != null) {
                filteredLoans = loanDAO.findByUser(selectedUserForReport);
            }
            break;
    }
}
```

---

## 📊 Modelo de Dados

### Diagrama de Relacionamentos
```
┌──────────────┐       ┌──────────────┐
│    Series    │       │     User     │
│  (Coleção)   │       │  (Usuário)   │
└──────┬───────┘       └──────┬───────┘
       │ 1                    │ 1
       │                      │
       │ N                    │ N
┌──────┴───────┐       ┌──────┴───────┐
│   Edition    │       │     Loan     │
│   (Edição)   │       │ (Empréstimo) │
└──────┬───────┘       └──────┬───────┘
       │ 1                     │
       │                       │ N
       │ N                     │
┌──────┴───────┐               │ 1
│   Magazine   │───────────────┘
│   (Revista)  │      N:1
└──────┬───────┘
       │ N
       │
       │ 1
┌──────┴───────┐
│     Box      │
│   (Caixa)    │
└──────────────┘
```

---

## 🎓 Casos de Uso

### Fluxo de Usuário

1. **Registro** → Recebe e-mail de confirmação
2. **Confirma E-mail** → Clica no link recebido
3. **Login** → Acessa o sistema
4. **Consulta Revistas Disponíveis** → Visualiza acervo
5. **Solicita Empréstimo** → Reserva uma revista
6. **Devolve Revista** → Marca devolução no prazo
7. **Consulta Histórico** → Visualiza seus empréstimos

### Fluxo Administrativo

1. **Cadastra Caixas** → Organiza armazenamento físico
2. **Cadastra Coleções** → Registra séries de quadrinhos
3. **Cadastra Edições** → Adiciona números específicos
4. **Cadastra Revistas** → Inclui exemplares no acervo
5. **Monitora Empréstimos** → Acompanha status e atrasos
6. **Gera Relatórios** → Analisa movimentação do acervo

---

## 🚀 Como Executar

1. **Clone o repositório**
2. **Configure o banco de dados** conforme instruções acima
3. **Configure as credenciais de e-mail** no `EmailUtil.java`
4. **Execute com Maven**:
```bash
   mvn clean install
```
5. **Deploy no servidor** (WildFly/GlassFish)
6. **Acesse**: `http://localhost:8080/biblios`

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 📝 Licença


Este projeto é um trabalho acadêmico desenvolvido para a disciplina de **Desenvolvimento de Aplicações Web** do curso de **Sistemas para Internet**.

---

## 👨‍💻 Autor

**Hugo Vinícius Rodrigues Pereira**

[![GitHub](https://img.shields.io/badge/GitHub-hugovrp-black?style=flat-square&logo=github)](https://github.com/hugovrp)