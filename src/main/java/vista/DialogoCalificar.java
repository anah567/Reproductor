package vista;

import modelo.Cancion;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.util.function.IntConsumer;

public class DialogoCalificar extends JDialog {

    private final JSlider slider;
    private final JLabel etiquetaValor;
    private final JLabel etiquetaEstrellas;
    private final JLabel titulo;

    private final JPanel panelCentro;
    private final JPanel panelBotones;

    private final BotonModerno botonCancelar;
    private final BotonModerno botonGuardar;

    public DialogoCalificar(JFrame propietario, Cancion cancion, IntConsumer alGuardar) {
        super(propietario, "Calificar canción", true);

        setLayout(new BorderLayout(10, 10));

        titulo = new JLabel(
                cancion.getNombre() + " - " + cancion.getArtista(),
                JLabel.CENTER
        );

        titulo.setFont(TemaOscuro.FUENTE_NEGRITA);
        titulo.setBorder(BorderFactory.createEmptyBorder(16, 16, 0, 16));

        add(titulo, BorderLayout.NORTH);

        panelCentro = new JPanel();
        panelCentro.setLayout(new java.awt.GridLayout(3, 1, 4, 4));
        panelCentro.setBorder(BorderFactory.createEmptyBorder(16, 24, 8, 24));

        slider = new JSlider(
                0,
                100,
                cancion.getCalificacion()
        );

        slider.setMajorTickSpacing(20);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setOpaque(true);

        etiquetaValor = new JLabel(
                "Calificación: " + cancion.getCalificacion() + " / 100",
                JLabel.CENTER
        );

        etiquetaValor.setFont(TemaOscuro.FUENTE_NORMAL);

        etiquetaEstrellas = new JLabel(
                construirEstrellas(cancion.getCalificacion()),
                JLabel.CENTER
        );

        etiquetaEstrellas.setFont(TemaOscuro.FUENTE_GRANDE);

        slider.addChangeListener(e -> {
            int valor = slider.getValue();

            etiquetaValor.setText(
                    "Calificación: " + valor + " / 100"
            );

            etiquetaEstrellas.setText(
                    construirEstrellas(valor)
            );
        });

        panelCentro.add(etiquetaEstrellas);
        panelCentro.add(etiquetaValor);
        panelCentro.add(slider);

        add(panelCentro, BorderLayout.CENTER);

        panelBotones = new JPanel(
                new FlowLayout(FlowLayout.CENTER, 10, 0)
        );

        panelBotones.setBorder(
                BorderFactory.createEmptyBorder(0, 16, 16, 16)
        );

        botonCancelar = new BotonModerno(
                "Cancelar",
                Color.decode("#64748B")
        );

        botonCancelar.addActionListener(
                e -> dispose()
        );

        botonGuardar = new BotonModerno(
                "Guardar",
                TemaManager.acento()
        );

        botonGuardar.addActionListener(e -> {
            alGuardar.accept(slider.getValue());
            dispose();
        });

        panelBotones.add(botonCancelar);
        panelBotones.add(botonGuardar);

        add(panelBotones, BorderLayout.SOUTH);

        actualizarTema();

        pack();
        setMinimumSize(new java.awt.Dimension(360, 260));
        setLocationRelativeTo(propietario);
    }

    public void actualizarTema() {

        getContentPane().setBackground(
                TemaManager.panelSecundario()
        );

        titulo.setForeground(
                TemaManager.textoPrincipal()
        );

        panelCentro.setBackground(
                TemaManager.panelSecundario()
        );

        panelBotones.setBackground(
                TemaManager.panelSecundario()
        );

        etiquetaValor.setForeground(
                TemaManager.acento()
        );

        etiquetaEstrellas.setForeground(
                TemaManager.acento()
        );

        slider.setBackground(
                TemaManager.panelSecundario()
        );

        slider.setForeground(
                TemaManager.textoSecundario()
        );

        botonGuardar.cambiarColorBase(
                TemaManager.acento()
        );

        if (Tema.esOscuro()) {

            botonCancelar.cambiarColorBase(
                    Color.decode("#3F3F46")
            );

            botonCancelar.setForeground(
                    TemaOscuro.TEXTO_PRINCIPAL
            );

            botonGuardar.setForeground(
                    TemaOscuro.TEXTO_PRINCIPAL
            );

        } else {

            botonCancelar.cambiarColorBase(
                    TemaClaro.PANEL_DESTACADO
            );

            botonCancelar.setForeground(
                    TemaClaro.TEXTO_PRINCIPAL
            );

            botonGuardar.setForeground(
                    Color.WHITE
            );
        }

        repaint();
        revalidate();
    }

    private String construirEstrellas(int calificacion) {
        int estrellasLlenas =
                Math.round(calificacion / 20f);

        StringBuilder sb =
                new StringBuilder();

        for (int i = 0; i < 5; i++) {

            sb.append(
                    i < estrellasLlenas
                            ? "\u2605"
                            : "\u2606"
            );
        }

        return sb.toString();
    }
}