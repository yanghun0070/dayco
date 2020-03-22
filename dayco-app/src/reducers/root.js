import { combineReducers } from 'redux';

import { user } from './user';

/**
 * Root reducer for project
 * @module dayco-app/reducers
 */
const rootReducer = combineReducers({
    user
});

export default rootReducer;
