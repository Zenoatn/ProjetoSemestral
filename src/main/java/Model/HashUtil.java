package Model;

import java.security.MessageDigest;

public class HashUtil {

    // ==========================================
    // GERAÇÃO DE HASH SHA-256
    // ==========================================
    public static String gerarHash(String senhaOriginal) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = algorithm.digest(senhaOriginal.getBytes("UTF-8"));
            
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                hexString.append(String.format("%02X", 0xFF & b));
            }
            return hexString.toString();
            
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar a senha", e);
        }
    }
}