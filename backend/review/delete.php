<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$requestUri = $_SERVER['REQUEST_URI'];
$segments = explode('/', rtrim($requestUri, '/'));
$deleteIndex = array_search('delete.php', $segments);
$response = [
    'status' => '',
    'message' => '',
];

if ($_SERVER['REQUEST_METHOD'] !== 'DELETE') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only DELETE requests are allowed';
} else {
    if (!isset($segments[$deleteIndex + 1])) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the review_id';
    } else {
        $review_id = (int)$segments[$deleteIndex + 1];

        $checkIfReviewTableExist = "SELECT * FROM \"review\" WHERE review_id = '$review_id'";
        $deleteFromReviewTable = "DELETE FROM \"review\" WHERE review_id = '$review_id'";

        if (pg_send_query($db, $checkIfReviewTableExist)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    if (pg_num_rows($res) >= 1) {
                        if (pg_send_query($db, $deleteFromReviewTable)) {
                            $res = pg_get_result($db);
                            if ($res) {
                                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                if ($state == 0) {
                                    // success delete review
                                    $response['status'] = 'OK';
                                    $response['message'] = 'Delete success';
                                } else {
                                    http_response_code(500); // Internal Server Error
                                    $response['status'] = 'INTERNAL SERVER ERROR';
                                    $response['message'] = pg_result_error($res);
                                }
                            }
                        }
                    } else {
                        http_response_code(404); // Not Found
                        $response['status'] = 'NOT FOUND';
                        $response['message'] = 'Review not found';
                    }
                } else {
                    http_response_code(500); // Internal Server Error
                    $response['status'] = 'INTERNAL SERVER ERROR';
                    $response['message'] = pg_result_error($res);
                }
            }
        }
    }
}

echo json_encode($response, JSON_PRETTY_PRINT);