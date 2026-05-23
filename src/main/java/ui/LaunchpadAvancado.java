package ui;

import Model.Usuario;
import Model.DrumKit;
import Model.Preset;
import Model.Presets;
import Audio.AudioPlayer;
import Database.PresetDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.List;
import java.util.ArrayList;

/**
 * Interface gráfica principal do Launchpad.
 * Suporta CRUD completo na nuvem, Loops exclusivos animados e integração Mouse/Teclado.
 */
public class LaunchpadAvancado extends JFrame {

    // Atributos para controle de dados
    private Usuario usuarioLogado;
    private DrumKit meuDrumKit;
    private List<Preset> presetsUsuario;
    
    // Lista para rastrear todos os pads e controlar a exclusividade dos loops
    private List<PadLuminoso> listaDePads;
    
    // Controle de estado da interface e do CRUD
    private boolean emModoEdicao = false;
    private Preset presetAtivo = null; 
    private JButton btnPreset;
    private JButton btnExcluirConta;
    private JLabel titulo;
    private JPopupMenu menuPresets;

    /**
     * Construtor da janela principal.
     */
    public LaunchpadAvancado(Usuario usuario, DrumKit kit) {
        this.usuarioLogado = usuario;
        this.meuDrumKit = kit;
        
        listaDePads = new java.util.ArrayList<>();

        // Lista mutável com presets padrão
        presetsUsuario = new java.util.ArrayList<>(
            Presets.carregarPresetsPadrao().subList(1, Presets.carregarPresetsPadrao().size())
        );

        // Busca automática na nuvem
        Database.PresetDAO presetDAO = new Database.PresetDAO();
        List<Preset> presetsBanco = presetDAO.buscarPresetsDoUsuario(usuarioLogado);
        presetsUsuario.addAll(presetsBanco); 

        // 1. CONFIGURAÇÕES DA JANELA
        setSize(800, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setUndecorated(true); 
        getContentPane().setBackground(Color.decode("#1E1E24")); 

        // 2. BARRA DE TÍTULO
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
        btnFechar.addActionListener(e -> {
            desligarLoops(); 
            System.exit(0);
        });
        
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

        // 3. DRUM PADS
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
            configurarInteracoesPad(pad, codigosTeclas[i]);
            painelPad.add(pad);
            listaDePads.add(pad); 
        }

        add(painelPad, BorderLayout.CENTER);

        // 4. RODAPÉ (SISTEMA DE PRESETS)
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBackground(Color.decode("#121212"));

        btnPreset = new JButton("Presets ▾");
        btnPreset.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnPreset.setForeground(Color.LIGHT_GRAY); 
        btnPreset.setBackground(Color.decode("#2A2A35")); 
        btnPreset.setFocusPainted(false);
        btnPreset.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnExlcuirUsuario = new JButton("Excluir conta");
        btnExlcuirUsuario.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnExlcuirUsuario.setForeground(Color.WHITE);
        btnExlcuirUsuario.setBackground(Color.decode("#CC3333"));
        btnExlcuirUsuario.setFocusPainted(false);
        btnExlcuirUsuario.setBorderPainted(false);

        btnExlcuirUsuario.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnExlcuirUsuario.addMouseListener(new MouseAdapter(){
            public void mouseEntered(MouseEvent evt){
                btnExlcuirUsuario.setBackground(Color.decode("#AA2222"));
            }
            public void mouseExited(MouseEvent evt){
                btnExlcuirUsuario.setBackground(Color.decode("#CC3333"));
            }
        });

        btnExlcuirUsuario.addActionListener(e -> eliminarUsuarioAtivo());


        
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
        painelRodape.add(btnExlcuirUsuario);
        add(painelRodape, BorderLayout.SOUTH);
    }

    /**
     * Limpa a memória de áudio e apaga as luzes e animações dos loops
     */
    private void desligarLoops() {
        AudioPlayer.pararTodosOsLoops();
        for (PadLuminoso pad : listaDePads) {
            pad.setLoopAtivado(false);
        }
    }

