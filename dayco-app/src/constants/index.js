/**
 * Gateway 상태에서 Social 로그인을 하기 위해서는 Gateway 에서 JWT 토큰을 생성해야 한다. 
 * @todo 추후 개선
 * https://github.com/jhipster/generator-jhipster/issues/4850
 * https://stackoverflow.com/questions/50908023/using-spring-security-oauth-using-a-custom-oauth-provider-i-get-authorization
 * 
 */
export const API_SOCIAL_URL = 'http://localhost:9999';
export const API_BASE_URL = 'http://localhost:8000/api';

export const OAUTH2_REDIRECT_URI = 'http://localhost:3000/oauth2/redirect'

export const GOOGLE_AUTH_URL = API_SOCIAL_URL + '/oauth2/authorization/google?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const GITHUB_AUTH_URL = API_SOCIAL_URL + '/oauth2/authorization/github?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const NAVER_AUTH_URL = API_SOCIAL_URL + '/oauth2/authorization/naver?redirect_uri=' + OAUTH2_REDIRECT_URI;
export const KAKAO_AUTH_URL = API_SOCIAL_URL + '/oauth2/authorization/kakao?redirect_uri=' + OAUTH2_REDIRECT_URI;


// 최대 보여질 Posts 댓글 개수
export const MAX_SHOW_POST_COMMENT = 5;

//Posts Edit Modal 
export const POSTS_CREATE_MODAL = "create";
export const POSTS_EDIT_MODAL = "edit";
export const POSTS_DELETE_MODAL = "delete";
export const POSTS_HIDE_MODAL = "hide"; 

//Posts 상태
export const POSTS_CREATED = "create";
export const POSTS_EDITED = "edit";
export const POSTS_DELETED = "delete";
export const POSTS_GETED = "get";

//Posts Like 상태
export const POSTS_LIKE_GETED = "likeGet";
export const POSTS_LIKE_INCREASED = "likeIncrease";
export const POSTS_LIKE_DECREASED = "likeDecrease";