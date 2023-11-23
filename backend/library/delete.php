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

        $checkIfLibraryTableExist = "SELECT * FROM \"library\" WHERE username = '$username' AND anime_id = '$anime_id'";
        $deleteFromLibraryTable = "DELETE FROM \"library\" WHERE username = '$username' AND anime_id = '$anime_id'";
        $deleteFromReviewTable = "DELETE FROM \"review\" WHERE username = '$username' AND anime_id = '$anime_id'";
        $checkIfReviewTableExist = "SELECT * FROM \"review\" WHERE username = '$username' AND anime_id = '$anime_id'";

        if (pg_send_query($db, $checkIfLibraryTableExist)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    if (pg_num_rows($res) >= 1) {
                        if (pg_send_query($db, $deleteFromLibraryTable)) {
                            $res = pg_get_result($db);
                            if ($res) {
                                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                if ($state == 0) {

                                    if (pg_send_query($db, $checkIfReviewTableExist)) {
                                        $resCheckReview = pg_get_result($db);
                                        if ($resCheckReview) {
                                            $state = pg_result_error_field($resCheckReview, PGSQL_DIAG_SQLSTATE);
                                            if ($state == 0) {
                                                if (pg_num_rows($resCheckReview) >= 1) {

                                                    if (pg_send_query($db, $deleteFromReviewTable)) {
                                                        $resDeleteReview = pg_get_result($db);
                                                        if ($resDeleteReview) {
                                                            $state = pg_result_error_field($resDeleteReview, PGSQL_DIAG_SQLSTATE);
                                                            if ($state == 0) {
                                                                // success delete review
                                                            } else {
                                                                http_response_code(500); // Internal Server Error
                                                                $response['status'] = 'INTERNAL SERVER ERROR';
                                                                $response['message'] = pg_result_error($resDeleteReview);
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                http_response_code(500); // Internal Server Error
                                                $response['status'] = 'INTERNAL SERVER ERROR';
                                                $response['message'] = pg_result_error($res);
                                            }
                                        }
                                    }

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
                        http_response_code(404); // Not Found (Anime not exist in library)
                        $response['status'] = 'NOT FOUND';
                        $response['message'] = 'Anime not exist in library';
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