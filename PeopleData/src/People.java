public class People {
    private String id;
    private String nama;
    private int tahunLahir;
    private String kategori;
    // tambahan atribut
    private String jenisKelamin;
    private String posisi;

    // Constructor
    public People() {
    }

    public People(String id, String nama, String kategori, String posisi, int tahunLahir, String jenisKelamin) {
        this.id = id;
        this.nama = nama;
        this.tahunLahir = tahunLahir;
        this.kategori = kategori;
        this.jenisKelamin = jenisKelamin;
        this.posisi=posisi;
    }

    // Getter dan Setter untuk id
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getter dan Setter untuk nama
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    // Getter dan Setter untuk tahunLahir
    public int getTahunLahir() { return tahunLahir; }
    public void setTahunLahir(int tahunLahir) { this.tahunLahir = tahunLahir; }

    // Getter dan Setter untuk kategori
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }

    // Getter dan Setter untuk jenisKelamin
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    // Getter dan Setter untuk Posisi
    public String getPosisi() { return posisi; }
    public void setPosisi(String posisi) { this.posisi = posisi; }
}
