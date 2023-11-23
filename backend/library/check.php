<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$response = [
    'status' => '',
    'message' => '',
    'available' => false,
    'anime_status' => '',
    'review_data' => array(),
];

if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only GET requests are allowed';
} else {
    if (!isset($_GET['username']) && !isset($_GET['anime_id'])) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the username and anime_id';
    } else if ($_GET['username'] == null && $_GET['anime_id'] == null) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the username and anime_id';
    } else {
        $username = $_GET['username'];
        $anime_id = $_GET['anime_id'];

        $checkLibraryTableIfExist = "SELECT * FROM \"library\" WHERE username = '$username' AND anime_id = '$anime_id'";
        if (pg_send_query($db, $checkLibraryTableIfExist)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    if (pg_num_rows($res) >= 1) {

                        $checkReviewTableIfExist = "SELECT * FROM \"review\" WHERE username = '$username' AND anime_id = '$anime_id'";
                        if (pg_send_query($db, $checkReviewTableIfExist)) {
                            $resReview = pg_get_result($db);
                            if ($resReview) {
                                $state = pg_result_error_field($resReview, PGSQL_DIAG_SQLSTATE);
                                if ($state == 0) {
                                    if (pg_num_rows($resReview) >= 1) {
                                        $data = array();
                                        $i = 0;
                                        while ($row = pg_fetch_assoc($resReview)) {
                                            $data[] = $row;
                                            $i++;
                                        }
                                        $response['status'] = 'OK';
                                        $response['message'] = 'Anime already exist in library';
                                        $response['available'] = true;
                                        $response['anime_status'] = pg_fetch_assoc($res)['current_status'];
                                        $response['review_data'] = $data;
                                    } else {
                                        $response['status'] = 'OK';
                                        $response['message'] = 'Anime already exist in library';
                                        $response['available'] = true;
                                        $response['anime_status'] = pg_fetch_assoc($res)['current_status'];
                                        $response['review_data'] = array();
                                    }
                                } else {
                                    http_response_code(500); // Internal Server Error
                                    $response['status'] = 'INTERNAL SERVER ERROR';
                                    $response['message'] = pg_result_error($res);
                                    $response['data'] = array();
                                }
                            }
                        } else {
                            $response['status'] = 'OK';
                            $response['message'] = 'Anime already exist in library';
                            $response['available'] = true;
                            $response['anime_status'] = pg_fetch_assoc($res)['current_status'];
                            $response['review_data'] = array();
                        }
                    } else {
                        $response['status'] = 'OK';
                        $response['message'] = 'Anime not exist in library';
                        $response['available'] = false;
                        $response['anime_status'] = '';
                        $response['review_data'] = array();
                    }
                } else {
                    http_response_code(500); // Internal Server Error
                    $response['status'] = 'INTERNAL SERVER ERROR';
                    $response['message'] = pg_result_error($res);
                    $response['available'] = false;
                    $response['anime_status'] = '';
                    $response['review_data'] = array();
                }
            }
        }
    }
}

echo json_encode($response, JSON_PRETTY_PRINT);