package Model;

public class Preset {
   
    private int presetId;
    private String nome;
    private Usuario criador;
    private DrumKit kit;

    public Preset(String nome, Usuario criador, DrumKit kit){

        setNome(nome);
        setCriador(criador);
        setKit(kit);
    }

      public Preset(int presetId, String nome, Usuario criador, DrumKit kit){

        setPresetId(presetId);
        setNome(nome);
        setCriador(criador);
        setKit(kit);
    }

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

    

    @Override
    public String toString(){
        return nome;
    }
}
