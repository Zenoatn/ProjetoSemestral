package Model;

/* Classe modelo que representa o usuário do sistema.
 * Responsável por armazenar as credenciais básicas que serão 
 * utilizadas na consulta e inserção no banco de dados.
 */


public class Usuario {
    
    private String ra;
    private String nome;

    /**
     * Construtor padrão do Usuário.
     * * @param ra   Registro Acadêmico do aluno (Chave primária no banco).
     * @param nome Nome completo do aluno.
     */
    public Usuario(String ra, String nome) {
        this.ra = ra;
        this.nome = nome;
    }

    // --- Getters e Setters ---

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}