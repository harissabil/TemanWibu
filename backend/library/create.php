<?php
header("Content-Type: application/json");
global $db;
require("../config.php");

$response = [
    'status' => '',
    'message' => '',
];

if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405); // Method Not Allowed
    $response['status'] = 'METHOD NOT ALLOWED';
    $response['message'] = 'Only POST requests are allowed';
} else {
    if (!empty($_POST['username']) && !empty($_POST['anime_id']) && !empty($_POST['current_status']) && !empty($_POST['title']) && !empty($_POST['poster_image']) && isset($_POST['rating']) && isset($_POST['episode'])) {
        $username = $_POST['username'];
        $anime_id = $_POST['anime_id'];
        $current_status = $_POST['current_status'];
        $title = $_POST['title'];
        $poster_image = $_POST['poster_image'];
        $rating = $_POST['rating'];
        $episode = $_POST['episode'];

        $checkAnimeTableIfExist = "SELECT * FROM \"anime\" WHERE anime_id = '$anime_id'";
        if (pg_send_query($db, $checkAnimeTableIfExist)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    if (pg_num_rows($res) >= 1) {
                        // do nothing because it is already exist
                    } else {
                        $insertIntoAnimeTable = "INSERT INTO \"anime\"(anime_id, title, poster_image, rating, episode) VALUES ('$anime_id', '$title', '$poster_image', '$rating', '$episode')";
                        if (pg_send_query($db, $insertIntoAnimeTable)) {
                            $res = pg_get_result($db);
                            if ($res) {
                                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                if ($state == 0) {
                                    // do nothing because it is already exist
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

        if (!empty($_POST['anime_review']) && !empty($_POST['anime_score'])) {
            $anime_review = $_POST['anime_review'];
            $anime_score = $_POST['anime_score'];

            $checkReviewTableIfExist = "SELECT * FROM \"review\" WHERE anime_id = '$anime_id' AND username = '$username'";
            if (pg_send_query($db, $checkReviewTableIfExist)) {
                $resCheckReview = pg_get_result($db);
                if ($resCheckReview) {
                    $state = pg_result_error_field($resCheckReview, PGSQL_DIAG_SQLSTATE);
                    if ($state == 0) {
                        if (pg_num_rows($resCheckReview) >= 1) {

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
                        $response['message'] = pg_result_error($resCheckReview);
                    }
                }
            }
        }

        $insertIntoLibraryTable = "INSERT INTO \"library\"(username, anime_id, current_status) VALUES ('$username', '$anime_id', '$current_status')";
        if (pg_send_query($db, $insertIntoLibraryTable)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    $response['status'] = 'OK';
                    $response['message'] = 'Added to library';
                } else {
                    http_response_code(500); // Internal Server Error
                    $response['status'] = 'INTERNAL SERVER ERROR';
                    $response['message'] = pg_result_error($res);
                }
            }
        }

    } else {
        http_response_code(400); // Bad Request
        $response['status'] = 'BAD REQUEST';
        $response['message'] = 'Please fill all fields';
    }
}

echo json_encode($response, JSON_PRETTY_PRINT);