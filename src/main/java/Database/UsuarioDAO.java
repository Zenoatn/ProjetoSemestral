package Database;

import Model.Preset;
import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ==========================================
// DAO PARA OPERAÇÕES DE CRUD DE USUÁRIOS
// ==========================================
public class UsuarioDAO {

    // ==========================================
    // READ: BUSCAR USUÁRIO PELO NOME COMPLETO
    // ==========================================
    public Usuario buscarPorNome(String nome) {
        String sql = "SELECT senha, nome FROM usuario WHERE nome = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(rs.getString("senha"), rs.getString("nome"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar usuário no banco: " + e.getMessage());
        }

        return usuario;
    }

    // ==========================================
    // CREATE: INSERIR NOVO USUÁRIO
    // ==========================================
    public void inserir(Usuario usuario) {
        String sql = "INSERT INTO usuario (senha, nome) VALUES (?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getSenha());
            stmt.setString(2, usuario.getNome());
            
            stmt.executeUpdate();
            System.out.println("Novo usuário cadastrado na Aiven com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar novo usuário: " + e.getMessage());
        }
    }

    // ==========================================
    // READ: BUSCAR IDs DOS PRESETS DO USUÁRIO
    // ==========================================
    public ArrayList<Integer> buscaPresetsUsuario(Usuario usuario){
        String sqlSelectPresets = "SELECT preset_id FROM presets WHERE usuario_nome = ?";
        ArrayList<Integer> presetsUsuario = new ArrayList<>();

        try (Connection conn = Conexao.getConnection()) {
            
            try (PreparedStatement stm = conn.prepareStatement(sqlSelectPresets)) {
                stm.setString(1, usuario.getNome());
                try(ResultSet rs = stm.executeQuery()){
                    while(rs.next()){
                        presetsUsuario.add(rs.getInt("preset_id"));
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Erro ao buscar os presets: " + e.getMessage());
            presetsUsuario.clear();
        }

        return presetsUsuario;
    }

    // ==========================================
    // DELETE: EXCLUIR USUÁRIO
    // ==========================================
    public void eliminar(Usuario usuario) {
        String sqlDelete = "DELETE FROM usuario WHERE nome = ?";

        try (Connection conn = Conexao.getConnection()) {
            
            try (PreparedStatement stmtPreset = conn.prepareStatement(sqlDelete)) {
                stmtPreset.setString(1, usuario.getNome());
                stmtPreset.executeUpdate();
                System.out.println("Usuário deletado");
            }   

        } catch (SQLException e) {
            System.err.println("Erro ao eliminar o usuário: " + e.getMessage());
        }
    }

    // ==========================================
    // BUSCA A SENHA DO USUÁRIO
    // ==========================================


    public String carregaSenha(Usuario usuario){
        String sqlSenha = "SELECT senha FROM usuario WHERE nome =?";
        String senha = "";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlSenha)) {

            stmt.setString(1, usuario.getNome());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    senha = rs.getString("senha");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar a senha do usuário no banco: " + e.getMessage());
        }

        return senha;
    }
}