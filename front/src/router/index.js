import { createRouter, createWebHistory } from 'vue-router'

import login from '../views/LoginView.vue'
import oauth2 from '../views/Redirect.vue'
import searchSchool from '../views/SerchSchoolView.vue'
import playground_soccer from '../views/Playground_soccer.vue'
import timePickerDialog from '../views/GameBuilderView.vue'
import game from '../views/GameDateSelectorView.vue'
import gameInfo from '../views/GameInfoView.vue'
import teamBuilding from '../views/TeamBuildingView.vue'
import userInfo from '../views/UserInfoView.vue'
import teamInfo from '../views/TeamInfoView.vue'
import gameRequest from '../views/GameRequestView.vue';
import playgroundInfo from '../views/PlaygroundInfoView.vue'

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
      component: timePickerDialog,

    },

    {
      path: '/game',
      name: 'game',
      component: game
    },

    {
      path: '/gameInfo/:gameId',
      name: 'gameInfo',
      component: gameInfo,
      props: true
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
    {
      path: '/teamInfo/:teamId',
      name: 'teamInfo',
      component: teamInfo,
      props: true
    },

    {
      path: '/user/requests',
      name: 'gameRequest',
      component: gameRequest,
      props: true
    },

    {
      path: '/playground/:playgroundId',
      name: 'playgroundInfo',
      component: playgroundInfo,
      props: true
    },
  ]
})

export default router
