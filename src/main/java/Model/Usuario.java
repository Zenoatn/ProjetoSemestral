package Model;

// ==========================================
// ENTIDADE USUÁRIO: IDENTIFICAÇÃO NO SISTEMA
// ==========================================
public class Usuario {
    
    // Atributos de credenciais (RA é a chave primária no banco)
    private String ra;
    private String nome;

    // ==========================================
    // CONSTRUTOR
    // ==========================================
    public Usuario(String ra, String nome) {
        this.ra = ra;
        this.nome = nome;
    }

    // ==========================================
    // GETTERS E SETTERS
    // ==========================================
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