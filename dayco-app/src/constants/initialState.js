/**
 * Initial state for the redux store
 * @type {Object}
 */
export default {
  user: {
      authenticated: false,
      joined: false,
      logon: false,
      id: null,
      token: null,
      currentUser: null
  },
  posts: {
    list: [],
    page: 0,
    rowNum : 0
  } 
}
