<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$response = [
    'status' => '',
    'message' => '',
];

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only POST requests are allowed';
} else if (!empty($_POST['username']) && !empty($_POST['password']) && !empty($_POST['name']) && !empty($_POST['email'])) {
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
                $response['status'] = 'OK';
                $response['message'] = 'Registration success';
            } else {
                if ($state == "23505") { // unique_violation
                    http_response_code(409); // Conflict
                    $response['status'] = 'CONFLICT';
                    $response['message'] = 'User already exists';
                } else {
                    http_response_code(500); // Internal Server Error
                    $response['status'] = 'INTERNAL SERVER ERROR';
                    $response['message'] = pg_result_error($res);
                }
            }
        }
    }
} else {
    http_response_code(400);
    $response['status'] = 'BAD REQUEST';
    $response['message'] = 'Please fill all fields';
}

echo json_encode($response, JSON_PRETTY_PRINT);