import { createStore, compose, applyMiddleware } from 'redux';
import rootReducer from '../reducers/root';
import thunk from 'redux-thunk';

let store;
export default initialState => {
    const createdStore = createStore(
        rootReducer,
        initialState,
        compose(
            applyMiddleware(thunk),
            typeof window !== 'undefined' && window.devToolsExtension
                ? window.devToolsExtension()
                : f => f
        )
    );
    store = createdStore;
    return store;
};