    private void ativarModoEdicao() {
        desligarLoops(); 
        this.presetAtivo = null; 
        emModoEdicao = true;
        meuDrumKit = new DrumKit();
        titulo.setText("   Launchpad Customizável - [CRIANDO NOVO PRESET]");
        titulo.setForeground(Color.decode("#FF3366")); 
        
        btnPreset.setText("💾 Salvar Preset");
        btnPreset.setBackground(Color.decode("#00CC66")); 
        btnPreset.setForeground(Color.BLACK);
    }

    private void restaurarVisualPadrao() {
        titulo.setText("   Launchpad Customizável - Usuário: " + usuarioLogado.getNome());
        titulo.setForeground(Color.LIGHT_GRAY);
        btnPreset.setText("Presets ▾");
        btnPreset.setBackground(Color.decode("#2A2A35"));
        btnPreset.setForeground(Color.LIGHT_GRAY);
    }

    private void salvarPreset() {
        // Se já existe um preset ativo, vamos EDITAR os sons na nuvem
        if (presetAtivo != null && presetAtivo.getPresetId() > 0) {
            
            int confirm = JOptionPane.showConfirmDialog(this, "Deseja salvar as alterações nos sons deste preset?", "Confirmar Edição", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                Database.PresetDAO dao = new Database.PresetDAO();
                dao.atualizarSons(presetAtivo.getPresetId(), meuDrumKit);
                
                JOptionPane.showMessageDialog(this, "Preset '" + presetAtivo.getNome() + "' atualizado com sucesso!");
                emModoEdicao = false;
                restaurarVisualPadrao();
            }
            
        } else {
            // Se não, vamos CRIAR um novo preset
            String nomePreset = JOptionPane.showInputDialog(this, "Digite o nome para o seu novo Preset:", "Salvar Preset", JOptionPane.PLAIN_MESSAGE);
            
            if (nomePreset != null && !nomePreset.trim().isEmpty()) {
                Preset novoPreset = new Preset(nomePreset, usuarioLogado, meuDrumKit);
                Database.PresetDAO dao = new Database.PresetDAO();
                dao.salvar(novoPreset);
                
                this.presetAtivo = novoPreset;
                presetsUsuario.add(novoPreset);
                carregarMenuPresets(); 
                
                JOptionPane.showMessageDialog(this, "Preset '" + nomePreset + "' salvo com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                emModoEdicao = false;
                restaurarVisualPadrao();
            }
        }
    }

    private void carregarPreset(Preset preset){
        desligarLoops(); 
        this.presetAtivo = preset; 
        meuDrumKit = preset.getKit();
        carregarMenuPresets(); 
        JOptionPane.showMessageDialog(this, "Preset '" + preset.getNome() + "' carregado com sucesso!");
    }

    private void renomearPresetAtivo() {
        if (presetAtivo == null) return;
        String novoNome = JOptionPane.showInputDialog(this, "Introduza o novo nome para o preset:", presetAtivo.getNome());
        
        if (novoNome != null && !novoNome.trim().isEmpty()) {
            Database.PresetDAO dao = new Database.PresetDAO();
            dao.atualizarNome(presetAtivo.getPresetId(), novoNome);
            presetAtivo.setNome(novoNome); 
            carregarMenuPresets(); 
            JOptionPane.showMessageDialog(this, "Preset renomeado com sucesso!");
        }
    }

    private void eliminarPresetAtivo() {
        if (presetAtivo == null) return;
        int resposta = JOptionPane.showConfirmDialog(this, 
                "Tem a certeza que deseja eliminar o preset '" + presetAtivo.getNome() + "'?", 
                "Confirmar Eliminação", JOptionPane.YES_NO_OPTION);
                
        if (resposta == JOptionPane.YES_OPTION) {
            desligarLoops();
            Database.PresetDAO dao = new Database.PresetDAO();
            dao.eliminar(presetAtivo.getPresetId());
            presetsUsuario.remove(presetAtivo); 
            presetAtivo = null;
            meuDrumKit = new DrumKit(); 
            carregarMenuPresets(); 
            JOptionPane.showMessageDialog(this, "Preset eliminado com sucesso!");
        }
    }

    private void eliminarUsuarioAtivo(){
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja eliminar o usuario '" + usuarioLogado.getNome() + 
            "' e todos seus presets?", "Confirmar Eliminação", JOptionPane.YES_NO_OPTION);

        if (resposta == JOptionPane.YES_OPTION){
            desligarLoops();
            Database.UsuarioDAO usuarioDAO = new Database.UsuarioDAO();
            List<Integer> presetsUsuarioDelete = new ArrayList<>(usuarioDAO.buscaPresetsUsuario(usuarioLogado));
            Database.PresetDAO presetDAO = new Database.PresetDAO();

            for(int i = 0; i < presetsUsuarioDelete.size(); i++){
                presetDAO.eliminar(presetsUsuarioDelete.get(i));
            }
            usuarioDAO.eliminar(usuarioLogado);
            dispose();

            new TelaLogin().setVisible(true);
        }
    }

