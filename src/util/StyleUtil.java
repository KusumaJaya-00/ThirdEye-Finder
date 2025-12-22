package util;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class StyleUtil {
    // --- COLORS ---
    public static final Color COL_HEADER_BG = Color.decode("#2c3e50");
    public static final Color COL_BG_APP = Color.decode("#ecf0f1");
    public static final Color COL_GREEN = Color.decode("#27ae60");
    public static final Color COL_RED = Color.decode("#c0392b");
    public static final Color COL_GREY_BTN = Color.decode("#7f8c8d");
    public static final Color COL_BLUE_SEARCH = Color.decode("#2980b9");
    public static final Color COL_TABLE_HEADER = Color.decode("#34495e");
    public static final Color COL_ORANGE_TEXT = Color.decode("#d35400");
    public static final Color COL_WA = Color.decode("#25D366");

    // Badge Colors
    public static final Color COL_BADGE_RED_BG = Color.decode("#ffebee");
    public static final Color COL_BADGE_RED_FG = Color.decode("#c62828");
    public static final Color COL_BADGE_BLUE_BG = Color.decode("#e3f2fd");
    public static final Color COL_BADGE_BLUE_FG = Color.decode("#1565c0");
    public static final Color COL_BADGE_GREEN_BG = Color.decode("#e8f5e9");
    public static final Color COL_BADGE_GREEN_FG = Color.decode("#2e7d32");

    public static final int COMP_HEIGHT = 45;

    public static void setGlobalFont() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font font = new Font("Segoe UI", Font.PLAIN, 12);
            java.util.Enumeration keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object k = keys.nextElement();
                Object v = UIManager.get(k);
                if (v instanceof javax.swing.plaf.FontUIResource) UIManager.put(k, font);
            }
        } catch (Exception e) {}
    }

    public static void styleField(JComponent c) {
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(200, COMP_HEIGHT));
        c.setMinimumSize(new Dimension(100, COMP_HEIGHT));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, COMP_HEIGHT));
    }

    // --- CUSTOM BUTTON ---
    public static class FlatButton extends JButton {
        public FlatButton(String text, Color bg) {
            super(text);
            setBackground(bg); setForeground(Color.WHITE);
            setFocusPainted(false); setBorderPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, COMP_HEIGHT));
            setMinimumSize(new Dimension(100, COMP_HEIGHT));
        }
        public void setCustomColor(Color bg) { setBackground(bg); }
    }

    // --- CUSTOM FILTER BUTTON (PILL SHAPE) ---
    public static class FilterButton extends JButton {
        private boolean isActive = false;

        public FilterButton(String text) {
            super(text);
            setContentAreaFilled(false); setFocusPainted(false);
            setForeground(Color.DARK_GRAY);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(90, 35));
        }

        public void setActive(boolean active) {
            this.isActive = active;
            if (active) setForeground(Color.WHITE);
            else setForeground(Color.DARK_GRAY);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (isActive) {
                g2.setColor(COL_HEADER_BG);
            } else {
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(1));
            }

            // Gambar Pill
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);

            // Gambar Border jika tidak aktif
            if (!isActive) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // --- RENDERER STATUS ---
    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            String status = (String) value;
            JPanel p = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color bgColor = Color.LIGHT_GRAY; Color fgColor = Color.DARK_GRAY;
                    if("Hilang".equals(status)) { bgColor = COL_BADGE_RED_BG; fgColor = COL_BADGE_RED_FG; }
                    else if("Ditemukan".equals(status)) { bgColor = COL_BADGE_BLUE_BG; fgColor = COL_BADGE_BLUE_FG; }
                    else if("Dikembalikan".equals(status)) { bgColor = COL_BADGE_GREEN_BG; fgColor = COL_BADGE_GREEN_FG; }

                    g2.setColor(bgColor);
                    int w = 110; int h = 26;
                    int x = (getWidth() - w) / 2; int y = (getHeight() - h) / 2;
                    g2.fillRoundRect(x, y, w, h, 20, 20);

                    g2.setColor(fgColor); g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    FontMetrics fm = g2.getFontMetrics();
                    int textX = x + (w - fm.stringWidth(status)) / 2;
                    int textY = y + ((h - fm.getHeight()) / 2) + fm.getAscent();
                    g2.drawString(status, textX, textY);
                }
            };
            p.setBackground(isSelected ? table.getSelectionBackground() : Color.WHITE);
            return p;
        }
    }
}