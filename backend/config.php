<?php
$host = "localhost";
$dbname = "projekbasdat";
$user = "postgres";
$password = "postgres";
$db = pg_connect('host=' . $host . ' dbname=' . $dbname . ' user=' . $user . ' password=' . $password);
if (!$db) {
    die("Failed to connect to the database: " . pg_last_error());
}