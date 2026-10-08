# Janji
Saya Faridchi Trianda Safaraz dengan NIM 2506827 mengerjakan Tugas Praktikum 4 pada Mata Kuliah Desain dan Pemrograman Berorientasi Objek (DPBO) untuk keberkahan-Nya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin

# Struktur File
```
TP4DPBO2526C2
├── PeopleData
│   └── src
│       ├── People.java
│       ├── PeopleMenu.java
│       └── PeopleMenu.form
├── Dokumentasi
│       └── penjelasantp4.mp4
│
└── README.md
```

# Desain
Program terdiri atas __2__ class, yaitu __People__ dan __PeopleMenu__. Berikut atribut dari masing-masing class:
- People:
  - ID
  - Nama
  - Kategori
  - Posisi
  - Tahun Lahir
  - Jenis Kelamin
- PeopleMenu:
  - listOrang

# Penjelasan
Setelah program dijalankan, akan muncul sebuah jendela bertajuk __Data Pemain Futsal__ yang berisi:
- formulir input untuk `id`, `nama`, `kategori` (U12, U15, U17, U19, U20), `posisi` (Anchor, Pivot, Flank, Keeper), `tahun lahir`, dan `jenis kelamin`,
- tombol __Add__ untuk menambahkan pemain baru ketika form diisi
- tombol __Update__ untuk mengubah data pemain yang ada pada tabel
- tombol __Cancel__ untuk membatalkan input pada form
- tabel yang menampilkan daftar pemain dengan atribut-atribut tersebut.

User dapat mengisi form dengan lengkap dan menekan tombol __Add__ untuk menambahkan data pemain futsal ke dalam tabel. Selain itu, pengguna juga bisa memilih salah satu pemain pada tabel untuk menampilkan detailnya di form. Setelah data pemain muncul di form, pengguna dapat memperbarui atau menghapusnya menggunakan tombol __Update__ atau __Delete__ yang akan aktif setelah data dipilih.

Jika pengguna mengubah nilai pada form dan menekan tombol __Update__, maka data pemain yang dipilih akan diperbarui di tabel. Sementara itu, ketika tombol __Delete__ ditekan, akan muncul jendela konfirmasi penghapusan, dan data pemain akan dihapus apabila pengguna memilih tombol __Yes__.

Sebelum data ditambahkan atau diperbarui, form akan divalidasi terlebih dahulu: seluruh isian wajib terisi, `id` tidak boleh sama dengan data lain, `tahun lahir` harus berupa angka dan tidak melebihi tahun sekarang, serta harus sesuai dengan batas umur kategori yang dipilih.

# Dokumentasi
<div>
    <a>Penjelasan lebih lengkap mengenai TP4 ini terdapat pada link video dibawah</a><br><br>
    <a href="https://youtu.be/DYElw_hx2uA"><img src="https://img.shields.io/badge/YouTube-%23FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white" /></a>
</div>