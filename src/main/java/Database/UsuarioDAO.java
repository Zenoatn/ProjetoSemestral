package Database;

import Model.Preset;
import Model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe responsável por realizar as operações de banco de dados para a entidade Usuario.
 */
public class UsuarioDAO {

    /**
     * Busca um usuário pelo RA. Retorna o usuário se achar, ou null se não existir.
     */
    public Usuario buscarPorRa(String ra) {
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

    /**
     * Insere um novo usuário no banco de dados.
     */
    public void inserir(Usuario usuario) {
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

    public ArrayList<Integer> buscaPresetsUsuario(Usuario usuario){
        String sqlSelectPresets = "SELECT preset_id FROM presets WHERE usuario_ra = ?";
        ArrayList<Integer> presetsUsuario = new ArrayList<>();

        try (Connection conn = Conexao.getConnection()) {
            
            try (PreparedStatement stm = conn.prepareStatement(sqlSelectPresets)) {
                stm.setString(1, usuario.getRa());
                try(ResultSet rs = stm.executeQuery();){
                    while(rs.next()){
                        presetsUsuario.add(rs.getInt("preset_id"));
                    }
                }
            }

        }catch (SQLException e) {
            System.err.println("Erro ao buscar os preset: " + e.getMessage());
            presetsUsuario.clear();
        }

        return presetsUsuario;
    }

    
    public void eliminar(Usuario usuario) {
        String sqlDelete = "DELETE FROM usuario WHERE ra = ?";

        try (Connection conn = Conexao.getConnection()) {
            
        
            try (PreparedStatement stmtPreset = conn.prepareStatement(sqlDelete)) {
                stmtPreset.setString(1, usuario.getRa());
                stmtPreset.executeUpdate();
                System.out.println("Usuário deletado");
            }   


        } catch (SQLException e) {
            System.err.println("Erro ao eliminar o preset: " + e.getMessage());
        }

    }
        
}