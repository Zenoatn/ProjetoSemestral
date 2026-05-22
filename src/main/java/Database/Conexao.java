package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável por fabricar as conexões com o banco de dados MySQL na Aiven.
 */
public class Conexao {
    
    // Dados extraídos exatamente do seu painel da Aiven
    private static final String HOST = "mysql-dbps-testedbaula.j.aivencloud.com"; 
    private static final String PORTA = "13790"; 
    private static final String BANCO_DE_DADOS = "launchpad.db";
    
    // Aiven exige conexão segura na nuvem, por isso o sslMode=REQUIRED
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORTA + "/" + BANCO_DE_DADOS + "?sslMode=REQUIRED";
    private static final String USUARIO = "avnadmin";
    private static final String SENHA = "AVNS_QrHcnlLUt9aoqfrwXC1";

    /**
     * Método que abre a porta de conexão com o banco.
     */
    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (SQLException e) {
            System.err.println("Erro crítico: Falha ao conectar com o banco de dados na Aiven!");
            throw e;
        }
    }

    // =============================================================
    // TESTE RÁPIDO DE CONEXÃO
    // =============================================================
    public static void main(String[] args) {
        System.out.println("⏳ Tentando conectar à Aiven na nuvem...");
        try {
            Connection conn = getConnection();
            System.out.println("✅ SUCESSO ABSOLUTO! O seu Java acabou de logar no banco de dados da Aiven!");
            conn.close();
        } catch (SQLException e) {
            System.err.println("❌ ERRO AO CONECTAR:");
            e.printStackTrace();
        }
    }
}