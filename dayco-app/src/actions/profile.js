import * as types from '../constants/types';
import * as API from '../services/http';
import * as Alert from '../actions/alert';

export function changeProfile(email, pw, fileBase64, fileName) {
    return dispatch => {
      return API.changeProfile(email, pw, fileBase64, fileName)
                 .then(async(response) => {
                    dispatch(Alert.createAlert({
                        variant : 'success', 
                        message : ' 프로필 수정 성공하였습니다.'
                    }));
            }).catch(function (error) {
                dispatch(Alert.createAlert({
                    variant : 'danger', 
                    message : '프로필 생성 실패하였습니다.'
                }));
            })
    }
}
 