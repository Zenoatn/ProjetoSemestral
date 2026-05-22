package ui;

import Model.Usuario;
import Model.DrumKit;
import Model.Preset;
import Model.Presets;
import Audio.AudioPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;

/**
 * Interface gráfica principal do Launchpad.
 * Desenvolvida exclusivamente em Java, sem uso de geradores gráficos automáticos.
 */
public class LaunchpadAvancado extends JFrame {

    // Atributos para controle de dados
    private Usuario usuarioLogado;
    private DrumKit meuDrumKit;

    private List<Preset> presetsUsuario;
    
    // Controle de estado da interface
    private boolean emModoEdicao = false;
    private JButton btnPreset;
    private JLabel titulo;
    
    // O menu é um atributo da classe para podermos adicionar itens nele dinamicamente
    private JPopupMenu menuPresets;

    /**
     * Construtor da janela do Launchpad.
     */
    public LaunchpadAvancado(Usuario usuario, DrumKit kit) {
        this.usuarioLogado = usuario;
        this.meuDrumKit = kit;

        // Convertido para ArrayList mutável para evitar erros de UnsupportedOperationException ao adicionar itens do banco
        presetsUsuario = new java.util.ArrayList<>(
            Presets.carregarPresetsPadrao().subList(1, Presets.carregarPresetsPadrao().size())
        );

        // =======================================================================
        // BUSCA AUTOMÁTICA DE PRESETS NA NUVEM DA AIVEN
        // =======================================================================
        Database.PresetDAO presetDAO = new Database.PresetDAO();
        List<Preset> presetsBanco = presetDAO.buscarPresetsDoUsuario(usuarioLogado);
        presetsUsuario.addAll(presetsBanco); 
        // =======================================================================

        // 1. CONFIGURAÇÕES DA JANELA
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true); 
        getContentPane().setBackground(Color.decode("#1E1E24")); 

        // 2. CRIANDO A BARRA DE TÍTULO ESCURA CUSTOMIZADA
        JPanel barraTitulo = new JPanel(new BorderLayout());
        barraTitulo.setBackground(Color.decode("#121212"));
        barraTitulo.setPreferredSize(new Dimension(getWidth(), 35));

