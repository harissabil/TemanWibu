<?php
$response = array();
global $db;
require("../config.php");

if (!empty($_POST['username'])) {
    $username = $_POST['username'];

    $query = "SELECT * FROM \"library\" JOIN public.anime a on library.anime_id = a.anime_id WHERE username = '$username' ORDER BY library.library_id DESC";
    if (pg_send_query($db, $query)) {
        $res = pg_get_result($db);
        if ($res) {
            $state = pg_result_error_field($res, PGSQL_DIAG_SQLSTATE);
            if ($state == 0) {
                if (pg_num_rows($res) >= 1) {
                    $data = array();
                    $i = 0;
                    while ($row = pg_fetch_assoc($res)) {
                        $row['library_id'] = (int)$row['library_id'];
                        $data[$i] = $row;
                        $i++;
                    }
                    $response['status'] = 'success';
                    $response['message'] = 'Get library success';
                    $response['data'] = $data;
                } else {
                    $response['status'] = 'success';
                    $response['message'] = 'Library is empty';
                    $response['data'] = array();
                }
            } else {
                $response['status'] = 'error';
                $response['message'] = pg_result_error($res);
                $response['data'] = array();
            }
        }
    }
} else {
    $response['status'] = 'error';
    $response['message'] = 'Please fill all fields';
    $response['data'] = array();
}

echo json_encode($response, JSON_PRETTY_PRINT);