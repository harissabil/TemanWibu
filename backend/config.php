<?php
$host = "localhost";
$dbname = "projekbasdat";
$user = "postgres";
$password = "postgres";
$db = pg_connect('host=' . $host . ' dbname=' . $dbname . ' user=' . $user . ' password=' . $password);
if (!$db) {
    die("Gagal terhubung dengan database: " . pg_connect_error());
}
