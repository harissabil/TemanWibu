<?php
$response = array();
global $db;
require("../config.php");

$query = "SELECT * FROM \"review\" JOIN \"anime\" USING (anime_id) ORDER BY review_id DESC";
if (pg_send_query($db, $query)) {
    $res = pg_get_result($db);
    if ($res) {
        $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
        if ($state == 0) {
            $response['status'] = 'success';
            $response['message'] = 'Read success';
            $response['data'] = array();
            while ($row = pg_fetch_assoc($res)) {
                $row['review_id'] = (int)$row['review_id'];
                $response['data'][] = $row;
            }
        } else {
            $response['status'] = 'error';
            $response['message'] = pg_result_error($res);
            $response['data'] = array();
        }
    }
}

echo json_encode($response, JSON_PRETTY_PRINT);