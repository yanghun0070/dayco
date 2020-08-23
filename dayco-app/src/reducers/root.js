import { combineReducers } from 'redux';

import { user } from './user';
import { posts } from './posts';

/**
 * Root reducer for project
 * @module dayco-app/reducers
 */
const rootReducer = combineReducers({
    user,
    posts
});

export default rootReducer;
