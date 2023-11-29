<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$response = [
    'status' => '',
    'message' => '',
];

if ($_SERVER['REQUEST_METHOD'] !== 'PUT') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only PUT requests are allowed';
} else {
    $inputData = file_get_contents("php://input");
    parse_str($inputData, $formData);

    if (!isset($formData['username']) && !isset($formData['anime_id']) && !isset($formData['current_status']) && !isset($formData['title']) && !isset($formData['poster_image']) && !isset($formData['rating']) && !isset($formData['episode'])) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the username, anime_id, current_status, title, poster_image, rating, episode';
    } else if ($formData['username'] == null && $formData['anime_id'] == null && $formData['current_status'] == null && $formData['title'] == null && $formData['poster_image'] == null && $formData['rating'] == null && $formData['episode'] == null) {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please provide the username, anime_id, current_status, title, poster_image, rating, episode';
    } else {
        $username = $formData['username'];
        $anime_id = $formData['anime_id'];
        $current_status = $formData['current_status'];
        $title = $formData['title'];
        $poster_image = $formData['poster_image'];
        $rating = $formData['rating'];
        $episode = $formData['episode'];

        if (isset($formData['anime_review'])) {
            $anime_review = $formData['anime_review'];
        } else {
            $anime_review = null;
        }

        if (isset($formData['anime_score'])) {
            $anime_score = $formData['anime_score'];
        } else {
            $anime_score = null;
        }

        $updateAnimeTable = "UPDATE \"anime\" SET title = '$title', poster_image = '$poster_image', rating = '$rating', episode = '$episode' WHERE anime_id = '$anime_id'";
        if (pg_send_query($db, $updateAnimeTable)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    // success
                } else {
                    http_response_code(500); // Internal Server Error
                    $response['status'] = 'INTERNAL SERVER ERROR';
                    $response['message'] = pg_result_error($res);
                }
            }
        }

        if ($anime_review !== null && $anime_score !== null) {
            $checkReviewTableIfExist = "SELECT * FROM \"review\" WHERE anime_id = '$anime_id' AND username = '$username'";
            if (pg_send_query($db, $checkReviewTableIfExist)) {
                $res = pg_get_result($db);
                if ($res) {
                    $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                    if ($state == 0) {
                        if (pg_num_rows($res) >= 1) {

                            // update review
                            $updateReviewTable = "UPDATE \"review\" SET anime_review = '$anime_review', anime_score = '$anime_score', review_date = now() WHERE anime_id = '$anime_id' AND username = '$username'";
                            if (pg_send_query($db, $updateReviewTable)) {
                                $res = pg_get_result($db);
                                if ($res) {
                                    $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                    if ($state == 0) {
                                        // success
                                    } else {
                                        http_response_code(500); // Internal Server Error
                                        $response['status'] = 'INTERNAL SERVER ERROR';
                                        $response['message'] = pg_result_error($res);
                                    }
                                }
                            }

                        } else {

                            // insert review
                            $insertIntoReviewTable = "INSERT INTO \"review\"(anime_id, username, anime_review, anime_score, review_date) VALUES ('$anime_id', '$username', '$anime_review', '$anime_score', now())";
                            if (pg_send_query($db, $insertIntoReviewTable)) {
                                $res = pg_get_result($db);
                                if ($res) {
                                    $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                    if ($state == 0) {
                                        // success
                                    } else {
                                        http_response_code(500); // Internal Server Error
                                        $response['status'] = 'INTERNAL SERVER ERROR';
                                        $response['message'] = pg_result_error($res);
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
        }
        $updateLibraryTable = "UPDATE \"library\" SET current_status = '$current_status' WHERE anime_id = '$anime_id' AND username = '$username'";
        if (pg_send_query($db, $updateLibraryTable)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    $response['status'] = 'OK';
                    $response['message'] = 'Library updated';
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