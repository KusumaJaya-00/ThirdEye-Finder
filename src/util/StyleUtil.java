package util;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * StyleUtil adalah kelas utilitas untuk mengatur tampilan (UI) aplikasi secara terpusat.
 * Berisi definisi warna, font, komponen custom (tombol), dan renderer tabel.
 * Tujuannya agar desain aplikasi konsisten dan mudah diubah dari satu tempat.
 */
public class StyleUtil {
    // COLORS 
    public static final Color COL_HEADER_BG = Color.decode("#003049");
    public static final Color COL_BG_APP = Color.decode("#ecf0f1");
    public static final Color COL_GREEN = Color.decode("#27ae60");
    public static final Color COL_RED = Color.decode("#c0392b");
    public static final Color COL_GREY_BTN = Color.decode("#7f8c8d");
    public static final Color COL_BLUE_SEARCH = Color.decode("#2980b9");
    public static final Color COL_BLUE_PANEL = Color.decode("#124559");
    public static final Color COL_TABLE_HEADER = COL_HEADER_BG;
    public static final Color COL_ORANGE_TEXT = Color.decode("#d35400");
    public static final Color COL_WA = Color.decode("#25D366");
    public static final Color COL_ORANGE_DECOR = Color.decode("#f39c12");

    // Badge Colors
    public static final Color COL_BADGE_RED_BG = Color.decode("#ffebee");
    public static final Color COL_BADGE_RED_FG = Color.decode("#c62828");
    public static final Color COL_BADGE_BLUE_BG = Color.decode("#e3f2fd");
    public static final Color COL_BADGE_BLUE_FG = Color.decode("#1565c0");
    public static final Color COL_BADGE_GREEN_BG = Color.decode("#e8f5e9");
    public static final Color COL_BADGE_GREEN_FG = Color.decode("#2e7d32");

    public static final int COMP_HEIGHT = 45; // Tinggi standar komponen input & tombol

