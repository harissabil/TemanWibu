<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$response = [
    'status' => '',
    'message' => '',
    'data' => [],
];

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only POST requests are allowed';
} else {
    $post_data = filter_input_array(INPUT_POST, FILTER_SANITIZE_STRING);

    if (empty($post_data['username']) || empty($post_data['password'])) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please fill all fields';
    } else {
        $username = $post_data['username'];
        $password = $post_data['password'];

        $query = "SELECT * FROM \"user\" WHERE username = '$username'";

        if ($result = pg_query($db, $query)) {
            if (pg_num_rows($result) >= 1) {
                $row = pg_fetch_assoc($result);
                $hashedPassword = $row['password'];

                if (password_verify($password, $hashedPassword)) {
                    $response['status'] = 'OK';
                    $response['message'] = 'Login success';
                    $row['password'] = $password;
                    $response['data'][] = $row;
                } else {
                    http_response_code(401); // Unauthorized
                    $response['status'] = 'UNAUTHORIZED';
                    $response['message'] = 'Password is incorrect';
                }
            } else {
                http_response_code(404); // Not Found
                $response['status'] = 'NOT FOUND';
                $response['message'] = 'User not found';
            }

            pg_free_result($result);
        } else {
            http_response_code(500); // Internal Server Error
            $response['status'] = 'INTERNAL SERVER ERROR';
            $response['message'] = pg_last_error($db);
        }
    }
}

echo json_encode($response, JSON_PRETTY_PRINT);