import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Year;
import java.util.ArrayList;

public class PeopleMenu extends JFrame{
    private JLabel title;
    private JTextField idField;
    private JTextField namaField;
    private JComboBox kategoriCBox;
    private JComboBox posisiCBox;
    private JTextField tahunField;
    private JRadioButton lakiRadioButton;
    private JRadioButton perempuanRadioButton;
    private JButton addUpdateButton;
    private JButton deleteButton;
    private JButton cancelButton;
    private JTable peopleTable;
    private JPanel mainPanel;
    // helper
    private ArrayList<People> listOrang;
    private int selectedIndex = -1;
    private ButtonGroup genderGroup;
    // tahun sekarang = batas maksimal tahun lahir
    private static final int TAHUN_SEKARANG = Year.now().getValue();

    public static void main(String[] args) {
        PeopleMenu menu=new PeopleMenu();
        menu.setSize(700, 600);
        menu.setLocationRelativeTo(null);
        menu.setContentPane(menu.mainPanel);
        menu.getContentPane().setBackground(Color.WHITE);
        menu.setVisible(true);
        menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    // constructor
    public PeopleMenu() {
        // inisialisasi listOrang
        listOrang = new ArrayList<>();
        // isi listOrang
        populateList();
        // isi tabel
        refreshTable();
        // ubah styling title
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        // atur isi combo box
        String[] posisiData = { "(Pilih Posisi)", "Anchor", "Pivot", "Flank", "Keeper" };
        String[] kategoriData = { "(Pilih Kategori)", "U12", "U15", "U17", "U19", "U20" };
        kategoriCBox.setModel(new DefaultComboBoxModel<>(kategoriData));
        posisiCBox.setModel(new DefaultComboBoxModel<>(posisiData));
        // kelompokkan radio button supaya hanya satu yang bisa terpilih
        genderGroup = new ButtonGroup();
        genderGroup.add(lakiRadioButton);
        genderGroup.add(perempuanRadioButton);
        // sembunyikan button delete (karena kalo belom apa apa di delete ga masuk logic)
        deleteButton.setVisible(false);
        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan, tampilkan konfirmasi dulu
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pilihan = JOptionPane.showConfirmDialog(
                        null,
                        "Apakah anda yakin ingin menghapus data ini?",
                        "Konfirmasi Hapus",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (pilihan == JOptionPane.YES_OPTION) {
                    deleteData();
                }
            }
        });

        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        peopleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = peopleTable.getSelectedRow();
                if (selectedIndex == -1) {
                    return;
                }
                // simpan value textfield, combo box, dan radio button
                // (kolom 0 = "No", jadi data mulai dari kolom 1)
                String curId = peopleTable.getModel().getValueAt(selectedIndex, 1).toString();
                String curNama = peopleTable.getModel().getValueAt(selectedIndex, 2).toString();
                String curKategori = peopleTable.getModel().getValueAt(selectedIndex, 3).toString();
                String curPosisi = peopleTable.getModel().getValueAt(selectedIndex, 4).toString();
                String curTahun = peopleTable.getModel().getValueAt(selectedIndex, 5).toString();
                String curGender = peopleTable.getModel().getValueAt(selectedIndex, 6).toString();

                // ubah isi textfield, combo box, dan radio button
                idField.setText(curId);
                namaField.setText(curNama);
                tahunField.setText(curTahun);
                kategoriCBox.setSelectedItem(curKategori);
                posisiCBox.setSelectedItem(curPosisi);
                if (curGender.equals("Laki-laki")) {
                    lakiRadioButton.setSelected(true);
                } else {
                    perempuanRadioButton.setSelected(true);
                }
                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    // pasang model tabel lalu rapikan kolom "No" (kecil dan rata tengah)
    // dipanggil setiap kali tabel di-refresh karena setModel() membuat ulang kolom
    private void refreshTable() {
        peopleTable.setModel(setTable());

        TableColumn noColumn = peopleTable.getColumnModel().getColumn(0);
        noColumn.setMinWidth(40);
        noColumn.setMaxWidth(50);
        noColumn.setPreferredWidth(40);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        noColumn.setCellRenderer(centerRenderer);
    }

    // batas tahun lahir paling tua (paling kecil) untuk tiap kategori
    private int getTahunMinimum(String kategori) {
        switch (kategori) {
            case "U12": return 2013;
            case "U15": return 2010; // 2010 masih boleh (keringanan satu tahun)
            case "U17": return 2008;
            case "U19": return 2006;
            case "U20": return 2005;
            default:    return 0;
        }
    }

    // batas tahun lahir paling muda (paling besar) untuk tiap kategori
    private int getTahunMaksimum(String kategori) {
        if (kategori.equals("U12")) {
            return 2019;
        }
        return TAHUN_SEKARANG;
    }

