import { combineReducers } from 'redux';

import { user } from './user';
import { posts, postsEditModal } from './posts';
import { postsEtc } from './postsEtc';
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
    postsEditModal,
    alerts,
    socket
});

export default rootReducer;