        titulo = new JLabel("   Launchpad Customizável - Usuário: " + usuarioLogado.getNome());
        titulo.setForeground(Color.LIGHT_GRAY);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 14));
        barraTitulo.add(titulo, BorderLayout.WEST);

        JButton btnFechar = new JButton("X");
        btnFechar.setFocusPainted(false);
        btnFechar.setBorderPainted(false);
        btnFechar.setBackground(Color.decode("#121212"));
        btnFechar.setForeground(Color.GRAY);
        btnFechar.setFont(new Font("SansSerif", Font.BOLD, 16));
        btnFechar.addActionListener(e -> System.exit(0));
        
        btnFechar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btnFechar.setForeground(Color.WHITE);
                btnFechar.setBackground(Color.RED);
            }
            public void mouseExited(MouseEvent evt) {
                btnFechar.setForeground(Color.GRAY);
                btnFechar.setBackground(Color.decode("#121212"));
            }
        });
        barraTitulo.add(btnFechar, BorderLayout.EAST);

        MoverJanela listener = new MoverJanela(this);
        barraTitulo.addMouseListener(listener);
        barraTitulo.addMouseMotionListener(listener);

        add(barraTitulo, BorderLayout.NORTH);

        // 3. CRIANDO OS DRUM PADS (TECLADO 3x4)
        JPanel painelPad = new JPanel(new GridLayout(3, 4, 20, 20)); 
        painelPad.setBackground(Color.decode("#1E1E24"));
        painelPad.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        String[] coresHex = {
            "#FF3366", "#FF6699", "#CC33FF", "#06e2c5", 
            "#FF6699", "#CC33FF", "#6600FF", "#06e2c5", 
            "#CC00FF", "#6600FF", "#0066FF", "#06e2c5"  
        };

        int[] codigosTeclas = {
            KeyEvent.VK_Q, KeyEvent.VK_W, KeyEvent.VK_E, KeyEvent.VK_R,
            KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D, KeyEvent.VK_F,
            KeyEvent.VK_Z, KeyEvent.VK_X, KeyEvent.VK_C, KeyEvent.VK_V
        };
        String[] nomesTeclas = {
            "Q", "W", "E", "R",
            "A", "S", "D", "F",
            "Z", "X", "C", "V"
        };

        for (int i = 0; i < 12; i++) {
            PadLuminoso pad = new PadLuminoso(Color.decode(coresHex[i]), nomesTeclas[i]);
            configurarKeyBinding(pad, codigosTeclas[i]);
            painelPad.add(pad);
        }

        add(painelPad, BorderLayout.CENTER);

        // 4. CRIANDO O RODAPÉ (SISTEMA DE PRESETS)
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBackground(Color.decode("#121212"));

        btnPreset = new JButton("Presets ▾");
        btnPreset.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPreset.setForeground(Color.LIGHT_GRAY); 
        btnPreset.setBackground(Color.decode("#2A2A35")); 
        btnPreset.setFocusPainted(false);
        btnPreset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        menuPresets = new JPopupMenu();
        carregarMenuPresets();

        btnPreset.addActionListener(e -> {
            if (emModoEdicao) {
                salvarPreset();
            } else {
                menuPresets.show(btnPreset, 0, btnPreset.getHeight());
            }
        });

        painelRodape.add(btnPreset);
        add(painelRodape, BorderLayout.SOUTH);
    }

    private void ativarModoEdicao() {
        emModoEdicao = true;
        meuDrumKit = new DrumKit();
        titulo.setText("   Launchpad Customizável - [CRIANDO NOVO PRESET]");
        titulo.setForeground(Color.decode("#FF3366")); 
        
        btnPreset.setText("💾 Salvar Preset");
        btnPreset.setBackground(Color.decode("#00CC66")); 
        btnPreset.setForeground(Color.BLACK);
    }

    private void salvarPreset() {
        String nomePreset = JOptionPane.showInputDialog(this, "Digite o nome para o seu novo Preset:", "Salvar Preset", JOptionPane.PLAIN_MESSAGE);
        
        if (nomePreset != null && !nomePreset.trim().isEmpty()) {
            
            Preset novoPreset = new Preset(nomePreset, usuarioLogado, meuDrumKit);
            
            // ==========================================
            // GRAVAÇÃO DO PRESET DIRETO NA NUVEM DA AIVEN
            // ==========================================
            Database.PresetDAO dao = new Database.PresetDAO();
            dao.salvar(novoPreset);
            // ==========================================

            presetsUsuario.add(novoPreset);

            // Adiciona o preset recém-criado na lista suspensa visualmente
            JMenuItem novoItem = new JMenuItem(nomePreset);
            novoItem.addActionListener(e -> { carregarPreset(novoPreset); });
            menuPresets.insert(novoItem, 0); // Insere no topo da lista customizada
            
            JOptionPane.showMessageDialog(this, "Preset '" + nomePreset + "' salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            
            // Restaura o visual padrão da interface
            emModoEdicao = false;
            titulo.setText("   Launchpad Customizável - Usuário: " + usuarioLogado.getNome());
            titulo.setForeground(Color.LIGHT_GRAY);
            
            btnPreset.setText("Presets ▾");
            btnPreset.setBackground(Color.decode("#2A2A35"));
            btnPreset.setForeground(Color.LIGHT_GRAY);
        }
    }

    private void carregarPreset(Preset preset){
        meuDrumKit = preset.getKit();
        JOptionPane.showMessageDialog(this, "Preset '" + preset.getNome() + "' carregado com sucesso!");
    }

    private void carregarMenuPresets() {
        menuPresets.removeAll();

        // Lista dinamicamente todos os presets carregados (Padrões + Nuvem)
        for(Preset p : presetsUsuario) {
            JMenuItem item = new JMenuItem(p.getNome());
            item.addActionListener(e -> { carregarPreset(p); });
            menuPresets.add(item);
        }

        menuPresets.addSeparator();

        // Opção para ativar o mapeamento de um novo kit
        JMenuItem itemNovo = new JMenuItem("Criar Novo Preset...");
        itemNovo.addActionListener(e -> ativarModoEdicao());
        menuPresets.add(itemNovo);
    }

    private void configurarKeyBinding(JButton pad, int keyCode) {
        InputMap im = pad.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = pad.getActionMap();

        im.put(KeyStroke.getKeyStroke(keyCode, 0, false), "pressionar");
        im.put(KeyStroke.getKeyStroke(keyCode, 0, true), "soltar");

        am.put("pressionar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!pad.getModel().isPressed()) {
                    pad.getModel().setArmed(true);
                    pad.getModel().setPressed(true); 
                    
                    String letraBotao = pad.getText();

                    if (emModoEdicao) {
                        JFileChooser fileChooser = new JFileChooser(new File("Assets"));
                        fileChooser.setDialogTitle("Escolha um arquivo .wav (16-bit) para a tecla " + letraBotao);
                        
                        int resposta = fileChooser.showOpenDialog(LaunchpadAvancado.this);
                        
                        if (resposta == JFileChooser.APPROVE_OPTION) {
                            File arquivoEscolhido = fileChooser.getSelectedFile();
                            meuDrumKit.associarSom(letraBotao, arquivoEscolhido.getPath());
                        }
                        
                        pad.getModel().setArmed(false);
                        pad.getModel().setPressed(false);
                        
                    } else {
                        String caminhoSom = meuDrumKit.obterCaminhoSom(letraBotao);
                        AudioPlayer.tocarSom(caminhoSom);
                    }
                }
            }
        });

        am.put("soltar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!emModoEdicao) {
                    pad.getModel().setArmed(false);
                    pad.getModel().setPressed(false); 
                }
            }
        });
    }

    public static void main(String[] args) {
        // Ponto de entrada movido para o TelaLogin.java
    }

    // =========================================================
    // SUBCLASSE INTERNA: ESTILIZAÇÃO E RENDERIZAÇÃO DOS PADS
    // =========================================================
    class PadLuminoso extends JButton {
        private Color corBase;

        public PadLuminoso(Color cor, String tecla) {
            super(tecla);
            this.corBase = cor;
            setContentAreaFilled(false); 
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR)); 
            
            double luminosidade = (0.299 * cor.getRed()) + (0.587 * cor.getGreen()) + (0.114 * cor.getBlue());
            if (luminosidade > 140) {
                setForeground(new Color(0, 0, 0, 80)); 
            } else {
                setForeground(new Color(255, 255, 255, 80)); 
            }
            setFont(new Font("SansSerif", Font.BOLD, 24));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            boolean pressionado = getModel().isPressed();

            Color corCentro = pressionado ? Color.WHITE : corBase.brighter().brighter();
            Color corBorda = pressionado ? corBase : corBase.darker();
            int margem = pressionado ? 2 : 0;

            Point2D centro = new Point2D.Float(width / 2f, height / 2f);
            float raio = Math.max(width, height); 
            float[] distancias = {0.0f, 1.0f};
            
            RadialGradientPaint pinturaRadial = new RadialGradientPaint(centro, raio, distancias, new Color[]{corCentro, corBorda});
            g2.setPaint(pinturaRadial);
            g2.fillRoundRect(margem, margem, width - (margem * 2), height - (margem * 2), 40, 40);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================
    // SUBCLASSE INTERNA: PERMITE ARRASTAR A JANELA SEM BORDA
    // =========================================================
    class MoverJanela extends MouseAdapter {
        private Point clickInicial;
        private JFrame janela;

        public MoverJanela(JFrame janela) {
            this.janela = janela;
        }

        public void mousePressed(MouseEvent e) {
            clickInicial = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            int thisX = janela.getLocation().x;
            int thisY = janela.getLocation().y;
            int xMoved = e.getX() - clickInicial.x;
            int yMoved = e.getY() - clickInicial.y;
            janela.setLocation(thisX + xMoved, thisY + yMoved);
        }
    }
}