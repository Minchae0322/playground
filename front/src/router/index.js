import { createRouter, createWebHistory } from 'vue-router'

import login from '../views/LoginView.vue'
import oauth2 from '../views/Redirect.vue'
import searchSchool from '../views/SerchSchoolView.vue'
import playground_soccer from '../views/Playground_soccer.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: login
    },
    {
      path: '/oauth2/redirect',
      name: 'oauth2',
      component: oauth2
    },

    {
      path: '/schools',
      name: 'school',
      component: searchSchool
    },

    {
      path: '/soccer',
      name: 'soccer',
      component: playground_soccer
    },
  ]
})

export default router
