<?php

class Json_process extends CI_Controller {
  private $host = "localhost";
  private $user = "u9445670_absensi";
  private $password = "u9445670_absensi";
  private $namaDb = "u9445670_absensi";

    function getInfoApps(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        $message = "";
        $versionApps = "";
        $link = "";
    
        $row_array['response'] = "";
    
        $result = mysqli_query($kon, "SELECT * FROM `info_apps` WHERE `id_info` = '1'");
        if ($result) {
            $dataExist = false;
            
            while ($row = mysqli_fetch_array($result)) {
                $dataExist = true;
                $message = $row['message'];
                $versionApps = $row['version_apps'];
                $link = $row['link'];
            }
            
            if ($dataExist){
                $row_array['message'] = $message;
                $row_array['version_apps'] = $versionApps;
                $row_array['link'] = $link;
                $row_array['response'] = "Success";
                
                echo json_encode($row_array);
            }
            else{
                $row_array['response'] = "Error, terjadi kesalahan database";
                echo json_encode($row_array);
            }
        }
        else{
            $row_array['response'] = "Error, terjadi kesalahan server";
            echo json_encode($row_array);
        }
    }
  
  function loginUser(){
    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content);
    $username = $decoded->username;
    $password = $decoded->password;
    $token = $decoded->token;

    $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
    $idUserServer = "";
    $userServer = "";
    $namaServer = "";
    $emailServer = "";
    $levelServer = "";
    $fotoServer = "";
    $phoneServer = "";
    $idDinasServer = "";

    $row_array['response'] = "";

