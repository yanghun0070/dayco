import * as types from '../constants/types';

export function createAlert(obj) {
    return dispatch => {
        dispatch({
            type: types.alert.ALERT_CREATE,
            alertObj: obj
        })
    };
}

export function removeAlert(id) {
    return dispatch => {
        dispatch({
            type: types.alert.ALERT_REMOVE,
            alertsRemoveId: id
        })
    };
}