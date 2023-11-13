<?php
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['username']) && !empty($_POST['anime_id'])) {
    $username = $_POST['username'];
    $anime_id = $_POST['anime_id'];

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
                                    $response['status'] = 'success';
                                    $response['message'] = 'Anime already exist in library';
                                    $response['available'] = true;
                                    $response['anime_status'] = pg_fetch_assoc($res)['current_status'];
                                    $response['review_data'] = $data;
                                } else {
                                    $response['status'] = 'success';
                                    $response['message'] = 'Anime already exist in library';
                                    $response['available'] = true;
                                    $response['anime_status'] = pg_fetch_assoc($res)['current_status'];
                                    $response['review_data'] = array();
                                }
                            } else {
                                $response['status'] = 'error';
                                $response['message'] = pg_result_error($res);
                                $response['data'] = array();
                            }
                        }
                    } else {
                        $response['status'] = 'success';
                        $response['message'] = 'Anime already exist in library';
                        $response['available'] = true;
                        $response['anime_status'] = pg_fetch_assoc($res)['current_status'];
                        $response['review_data'] = array();
                    }


                } else {

                    $response['status'] = 'success';
                    $response['message'] = 'Anime not exist in library';
                    $response['available'] = false;
                    $response['anime_status'] = '';
                    $response['review_data'] = array();

                }

            } else {
                $response['status'] = 'error';
                $response['message'] = pg_result_error($res);
                $response['available'] = false;
                $response['anime_status'] = '';
                $response['review_data'] = array();
            }
        }
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Please fill all fields';
    $response['available'] = false;
    $response['anime_status'] = '';
    $response['review_data'] = array();
}

echo json_encode($response, JSON_PRETTY_PRINT);