    if (isset($username) && isset($password)) {
      $result = mysqli_query($kon, "SELECT * FROM `users` WHERE `username` = '$username'");
      if ($result) {
        $userExist = false;
        $userActive = false;
        $hashed_password = "";

        while ($row = mysqli_fetch_array($result)) {
          if ($row['active'] == 1){
            $userActive = true;
          }
          $userExist = true;
          $idUserServer = $row['id_users'];
          $userServer = $row['username'];
          $hashed_password = $row['password'];
          $namaServer = $row['nama'];
          $emailServer = $row['email'];
          $levelServer = $row['level'];
          $fotoServer = $row['foto'];
          $phoneServer = $row['phone'];
          $idDinasServer = $row['id_dinas'];
        }
        
        if ($userExist){
          if(password_verify($password, $hashed_password)) {
            if ($userActive){
              $row_array['idUser'] = $idUserServer;
              $row_array['username'] = $userServer;
              $row_array['nama'] = $namaServer;
              $row_array['email'] = $emailServer;
              $row_array['level'] = $levelServer;
              $row_array['foto'] = $fotoServer;
              $row_array['phone'] = $phoneServer;
              $row_array['idDinas'] = $idDinasServer;
              $row_array['token'] = $token;
              $row_array['response'] = "Success";

              $this->setToken($row_array, $token);
            }
            else{
              $row_array['response'] = "User sudah di nonaktifkan oleh Admin";
              echo json_encode($row_array);
            }
          }
          else{
            $row_array['response'] = "Wrong password";
            echo json_encode($row_array);
          }
        }
        else{
          $row_array['response'] = "Username doesn't exist";
          echo json_encode($row_array);
        }
      }
      else{
        $row_array['response'] = "Username doesn't exist";
        echo json_encode($row_array);
      }
    } else {
      $row_array['response'] = "Failed to send data";
      echo json_encode($row_array);
    }
  }
  
  function checkToken(){
      $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $username = $decoded->username;
        $token = $decoded->token;
        
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        if (isset($username) && isset($token)) {
            $result = mysqli_query($kon, "SELECT * FROM `users` WHERE `username` = '$username'");
            
            if ($result) {
                $tokenServer = "";
                
                while ($row = mysqli_fetch_array($result)) {
                    $tokenServer = $row['token'];
                }
                
                if ($tokenServer == $token){
                    $row_array['response'] = "Success";
                    
                    echo json_encode($row_array);
                }
                else{
                    $row_array['response'] = "Maaf, Akun Anda telah login di perangkat yang lain";
                    
                    echo json_encode($row_array);
                }
            }
            else{
                $row_array['response'] = "Data pegawai tidak ditemukan";
                echo json_encode($row_array);
            }
        } else {
            $row_array['response'] = "Failed to send data";
            echo json_encode($row_array);
        }
  }
  
  function getDataPegawai(){
    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content);
    $username = $decoded->username;

    $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
    
    $idUserServer = "";
    $userServer = "";
    $namaServer = "";
    $emailServer = "";
    $levelServer = "";
    $fotoServer = "";
    $phoneServer = "";
    $token = "";
    $idDinasServer = "";

    $row_array['response'] = "";

    if (isset($username)) {
      $result = mysqli_query($kon, "SELECT * FROM `users` WHERE `username` = '$username'");
      if ($result) {
        $userExist = false;

        while ($row = mysqli_fetch_array($result)) {
          $userExist = true;
          $idUserServer = $row['id_users'];
          $userServer = $row['username'];
          $hashed_password = $row['password'];
          $namaServer = $row['nama'];
          $emailServer = $row['email'];
          $levelServer = $row['level'];
          $fotoServer = $row['foto'];
          $phoneServer = $row['phone'];
          $token = $row['token'];
          $idDinasServer = $row['id_dinas'];
        }
        
        if ($userExist){
          $row_array['idUser'] = $idUserServer;
          $row_array['username'] = $userServer;
          $row_array['nama'] = $namaServer;
          $row_array['email'] = $emailServer;
          $row_array['level'] = $levelServer;
          $row_array['foto'] = $fotoServer;
          $row_array['phone'] = $phoneServer;
          $row_array['idDinas'] = $idDinasServer;
          $row_array['token'] = $token;
          $row_array['response'] = "Success";
          
          echo json_encode($row_array);
        }
        else{
          $row_array['response'] = "Data pegawai tidak ditemukan";
          echo json_encode($row_array);
        }
      }
      else{
        $row_array['response'] = "Data pegawai tidak ditemukan";
        echo json_encode($row_array);
      }
    } else {
      $row_array['response'] = "Failed to send data";
      echo json_encode($row_array);
    }
  }
  
  function getDataAdmin(){
    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content);
    $id_dinas = $decoded->id_dinas;

    $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
    
    $idUserServer = "";
    $userServer = "";
    $namaServer = "";
    $emailServer = "";
    $levelServer = "";
    $fotoServer = "";
    $phoneServer = "";
    $token = "";
    $idDinasServer = "";

    $row_array['response'] = "";

    if (isset($id_dinas)) {
      $result = mysqli_query($kon, "SELECT * FROM `users` WHERE `id_dinas` = '$id_dinas' AND `level` = 'Operator'");
      if ($result) {
        $userExist = false;

        while ($row = mysqli_fetch_array($result)) {
          $userExist = true;
          $idUserServer = $row['id_users'];
          $userServer = $row['username'];
          $hashed_password = $row['password'];
          $namaServer = $row['nama'];
          $emailServer = $row['email'];
          $levelServer = $row['level'];
          $fotoServer = $row['foto'];
          $phoneServer = $row['phone'];
          $token = $row['token'];
          $idDinasServer = $row['id_dinas'];
        }
        
        if ($userExist){
          $row_array['idUser'] = $idUserServer;
          $row_array['username'] = $userServer;
          $row_array['nama'] = $namaServer;
          $row_array['email'] = $emailServer;
          $row_array['level'] = $levelServer;
          $row_array['foto'] = $fotoServer;
          $row_array['phone'] = $phoneServer;
          $row_array['idDinas'] = $idDinasServer;
          $row_array['token'] = $token;
          $row_array['response'] = "Success";
          
          echo json_encode($row_array);
        }
        else{
          $row_array['response'] = "Admin tidak ditemukan";
          echo json_encode($row_array);
        }
      }
      else{
        $row_array['response'] = "Admin tidak ditemukan";
        echo json_encode($row_array);
      }
    } else {
      $row_array['response'] = "Failed to send data";
      echo json_encode($row_array);
    }
  }

  function createAbsensi(){
        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $nip = $decoded->nip;
        $id_hari = $decoded->id_hari;
        $latitude = $decoded->latitude;
        $longitude = $decoded->longitude;
        $jenis = $decoded->jenis;
        $date_created = $decoded->date_created;
        
        
        $status = '0';
        $img = '';
        $confirmed_by = '';
        
        if(isset($decoded->status)){
            $status = $decoded->status;
        }
        
        if(isset($decoded->img)){
            $img = $decoded->img;
        }
        
        if(isset($decoded->confirmed_by)){
            $confirmed_by = $decoded->confirmed_by;
        }
        
        $this->checkAbsensi($nip, $id_hari, $latitude, $longitude, $jenis, $date_created, $status, $img, $confirmed_by);
  }
  
  function checkAbsensi($nip, $id_hari, $latitude, $longitude, $jenis,   $date_created, $status, $img, $confirmed_by){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        $resultCheck = mysqli_query($kon, "SELECT * FROM `absensi` WHERE `nip` = '$nip' AND `id_hari` = '$id_hari' AND `jenis` = '$jenis'");
            
        if ($resultCheck) {
            $id_absensi = "";
    
            while ($row = mysqli_fetch_array($resultCheck)) {
                $id_absensi = $row['id_absensi'];
            }
    
            if ($id_absensi != ""){
                $result = mysqli_query($kon, "UPDATE `absensi` SET `latitude` = '$latitude', `longitude` = '$longitude', `date_created` = '$date_created', `confirmed_by` = '$confirmed_by', `status` = '0' WHERE `id_absensi` = '$id_absensi'");
            
                if ($result) {
                    $row_array['response'] = "Success";
                    $row_array['id_absensi'] = $id_absensi;
                    
                    echo json_encode($row_array);
                }
                else{
                    $row_array['response'] = "Failed to send data";
                    echo json_encode($row_array);
                }
            }
            else{
                $result = mysqli_query($kon, "INSERT INTO `absensi` (`nip`, `id_hari`, `img`, `latitude`, `longitude`, `jenis`, `status`, `date_created`, `confirmed_by`) VALUES ('$nip', '$id_hari', '$img', '$latitude', '$longitude', '$jenis', '$status', '$date_created', '$confirmed_by');");
    
                if ($result) {
                    $id_absensi = mysqli_insert_id($kon);
                    
                    $row_array['response'] = "Success";
                    $row_array['id_absensi'] = $id_absensi;
                    
                    echo json_encode($row_array);
                }
                else{
                    $row_array['response'] = "Failed to send data";
                    echo json_encode($row_array);
                }
            }                
        }
        else{
            $result = mysqli_query($kon, "INSERT INTO `absensi` (`nip`, `id_hari`, `img`, `latitude`, `longitude`, `jenis`, `status`, `date_created`, `confirmed_by`) VALUES ('$nip', '$id_hari', '$img', '$latitude', '$longitude', '$jenis', '$status', '$date_created', '$confirmed_by');");
    
            if ($result) {
                $id_absensi = mysqli_insert_id($kon);
                
                $row_array['response'] = "Success";
                $row_array['id_absensi'] = $id_absensi;
                
                echo json_encode($row_array);
            }
            else{
                $row_array['response'] = "Failed to send data";
                echo json_encode($row_array);
            }
        }
        mysqli_close($kon);
    }
  
  function updateAbsensiUlang(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);

        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $id_absensi = $decoded->id_absensi;
        $latitude = $decoded->latitude;
        $longitude = $decoded->longitude;
        $date_created = $decoded->date_created;
        
        $result = mysqli_query($kon, "UPDATE `absensi` SET `latitude` = '$latitude', `longitude` = '$longitude', `date_created` = '$date_created', `status` = '0' WHERE `id_absensi` = '$id_absensi'");
        
        if ($result) {
            $row_array['response'] = "Success";
            $row_array['id_absensi'] = $id_absensi;
            
            echo json_encode($row_array);
        }
        else{
            $row_array['response'] = "Failed to send data";
            echo json_encode($row_array);
        }
        
        mysqli_close($kon);
  }
  
  function updateAbsensiStatus(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);

        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $id_absensi = $decoded->id_absensi;
        $status = $decoded->status;
        $jenis = $decoded->jenis;
        $confirmed_by = $decoded->confirmed_by;
        
        $result = mysqli_query($kon, "UPDATE `absensi` SET `status` = '$status', `jenis` = '$jenis', `confirmed_by` = '$confirmed_by' WHERE `id_absensi` = '$id_absensi'");
        
        if ($result) {
            $row_array['response'] = "Success";
            
            echo json_encode($row_array);
        }
        else{
            $row_array['response'] = "Failed to send data";
            echo json_encode($row_array);
        }
        
        mysqli_close($kon);
  }
  
  function updateProfil(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);

        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        
        if (isset($decoded->id_users) && isset($decoded->mail) && isset($decoded->phone) && isset($decoded->password)) {
            $email = $decoded->mail;
            $phone = $decoded->phone;
            $nama = $decoded->nama;
            $id_users = $decoded->id_users;
            $password = $decoded->password;
            $encryptPassword = password_hash($password, PASSWORD_BCRYPT);
            
            $result = mysqli_query($kon, "UPDATE `users` SET `email` = '$email', `phone` = '$phone', `nama` = '$nama', `password` = '$encryptPassword' WHERE `id_users` = '$id_users'")  ;
        
            if ($result) {
                $row_array['response'] = "Success";
                
                echo json_encode($row_array);
            }
            else{
                $row_array['response'] = "Failed to send data";
                
                echo json_encode($row_array);
            }
            
            mysqli_close($kon);
        }
        else if (isset($decoded->id_users) && isset($decoded->mail)  && isset($decoded->phone)){
            $email = $decoded->mail;
            $nama = $decoded->nama;
            $phone = $decoded->phone;
            $id_users = $decoded->id_users;
            
            $result = mysqli_query($kon, "UPDATE `users` SET `email` = '$email', `phone` = '$phone', `nama` = '$nama' WHERE `id_users` = '$id_users'");
        
            if ($result) {
                $row_array['response'] = "Success";
                
                echo json_encode($row_array);
            }
            else{
                $row_array['response'] = "Failed to send data";
                echo json_encode($row_array);
            }
            
            mysqli_close($kon);
        }
        else{
            $row_array['response'] = "Error, please fill email";
            echo json_encode($row_array);
        }
  }
  
  function setToken($data_user, $token){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);

        $username = $data_user['username'];
    
        $result = mysqli_query($kon, "UPDATE `users` SET `token` = '$token' WHERE `username` = '$username'");

        if ($result) {
            echo json_encode($data_user);
        }
        else{
            $row_array['response'] = "Failed to update Token";
            echo json_encode($row_array);
        }
        
        mysqli_close($kon);
  }
  
  function deleteToken(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $username = $decoded->username;
    
        $result = mysqli_query($kon, "UPDATE `users` SET `token` = '' WHERE `username` = '$username'");

        if ($result) {
            $row_array['response'] = "Success";
            echo json_encode($row_array);
        }
        else{
            $row_array['response'] = "Failed to update Token";
            echo json_encode($row_array);
        }
        
        mysqli_close($kon);
  }

  function getHariKerja(){
    $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
    
    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content);
    $tglSekarang = $decoded->tglSekarang;

    $resultIdHari = "";
    $resultHari = "";
    $resultMasukKerja = "";
    $resultPulangKerja = "";
    $resultMasukApel = "";
    $resultPulangApel = "";
    $keterangan = "";

    $row_array['response'] = "";

    if (isset($tglSekarang)) {
      $result = mysqli_query($kon, "SELECT * FROM `hari_kerja` WHERE `hari` = '$tglSekarang'");
      
      if ($result) {
        $dataExist = false;

        while ($row = mysqli_fetch_array($result)) {
          $dataExist = true;
          $resultIdHari = $row['id_hari'];
          $resultHari = $row['hari'];
          $resultMasukKerja = $row['masuk_kerja'];
          $resultPulangKerja = $row['pulang_kerja'];
          $resultMasukApel = $row['masuk_apel'];
          $resultPulangApel = $row['pulang_apel'];
          $keterangan = $row['keterangan'];
        }
        
        if ($dataExist){
          $row_array['id_hari'] = $resultIdHari;
          $row_array['hari'] = $resultHari;
          $row_array['masuk_kerja'] = $resultMasukKerja;
          $row_array['pulang_kerja'] = $resultPulangKerja;
          $row_array['masuk_apel'] = $resultMasukApel;
          $row_array['pulang_apel'] = $resultPulangApel;
          $row_array['keterangan'] = $keterangan;
          $row_array['response'] = "Success";
          echo json_encode($row_array);
        }
        else{
          $row_array['response'] = "Tanggal tidak tersedia";
          echo json_encode($row_array);
        }
      }
      else{
        $row_array['response'] = "Tanggal tidak tersedia";
        echo json_encode($row_array);
      }
    } else {
      $row_array['response'] = "Failed to send data";
      echo json_encode($row_array);
    }
  }
  
  function getAbsensiByJenis(){
    $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
    
    $content = trim(file_get_contents("php://input"));
    $decoded = json_decode($content);
    $nip = $decoded->nip;
    $id_hari = $decoded->id_hari;
    $jenis = $decoded->jenis;

    $resultIdAbsensi = "";
    $resultNip = "";
    $resultIdHari = "";
    $resultImg = "";
    $resultLatitude = "";
    $resultLongitude = "";
    $resultJenis = "";
    $resultStatus = "";
    $resultDateCreated = "";

    $row_array['response'] = "";

    if (isset($nip) && isset($id_hari) && isset($jenis)) {
        $result = mysqli_query($kon, "SELECT * FROM `absensi` WHERE `nip` = '$nip' AND `id_hari` = '$id_hari' AND `jenis` = '$jenis'");
        
        if ($result) {
            $dataExist = false;
    
            while ($row = mysqli_fetch_array($result)) {
              $dataExist = true;
              $resultIdAbsensi = $row['id_absensi'];
              $resultNip = $row['nip'];
              $resultIdHari = $row['id_hari'];
              $resultImg = $row['img'];
              $resultLatitude = $row['latitude'];
              $resultLongitude = $row['longitude'];
              $resultJenis = $row['jenis'];
              $resultStatus = $row['status'];
              $resultDateCreated = $row['date_created'];
            }
        
            if ($dataExist){
              $row_array['id_absensi'] = $resultIdAbsensi;
              $row_array['nip'] = $resultNip;
              $row_array['id_hari'] = $resultIdHari;
              $row_array['img'] = $resultImg;
              $row_array['latitude'] = $resultLatitude;
              $row_array['longitude'] = $resultLongitude;
              $row_array['jenis'] = $resultJenis;
              $row_array['status'] = $resultStatus;
              $row_array['date_created'] = $resultDateCreated;
              $row_array['response'] = "Success";
              echo json_encode($row_array);
            }
            else{
              $row_array['response'] = "Absensi tidak ditemukan";
              echo json_encode($row_array);
            }
        }
          else{
            $row_array['response'] = "Absensi tidak ditemukan";
            echo json_encode($row_array);
          }
        } else {
          $row_array['response'] = "Absensi tidak ditemukan";
          echo json_encode($row_array);
        }
    }
    
    function getAbsensi(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $nip = $decoded->nip;
        $id_hari = $decoded->id_hari;
    
        if (isset($nip) && isset($id_hari)) {
            $result = mysqli_query($kon, "SELECT * FROM `absensi` WHERE `nip` = '$nip' AND `id_hari` = '$id_hari'");
            
            $data_result = array();
            
            if ($result) {
                $dataExist = false;
        
                while ($row = mysqli_fetch_array($result)) {
                  $dataExist = true;
                  $dataAbsen['id_absensi'] = $row['id_absensi'];
                  $dataAbsen['nip'] = $row['nip'];
                  $dataAbsen['id_hari'] = $row['id_hari'];
                  $dataAbsen['img'] = $row['img'];
                  $dataAbsen['latitude'] = $row['latitude'];
                  $dataAbsen['longitude'] = $row['longitude'];
                  $dataAbsen['jenis'] = $row['jenis'];
                  $dataAbsen['status'] = $row['status'];
                  $dataAbsen['date_created'] = $row['date_created'];
                  
                  array_push($data_result,$dataAbsen);
                }
            
                if ($dataExist){
                  $row_array['response'] = "Success";
                  $row_array['list_absen'] = $data_result;
                  echo json_encode($row_array);
                }
                else{
                  $row_array['response'] = "Absensi tidak ditemukan";
                  $noData = array();
                  $row_array['list_absen'] = $noData;
                  echo json_encode($row_array);
                }
            }
            else{
                $row_array['response'] = "Absensi tidak ditemukan";
                $noData = array();
                $row_array['list_absen'] = $noData;
                echo json_encode($row_array);
            }
        } else {
            $row_array['response'] = "Absensi tidak ditemukan";
            $noData = array();
            $row_array['list_absen'] = $noData;
            echo json_encode($row_array);
        }
    }
    
    function getUserAlreadyAbsensi(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $id_hari = $decoded->id_hari;
        $id_dinas = $decoded->id_dinas;
        
        if (isset($id_hari) && isset($id_dinas)) {
            $result = mysqli_query($kon, "SELECT * FROM `absensi` WHERE `id_hari` = '$id_hari' ORDER BY `status` ASC, `date_created` DESC");
            
            $data_result = array();
            
            if ($result) {
                $dataExist = false;
        
                while ($row = mysqli_fetch_array($result)) {
                  
                  $nip = $row['nip'];
                  $dataAbsen['id_absensi'] = $row['id_absensi'];
                  $dataAbsen['nip'] = $nip;
                  $dataAbsen['id_hari'] = $row['id_hari'];
                  $dataAbsen['img'] = $row['img'];
                  $dataAbsen['latitude'] = $row['latitude'];
                  $dataAbsen['longitude'] = $row['longitude'];
                  $dataAbsen['jenis'] = $row['jenis'];
                  $dataAbsen['status'] = $row['status'];
                  $dataAbsen['date_created'] = $row['date_created'];
                 
                  if (isset($nip)) {
                    if(isset($decoded->search)){
                        $search = $decoded->search;
                        $resultUser = mysqli_query($kon, "SELECT * FROM `users` WHERE `username` = '$nip' AND `nama` LIKE '%$search%'");
                    }
                    else{
                        $resultUser = mysqli_query($kon, "SELECT * FROM `users` WHERE `username` = '$nip'");
                    }
                    
                    $idUserServer = "";
                    $userServer = "";
                    $namaServer = "";
                    $emailServer = "";
                    $levelServer = "";
                    $fotoServer = "";
                    $phoneServer = "";
                    $token = "";
                    $idDinasServer = "";
                    
                    if ($resultUser) {
                      $userExist = false;

                      while ($rowUser = mysqli_fetch_array($resultUser)) {
                        if ($id_dinas == $rowUser['id_dinas']) {
                          $userExist = true;
                          $dataExist = true;
                          
                          $idUserServer = $rowUser['id_users'];
                          $hashed_password = $rowUser['password'];
                          $namaServer = $rowUser['nama'];
                          $emailServer = $rowUser['email'];
                          $levelServer = $rowUser['level'];
                          $fotoServer = $rowUser['foto'];
                          $phoneServer = $rowUser['phone'];
                          $token = $rowUser['token'];
                          $idDinasServer = $rowUser['id_dinas'];
                        }
                      }
                      
                      if ($userExist){
                        $dataAbsen['idUser'] = $idUserServer;
                        $dataAbsen['nama'] = $namaServer;
                        $dataAbsen['email'] = $emailServer;
                        $dataAbsen['level'] = $levelServer;
                        $dataAbsen['foto'] = $fotoServer;
                        $dataAbsen['phone'] = $phoneServer;
                        $dataAbsen['idDinas'] = $idDinasServer;
                        $dataAbsen['token'] = $token;
                        
                        array_push($data_result, $dataAbsen);
                      }
                    }
                  }
                }
            
                if ($dataExist){
                  $dataResult['response'] = "Success";
                  $dataResult['list_absen'] = $data_result;
                  echo json_encode($dataResult);
                }
                else{
                  $dataResult['response'] = "Belum ada data";
                  $noData = array();
                  $dataResult['list_absen'] = $noData;
                  echo json_encode($dataResult);
                }
            }
            else{
                $dataResult['response'] = "Belum ada data";
                $noData = array();
                $dataResult['list_absen'] = $noData;
                echo json_encode($dataResult);
            }
        } else {
            $dataResult['response'] = "Data dinas dan hari kerja tidak ditemukan";
            $noData = array();
            $dataResult['list_absen'] = $noData;
            echo json_encode($dataResult);
        }
    }
    
    function getUserNotAbsensi(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $id_hari = $decoded->id_hari;
        $id_dinas = $decoded->id_dinas;
        
        if (isset($id_hari) && isset($id_dinas)) {
            if(isset($decoded->search)){
                $search = $decoded->search;
                $resultUser = mysqli_query($kon, "SELECT * FROM `users` WHERE `id_dinas` = '$id_dinas' AND `level` = 'Pegawai' AND `nama` LIKE '%$search%' LIMIT 100");
            }
            else{
                $resultUser = mysqli_query($kon, "SELECT * FROM `users` WHERE `id_dinas` = '$id_dinas' AND `level` = 'Pegawai' LIMIT 100");
            }
          

          $data_result = array();
          
          if ($resultUser) {
            $userExist = false;

            while ($rowUser = mysqli_fetch_array($resultUser)) {
              if ($id_dinas == $rowUser['id_dinas']) {
                $username = $rowUser['username'];
                
                $dataAbsen['idUser'] = $rowUser['id_users'];
                $dataAbsen['nama'] = $rowUser['nama'];
                $dataAbsen['username'] = $username;
                $dataAbsen['email'] = $rowUser['email'];
                $dataAbsen['level'] = $rowUser['level'];
                $dataAbsen['foto'] = $rowUser['foto'];
                $dataAbsen['phone'] = $rowUser['phone'];
                $dataAbsen['idDinas'] = $rowUser['id_dinas'];
                $dataAbsen['token'] = $rowUser['token'];
                
                $result = mysqli_query($kon, "SELECT * FROM `absensi` WHERE `id_hari` = '$id_hari' AND `nip` = '$username'");
                
                if ($result) {
                    $dataExist = false;
                    
                    while ($rowAbsen = mysqli_fetch_array($result)) {
                        $dataExist = true;
                    }
                    
                    if($dataExist == false){
                        $userExist = true;
                    
                        array_push($data_result, $dataAbsen);
                    }
                }
                else{
                    $userExist = true;
                    
                    array_push($data_result, $dataAbsen);
                }
              }
            }
            
            if ($userExist){
              $dataResult['response'] = "Success";
              $dataResult['list_absen'] = $data_result;
              echo json_encode($dataResult);
            }
            else{
              $dataResult['response'] = "Tidak ada data user yang ditemukan";
              $noData = array();
              $dataResult['list_absen'] = $noData;
              echo json_encode($dataResult);
            }
          }
          else{
            $dataResult['response'] = "Tidak ada data user yang ditemukan";
            $noData = array();
            $dataResult['list_absen'] = $noData;
            echo json_encode($dataResult);
          }
        }
        else{
          $dataResult['response'] = "Data dinas dan hari kerja tidak ditemukan";
          $noData = array();
          $dataResult['list_absen'] = $noData;
          echo json_encode($dataResult);
        }         
    }
    
    function getRiwayat(){
        $kon = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb);
        
        $content = trim(file_get_contents("php://input"));
        $decoded = json_decode($content);
        $nip = $decoded->nip;
    
        if (isset($nip)) {
            $result = mysqli_query($kon, "SELECT * FROM `absensi` WHERE `nip` = '$nip' ORDER BY `date_created` DESC");
            
            $data_result = array();
            
            if ($result) {
                $dataExist = false;
        
                while ($row = mysqli_fetch_array($result)) {
                  $dataExist = true;
                  $dataAbsen['id_absensi'] = $row['id_absensi'];
                  $dataAbsen['nip'] = $row['nip'];
                  $dataAbsen['id_hari'] = $row['id_hari'];
                  $dataAbsen['img'] = $row['img'];
                  $dataAbsen['latitude'] = $row['latitude'];
                  $dataAbsen['longitude'] = $row['longitude'];
                  $dataAbsen['jenis'] = $row['jenis'];
                  $dataAbsen['status'] = $row['status'];
                  $dataAbsen['date_created'] = $row['date_created'];
                  
                  array_push($data_result,$dataAbsen);
                }
            
                if ($dataExist){
                  $row_array['response'] = "Success";
                  $row_array['list_absen'] = $data_result;
                  echo json_encode($row_array);
                }
                else{
                  $row_array['response'] = "Belum ada data absensi";
                  $noData = array();
                  $row_array['list_absen'] = $noData;
                  echo json_encode($row_array);
                }
            }
            else{
                $row_array['response'] = "Belum ada data absensi";
                $noData = array();
                $row_array['list_absen'] = $noData;
                echo json_encode($row_array);
            }
        } else {
            $row_array['response'] = "Belum ada data absensi";
            $noData = array();
            $row_array['list_absen'] = $noData;
            echo json_encode($row_array);
        }
    }
    
    function compressImage($destination) { 
        $config['image_library'] = 'gd2';
        $config['source_image'] = $destination;
        $config['create_thumb'] = false;
        $config['maintain_ratio'] = TRUE;
        $config['quality'] = '90%';
        $config['width']         = 500;
        $config['height']       = 500;
        
        $this->load->library('image_lib', $config);
        
        return $this->image_lib->resize(); 
    }
    
    // Fungsi String Acak
    function randomString($length)
    {
        $str        = "";
        $characters = 'abcdefghijklmnopqrstuvwxyz1234567890';
        $max        = strlen($characters) - 1;
        for ($i = 0; $i < $length; $i++) {
            $rand = mt_rand(0, $max);
            $str .= $characters[$rand];
        }
        return $str;
    }
    
    function uploadImageAbsen(){
		$upload_url = site_url().'assets/img/absensi/'; 
		echo $upload_url;
		$response = array(); 
		$id_absensi = $_POST['id_absensi'];
		$nameFile = $this->randomString(16);
// 		$nameFile = $_POST['nameFile'];

		if($_SERVER['REQUEST_METHOD']=='POST'){
			if(isset($_POST['id_absensi']) and isset($_FILES['image']['name'])){
				$con = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb) or die('Unable to Connect...');

				$fileinfo = pathinfo($_FILES['image']['name']);

				$extension = $fileinfo['extension'];

				$file_url = $upload_url.$nameFile.'.'.$extension;
				
				$file_path = 'assets/img/absensi/'.$nameFile.'.'.$extension;

				try{
                    $config['upload_path']= "./assets/img/absensi/";
                    $config['allowed_types']= 'jpg|jpeg|png';
                    $config['max_size']='100000';
                    $config['file_name'] = $nameFile;
                    
                    $this->load->library('upload', $config);
                    
                    if($this->upload->do_upload('image')){
                        $resultCompress = $this->compressImage($file_path);
                        
                        if($resultCompress){
                            $sql = "UPDATE `absensi` SET `img` = '$file_url' WHERE `id_absensi` = '$id_absensi'";
    					
        					if(mysqli_query($con,$sql)){
        						$response['error'] = false; 
        						$response['url'] = $file_url; 
        						$response['name'] = $id_absensi;
        					}
                        }
                        else{
                            $response['error'] = true;
                            $response['message'] = "Gagal mengkompress foto";
                        }
                        
                    }else{ 
                        $response['error'] = true;
                        $response['message'] = "Gagal mengupload foto";
                    } 
					
				}catch(Exception $e){
					$response['error']=true;
					$response['message']=$e->getMessage();
				}		
				echo json_encode($response);


				mysqli_close($con);
			}else{
				$response['error']=true;
				$response['message']='Please choose a file';
				echo json_encode($response);
			}
		}
	}
	
	function uploadImageProfil(){
		$upload_url = site_url().'assets/img/users/'; 
		echo $upload_url;
		$response = array(); 
		$id_users = $_POST['id_users'];
		$nameFile = $_POST['nameFile'];
// 		$nameFile = $this->randomString(16);

		if($_SERVER['REQUEST_METHOD']=='POST'){
			if(isset($_POST['id_users']) and isset($_POST['nameFile']) and isset($_FILES['image']['name'])){
				$con = mysqli_connect($this->host, $this->user, $this->password, $this->namaDb) or die('Unable to Connect...');

				$fileinfo = pathinfo($_FILES['image']['name']);

				$extension = $fileinfo['extension'];

				$file_url = $upload_url.$nameFile.'.'.$extension;
				
				$file_path = 'assets/img/users/'.$nameFile.'.'.$extension;

				try{
					move_uploaded_file($_FILES['image']['tmp_name'],$file_path);
					
					$sql = "UPDATE `users` SET `foto` = '$file_url' WHERE `id_users` = '$id_users'";
					
					if(mysqli_query($con,$sql)){
						$response['error'] = false; 
						$response['url'] = $file_url; 
						$response['name'] = $id_users;
					}
				}catch(Exception $e){
					$response['error']=true;
					$response['message']=$e->getMessage();
				}		
				echo json_encode($response);


				mysqli_close($con);
			}else{
				$response['error']=true;
				$response['message']='Please choose a file';
				echo json_encode($response);
			}
		}
	}
}
?>
