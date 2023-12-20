import { createRouter, createWebHistory } from 'vue-router'

import login from '../views/LoginView.vue'
import oauth2 from '../views/Redirect.vue'
import searchSchool from '../views/SerchSchoolView.vue'
import playground_soccer from '../views/Playground_soccer.vue'
import timePickerDialog from '../views/TimePickerDialogView.vue'
import game from '../views/GameView.vue'
import gameInfo from '../views/GameInfoView.vue'
import teamBuilding from '../views/TeamBuildingView.vue'
import userInfo from '../views/UserInfoView.vue'


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
    {
      path: '/timePicker',
      name: 'timePicker',
      component: timePickerDialog
    },

    {
      path: '/game',
      name: 'game',
      component: game
    },

    {
      path: '/gameInfo',
      name: 'gameInfo',
      component: gameInfo
    },

    {
      path: '/makeTeam',
      name: 'makeTeam',
      component: teamBuilding
    },

    {
      path: '/userInfo',
      name: 'userInfo',
      component: userInfo
    },
  ]
})

export default router