    private void carregarMenuPresets() {
        menuPresets.removeAll();
        for(Preset p : presetsUsuario) {
            JMenuItem item = new JMenuItem(p.getNome());
            item.addActionListener(e -> { carregarPreset(p); });
            menuPresets.add(item);
        }
        menuPresets.addSeparator();

        // Menu de Edição para Presets da Nuvem
        if (presetAtivo != null && presetAtivo.getPresetId() > 0) {
            JMenuItem itemEditarSons = new JMenuItem("🎧 Editar Sons deste Preset");
            itemEditarSons.addActionListener(e -> {
                emModoEdicao = true;
                titulo.setText("   Launchpad Customizável - [EDITANDO: " + presetAtivo.getNome() + "]");
                titulo.setForeground(Color.decode("#f5b041")); 
                btnPreset.setText("💾 Salvar Alterações");
                btnPreset.setBackground(Color.decode("#00CC66"));
                btnPreset.setForeground(Color.BLACK);
            });
            menuPresets.add(itemEditarSons);

            JMenuItem itemRenomear = new JMenuItem("✏️ Renomear Preset Atual");
            itemRenomear.addActionListener(e -> renomearPresetAtivo());
            menuPresets.add(itemRenomear);

            JMenuItem itemEliminar = new JMenuItem("🗑️ Eliminar Preset Atual");
            itemEliminar.addActionListener(e -> eliminarPresetAtivo());
            menuPresets.add(itemEliminar);
            
            menuPresets.addSeparator();
        }

        JMenuItem itemNovo = new JMenuItem("Criar Novo Preset...");
        itemNovo.addActionListener(e -> ativarModoEdicao());
        menuPresets.add(itemNovo);
    }

