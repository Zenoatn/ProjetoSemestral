package Model;

// ==========================================
// ENTIDADE USUÁRIO: IDENTIFICAÇÃO NO SISTEMA
// ==========================================
public class Usuario {
    
    // Atributos de credenciais (Nome é a chave essencial do banco)
    private String senha;
    private String nome;

    // ==========================================
    // CONSTRUTOR
    // ==========================================
    public Usuario(String senha, String nome) {
        this.senha = senha;
        this.nome = nome;
    }

    // ==========================================
    // GETTERS E SETTERS
    // ==========================================
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}