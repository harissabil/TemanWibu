<?php
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['username']) && !empty($_POST['anime_id']) && !empty($_POST['current_status']) && !empty($_POST['title']) && !empty($_POST['poster_image']) && !empty($_POST['rating']) && !empty($_POST['episode'])) {
    $username = $_POST['username'];
    $anime_id = $_POST['anime_id'];
    $current_status = $_POST['current_status'];
    $title = $_POST['title'];
    $poster_image = $_POST['poster_image'];
    $rating = $_POST['rating'];
    $episode = $_POST['episode'];

    $updateAnimeTable = "UPDATE \"anime\" SET title = '$title', poster_image = '$poster_image', rating = '$rating', episode = '$episode' WHERE anime_id = '$anime_id'";
    if (pg_send_query($db, $updateAnimeTable)) {
        $res = pg_get_result($db);
        if ($res) {
            $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
            if ($state == 0) {
                // success
            } else {
                $response['status'] = 'error';
                $response['message'] = pg_result_error($res);
            }
        }
    }

    if (!empty($_POST['anime_review']) && !empty($_POST['anime_score'])) {
        $anime_review = $_POST['anime_review'];
        $anime_score = $_POST['anime_score'];

        $checkReviewTableIfExist = "SELECT * FROM \"review\" WHERE anime_id = '$anime_id' AND username = '$username'";
        if (pg_send_query($db, $checkReviewTableIfExist)) {
            $res = pg_get_result($db);
            if ($res) {
                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                if ($state == 0) {
                    if (pg_num_rows($res) >= 1) {

                        // update review
                        $updateReviewTable = "UPDATE \"review\" SET anime_review = '$anime_review', anime_score = '$anime_score' WHERE anime_id = '$anime_id' AND username = '$username'";
                        if (pg_send_query($db, $updateReviewTable)) {
                            $res = pg_get_result($db);
                            if ($res) {
                                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                if ($state == 0) {
                                    // success
                                } else {
                                    $response['status'] = 'error';
                                    $response['message'] = pg_result_error($res);
                                }
                            }
                        }

                    } else {

                        // insert review
                        $insertIntoReviewTable = "INSERT INTO \"review\"(anime_id, username, anime_review, anime_score) VALUES ('$anime_id', '$username', '$anime_review', '$anime_score')";
                        if (pg_send_query($db, $insertIntoReviewTable)) {
                            $res = pg_get_result($db);
                            if ($res) {
                                $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
                                if ($state == 0) {
                                    // success
                                } else {
                                    $response['status'] = 'error';
                                    $response['message'] = pg_result_error($res);
                                }
                            }
                        }
                    }

                } else {
                    $response['status'] = 'error';
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
                $response['status'] = 'success';
                $response['message'] = 'Updated library';
            } else {
                $response['status'] = 'error';
                $response['message'] = pg_result_error($res);
            }
        }
    }

} else {
    $response['status'] = 'error';
    $response['message'] = 'Please fill all fields';
}

echo json_encode($response, JSON_PRETTY_PRINT);
