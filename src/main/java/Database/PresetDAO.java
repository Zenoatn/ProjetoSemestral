package Database;

import Model.Preset;
import Model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

/**
 * Classe responsável por salvar os presets e as teclas configuradas no banco de dados.
 */
public class PresetDAO {

    /**
     * Salva o preset e os sons associados no banco de dados.
     */
    public void salvar(Preset preset) {
        // Ajustado para 'usuario_ra'
        String sqlPreset = "INSERT INTO presets (nome, usuario_ra) VALUES (?, ?)";
        
        // Ajustado para 'preset_id' e 'caminho_audio'
        String sqlSom = "INSERT INTO preset_sons (preset_id, tecla, caminho_audio) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtPreset = conn.prepareStatement(sqlPreset, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Salva o Nome do Preset e o RA do criador
            stmtPreset.setString(1, preset.getNome());
            stmtPreset.setString(2, preset.getCriador().getRa());
            stmtPreset.executeUpdate();

            // 2. Recupera o ID gerado automaticamente pelo MySQL
            try (ResultSet rs = stmtPreset.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGerado = rs.getInt(1);
                    preset.setPresetId(idGerado); 

                    // 3. Salva os arquivos de som de cada tecla mapeada
                    try (PreparedStatement stmtSom = conn.prepareStatement(sqlSom)) {
                        Map<String, String> sons = preset.getKit().getTodosOsSons();
                        
                        for (Map.Entry<String, String> entry : sons.entrySet()) {
                            String caminho = entry.getValue();
                            
                            // Só insere se a tecla tiver um arquivo de som associado
                            if (caminho != null && !caminho.trim().isEmpty()) {
                                stmtSom.setInt(1, idGerado);
                                stmtSom.setString(2, entry.getKey()); // Ex: "Q", "W"
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
    /**
     * Busca todos os presets salvos no banco de dados para um determinado usuário.
     */
    public java.util.List<Preset> buscarPresetsDoUsuario(Usuario usuario) {
        java.util.List<Preset> lista = new java.util.ArrayList<>();
        
        // Query para buscar os presets do usuário
        String sqlPresets = "SELECT preset_id, nome FROM presets WHERE usuario_ra = ?";
        // Query para buscar os sons daquele preset específico
        String sqlSons = "SELECT tecla, caminho_audio FROM preset_sons WHERE preset_id = ?";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmtPreset = conn.prepareStatement(sqlPresets)) {

            stmtPreset.setString(1, usuario.getRa());
            
            try (ResultSet rsPreset = stmtPreset.executeQuery()) {
                while (rsPreset.next()) {
                    int idPreset = rsPreset.getInt("preset_id");
                    String nomePreset = rsPreset.getString("nome");

                    // Criamos um DrumKit em branco para preencher com os sons do banco
                    Model.DrumKit kit = new Model.DrumKit();

                    // Busca os sons desse preset
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

                    // Cria o objeto Preset completo e adiciona na lista
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
}