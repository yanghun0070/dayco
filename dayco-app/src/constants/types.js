export const user = {
    LOGIN_SUCCESS: 'dayco-app/login/success',
    JOIN_SUCCESS: 'dayco-app/join/success',
    LOGOUT_SUCCESS: 'dayco-app/logout/success',
    CURRENT_USER_SUCCESS: 'dayco-app/current-user/success',
    LOGIN_FAIL:  'dayco-app/login/fail',
    JOIN_FAIL:  'dayco-app/join/fail',
    LOGOUT_FAIL:  'dayco-app/logout/fail',
    CURRENT_USER_FAIL: 'dayco-app/current-user/fail',
};

export const posts = {
    LIST_SUCCESS: 'dayco-app/posts/list/success',
    GET_SUCCESS: 'dayco-app/posts/get/success',
    CREATE_SUCCESS: 'dayco-app/posts/create/success',
    EDIT_SUCCESS: 'dayco-app/posts/edit/success',
    DELETE_SUCCESS: 'dayco-app/posts/delete/success',
    LIST_FAIL: 'dayco-app/posts/list/fail',
    GET_FAIL: 'dayco-app/posts/get/fail',
    CREATE_FAIL: 'dayco-app/posts/create/fail',
    EDIT_FAIL: 'dayco-app/posts/edit/fail',
    DELETE_FAIL: 'dayco-app/posts/delete/fail'
};

export const postsEditModal = {
    MODAL_CREATE: 'dayco-app/posts-modal/create',
    MODAL_EDIT: 'dayco-app/posts-modal/edit',
    MODAL_DELETE: 'dayco-app/posts-modal/delete',
    MODAL_HIDE: 'dayco-app/posts-modal/hide'
}

export const postsDetailModal = {
    MODAL_SHOW: 'dayco-app/postsdetail-modal/show',
    MODAL_HIDE: 'dayco-app/postsdetail-modal/hide'
}

export const alert = {
    ALERT_CREATE : 'dayco-app/alert/create',
    ALERT_REMOVE : 'dayco-app/alert/remove' 
}

export const postsLikeCount = {
    ADD_SUCESS: 'dayco-app/posts/like/add/success',
    GET_SUCCESS: 'dayco-app/posts/like/get/success',
    INCREASE_SUCCESS : 'dayco-app/posts/like/increase/success',
    DECREASE_SUCCESS : 'dayco-app/posts/like/decrease/success',
    ADD_FAIL: 'dayco-app/posts/like/increase/add/fail',
    GET_FAIL: 'dayco-app/posts/like/increase/get/fail',
    INCREASE_FAIL : 'dayco-app/posts/like/increase/fail',
    DECREASE_FAIL : 'dayco-app/posts/like/decrease/fail'
}

export const postsComment = {
    PAGING_LIST_SUCCESS: 'dayco-app/posts/comment/paging-list/success',
    PAGING_DETAIL_LIST_SUCCESS: 'dayco-app/posts/comment/paging-detail-list/success',
    CREATE_SUCCESS: 'dayco-app/posts/comment/create/success',
    EDIT_SUCCESS: 'dayco-app/posts/comment/edit/success',
    DELETE_SUCCESS: 'dayco-app/posts/comment/delete/success',
    PAGING_LIST_FAIL: 'dayco-app/posts/comment/paginglist/fail',
    PAGING_DETAIL_LIST_FAIL: 'dayco-app/posts/comment/paging-detail-list/fail',
    CREATE_FAIL: 'dayco-app/posts/comment/create/fail',
    EDIT_FAIL: 'dayco-app/posts/comment/edit/fail',
    DELETE_FAIL: 'dayco-app/posts/comment/delete/fail',
}

export const actionStatus = {
    SOCKET_POST_CREATE : 'dayco-app/socket/posts/create',
    SOCKET_POST_EDIT : 'dayco-app/socket/posts/edit',
    SOCKET_POST_DELETE : 'dayco-app/socket/posts/delete',
    SOCKET_POST_LIKE_GET :'dayco-app/socket/posts/like/get',
    SOCKET_POST_LIKE_INCREASE :'dayco-app/socket/posts/like/increase',
    SOCKET_POST_LIKE_DECREASE :'dayco-app/socket/posts/like/decrease'
}