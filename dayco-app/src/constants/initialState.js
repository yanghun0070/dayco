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
    detail: {
      
    },
    page: 0,
    rowNum : 0
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
    status: 'create' //create, edit, delete
  },
  alerts: {
    list: [], 
  },
  socket: {
    isSocket: true
  }
}
