<?php
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['review_id'])) {
    $review_id = $_POST['review_id'];

    $deleteFromReviewTable = "DELETE FROM \"review\" WHERE review_id = '$review_id'";

    if (pg_send_query($db, $deleteFromReviewTable)) {
        $resDeleteReview = pg_get_result($db);
        if ($resDeleteReview) {
            $state = pg_result_error_field($resDeleteReview, PGSQL_DIAG_SQLSTATE);
            if ($state == 0) {
                // success delete review
                $response['status'] = 'success';
                $response['message'] = 'Delete success';
            } else {
                $response['status'] = 'error';
                $response['message'] = pg_result_error($resDeleteReview);
            }
        }
    }

} else {
    $response['status'] = 'error';
    $response['message'] = 'Please fill all fields';
}

echo json_encode($response, JSON_PRETTY_PRINT);