    // mengisi tabel dari listOrang
    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] cols = { "No", "ID", "Nama", "Kategori", "Posisi", "Tahun Lahir", "Jenis Kelamin" };
        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel tmp = new DefaultTableModel(null, cols);
        // isi tabel dengan listOrang
        for (int i = 0; i < listOrang.size(); i++) {
            Object[] row = {
                    i + 1,
                    listOrang.get(i).getId(),
                    listOrang.get(i).getNama(),
                    listOrang.get(i).getKategori(),
                    listOrang.get(i).getPosisi(),
                    listOrang.get(i).getTahunLahir(),
                    listOrang.get(i).getJenisKelamin()
            };
            tmp.addRow(row);
        }
        return tmp;
    }
    // mengambil jenis kelamin dari radio button (null jika belum dipilih)
    private String getSelectedGender() {
        if (lakiRadioButton.isSelected()) {
            return "Laki-laki";
        } else if (perempuanRadioButton.isSelected()) {
            return "Perempuan";
        }
        return null;
    }
    // validasi input form; return true jika semua valid
    private boolean validateForm() {
        if (idField.getText().trim().isEmpty() || namaField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "ID dan Nama tidak boleh kosong!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (kategoriCBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih kategori terlebih dahulu!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (posisiCBox.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Pilih posisi terlebih dahulu!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (getSelectedGender() == null) {
            JOptionPane.showMessageDialog(null, "Pilih jenis kelamin terlebih dahulu!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // ID tidak boleh sama dengan data lain
        // (selectedIndex dilewati agar saat update, ID milik baris sendiri tidak dianggap dobel)
        String idBaru = idField.getText().trim();
        for (int i = 0; i < listOrang.size(); i++) {
            if (i != selectedIndex && listOrang.get(i).getId().equalsIgnoreCase(idBaru)) {
                JOptionPane.showMessageDialog(null, "ID " + idBaru + " sudah dipakai!", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // tahun lahir harus angka
        int tahun;
        try {
            tahun = Integer.parseInt(tahunField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // tahun lahir tidak boleh melebihi tahun sekarang
        if (tahun > TAHUN_SEKARANG) {
            JOptionPane.showMessageDialog(null,
                    "Tahun lahir tidak boleh lebih dari " + TAHUN_SEKARANG + "!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // tahun lahir harus sesuai batas kategori
        String kategori = kategoriCBox.getSelectedItem().toString();
        int tahunMin = getTahunMinimum(kategori);
        int tahunMaks = getTahunMaksimum(kategori);
        if (tahun < tahunMin) {
            JOptionPane.showMessageDialog(null,
                    "Tahun lahir untuk kategori " + kategori + " minimal " + tahunMin
                            + ".\nPemain yang lebih tua harus masuk ke kategori di atasnya!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (tahun > tahunMaks) {
            JOptionPane.showMessageDialog(null,
                    "Tahun lahir untuk kategori " + kategori + " maksimal " + tahunMaks + "!",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    // CREATE
    public void insertData() {
        try {
            if (!validateForm()) {
                return;
            }

            // ambil value dari textfield, combobox, dan radio button
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunField.getText().trim());
            String kategori = kategoriCBox.getSelectedItem().toString();
            String posisi = posisiCBox.getSelectedItem().toString();
            String jenisKelamin = getSelectedGender();

            // tambahkan data ke dalam list
            listOrang.add(new People(id, nama, kategori, posisi, tahunLahir, jenisKelamin));

            // update tabel
            refreshTable();

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Insert berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE
    public void updateData() {
        try {
            if (!validateForm()) {
                return;
            }

            // ambil data dari form
            String id = idField.getText().trim();
            String nama = namaField.getText().trim();
            int tahunLahir = Integer.parseInt(tahunField.getText().trim());
            String kategori = kategoriCBox.getSelectedItem().toString();
            String posisi = posisiCBox.getSelectedItem().toString();
            String jenisKelamin = getSelectedGender();

            // ubah data person di list
            listOrang.get(selectedIndex).setId(id);
            listOrang.get(selectedIndex).setNama(nama);
            listOrang.get(selectedIndex).setTahunLahir(tahunLahir);
            listOrang.get(selectedIndex).setKategori(kategori);
            listOrang.get(selectedIndex).setPosisi(posisi);
            listOrang.get(selectedIndex).setJenisKelamin(jenisKelamin);

            // update tabel
            refreshTable();

            // bersihkan form
            clearForm();

            // feedback
            System.out.println("Update berhasil");
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Tahun lahir harus berupa angka!", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    // DELETE (konfirmasi sudah ditangani di listener deleteButton)
    public void deleteData() {
        // hapus data dari list
        listOrang.remove(selectedIndex);
        // update tabel
        refreshTable();
        // bersihkan form
        clearForm();
        // feedback
        System.out.println("Delete berhasil");
        JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
    }

    public void clearForm() {
        // kosongkan semua textfield, combo box, dan radio button
        idField.setText("");
        namaField.setText("");
        tahunField.setText("");
        kategoriCBox.setSelectedIndex(0);
        posisiCBox.setSelectedIndex(0);
        genderGroup.clearSelection();
        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    // data awal
    private void populateList() {
        listOrang.add(new People("P001", "Gerrard Pique", "U17", "Pivot", 2009, "Laki-laki"));
        listOrang.add(new People("P002", "Sade Adu", "U12", "Flank", 2013, "Perempuan"));
        listOrang.add(new People("P003", "Donald Glover", "U20", "Pivot", 2007, "Laki-laki"));
        listOrang.add(new People("P004", "Analog Chorus", "U15", "Anchor", 2012, "Laki-laki"));
        listOrang.add(new People("P005", "White Skkeleton", "U19", "Keeper", 2008, "Perempuan"));
    }
}