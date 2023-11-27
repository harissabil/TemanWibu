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

        $query = "SELECT * FROM \"review\" JOIN \"anime\" USING (anime_id) WHERE username = '$username' ORDER BY anime_id DESC";
        if (pg_send_query($db, $query)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    $response['status'] = 'OK';
                    $response['message'] = 'Review successfully retrieved';
                    $response['data'] = array();
                    while ($row = pg_fetch_assoc($res)) {
                        $row['review_id'] = (int)$row['review_id'];
                        $response['data'][] = $row;
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