import initialState from '../constants/initialState';
import * as types from '../constants/types';   


/**
 * the alert reducer is responsible
 * @param {*} state 
 * @param {*} action 
 */
export function alerts(state = initialState.alerts, action) {
    switch (action.type) {
        case types.alert.ALERT_CREATE:
            const { alertObj } = action;

            //alert id 값을 추가시킨다.
            let alert = (state.list.length === 0) ? 
                Object.assign({}, state, { 
                    id: 1,
                    variant: alertObj.variant, 
                    message: alertObj.message
                }) : Object.assign({}, state, { 
                    id: state.list[state.list.length - 1].id + 1,
                    variant: alertObj.variant, 
                    message: alertObj.message
                });
        
            //새로 추가된 항목은 맨 위에 보이도록 한다.
            const alerts = [
                alert, ...state.list
            ];
            return Object.assign({}, state, {list: [...alerts]});
        case types.alert.ALERT_REMOVE:
            const { alertsRemoveId } = action;
            const deleteAlertList = state.list.filter(function(alert){
                return (alert.id !== alertsRemoveId);
            }).map((alerts) => {
                return alerts;
            });
            return Object.assign({}, state, {list: [...deleteAlertList]});
        default:
                return state;
    }
}