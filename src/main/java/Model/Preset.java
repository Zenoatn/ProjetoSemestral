package Model;

// ==========================================
// ENTIDADE PRESET: CONFIGURAÇÃO SALVA DO USUÁRIO
// ==========================================
public class Preset {
   
    // Atributos de identificação e composição
    private int presetId;
    private String nome;
    private Usuario criador;
    private DrumKit kit;

    // ==========================================
    // CONSTRUTORES
    // ==========================================
    
    // Construtor para criação de um novo preset (ainda sem ID no banco)
    public Preset(String nome, Usuario criador, DrumKit kit){
        setNome(nome);
        setCriador(criador);
        setKit(kit);
    }

    // Construtor para carregamento de um preset já existente no banco
    public Preset(int presetId, String nome, Usuario criador, DrumKit kit){
        setPresetId(presetId);
        setNome(nome);
        setCriador(criador);
        setKit(kit);
    }

    // ==========================================
    // SETTERS (MODIFICADORES)
    // ==========================================
    public void setPresetId(int presetId) {
        this.presetId = presetId;
    }
    
    public void setCriador(Usuario criador) {
        this.criador = criador;
    }
    
    public void setKit(DrumKit kit) {
        this.kit = kit;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    // ==========================================
    // GETTERS (ACESSORES)
    // ==========================================
    public Usuario getCriador() {
        return criador;
    }
    
    public DrumKit getKit() {
        return kit;
    }
    
    public String getNome() {
        return nome;
    }
    
    public int getPresetId() {
        return presetId;
    }

    // ==========================================
    // MÉTODOS AUXILIARES
    // ==========================================
    
    // Define como o objeto será exibido em componentes visuais (ex: Menus)
    @Override
    public String toString(){
        return nome;
    }
}