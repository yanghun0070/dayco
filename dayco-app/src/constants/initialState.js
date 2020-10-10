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
    rowNum : 0,
    detail: {}
  },
  postsEtc: { // like 처리
    likes: [],
    detail: {}
  },
  postsComment: {
    comments: [],
    status: 'get', //get, edit, remove
    detail: {
      page: 0,
      rowNum : 0,
      comments: []
    }
  },
  postsDetailModal: { //게시글 상세 Modal 창 보여주기 여부
    isShow: false
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