    // Mengatur LookAndFeel agar aplikasi mengikuti gaya sistem operasi (Windows/Mac/Linux).
    public static void setGlobalFont() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
    }
     // Memberikan style standar pada komponen input (TextField/ComboBox/PasswordField).
     // Menambahkan border, padding, dan ukuran standar.
    public static void styleField(JComponent c) {
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(0, 15, 0, 15)
        ));
        c.setBackground(Color.WHITE);
        c.setPreferredSize(new Dimension(100, COMP_HEIGHT));
        c.setMinimumSize(new Dimension(100, COMP_HEIGHT));
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, COMP_HEIGHT)); // Agar bisa melebar horizontal
        c.setOpaque(true);
        c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    // COMPONENT FACTORY
    // Membantu membuat Label dengan font dan warna yang konsisten
    public static JLabel createTitleLabel(String text, Color color) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 28));
        l.setForeground(color);
        return l;
    }

    public static JLabel createHeaderLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 18));
        l.setForeground(Color.WHITE);
        return l;
    }

    public static JLabel createSubtitleLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(new Color(200, 200, 200));
        return l;
    }

    public static JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Color.GRAY);
        return l;
    }

    // CUSTOM BUTTON 
    public static class FlatButton extends JButton {
        private Color bgColor;

        public FlatButton(String text, Color bg) {
            super(text);
            this.bgColor = bg;
            setContentAreaFilled(false); // Hilangkan efek tombol 3D default
            setFocusPainted(false);      // Hilangkan garis fokus saat diklik
            setBorderPainted(false);     // Hilangkan border default
            setOpaque(false);

            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(100, COMP_HEIGHT));
            setMinimumSize(new Dimension(100, COMP_HEIGHT));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { setBackground(bgColor.darker()); repaint(); }
                public void mouseExited(MouseEvent e) { setBackground(bgColor); repaint(); }
            });
        }

        public void setCustomColor(Color bg) { this.bgColor = bg; repaint(); }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8); // border-radius (radius 8)
            g2.dispose();
            super.paintComponent(g); // Gambar teks tombol di atas background
        }
    }

    // CUSTOM FILTER BUTTON (Pill Shape)
    public static class FilterButton extends JButton {
        private boolean isActive = false;

        public FilterButton(String text) {
            super(text);
            setContentAreaFilled(false); setFocusPainted(false);
            setBorderPainted(false); setOpaque(false);
            setForeground(Color.DARK_GRAY);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(90, 35));
        }

        public void setActive(boolean active) {
            this.isActive = active;
            // Ubah warna teks berdasarkan status aktif
            setForeground(active ? Color.WHITE : Color.DARK_GRAY);
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
            g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            if (!isActive) {
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // RENDERERS ISI TABEL
    // Menampilkan status dalam bentuk Badge/Pill berwarna sesuai isinya.
    public static class StatusBadgeRenderer extends DefaultTableCellRenderer {
        public StatusBadgeRenderer() { setHorizontalAlignment(CENTER); }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int col) {
            super.getTableCellRendererComponent(table, value, isSel, hasFocus, row, col);
            // Simpan data ke client property untuk digunakan saat menggambar (paintComponent)
            putClientProperty("val", value);
            putClientProperty("sel", isSel);
            putClientProperty("sel_col", table.getSelectionBackground());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            // 1. Gambar Background Sel (Putih atau Biru Selection)
            boolean isSel = Boolean.TRUE.equals(getClientProperty("sel"));
            g.setColor(isSel ? (Color)getClientProperty("sel_col") : Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());

            // 2. Tentukan Warna Badge berdasarkan teks status
            Object val = getClientProperty("val");
            String s = val != null ? val.toString() : "";
            Color bg=Color.LIGHT_GRAY, fg=Color.BLACK;

            if("Hilang".equals(s)) { bg=COL_BADGE_RED_BG; fg=COL_BADGE_RED_FG; }
            else if("Ditemukan".equals(s)) { bg=COL_BADGE_BLUE_BG; fg=COL_BADGE_BLUE_FG; }
            else if("Dikembalikan".equals(s)) { bg=COL_BADGE_GREEN_BG; fg=COL_BADGE_GREEN_FG; }

            // 3. Gambar Bentuk Badge (Pill)
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);

            int marginX = 15;
            int marginY = 8;
            g2.fillRoundRect(marginX, marginY, getWidth()-(marginX*2), getHeight()-(marginY*2), 20, 20);
            g2.dispose();

            // 4. Gambar Teks (Otomatis digambar oleh super.paintComponent di posisi tengah)
            setForeground(fg);
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setOpaque(false);
            super.paintComponent(g);
            setOpaque(true);
        }
    }


     // Renderer untuk kolom "Aksi" (Tombol Klaim WA).
     // Hanya muncul jika status barang adalah "Ditemukan".
    public static class AksiButtonRenderer extends DefaultTableCellRenderer {
        public AksiButtonRenderer() { setHorizontalAlignment(CENTER); }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSel, boolean hasFocus, int row, int col) {
            // Cek status barang di kolom ke-4 (Index 4 = Status)
            Object statObj = table.getValueAt(row, 4);
            String status = statObj != null ? statObj.toString() : "";

            // Jika status bukan "Ditemukan", jangan tampilkan apa-apa
            if(!"Ditemukan".equalsIgnoreCase(status.trim())) {
                super.getTableCellRendererComponent(table, "", isSel, hasFocus, row, col);
                putClientProperty("active", false);
                return this;
            }

            // Jika "Ditemukan", tampilkan teks "KLAIM WA"
            super.getTableCellRendererComponent(table, "KLAIM WA", isSel, hasFocus, row, col);
            putClientProperty("active", true);
            putClientProperty("sel", isSel);
            putClientProperty("sel_col", table.getSelectionBackground());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Jika tombol tidak aktif, gambar default saja (kosong)
            if(Boolean.FALSE.equals(getClientProperty("active"))) {
                super.paintComponent(g); return;
            }

            // 1. Gambar Background Sel
            boolean isSel = Boolean.TRUE.equals(getClientProperty("sel"));
            g.setColor(isSel ? (Color)getClientProperty("sel_col") : Color.WHITE);
            g.fillRect(0,0,getWidth(),getHeight());

            // 2. Gambar Tombol Hijau WA
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(COL_WA);

            int marginX = 20;
            int marginY = 8;
            g2.fillRoundRect(marginX, marginY, getWidth()-(marginX*2), getHeight()-(marginY*2), 20, 20);
            g2.dispose();

            // 3. Gambar Teks
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setOpaque(false);
            super.paintComponent(g);
            setOpaque(true);
        }
    }
}