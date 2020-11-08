import { POSTS_CREATE_MODAL, POSTS_EDIT_MODAL, POSTS_DELETE_MODAL, POSTS_HIDE_MODAL } from '../constants';
import initialState from '../constants/initialState';
import * as types from '../constants/types';

/**
 * The posts reducer is responsible
 * @param {*} state 
 * @param {*} action 
 */
export function posts(state = initialState.posts, action) {
    switch (action.type) {
        case types.posts.LIST_SUCCESS: 
        {
            const { postsList } = action;
            return Object.assign({}, state, {list: [...postsList]});
        }
        case types.posts.CREATE_SUCCESS:
        {
            const { createdPosts } = action;
            /**
             * week reference problem
             * state.list.unshift(posts); (x)
             * 
             * @see https://redux.js.org/troubleshooting
             * @see https://github.com/reduxjs/redux/issues/585
             */
            const postsList = [
                createdPosts, ...state.list
              ]
            return Object.assign({}, state, 
                {
                    list: [
                        ...postsList
                    ]
                });
        }
        case types.posts.EDIT_SUCCESS:
        {
            const { editedPosts } = action; 
            let changedPosts;
            const editedPostsList = state.list.map((posts) => {
                if(editedPosts.id === posts.id) {
                    changedPosts = Object.assign(posts, editedPosts);
                    return changedPosts;
                }
                return posts;
            });
            return Object.assign({}, state, 
                {
                    detail: {
                        ...changedPosts
                    },
                    list: [
                        ...editedPostsList
                    ]
                });
        }
        case types.posts.DELETE_SUCCESS:
        {
            const { deletePostsId } = action;
            const deletePostsList = state.list.filter(function(posts){
                return (posts.id !== deletePostsId);
            }).map((posts) => {
                return posts;
            });
            return Object.assign({}, state, { list: [ ...deletePostsList ] });
        } 
        case types.posts.GET_SUCCESS:
        {
            const { posts } = action; 
            return Object.assign({}, state, { 
                detail: { 
                    ...posts,
                    pageOfComments: {
                        number: 0,
                        size: 5
                    }
                }
            });
        }
        case types.posts.LIKE_INCREASE_SUCCESS:
        {
            const { id, likeCount } = action;
            let changedPosts;
            const postsList = state.list.map((posts) => {
                if(id === posts.id) {
                    changedPosts = Object.assign(posts, {
                        postsLike: { 
                            id: id,
                            likeCount: likeCount 
                        }
                    });
                    return changedPosts;
                }
                return posts;
            });
            return Object.assign({}, state, {
                detail: {
                    ...changedPosts
                },
                list: [
                    ...postsList
                ]
            });
        }
        case types.posts.PAGING_COMMENT_SUCCESS:
        {
            const { postsId, pageOfComments } = action;
            const postsComments = pageOfComments.content
            let changedPosts;
            const postsList = state.list.map((posts) => {
                if(postsId === posts.id) {
                    const changedPostsComments = [
                        ...postsComments
                    ]
                    changedPosts = Object.assign(posts, { 
                        postsComments: changedPostsComments
                    }); 
                    return changedPosts;
                }
                return posts;
            });
            return Object.assign({}, state, {
                detail: { 
                    ...changedPosts, 
                    pageOfComments: pageOfComments 
                },
                list: [...postsList]
            });
        }
        //Detail Modal 창에서만 댓글 페이징 처리를 한다.
        case types.posts.PAGING_COMMENT_FROM_DETAIL_SUCCESS: 
        {
            const { postsId, pageOfComments } = action;
            const postsComments = pageOfComments.content
            const detail = Object.assign(state.detail, {
                postsComments: [...postsComments], 
                pageOfComments: pageOfComments 
            });
            return Object.assign({}, state, {
                detail: {...detail}
            }); 
        }
        case types.posts.EDIT_COMMENT_SUCCESS:
        {
            const { editedPostsComment } = action;
            let changedPosts;
            const postsList = state.list.map((posts) => {
                //Comment 수정되었니?
                const editComment = posts.postsComments.filter((comment) => {
                    return (comment.commentId === editedPostsComment.commentId)
                })
                //Comment 수정되었다면
                if(editComment.length > 0) {
                    const changedPostsComments = posts.postsComments.map((comment) => {
                        if(comment.commentId == editedPostsComment.commentId) {
                            return Object.assign({}, editedPostsComment);
                        }
                        return comment;
                    });
                    changedPosts = Object.assign(posts, { postsComments: changedPostsComments });
                    return changedPosts;
                } else {
                    return posts;
                }
            });
            return Object.assign({}, state, {
                detail: {...changedPosts},
                list: [...postsList]
            });
        }
        default:
            return state;
    }
}

/**
 * the posts edit modal reducer is responsible
 * @param {*} state 
 * @param {*} action 
 */
export function postsEditModal(state = initialState.postsEditModal, action) {
    switch (action.type) {
      case types.postsEditModal.MODAL_CREATE:
        return Object.assign({}, state, {
            isShow: true, 
            status: POSTS_CREATE_MODAL, 
            title: '',
            content: ''
        });
      case types.postsEditModal.MODAL_EDIT:
        return Object.assign({}, state, {
            isShow: true, 
            status: POSTS_EDIT_MODAL,
            id: action.id,
            title: action.title,
            content: action.content,
            author: action.author
        });
      case types.postsEditModal.MODAL_DELETE:
        return Object.assign({}, state, {
            isShow: true, 
            status: POSTS_DELETE_MODAL,
            id: action.id,
            author: action.author
        }); 
      case types.postsEditModal.MODAL_HIDE:
        return Object.assign({}, state, { 
            isShow: false, 
            status: POSTS_HIDE_MODAL
        });
        default:
            return state;
    }
}

/**
 * the posts detail modal reducer is responsible
 * @param {*} state 
 * @param {*} action 
 */
export function postsDetailModal(state = initialState.postsDetailModal, action) {
    switch(action.type) {
        case types.postsDetailModal.MODAL_SHOW:
            return Object.assign({}, state, {isShow: true});
        case types.postsDetailModal.MODAL_HIDE:
            return Object.assign({}, state, { isShow: false});
        default:
            return state;
    }
} 