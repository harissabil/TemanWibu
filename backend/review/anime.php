<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$requestUri = $_SERVER['REQUEST_URI'];
$segments = explode('/', rtrim($requestUri, '/'));
$deleteIndex = array_search('anime.php', $segments);
$response = [
    'status' => '',
    'message' => '',
    'data' => []
];

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only GET requests are allowed';
} else {
    if (!isset($segments[$deleteIndex + 1])) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the anime_id';
        $response['data'] = array();
    } else {
        $anime_id = (int)$segments[$deleteIndex + 1];

        $query = "SELECT * FROM \"review\" JOIN \"anime\" USING (anime_id) WHERE anime_id = '$anime_id' ORDER BY review_date DESC";
        if (pg_send_query($db, $query)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    $response['status'] = 'OK';
                    if (pg_num_rows($res) >= 1) {
                        $response['message'] = 'Review successfully retrieved';
                        $response['data'] = array();
                        while ($row = pg_fetch_assoc($res)) {
                            $row['review_id'] = (int)$row['review_id'];
                            $response['data'][] = $row;
                        }
                    } else {
                        $response['message'] = 'No reviews yet';
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
