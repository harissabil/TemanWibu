<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

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
    $query = "SELECT * FROM \"review\" JOIN \"anime\" USING (anime_id) ORDER BY review_date DESC";

    if (pg_send_query($db, $query)) {
        $res = pg_get_result($db);
        if ($res) {
            $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
            if ($state == 0) {
                $response['status'] = 'OK';
                $response['message'] = 'Read success';
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

echo json_encode($response, JSON_PRETTY_PRINT);