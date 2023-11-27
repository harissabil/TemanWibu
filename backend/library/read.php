<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$requestUri = $_SERVER['REQUEST_URI'];
$segments = explode('/', rtrim($requestUri, '/'));
$deleteIndex = array_search('read.php', $segments);
$response = [
    'status' => '',
    'message' => '',
    'data' => [],
];

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only GET requests are allowed';
} else {
    if (!isset($segments[$deleteIndex + 1])) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the username';
        $response['data'] = array();
    } else {
        $username = $segments[$deleteIndex + 1];

        $query = "SELECT * FROM \"library\" JOIN public.anime a on library.anime_id = a.anime_id WHERE username = '$username' ORDER BY library.library_id DESC";
        if (pg_send_query($db, $query)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    if (pg_num_rows($res) >= 1) {
                        $data = array();
                        $i = 0;
                        while ($row = pg_fetch_assoc($res)) {
                            $row['library_id'] = (int)$row['library_id'];
                            $data[$i] = $row;
                            $i++;
                        }
                        $response['status'] = 'OK';
                        $response['message'] = 'Library is not empty';
                        $response['data'] = $data;
                    } else {
                        $response['status'] = 'OK';
                        $response['message'] = 'Library is empty';
                        $response['data'] = array();
                    }
                } else {
                    http_response_code(500); // Internal Server Error
                    $response['status'] = 'INTERNAL SERVER ERROR';
                    $response['message'] = pg_result_error($res);
                    $response['data'] = array();
                }
            }
        }
    }
}

echo json_encode($response, JSON_PRETTY_PRINT);