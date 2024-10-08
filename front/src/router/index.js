import { createRouter, createWebHistory } from 'vue-router'

import login from '../views/LoginView.vue'
import oauth2 from '../views/Redirect.vue'
import searchSchool from '../views/SerchSchoolView.vue'
import playground from '../views/playground/PlaygroundView.vue'
import timePickerDialog from '../views/game/GameBuilderView.vue'
import gameDateSelector from '../views/game/GameDateSelectorView.vue'
import gameInfo from '../views/game/GameInfoView.vue'
import teamBuilding from '../views/team/TeamBuildingView.vue'
import userInfo from '../views/UserInfoView.vue'
import teamInfo from '../views/team/TeamInfoView.vue'
import gameRequest from '../views/game/GameRequestView.vue';
import playgroundInfo from '../views/playground/PlaygroundInfoView.vue'
import myTeam from '../views/team/MyTeamView.vue'
import teamRequest from "@/views/team/TeamRequestView.vue";
import home from "@/views/HomeView.vue";
import myGame from '../views/game/MyGameView.vue'
import hostGame from '../views/game/HostGameView.vue'
import playgroundList from '../views/playground/PlaygroundListView.vue'
import teamList from '../views/team/TeamListView.vue';
import App from "@/App.vue";
import friendlyGameInfo from "@/views/game/FriendlyGameInfoView.vue";
import mSidebar from "@/views/mobile/sidebarView.vue"
import addPhoto from '@/views/addphotoView.vue'
import signup from '@/views/signupView.vue'
import ranking from '@/views/RankingView.vue'
import gameResult from '@/views/game/GameResultView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/', // 도메인 입력 시 초기 페이지를 '/home'으로 설정
      redirect: '/home'
    },
    {
      path: '/photo',
      name: 'photo',
      component: addPhoto
    },
      {
      path: '/home',
      name: 'home',
      component: home
    },
    {
      path: '/app',
      name: 'app',
      component: App
    },
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
      path: '/user/game/host',
      name: 'hostGame',
      component: hostGame

    },
    {
      path: '/playground/:playgroundId/:receivedGameId',
      name: 'playground',
      component: playground,
      props: true
    },
    {
      path: '/timePicker',
      name: 'timePicker',
      component: timePickerDialog,

    },
    {
      path: '/game/result',
      name: 'game/result',
      component: gameResult,
      props: true,
    },
    {
      path: '/gameDateSelect',
      name: 'gameDateSelector',
      component: gameDateSelector,
      props: true,

    },

    {
      path: '/gameInfo/:gameId',
      name: 'gameInfo',
      component: gameInfo,
      props: true
    },
    {
      path: '/friendlyGameInfo/:gameId',
      name: 'friendlyGameInfo',
      component: friendlyGameInfo,
      props: true
    },
    {
      path: '/makeTeam',
      name: 'teamBuilding',
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
      path: '/user/team/my',
      name: 'myTeam',
      component: myTeam
    },
    {
      path: '/playground/list/:sportsEvent',
      name: 'playgroundList',
      component: playgroundList,
      props: true
    },


    {
      path: '/team/list',
      name: 'teamList',
      component: teamList,
    },
    {
      path: '/user/game/my',
      name: 'myGame',
      component: myGame
    },
    {
      path: '/user/requests',
      name: 'gameRequest',
      component: gameRequest,
      props: true
    },

    {
      path: '/playground/info/:playgroundId',
      name: 'playgroundInfo',
      component: playgroundInfo,
      props: true
    },

    {
      path: '/user/request/team',
      name: 'teamRequest',
      component: teamRequest,
    },
    {
      path: '/user/signup',
      name: 'signup',
      component: signup,
    },
    {
      path: '/m/sidebar',
      name: 'mSidebar',
      component: mSidebar,
    },

    {
      path: '/ranking',
      name: 'ranking',
      component: ranking,
    },

  ]
})

export default router