    private void configurarInteracoesPad(PadLuminoso pad, int keyCode) {
        // Interação Teclado
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
                    executarAcaoPressionar(pad);
                }
            }
        });

        am.put("soltar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                executarAcaoSoltar(pad);
            }
        });

        // Interação Mouse
        pad.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                executarAcaoPressionar(pad);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                executarAcaoSoltar(pad);
            }
        });
    }

    private void executarAcaoPressionar(PadLuminoso padLum) {
        String nomeRealDaTecla = padLum.getTextoOriginal();

        if (emModoEdicao) {
            JFileChooser fileChooser = new JFileChooser(new File("Assets"));
            fileChooser.setDialogTitle("Escolha um arquivo .wav para a tecla " + nomeRealDaTecla);
            int resposta = fileChooser.showOpenDialog(LaunchpadAvancado.this);
            
            if (resposta == JFileChooser.APPROVE_OPTION) {
                File arquivoEscolhido = fileChooser.getSelectedFile();
                meuDrumKit.associarSom(nomeRealDaTecla, arquivoEscolhido.getPath());
            }
            
            padLum.getModel().setArmed(false);
            padLum.getModel().setPressed(false);
            
        } else {
            String caminhoSom = meuDrumKit.obterCaminhoSom(nomeRealDaTecla);
            
            // Loop Exclusivo (R, F, V)
            if (nomeRealDaTecla.equals("R") || nomeRealDaTecla.equals("F") || nomeRealDaTecla.equals("V")) {
                if (padLum.isLoopAtivado()) {
                    padLum.setLoopAtivado(false);
                    AudioPlayer.pararLoop(nomeRealDaTecla); 
                } else {
                    // Desliga todos os outros antes de iniciar este
                    AudioPlayer.pararTodosOsLoops(); 
                    for (PadLuminoso p : listaDePads) {
                        p.setLoopAtivado(false);
                    }
                    padLum.setLoopAtivado(true);
                    AudioPlayer.iniciarLoop(caminhoSom, nomeRealDaTecla); 
                }
            } else {
                AudioPlayer.tocarSom(caminhoSom);
            }
        }
    }

    private void executarAcaoSoltar(PadLuminoso padLum) {
        if (!emModoEdicao) {
            padLum.getModel().setArmed(false);
            padLum.getModel().setPressed(false); 
        }
    }

    public static void main(String[] args) {
        // Ponto de entrada via TelaLogin
    }

    // =========================================================
    // SUBCLASSE: PADS LUMINOSOS COM ANIMAÇÃO DE SPINNER
    // =========================================================
    class PadLuminoso extends JButton {
        private Color corBase;
        private String textoOriginal; 
        private boolean loopAtivado = false; 
        
        private Timer timerAnimacao;
        private int anguloAnimacao = 0;

        public PadLuminoso(Color cor, String tecla) {
            super(tecla);
            this.textoOriginal = tecla;
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

            timerAnimacao = new Timer(30, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    anguloAnimacao += 15; 
                    if (anguloAnimacao >= 360) {
                        anguloAnimacao -= 360;
                    }
                    repaint(); 
                }
            });
        }

        public String getTextoOriginal() {
            return this.textoOriginal;
        }

        public void setLoopAtivado(boolean ativo) {
            this.loopAtivado = ativo;
            if (ativo) {
                setText(""); 
                timerAnimacao.start(); 
            } else {
                setText(textoOriginal); 
                timerAnimacao.stop(); 
                anguloAnimacao = 0; 
            }
            repaint(); 
        }

        public boolean isLoopAtivado() {
            return this.loopAtivado;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            
            boolean pressionado = getModel().isPressed() || loopAtivado;

            Color corCentro = pressionado ? Color.WHITE : corBase.brighter().brighter();
            Color corBorda = pressionado ? corBase : corBase.darker();
            int margem = pressionado ? 2 : 0;

            Point2D centro = new Point2D.Float(width / 2f, height / 2f);
            float raio = Math.max(width, height); 
            float[] distancias = {0.0f, 1.0f};
            
            RadialGradientPaint pinturaRadial = new RadialGradientPaint(centro, raio, distancias, new Color[]{corCentro, corBorda});
            g2.setPaint(pinturaRadial);
            g2.fillRoundRect(margem, margem, width - (margem * 2), height - (margem * 2), 40, 40);

            if (loopAtivado) {
                int tamanhoSpinner = Math.min(width, height) / 3; 
                int pos_X = (width - tamanhoSpinner) / 2;
                int pos_Y = (height - tamanhoSpinner) / 2;

                g2.setStroke(new BasicStroke(6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); 
                
                g2.setColor(new Color(0, 0, 0, 30)); 
                g2.drawOval(pos_X, pos_Y, tamanhoSpinner, tamanhoSpinner);

                g2.setColor(corBase); 
                g2.drawArc(pos_X, pos_Y, tamanhoSpinner, tamanhoSpinner, -anguloAnimacao, 200); 
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // =========================================================
    // SUBCLASSE INTERNA: PERMITE ARRASTAR A JANELA
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