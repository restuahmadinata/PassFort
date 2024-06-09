# PassFort

Passfort adalah aplikasi password manager yang dibuat menggunakan Javafx dengan tampilan minimalis dan fitur esensial.

## Group Name : Weebs-Prjkt
Nama Anggota :

  1. H071231021- Restu Ahmadinata
  2. H071231001- Zaenab Putri Az Zakiyyah
  3. H071231037- Mohammad Alief Hasyani

## Tema yang dipilih : Tools and Productivity
## Nama Pendamping : Kelvin Leonardo S
## Tim Juri :

  1. Ketua Umum BEM
  2. Muhammad Aqsa Ramadhan

## Executive Summary
PassFort adalah sebuah aplikasi Password Manager yang dirancang untuk membantu masyarakat Indonesia (atau orang asing juga bisa) yang sering mengalami lupa kata sandi, terutama untuk akun-akunpenting seperti Gmail, Facebook, Instagram, dan aplikasi lainnya yang memerlukan keamanan tinggi

### 1. Masalah
Di era digital saat ini, pengguna internet semakin banyak memiliki berbagai akun online untuk berbagai keperluan seperti media sosial, email, dan lain-lain. Setiap akun ini pasti memerlukan kata sandi yang kuat untuk menjaga keamanan informasi pribadi. Masalah utama yang sering dihadapi pengguna salah satunya adalah sering mengalami lupa kata sandi, terutama untuk akun-akun penting seperti Gmail, Facebook, Instagram, dan aplikasi lainnya yang memerlukan keamanan tinggi.

### 2. Solusi
Kami merancang PassFort - sebuah aplikasi password manager - agar dapat membantu pengguna mengatasi masalah kesulitan mengingat banyak kata sandi dengan menyediakan tempat yang aman untuk menyimpan dan mengelola kata sandi mereka.
Dengan demikian, pengguna tidak perlu lagi menggunakan kata sandi yang sama untuk berbagai akun, yang dapat membahayakan keamanan dan privasi.

## Fitur Aplikasi
1. Menggunakan dua database yaitu UserDatabase yang menyimpan akun user (untuk Sign Up dan Log In), dan AppDatabase yang menyimpan data akun yang dibuat oleh user.
2. Sign Up untuk akun baru dan Log In untuk akun yang sudah ada pada UserDatabase.
3. Dapat membuat akun baru untuk disimpan pada AppDatabase.
4. Dapat meng-update password akun yang sudah ada padaAppDatabase.
5. Dapat menghapus akun yang dibuat oleh user pada AppDatabase.
6. Menampilkan tabel/database akun yang telah dibuat oleh user pada AppDatabase.
7. Menampilkan profil user (masih pengembangan)
8. Menampilkan profil developer pada About Us.
9. Akun admin dapat menghapus akun user pada UserDatabase.

## Penjelasan Penerapan Prinsip OOP
1. User.java
User adalah kelas abstrak yang tidak dapat diinstansiasi secara langsung. Kelas ini menyediakan kerangka dasar (method abstrak) yang harus diimplementasikan oleh subclass. Ini adalah contoh dari Abstraction. Kelas User menyembunyikan implementasi detail dari variabel instance seperti id, username, password, dan role dengan menggunakan akses modifier private. Akses ke variabel ini diberikan melalui method getter. Ini adalah contoh dari Encapsulation.

`(Abstraction, Encapsulation)`

2. Regular.java dan Admin.java
Kelas Regular dan Admin mewarisi dari kelas User, menggunakan kembali properti dan method yang ada di kelas User. Ini adalah contoh dari Inheritance. Kelas Regular dan Admin mengoverride method `displayUserRole()` dari kelas User untuk memberikan implementasi spesifik. Ini adalah contoh dari Polymorphism.

`(Inheritance, Polymorphism)`

4. UserAppData.java
Kelas UserAppData menggunakan akses modifier private untuk variabel instance dan menyediakan method getter dan setter untuk mengakses dan memodifikasi nilai variabel tersebut. Ini adalah contoh dari Encapsulation.

`(Encapsulation)`

## Mentoring
1. Kelvin Leonardo S – Rabu, 22 Mei 2024-
2. Kelvin Leonardo S – Kamis, 23 Mei 2024
3. Kelvin Leonardo S – Kamis, 30 Mei 2024
4. Kelvin Leonardo S – Jumat, 31 Mei 2024

## Screenshots

### *Sign Scene*
![Sign Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/signScene.png)

### *Login Scene*
![Login Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/loginScene.png)

### *Admin Scene*
![Admin Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/adminScene.png)

### *Admin Thanos*
![Admin Thanos](https://github.com/restuahmadinata/PassFort/blob/main/readme/adminThanos.png)

### *Create Scene*
![Create Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/createScene.png)

### *Update Scene*
![Update Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/updateScene.png)

### *Delete Scene*
![Delete Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/deleteScene.png)

### *Generate Scene*
![Generate Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/generateScene.png)

### *Database Scene*
![Database Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/databaseScene.png)

### *User Scene*
![User Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/userScene.png)

### *About Scene*
![About Scene](https://github.com/restuahmadinata/PassFort/blob/main/readme/aboutScene.png)


## Pengujian Aplikasi
![Table1](https://github.com/restuahmadinata/PassFort/blob/main/readme/tabel1.png)
![Table2](https://github.com/restuahmadinata/PassFort/blob/main/readme/tabel2.png)
![Table3](https://github.com/restuahmadinata/PassFort/blob/main/readme/tabel3.png)
