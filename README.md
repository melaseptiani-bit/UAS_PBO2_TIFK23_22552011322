# UAS Pemrograman Berorientasi Obyek 2
<ul>
  <li>Mata Kuliah: Pemrograman Berorientasi Obyek 2</li>
  <li>Dosen Pengampu: <a href="https://github.com/Muhammad-Ikhwan-Fathulloh">Muhammad Ikhwan Fathulloh</a></li>
</ul>

## Profil
<ul>
  <li>Nama: {Mela Septiani}</li>
  <li>NIM: {22552011322}</li>
  <li>Studi Kasus: {Kasir Transportasi}</li>
</ul>

## Judul Studi Kasus
<p>Kasir Transportasi.</p>

## Penjelasan Studi Kasus
<p>Studi kasus ini berfokus pada pengembangan sistem kasir untuk layanan transportasi, seperti bus, dan mobil. Sistem ini bertujuan untuk mempermudah pencatatan transaksi, pengelolaan data penumpang, dan pelacakan pembayaran.

Sistem kasir transportasi harus mampu:

Mengelola data pelanggan (tujuan, dan jarak).

Menghitung total biaya berdasarkan tujuan, jarak, dan jumlah penumpang.

Memberikan laporan transaksi </p>

## Penjelasan 4 Pilar OOP dalam Studi Kasus

### 1. Inheritance
<p>Kita memiliki kelas dasar `Kendaraan` yang memiliki atribut umum seperti `Mobil` dan `Bus`.
  Kelas turunan seperti `Mobil` dan `Bus` dapat mewarisi atribut ini dan menambahkan fitur spesifik seperti `Kapasitas`.</p>

### 2. Encapsulation
<p>Kelas `Kasirtransportasi` dapat memiliki metode untuk menghitung total pembayaran dan mengakses informasi tarif transportasi.
</p>

### 3. Polymorphism
<p>Metode `hitungBiaya()` bisa digunakan dalam kelas `Kasir` untuk menghitung total biaya dan transaksi</p>

### 4. Abstract
<p>Kelas `Mobil` dan `Bus` yang merupakan subclass dari `Transportasi` harus mengimplementasikan metode `hitungBiaya()`, sehingga pengguna hanya perlu berfokus pada penggunaan metode ini tanpa harus memahami detail implementasinya</p>

## Demo Proyek
<ul>
  <li>Github: <a href="https://github.com/melaseptiani-bit">Github</a></li>
  <li>Youtube: <a href="https://youtu.be/kI6P7lJ4Kjg">Youtube</a></li>
</ul>
