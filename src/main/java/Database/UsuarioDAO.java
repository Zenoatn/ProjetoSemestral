package Database;

import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Classe responsável por realizar as operações de banco de dados
 * para a entidade Usuario.
 */
public class UsuarioDAO {

    /**
     * Busca um usuário pelo RA. Se não existir, cadastra um novo automaticamente.
     */
    public Usuario buscarOuCriar(String ra, String nome) {
        Usuario usuario = buscarPorRa(ra);

        if (usuario == null) {
            usuario = new Usuario(ra, nome);
            inserir(usuario);
        } else {
            System.out.println("Usuário encontrado no banco: " + usuario.getNome());
        }

        return usuario;
    }

    private Usuario buscarPorRa(String ra) {
        String sql = "SELECT ra, nome FROM usuario WHERE ra = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, ra);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(rs.getString("ra"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário no banco: " + e.getMessage());
        }

        return usuario;
    }

    private void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (ra, nome) VALUES (?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getRa());
            stmt.setString(2, usuario.getNome());
            
            stmt.executeUpdate();
            System.out.println("Novo usuário cadastrado na Aiven com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar novo usuário: " + e.getMessage());
        }
    }
}