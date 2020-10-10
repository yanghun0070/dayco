import { combineReducers } from 'redux';

import { user } from './user';
import { posts, postsEditModal, postsDetailModal } from './posts';
import { postsEtc } from './postsEtc';
import { postsComment } from './postsComment';
import { alerts } from './alerts';
import { socket } from './socket';

/**
 * Root reducer for project
 * @module dayco-app/reducers
 */
const rootReducer = combineReducers({
    user,
    posts,
    postsEtc,
    postsComment,
    postsDetailModal,
    postsEditModal,
    alerts,
    socket
});

export default rootReducer;
