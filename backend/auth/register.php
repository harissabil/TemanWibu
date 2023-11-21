<?php
header("Content-Type: application/json");
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['name']) && !empty($_POST['email'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];
    $name = $_POST['name'];
    $email = $_POST['email'];

    $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

    $query = "INSERT INTO \"user\"(username, password, name, email) VALUES ('$username', '$hashedPassword', '$name', '$email')";

    if (pg_send_query($db, $query)) {
        $res = pg_get_result($db);
        if ($res) {
            $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
            if ($state == 0) {
                $response['status'] = 'success';
                $response['message'] = 'Registration success';
            } else {
                $response['status'] = 'error';
                if ($state == "23505") { // unique_violation
                    $response['message'] = 'User already exists';
                } else {
                    $response['message'] = pg_result_error($res);
                }
            }
        }
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Please fill all fields';
}

echo json_encode($response, JSON_PRETTY_PRINT);