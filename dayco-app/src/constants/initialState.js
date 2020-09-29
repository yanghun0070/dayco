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
  },
  postsEtc: { //posts comment, like 처리
    likes: [],
    comment: []
  },
  postsEditModal: {
    isShow: false,
    id: -1,
    title: "",
    content: "",
    author: "",
    status: 'create' //create, edit, hide
  },
  alerts: {
    list: [], 
  },
  socket: {
    isSocket: true,
    actionStatus: ""
  }
}
