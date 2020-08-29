import { combineReducers } from 'redux';

import { user } from './user';
import { posts, postsEditModal } from './posts';

/**
 * Root reducer for project
 * @module dayco-app/reducers
 */
const rootReducer = combineReducers({
    user,
    posts,
    postsEditModal
});

export default rootReducer;
