<?php
header("Content-Type: application/json");
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['username']) && !empty($_POST['password'])) {
    $username = $_POST['username'];
    $password = $_POST['password'];

    $query = "SELECT * FROM \"user\" WHERE username = '$username' AND password = '$password'";

    if (pg_send_query($db, $query)) {
        $res = pg_get_result($db);
        if ($res) {
            $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
            if ($state == 0) {
                if (pg_num_rows($res) >= 1) {
                    $data = array();
                    while ($row = pg_fetch_assoc($res)) {
                        $data[] = $row;
                    }
                    $response['status'] = 'success';
                    $response['message'] = 'Login success';
                    $response['data'] = $data;
                } else {
                    $response['status'] = 'error';
                    $response['message'] = 'Username or password is wrong';
                    $response['data'] = array();
                }
            } else {
                $response['status'] = 'error';
                $response['message'] = pg_result_error($res);
                $response['data'] = array();
            }
        }
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Please fill all fields';
    $response['data'] = array();
}

echo json_encode($response, JSON_PRETTY_PRINT);

