package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class StyleUtil {
    // --- COLORS ---
    public static final Color COL_HEADER_BG = Color.decode("#2c3e50"); // Biru Tua Header
    public static final Color COL_BG_APP = Color.decode("#ecf0f1");    // Abu-abu Background
    public static final Color COL_GREEN = Color.decode("#27ae60");     // Hijau Tombol
    public static final Color COL_RED = Color.decode("#c0392b");       // Merah Tombol
    public static final Color COL_GREY_BTN = Color.decode("#7f8c8d");  // Abu Tombol
    public static final Color COL_BLUE_SEARCH = Color.decode("#2980b9"); // Biru Search/Link
    public static final Color COL_TABLE_HEADER = Color.decode("#34495e");
    public static final Color COL_ORANGE_TEXT = Color.decode("#d35400");
    public static final Color COL_WA = Color.decode("#25D366");

    // Warna Dekorasi Baru
    public static final Color COL_ORANGE_DECOR = Color.decode("#f39c12");

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
            // Pakai font sistem, tapi kita override di komponen spesifik
        } catch (Exception e) {}
    }

    public static void styleField(JComponent c) {
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(100, COMP_HEIGHT));
        c.setMinimumSize(new Dimension(100, COMP_HEIGHT));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, COMP_HEIGHT));
        c.setOpaque(true);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    // --- CUSTOM BUTTON ---
    public static class FlatButton extends JButton {
        private Color bgColor;

        public FlatButton(String text, Color bg) {
            super(text);
            this.bgColor = bg;
            setContentAreaFilled(false); setFocusPainted(false);
            setBorderPainted(false); setOpaque(false);

            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, COMP_HEIGHT));
            setMinimumSize(new Dimension(100, COMP_HEIGHT));
        }

        public void setCustomColor(Color bg) { this.bgColor = bg; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // --- CUSTOM FILTER BUTTON (PILL SHAPE) ---
    public static class FilterButton extends JButton {
        private boolean isActive = false;

        public FilterButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
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
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30); // Pill Shape
            if (!isActive) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // --- RENDERERS ---
    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            String status = (String) value;
            JPanel p = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    if(isSelected) g.setColor(table.getSelectionBackground()); else g.setColor(Color.WHITE);
                    g.fillRect(0,0,getWidth(),getHeight());
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color bg=Color.LIGHT_GRAY, fg=Color.BLACK;
                    if("Hilang".equals(status)) { bg=COL_BADGE_RED_BG; fg=COL_BADGE_RED_FG; }
                    else if("Ditemukan".equals(status)) { bg=COL_BADGE_BLUE_BG; fg=COL_BADGE_BLUE_FG; }
                    else if("Dikembalikan".equals(status)) { bg=COL_BADGE_GREEN_BG; fg=COL_BADGE_GREEN_FG; }

                    g2.setColor(bg);
                    int w=110, h=26, x=(getWidth()-w)/2, y=(getHeight()-h)/2;
                    g2.fillRoundRect(x,y,w,h,20,20);
                    g2.setColor(fg); g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(status, x+(w-fm.stringWidth(status))/2, y+((h-fm.getHeight())/2)+fm.getAscent()-2);
                }
            };
            return p;
        }
    }

    public static class AksiButtonRenderer extends JPanel implements TableCellRenderer {
        private String status;
        private boolean isSelected;
        public AksiButtonRenderer() { setOpaque(true); }
        @Override
        public Component getTableCellRendererComponent(JTable t, Object v, boolean isSel, boolean hasF, int r, int c) {
            this.status = (String) t.getValueAt(r, 4); this.isSelected = isSel; return this;
        }
        @Override
        protected void paintComponent(Graphics g) {
            if(isSelected) g.setColor(new Color(232, 240, 254)); else g.setColor(Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());
            if(status != null && "Ditemukan".equalsIgnoreCase(status.trim())) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(COL_WA);
                int w=90, h=26, x=(getWidth()-w)/2, y=(getHeight()-h)/2;
                g2.fillRoundRect(x,y,w,h,20,20);
                g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                String txt = "Klaim WA";
                g2.drawString(txt, x+(w-fm.stringWidth(txt))/2, y+((h-fm.getHeight())/2)+fm.getAscent()-2);
            }
        }
    }
}