import { combineReducers } from 'redux';

import { user } from './user';
import { posts, postsEditModal, postsDetailModal } from './posts';
import { alerts } from './alerts';
import { socket } from './socket';

/**
 * Root reducer for project
 * @module dayco-app/reducers
 */
const rootReducer = combineReducers({
    user,
    posts,
    postsDetailModal,
    postsEditModal,
    alerts, 
    socket
}); 

export default rootReducer;
