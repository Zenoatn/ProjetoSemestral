package Database;

import Model.Preset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

// ==========================================
// DAO PARA OPERAÇÕES DE CRUD DE PRESETS
// ==========================================
public class PresetDAO {

    // ==========================================
    // CREATE: INSERIR NOVO PRESET E MAPEAMENTOS
    // ==========================================
    public void salvar(Preset preset) {
        String sqlPreset = "INSERT INTO presets (nome, usuario_ra) VALUES (?, ?)";
        String sqlSom = "INSERT INTO preset_sons (preset_id, tecla, caminho_audio) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtPreset = conn.prepareStatement(sqlPreset, Statement.RETURN_GENERATED_KEYS)) {

            stmtPreset.setString(1, preset.getNome());
            stmtPreset.setString(2, preset.getCriador().getRa());
            stmtPreset.executeUpdate();

            try (ResultSet rs = stmtPreset.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    preset.setPresetId(idGerado); 

                    try (PreparedStatement stmtSom = conn.prepareStatement(sqlSom)) {
                        Map<String, String> sons = preset.getKit().getTodosOsSons();
                        
                        for (Map.Entry<String, String> entry : sons.entrySet()) {
                            String caminho = entry.getValue();
                            
                            if (caminho != null && !caminho.trim().isEmpty()) {
                                stmtSom.setInt(1, idGerado);
                                stmtSom.setString(2, entry.getKey()); 
                                stmtSom.setString(3, caminho);
                                stmtSom.executeUpdate();
                            }
                        }
                    }
                }
            }
            System.out.println("Preset salvo no banco de dados com sucesso!");

        } catch (SQLException e) {
            System.err.println("Erro ao salvar o preset no banco: " + e.getMessage());
        }
    }

    // ==========================================
    // READ: BUSCAR PRESETS POR USUÁRIO
    // ==========================================
    public java.util.List<Preset> buscarPresetsDoUsuario(Model.Usuario usuario) {
        java.util.List<Preset> lista = new java.util.ArrayList<>();
        
        String sqlPresets = "SELECT preset_id, nome FROM presets WHERE usuario_ra = ?";
        String sqlSons = "SELECT tecla, caminho_audio FROM preset_sons WHERE preset_id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtPreset = conn.prepareStatement(sqlPresets)) {

            stmtPreset.setString(1, usuario.getRa());
            
            try (ResultSet rsPreset = stmtPreset.executeQuery()) {
                while (rsPreset.next()) {
                    int idPreset = rsPreset.getInt("preset_id");
                    String nomePreset = rsPreset.getString("nome");

                    Model.DrumKit kit = new Model.DrumKit();

                    try (PreparedStatement stmtSons = conn.prepareStatement(sqlSons)) {
                        stmtSons.setInt(1, idPreset);
                        try (ResultSet rsSons = stmtSons.executeQuery()) {
                            while (rsSons.next()) {
                                String tecla = rsSons.getString("tecla");
                                String caminho = rsSons.getString("caminho_audio");
                                kit.associarSom(tecla, caminho);
                            }
                        }
                    }

                    Preset p = new Preset(nomePreset, usuario, kit);
                    p.setPresetId(idPreset);
                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar presets no banco: " + e.getMessage());
        }

        return lista;
    }

    // ==========================================
    // UPDATE: ALTERAR NOME DO PRESET
    // ==========================================
    public void atualizarNome(int idPreset, String novoNome) {
        String sql = "UPDATE presets SET nome = ? WHERE preset_id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, novoNome);
            stmt.setInt(2, idPreset);
            stmt.executeUpdate();
            System.out.println("Nome do preset atualizado com sucesso na Aiven!");

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar nome do preset: " + e.getMessage());
        }
    }

    // ==========================================
    // UPDATE: SINCRONIZAR SONS (CLEAR & INSERT)
    // ==========================================
    public void atualizarSons(int idPreset, Model.DrumKit kit) {
        String sqlDelete = "DELETE FROM preset_sons WHERE preset_id = ?";
        String sqlInsert = "INSERT INTO preset_sons (preset_id, tecla, caminho_audio) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection()) {
            
            try (PreparedStatement stmtDel = conn.prepareStatement(sqlDelete)) {
                stmtDel.setInt(1, idPreset);
                stmtDel.executeUpdate();
            }

            try (PreparedStatement stmtIns = conn.prepareStatement(sqlInsert)) {
                Map<String, String> sons = kit.getTodosOsSons();
                for (Map.Entry<String, String> entry : sons.entrySet()) {
                    String caminho = entry.getValue();
                    if (caminho != null && !caminho.trim().isEmpty()) {
                        stmtIns.setInt(1, idPreset);
                        stmtIns.setString(2, entry.getKey());
                        stmtIns.setString(3, caminho);
                        stmtIns.executeUpdate();
                    }
                }
            }
            System.out.println("Sons do preset atualizados na nuvem!");
            
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar sons: " + e.getMessage());
        }
    }

    // ==========================================
    // DELETE: EXCLUIR PRESET EM CASCATA MANUAL
    // ==========================================
    public void eliminar(int idPreset) {
        String sqlSons = "DELETE FROM preset_sons WHERE preset_id = ?";
        String sqlPreset = "DELETE FROM presets WHERE preset_id = ?";

        try (Connection conn = Conexao.getConnection()) {
            
            try (PreparedStatement stmtSons = conn.prepareStatement(sqlSons)) {
                stmtSons.setInt(1, idPreset);
                stmtSons.executeUpdate();
            }

            try (PreparedStatement stmtPreset = conn.prepareStatement(sqlPreset)) {
                stmtPreset.setInt(1, idPreset);
                stmtPreset.executeUpdate();
            }
            
            System.out.println("Preset e sons eliminados com sucesso da nuvem!");

        } catch (SQLException e) {
            System.err.println("Erro ao eliminar o preset: " + e.getMessage());
        }
    }
}