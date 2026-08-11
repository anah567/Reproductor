package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BotonModerno extends JButton {

    private Color colorBase;
    private Color colorHover;
    private final boolean soloBorde;

    public BotonModerno(String texto, Color colorBase) {
        this(texto, colorBase, false);
    }

    public BotonModerno(String texto, Color colorBase, boolean soloBorde) {
        super(texto);

        this.colorBase = colorBase;
        this.colorHover = colorBase.brighter();
        this.soloBorde = soloBorde;

        setFont(TemaOscuro.FUENTE_NEGRITA);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(9, 18, 9, 18));

        actualizarApariencia();

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (isEnabled()) {
                    setBackground(colorHover);
                    repaint();
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(colorBase);
                repaint();
            }
        });
    }

    public void cambiarColorBase(Color nuevoColor) {
        colorBase = nuevoColor;
        colorHover = nuevoColor.brighter();
        actualizarApariencia();
    }

    public void actualizarApariencia() {
        setBackground(colorBase);

        if (soloBorde) {
            setForeground(colorBase);
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        if (soloBorde) {
            Color fondo;

            if (Tema.esOscuro()) {
                fondo = TemaOscuro.PANEL_SECUNDARIO;
            } else {
                fondo = TemaClaro.PANEL_SECUNDARIO;
            }
            g2.setColor(fondo);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.setColor(isEnabled() ? getBackground() : getBackground().darker());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

        } else {
            g2.setColor(isEnabled() ? getBackground() : getBackground().darker());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        }

        g2.dispose();
        super.paintComponent(g);
    }
}