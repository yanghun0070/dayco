import initialState from '../constants/initialState';
import * as types from '../constants/types';

/**
 * 댓글 처리
 * @param {*} state
 * @param {*} action
 */
export function postsComment(state = initialState.postsComment, action) {
    switch (action.type) {
        case types.postsComment.PAGING_LIST_SUCCESS:
            {
                const { pageOfComments } = action; 
                return Object.assign({}, state, { 
                    status: 'get', 
                    comments: [...pageOfComments]
                });
            }
        case types.postsComment.CREATE_SUCCESS:
            {
                const { postsComment } = action; 
                const postsComments = [
                    postsComment, ...state.comments
                ]
                // 상태를 복사한다.
                const detailState = Object.assign({}, state.detail)
                // 댓글 앞 항목에 추가한다.
                detailState.comments.unshift(postsComment);
                return Object.assign({}, state, { 
                    status: 'create',
                    comments: [...postsComments],
                    detail: detailState
                });
            }
        case types.postsComment.EDIT_SUCCESS:
            {
                const { postsComment } = action;
                //변경되어진 댓글
                const afterComments = state.comments.map((comment) => {
                    if(comment.commentId == postsComment.commentId) {
                        return Object.assign({}, postsComment);
                    }
                    return comment;
                });
                // 상세 페이지 상태를 복사한다.
                const datailState = Object.assign({}, state.detail);
                // 상세 페이지 댓글을 수정한다.
                datailState.comments = datailState.comments.map((comment) => {
                    if(comment.commentId ==  postsComment.commentId) {
                        return Object.assign({}, postsComment);
                    }
                    return comment;
                });
                return Object.assign({}, state, {
                    status: 'edit',
                    comments: [...afterComments],
                    detail: datailState
                });
            }
        case types.postsComment.DELETE_SUCCESS:
            {
                // 삭제되어진 댓글 ID
                const { postsCommentId } = action;
                //변경되어진 댓글
                const afterComments = state.comments.filter(function(comment){
                    return (comment.commentId !== postsCommentId);
                });
                // 상세 페이지 상태를 복사한다.
                const datailState = Object.assign({}, state.detail);
                datailState.comments = datailState.comments.filter(function(comment){
                    return (comment.commentId !== postsCommentId);
                });
                return Object.assign({}, state, {
                    status: 'delete',
                    comments: [...afterComments],
                    detail: datailState
                });
            }
        case types.postsComment.PAGING_DETAIL_LIST_SUCCESS:
            {
                const { pageOfComments } = action;
                return Object.assign({}, state, {
                    status: 'get',
                    detail: { 
                        page: pageOfComments.number, 
                        rowNum : pageOfComments.size,
                        comments: pageOfComments.content
                    }
                });
            }
        case types.postsComment.PAGING_LIST_FAIL:
            return state; 
        case types.postsComment.CREATE_FAIL:
            return state; 
        case types.postsComment.EDIT_FAIL:
            return state; 
        case types.postsComment.DELETE_FAIL:
            return state; 
        default:
            return state;
    }
}