<?php
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['username']) && !empty($_POST['anime_id'])) {
    $username = $_POST['username'];
    $anime_id = $_POST['anime_id'];

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
                                                            $response['status'] = 'error';
                                                            $response['message'] = pg_result_error($resDeleteReview);
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

                                $response['status'] = 'success';
                                $response['message'] = 'Delete success';

                            } else {
                                $response['status'] = 'error';
                                $response['message'] = pg_result_error($res);
                            }
                        }
                    }
                } else {
                    $response['status'] = 'error';
                    $response['message'] = 'Anime not exist in library';
                }
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