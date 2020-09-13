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
            const { postsList } = action;
            return Object.assign({}, state, {list: [...postsList]});
        case types.posts.CREATE_SUCCESS:
            const { posts } = action; 
            /**
             * week reference problem
             * state.list.unshift(posts); (x)
             * 
             * @see https://redux.js.org/troubleshooting
             * @see https://github.com/reduxjs/redux/issues/585
             */
            const addPostsList = [
                posts, ...state.list
              ]
            return Object.assign({}, state, {list: [...addPostsList]});
        case types.posts.EDIT_SUCCESS:
            const { editPosts } = action; 
            const editPostsList = state.list.map((posts) => {
                if(editPosts.id === posts.id) {
                    return Object.assign({}, editPosts);
                }
                return posts;
            });
            return Object.assign({}, state, {list: [...editPostsList]});
        case types.posts.DELETE_SUCCESS:
            const {deletePostsId} = action;
            const deletePostsList = state.list.filter(function(posts){
                return (posts.id !== deletePostsId);
            }).map((posts) => {
                return posts;
            });
            return Object.assign({}, state, {list: [...deletePostsList]});
        case types.posts.EDIT_FAIL:
            return state;
        case types.posts.LIST_FAIL:
            return state;
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
            status: 'create', 
            title: '',
            content: ''
        });
      case types.postsEditModal.MODAL_EDIT:
        return Object.assign({}, state, {
            isShow: true, 
            status: 'edit',
            id: action.id,
            title: action.title,
            content: action.content,
            author: action.author
        });
      case types.postsEditModal.MODAL_DELETE:
        return Object.assign({}, state, {
            isShow: true, 
            status: 'delete',
            id: action.id,
            author: action.author
        }); 
      case types.postsEditModal.MODAL_HIDE:
        return Object.assign({}, state, { isShow: false, status: 'hide'});
        default:
            return state;
    }
}